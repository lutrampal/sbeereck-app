package com.sbeereck.lutrampal.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
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
    private TextView normalBeerPriceTv;
    private TextView specialBeerPriceTv;
    private float normalBeerPrice;
    private float specialBeerPrice;

    public BeerTransactionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beer_transaction, container, false);
        servedBeersListView = view.findViewById(R.id.products_listview);
        servedBeers = (Map<Product, BeerCategory>) getArguments()
                .getSerializable("servedBeers");
        normalBeerPrice = getArguments().getFloat("normalPrice");
        specialBeerPrice = getArguments().getFloat("specialPrice");
        servedBeersListView.setAdapter(
                new BeerTransactionListItemAdapter(getActivity(), servedBeers));
        servedBeersListView.setItemChecked(0, true);
        quantityTv = view.findViewById(R.id.quantity_tv);
        normalBeerPriceTv = view.findViewById(R.id.normal_beer_price_tv);
        specialBeerPriceTv = view.findViewById(R.id.special_beer_price_tv);
        normalBeerPriceTv.setText(String.format("%.2f", normalBeerPrice) + "€/demi");
        specialBeerPriceTv.setText(String.format("%.2f", specialBeerPrice) + "€/demi");
        view.findViewById(R.id.plus_quantity_button).setOnClickListener(getPlusQuantityClickListener());
        view.findViewById(R.id.minus_quantity_button).setOnClickListener(getMinusQuantityClickListener());
        ((RadioGroup) view.findViewById(R.id.glass_size_rg))
                .setOnCheckedChangeListener(getOnGlassSizeChangeListenener());
        return view;
    }

    private RadioGroup.OnCheckedChangeListener getOnGlassSizeChangeListenener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.half_pint_rb:
                        normalBeerPriceTv.setText(String.format("%.2f", normalBeerPrice)
                                + "€/demi");
                        specialBeerPriceTv.setText(String.format("%.2f", specialBeerPrice)
                                + "€/demi");
                        break;
                    case R.id.pint_rb:
                        normalBeerPriceTv.setText(String.format("%.2f",
                                normalBeerPrice*Product.NB_HALF_PINTS_FOR_A_PINT)
                                + "€/pinte");
                        specialBeerPriceTv.setText(String.format("%.2f",
                                specialBeerPrice*Product.NB_HALF_PINTS_FOR_A_PINT)
                                + "€/pinte");
                        break;
                    case R.id.pitcher_rb:
                        normalBeerPriceTv.setText(String.format("%.2f",
                                normalBeerPrice*Product.NB_HALF_PINTS_FOR_A_PITCHER)
                                + "€/pichet");
                        specialBeerPriceTv.setText(String.format("%.2f",
                                specialBeerPrice*Product.NB_HALF_PINTS_FOR_A_PITCHER)
                                + "€/pichet");
                        break;
                }
            }
        };
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
