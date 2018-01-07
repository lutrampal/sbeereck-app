package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Member;

import java.util.ArrayList;
import java.util.List;


public class MemberListItemAdapter extends BaseAdapter implements Filterable {

    protected static LayoutInflater inflater = null;
    protected List<Member> members;
    protected List<Member> filteredMembers;
    protected Context context;

    public MemberListItemAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
        this.filteredMembers = members;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Member> getFilteredMembers() {
        return filteredMembers;
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
        tvMemberName.setText(m.getFirstName() + " " + m.getLastName());
        tvMemberBalance.setText(m.getBalance() + "€");
        tvMemberName.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        tvMemberBalance.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        if (m.getBalance() < Placeholders.getPlaceHolderBalanceTooLowThreshold()) { // threshold should be changed dynamically in the future.
            tvMemberName.setTextColor(context.getResources().getColor(R.color.colorBalanceTooLow));
            tvMemberBalance.setTextColor(context.getResources().getColor(R.color.colorBalanceTooLow));
        }
        if (!m.isMembershipValid()) {
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
                    List<Member> filteredMembers = new ArrayList<>();

                    for (Member m : members) {
                        if ((m.getLastName() + m.getFirstName()).toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredMembers.add(m);
                    }

                    results.values = filteredMembers;
                    results.count = filteredMembers.size();
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
