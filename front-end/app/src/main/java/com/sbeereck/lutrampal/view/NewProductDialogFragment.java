package com.sbeereck.lutrampal.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.ProductController;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductDialogFragment extends DialogWithOkClickListener<Product> {

    private Boolean isEditProductDialog = false;
    private ProductController controller;
    private Product product = null;
    private EditText nameEt;
    private RadioGroup typeRg;
    private EditText priceEt;

    public NewProductDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_new_product_dialog, null);
        nameEt = v.findViewById(R.id.name_et);
        typeRg = v.findViewById(R.id.type_rg);
        priceEt = v.findViewById(R.id.price_et);
        controller = new ProductController(Placeholders.getPlaceHolderDataManager());
        int positiveButtonId = R.string.add;
        if (getArguments() != null && getArguments().getSerializable("product") != null) {
            positiveButtonId = R.string.edit;
            product = (Product) getArguments().getSerializable("product");
            makeEditDialog(v);
        }
        return builder.setView(v)
                .setPositiveButton(positiveButtonId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked add button
                        addProduct();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
    }

    private void addProduct() {
        String name = nameEt.getText().toString();
        Float price;
        try {
            price = Float.valueOf(priceEt.getText().toString());
        } catch (Exception e) {
            price = 0f;
        }
        ProductType type = null;
        switch (typeRg.getCheckedRadioButtonId()) {
            case R.id.beer_radio_button:
                type = ProductType.BEER;
                break;
            case R.id.deposit_radio_button:
                type = ProductType.DEPOSIT;
                break;
            case R.id.food_radio_button:
                type = ProductType.FOOD;
                break;
        }
        int id = -1;
        if (product != null) {
            id = product.getId();
        }
        product = new Product(id, name, price, type);
        new AddProductTask().execute();
    }

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    private class AddProductTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                if (isEditProductDialog) {
                    controller.editProduct(product);
                } else {
                    product.setId(controller.addProduct(product));
                }
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void obj) {
            if (e != null) {
                Toast.makeText(context,
                        getString(R.string.product_adding_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (mOnOkButtonClickListener != null) {
                mOnOkButtonClickListener.onOkButtonClick(product, isEditProductDialog);
            }
        }
    }


    private void makeEditDialog(View v) {
        TextView tvTitle = v.findViewById(R.id.title_tv);
        tvTitle.setText(R.string.edit_product_dialog_title);
        EditText etName = v.findViewById(R.id.name_et);
        etName.setText(product.getName());
        EditText etPrice = v.findViewById(R.id.price_et);
        etPrice.setText(String.valueOf(product.getPrice()));
        isEditProductDialog = true;
        RadioButton rb = null;
        switch (product.getType()) {
            case BEER:
                rb = v.findViewById(R.id.beer_radio_button);
                break;
            case DEPOSIT:
                rb = v.findViewById(R.id.deposit_radio_button);
                break;
            case FOOD:
                rb = v.findViewById(R.id.food_radio_button);
                break;
        }
        rb.setChecked(true);
    }
}
