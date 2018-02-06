package com.sbeereck.lutrampal.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.TransactionController;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Transaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateBalanceDialogFragment extends DialogWithOkClickListener<Member> {

    Member member = null;
    EditText labelEt;
    EditText amountEt;
    TransactionController transactionController;

    public UpdateBalanceDialogFragment() {
        // Required empty public constructor
    }

    private class UpdateBalanceTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                String label = labelEt.getText().toString();
                if (label.trim().isEmpty()) {
                    throw new Exception("Motif invalide");
                }
                float amount;
                try {
                    amount = Float.valueOf(amountEt.getText().toString());
                } catch (Exception e) {
                    throw new Exception("Montant invalide");
                }
                Transaction t = new Transaction(member, amount, label);
                transactionController.addTransaction(t);
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(getActivity(),
                        getString(R.string.update_balance_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (mOnOkButtonClickListener != null) {
                mOnOkButtonClickListener.onOkButtonClick(member, false);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_update_balance_dialog, null);
        labelEt = v.findViewById(R.id.label_et);
        amountEt = v.findViewById(R.id.amount_et);
        member = (Member) getArguments().getSerializable("member");
        transactionController = new TransactionController(Placeholders.getPlaceHolderDataManager());
        return builder.setView(v)
                .setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new UpdateBalanceTask().execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
    }
}
