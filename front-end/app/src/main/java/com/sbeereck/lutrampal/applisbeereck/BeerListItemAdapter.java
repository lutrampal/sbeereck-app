package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;

import java.util.Map;

public class BeerListItemAdapter extends BaseAdapter {

    protected Context context;
    private Map<Product, BeerCategory> servedBeers;

    BeerListItemAdapter(Context context, Map<Product, BeerCategory> servedBeers) {
        this.context = context;
        this.servedBeers = servedBeers;
    }

    @Override
    public int getCount() {
        return servedBeers.size();
    }

    @Override
    public Object getItem(int i) {
        return servedBeers.entrySet().toArray()[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = new TextView(context);
        convertView.setPadding(0, 15, 0, 15);
        Map.Entry<Product, BeerCategory> item = (Map.Entry<Product, BeerCategory>) getItem(i);
        ((TextView) convertView).setText(item.getKey().getName());
        switch (item.getValue()) {
            case SPECIAL:
                ((TextView) convertView).setTextColor(context.getResources().getColor(R.color.colorBeer));
                break;
        }
        return convertView;
    }

}
