package com.sbeereck.lutrampal.applisbeereck;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Product;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductDialogFragment extends DialogFragment {

    Product p = null;

    public NewProductDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_new_product_dialog, null);
        int positiveButtonId = R.string.add;
        if (getArguments() != null && getArguments().getSerializable("product") != null) {
            positiveButtonId = R.string.edit;
            p = (Product) getArguments().getSerializable("product");
            makeEditDialog(v);
        }
        return builder.setView(v)
                .setPositiveButton(positiveButtonId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked add button
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
    }

    private void makeEditDialog(View v) {
        TextView tvTitle = v.findViewById(R.id.title_tv);
        tvTitle.setText(R.string.edit_product_dialog_title);
        EditText etName = v.findViewById(R.id.name_et);
        etName.setText(p.getName());
        EditText etPrice = v.findViewById(R.id.price_et);
        etPrice.setText(String.valueOf(p.getPrice()));
        RadioButton rb = null;
        switch (p.getType()) {
            case BEER:
                rb = v.findViewById(R.id.beer_radio_button);
                break;
            case DEPOSIT:
                rb = v.findViewById(R.id.deposit_radio_button);
                break;
            case FOOD:
                rb = v.findViewById(R.id.other_radio_button);
                break;
        }
        rb.setChecked(true);
    }


}
