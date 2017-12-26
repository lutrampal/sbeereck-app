package com.sbeereck.lutrampal.applisbeereck;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.School;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberInfoDialogFragment extends DialogFragment {

    public MemberInfoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        Float balance = getArguments().getFloat("balance");
        String school = getArguments().getString("school");
        String email = getArguments().getString("email");
        String phone = getArguments().getString("phone");
        Boolean membership = getArguments().getBoolean("membership");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_member_info_dialog, null);
        TextView tvName = v.findViewById(R.id.member_name);
        TextView tvBalance = v.findViewById(R.id.member_balance);
        TextView tvSchool = v.findViewById(R.id.member_school);
        TextView tvEmail = v.findViewById(R.id.member_email);
        TextView tvPhone = v.findViewById(R.id.member_phone);
        TextView tvMembership = v.findViewById(R.id.membership);
        tvName.setText(name);
        tvBalance.setText(balance + "€");
        if (balance < 10) { // threshold shoud be changed dynamically in the future
            tvBalance.setTextColor(getContext().getResources().getColor(R.color.colorBalanceTooLow));
        }
        tvSchool.setText(school);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        if (membership) {
            tvMembership.setText(R.string.valid_membership);
        } else {
            tvMembership.setText(R.string.invalid_membership);
            tvMembership.setTextColor(getContext().getResources().
                    getColor(R.color.colorOutdatedMembership));
        }

        return builder.setView(v)
                .create();
    }
}
