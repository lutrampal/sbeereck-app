package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.sbeereck.lutrampal.model.Product;

import java.util.List;


public class ProductSingleChoiceListItemAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private List<Product> products;

    ProductSingleChoiceListItemAdapter(Context context, List<Product> products) {
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_single_choice, null);
        }
        CheckedTextView tv = convertView.findViewById(android.R.id.text1);
        tv.setText(products.get(position).getName() + " (" + products.get(position).getPrice() + "€)");
        return convertView;
    }

}
