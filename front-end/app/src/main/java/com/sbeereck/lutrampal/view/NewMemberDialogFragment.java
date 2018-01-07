package com.sbeereck.lutrampal.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.School;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewMemberDialogFragment extends DialogFragment {

    private OnOkButtonClickListener<Member> mOnOkButtonClickListener;
    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText emailEt;
    private EditText phoneEt;
    private Spinner schoolSpinner;
    private Member member = null;
    private MemberController controller = null;

    public NewMemberDialogFragment() {
        // Required empty public constructor
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener listener) {
        mOnOkButtonClickListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_new_member_dialog, null);
        controller = new MemberController(Placeholders.getPlaceHolderDataManager());
        schoolSpinner = v.findViewById(R.id.school_spinner);
        schoolSpinner.setAdapter(new SchoolSpinnerAdapter(v.getContext()));
        firstNameEt = v.findViewById(R.id.first_name_edit_text);
        lastNameEt = v.findViewById(R.id.last_name_edit_text);
        emailEt = v.findViewById(R.id.email_edit_text);
        phoneEt = v.findViewById(R.id.phone_edit_text);
        return builder.setView(v)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addMember();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
    }

    private void addMember() {
        member = new Member(firstNameEt.getText().toString(),
                lastNameEt.getText().toString(),
                phoneEt.getText().toString(),
                emailEt.getText().toString(),
                School.getSchool(schoolSpinner.getSelectedItem().toString()));
        new AddMemberTask().execute();
    }

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    private class AddMemberTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                member.setId(controller.addMember(member));
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void obj) {
            if (e != null) {
                Toast.makeText(context,
                        getString(R.string.member_adding_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (mOnOkButtonClickListener != null) {
                mOnOkButtonClickListener.onOkButtonClick(member, false);
            }
        }
    }
}
