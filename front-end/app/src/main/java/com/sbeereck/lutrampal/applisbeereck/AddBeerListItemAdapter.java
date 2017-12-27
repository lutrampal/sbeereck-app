package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 27/12/2017.
 */

public class AddBeerListItemAdapter extends BaseAdapter {

    private List<Product> beers;
    private LayoutInflater inflater = null;
    private Context context;
    private Map<Product, BeerCategory> selectedBeers;

    public AddBeerListItemAdapter(Context context, List<Product> beers,
                                  Map<Product, BeerCategory> selectedBeers) {
        this.context = context;
        this.beers = beers;
        this.selectedBeers = selectedBeers;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Map<Product, BeerCategory> getSelectedBeers() {
        return selectedBeers;
    }

    @Override
    public int getCount() {
        return beers.size();
    }

    @Override
    public Object getItem(int i) {
        return beers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.add_beer_list_item, null);
        TextView beerNameTv = convertView.findViewById(R.id.beer_name);
        RadioButton noRb = convertView.findViewById(R.id.no_rb);
        RadioButton normalRb = convertView.findViewById(R.id.normal_rb);
        RadioButton specialRb = convertView.findViewById(R.id.special_rb);
        final Product beer = beers.get(i);
        if (selectedBeers.containsKey(beer)) {
            switch (selectedBeers.get(beer)) {
                case NORMAL:
                    normalRb.setChecked(true);
                    break;
                case SPECIAL:
                    specialRb.setChecked(true);
                    break;
            }
        }
        normalRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedBeers.put(beer, BeerCategory.NORMAL);
                }
            }
        });
        specialRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedBeers.put(beer, BeerCategory.SPECIAL);
                }
            }
        });
        noRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedBeers.remove(beer);
                }
            }
        });
        beerNameTv.setText(beer.getName());
        return convertView;
    }
}
