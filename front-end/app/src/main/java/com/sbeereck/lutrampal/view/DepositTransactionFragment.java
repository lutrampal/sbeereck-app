package com.sbeereck.lutrampal.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Product;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositTransactionFragment extends Fragment {

    private List<Product> deposits;
    private View view;
    private ListView depositsListView;
    private int quantity = 1;
    private TextView quantityTv;

    public DepositTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deposit_transaction, container, false);
        depositsListView = view.findViewById(R.id.deposits_listview);
        deposits = Placeholders.getPlaceHolderDeposits();
        depositsListView.setAdapter(new ProductSingleChoiceListItemAdapter(getActivity(), deposits));
        depositsListView.setItemChecked(0, true);
        quantityTv = view.findViewById(R.id.quantity_tv);
        view.findViewById(R.id.plus_quantity_button).setOnClickListener(getPlusQuantityClickListener());
        view.findViewById(R.id.minus_quantity_button).setOnClickListener(getMinusQuantityClickListener());
        return view;
    }

    private View.OnClickListener getMinusQuantityClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityTv.setText(String.valueOf(quantity));
                }
            }
        };
    }

    private View.OnClickListener getPlusQuantityClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity < 99) {
                    quantity++;
                    quantityTv.setText(String.valueOf(quantity));
                }
            }
        };
    }

}
