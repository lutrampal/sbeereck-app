package com.sbeereck.lutrampal.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.model.Member;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberInfoDialogFragment extends DialogFragment {

    private MemberController controller;
    private TextView tvMembership;
    private TextView tvName;
    private TextView tvBalance;
    private TextView tvSchool;
    private TextView tvEmail;
    private TextView tvPhone;
    private float balanceTooLowThreshold = 0;

    public MemberInfoDialogFragment() {
        // Required empty public constructor
    }

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    private class FetchMemberTask extends AsyncTaskWithLoadAnimation<Void, Integer, Member> {

        private final int memberId;
        private Exception e = null;

        public FetchMemberTask(Context context, int memberId) {
            super(context);
            this.memberId = memberId;
        }

        @Override
        protected Member doInBackground(Void ... voids) {
            try {
                return controller.getMember(memberId);
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Member member) {
            super.onPostExecute(member);
            if (e != null) {
                Toast.makeText(getActivity(),
                        getString(R.string.member_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            tvName.setText(member.getFirstName() + " " + member.getLastName());
            tvBalance.setText(member.getBalance() + "€");
            if (member.getBalance() < balanceTooLowThreshold) {
                tvBalance.setTextColor(getContext().getResources().getColor(R.color.colorBalanceTooLow));
            }
            if (member.getSchool() == null) {
                tvSchool.setVisibility(View.GONE);
            } else {
                tvSchool.setText(member.getSchool().toString());
            }
            if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
                tvEmail.setVisibility(View.GONE);
            } else {
                tvEmail.setText(member.getEmail());
            }
            if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
                tvPhone.setVisibility(View.GONE);
            } else {
                tvPhone.setText(member.getPhone());
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yy");
            String parenthesisStr;
            if (member.isFormerStaff()) {
                parenthesisStr = "staff";
            } else {
                parenthesisStr = df.format(member.getLastMembershipPayment());
            }
            if (member.isMembershipValid()) {
                tvMembership.setText(getString(R.string.valid_membership)
                        + " (" + parenthesisStr + ")");
            } else {
                tvMembership.setText(getString(R.string.invalid_membership)
                        + " (" + parenthesisStr + ")");
                tvMembership.setTextColor(getContext().getResources().
                        getColor(R.color.colorOutdatedMembership));
            }

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        balanceTooLowThreshold = getArguments().getFloat("balance_too_low_threshold");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_member_info_dialog, null);
        tvName = v.findViewById(R.id.member_name);
        tvBalance = v.findViewById(R.id.member_balance);
        tvSchool = v.findViewById(R.id.member_school);
        tvEmail = v.findViewById(R.id.member_email);
        tvPhone = v.findViewById(R.id.member_phone);
        tvMembership = v.findViewById(R.id.membership);
        RESTDataManager dataManager = RESTDataManagerSingleton
                .getDataManager(getActivity());
        if (dataManager != null) {
            controller = new MemberController(dataManager);
            new FetchMemberTask(getActivity(), id).execute();
        }
        return builder.setView(v)
                .create();
    }
}
