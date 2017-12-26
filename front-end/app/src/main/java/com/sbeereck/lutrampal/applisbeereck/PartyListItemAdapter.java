package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Party;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PartyListItemAdapter extends BaseAdapter implements Filterable {

    private List<Party> parties;
    private List<Party> filteredParties;
    public List<Party> getFilteredParties() {
        return filteredParties;
    }
    private static LayoutInflater inflater = null;

    public PartyListItemAdapter(Context context, List<Party> parties) {
        this.parties = parties;
        this.filteredParties = parties;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredParties.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredParties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private DateFormat df = new SimpleDateFormat("dd/MM/yy");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.party_list_item, null);
        TextView tvNbAttendees = vi.findViewById(R.id.party_attendees_tv);
        TextView tvDate = vi.findViewById(R.id.party_date_tv);
        TextView tvincome = vi.findViewById(R.id.party_income_tv);
        TextView tvName = vi.findViewById(R.id.party_name_tv);
        Party p = filteredParties.get(position);
        tvNbAttendees.setText(p.getNumberOfAttendees() + " participants");
        tvDate.setText(df.format(p.getDate()));
        tvincome.setText(p.getIncome() + "€");
        tvName.setText(p.getName());
        return vi;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = parties;
                    results.count = parties.size();
                }
                else {
                    List<Party> filteredParties = new ArrayList<>();

                    for (Party p : parties) {
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
                filteredParties = (List<Party>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}