package com.sbeereck.lutrampal.applisbeereck;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sbeereck.lutrampal.model.Product;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeersAndOthersFragment extends GeneralMainViewFragment {

    private List<Product> products;

    public BeersAndOthersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beers_and_others, container, false);
        products = Placeholders.getPlaceHolderProducts();
        super.onCreateView(inflater, container, savedInstanceState);
        mListview.setAdapter(new ProductListItemAdapter(getActivity(), products));
        mActivity.getSupportActionBar().setTitle(R.string.beer_and_other_fragment_name);
        mFabAdd.setOnClickListener(getFabAddClickListener());
        return view;
    }

    private View.OnClickListener getFabAddClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewProductDialogFragment();
                Bundle args = new Bundle();
                args.putBoolean("isEditDialog", false);
                dialog.setArguments(args);
                dialog.show(mActivity.getSupportFragmentManager(),
                        "NewProductDialogFragment");
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        if (v.getId() == R.id.main_listview) {
            inflater.inflate(R.menu.menu_delete_edit, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                editProduct(info.position);
                return true;
            case R.id.delete:
                // user wants to delete a product
                deleteProduct(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editProduct(int position) {
        Product productToEdit = ((ProductListItemAdapter) mListview.getAdapter())
                .getFilteredProducts().get(position);
        DialogFragment dialog = new NewProductDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", productToEdit);
        dialog.setArguments(args);
        dialog.show(mActivity.getSupportFragmentManager(),
                "EditProductDialogFragment");
    }

    private void deleteProduct(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog d = builder.setMessage(R.string.delete_product_confirm)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked delete button
                        List<Product> filteredProducts = ((ProductListItemAdapter) mListview.getAdapter())
                                .getFilteredProducts();
                        Product productToRemove = filteredProducts.get(position);
                        filteredProducts.remove(position);
                        products.remove(productToRemove);
                        ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.show();
    }


}
