package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutrampal on 26/12/2017.
 */

public class ProductListItemAdapter extends BaseAdapter implements Filterable {

    private static LayoutInflater inflater = null;
    private List<Product> products;
    private List<Product> filteredProducts;
    private Context context;

    public ProductListItemAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.filteredProducts = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    @Override
    public int getCount() {
        return filteredProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.product_list_item, null);
        TextView tvProductName = convertView.findViewById(R.id.product_name);
        TextView tvProductPrice = convertView.findViewById(R.id.product_price);
        Product p = filteredProducts.get(i);
        tvProductName.setText(p.getName());
        switch (p.getType()) {
            case BEER:
                tvProductPrice.setText(p.getPrice() + "€/demi");
                tvProductName.setTextColor(context.getResources().getColor(R.color.colorBeer));
                tvProductPrice.setTextColor(context.getResources().getColor(R.color.colorBeer));
                break;
            case DEPOSIT:
                tvProductPrice.setText(p.getPrice() + "€");
                tvProductName.setTextColor(context.getResources().getColor(R.color.colorDeposit));
                tvProductPrice.setTextColor(context.getResources().getColor(R.color.colorDeposit));
                break;
            case FOOD:
                tvProductPrice.setText(p.getPrice() + "€");
                tvProductName.setTextColor(context.getResources().getColor(R.color.colorOther));
                tvProductPrice.setTextColor(context.getResources().getColor(R.color.colorOther));
                break;
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = products;
                    results.count = products.size();
                } else {
                    List<Product> filteredParties = new ArrayList<>();

                    for (Product p : products) {
                        if (p.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredParties.add(p);
                    }

                    results.values = filteredParties;
                    results.count = filteredParties.size();
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProducts = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
