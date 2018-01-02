package com.sbeereck.lutrampal.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeerTransactionFragment extends Fragment {

    private Map<Product, BeerCategory> servedBeers;
    private View view;
    private ListView servedBeersListView;
    private int quantity = 1;
    private TextView quantityTv;

    public BeerTransactionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beer_transaction, container, false);
        servedBeersListView = view.findViewById(R.id.beers_list_view);
        servedBeers = (Map<Product, BeerCategory>) getArguments()
                .getSerializable("servedBeers");
        float normalBeerPrice = getArguments().getFloat("normalPrice");
        float specialBeerPrice = getArguments().getFloat("specialPrice");
        servedBeersListView.setAdapter(
                new BeerTransactionListItemAdapter(getActivity(),
                        servedBeers, normalBeerPrice, specialBeerPrice));
        servedBeersListView.setItemChecked(0, true);
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
