package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutrampal on 26/12/2017.
 */

public class TransactionListItemAdapter extends BaseAdapter implements Filterable {

    private static LayoutInflater inflater = null;
    private List<Transaction> transactions;
    private List<Transaction> filteredTransactions;
    private Context context;

    public TransactionListItemAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
        this.filteredTransactions = transactions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Transaction> getFilteredTransactions() {
        return filteredTransactions;
    }

    @Override
    public int getCount() {
        return filteredTransactions.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredTransactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.transaction_list_item, null);
        TextView tvMember = convertView.findViewById(R.id.member_tv);
        TextView tvAmount = convertView.findViewById(R.id.amount_tv);
        TextView tvLabel = convertView.findViewById(R.id.label_tv);
        Transaction t = filteredTransactions.get(i);

        tvMember.setText(t.getMember().getFirstName() + " " + t.getMember().getLastName());
        String sign;
        if (t.getAmount() < 0) {
            sign = "";
            tvAmount.setTextColor(context.getResources().getColor(R.color.colorBalanceTooLow));
        } else {
            sign = "+";
            tvAmount.setTextColor(context.getResources().getColor(R.color.colorPositiveBalance));
        }
        tvAmount.setText(sign + String.format("%.2f", t.getAmount()) + "€");
        tvLabel.setText(t.getLabel());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = transactions;
                    results.count = transactions.size();
                } else {
                    List<Transaction> filteredTransactions = new ArrayList<>();

                    for (Transaction t : transactions) {
                        if ((t.getMember().getFirstName() + " " + t.getMember().getLastName()
                                + t.getLabel()).toLowerCase()
                                .contains(constraint.toString().toLowerCase()))
                            filteredTransactions.add(t);
                    }

                    results.values = filteredTransactions;
                    results.count = filteredTransactions.size();
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredTransactions = (List<Transaction>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}
