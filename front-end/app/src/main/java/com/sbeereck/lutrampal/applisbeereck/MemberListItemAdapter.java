package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Member;

import java.util.ArrayList;
import java.util.List;


public class MemberListItemAdapter extends BaseAdapter implements Filterable {

    private List<Member> members;
    private List<Member> filteredMembers;
    public List<Member> getFilteredMembers() {
        return filteredMembers;
    }
    private static LayoutInflater inflater = null;
    private Context context;

    public MemberListItemAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
        this.filteredMembers = members;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredMembers.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredMembers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.member_list_item, null);
        TextView tvMemberName = convertView.findViewById(R.id.member_name_tv);
        TextView tvMemberBalance = convertView.findViewById(R.id.member_balance_tv);
        Member m = filteredMembers.get(i);
        tvMemberName.setText(m.getName());
        tvMemberBalance.setText(m.getBalance() + "€");
        tvMemberName.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        tvMemberBalance.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        if (m.getBalance() < 10) { // threshold should be changed dynamically in the future.
            tvMemberName.setTextColor(context.getResources().getColor(R.color.colorBalanceTooLow));
            tvMemberBalance.setTextColor(context.getResources().getColor(R.color.colorBalanceTooLow));
        }
        if (!m.getMembership()) {
            tvMemberName.setTextColor(context.getResources().getColor(R.color.colorOutdatedMembership));
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
                    results.values = members;
                    results.count = members.size();
                }
                else {
                    List<Member> filteredParties = new ArrayList<>();

                    for (Member m : members) {
                        if (m.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredParties.add(m);
                    }

                    results.values = filteredParties;
                    results.count = filteredParties.size();
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredMembers = (List<Member>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
