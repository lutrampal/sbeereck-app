package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;

import java.util.Map;

/**
 * Created by lutrampal on 27/12/2017.
 */

public class BeerTransactionListItemAdapter extends BeerListItemAdapter {

    private LayoutInflater inflater;

    public BeerTransactionListItemAdapter(Context context, Map<Product, BeerCategory> servedBeers) {
        super(context, servedBeers);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_single_choice, null);
        }
        CheckedTextView tv = convertView.findViewById(android.R.id.text1);
        super.getView(position, tv, parent);
        return convertView;
    }
}
