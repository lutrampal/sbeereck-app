package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Member;

import java.util.List;

public class MemberAutocompleteTextViewAdapter extends MemberListItemAdapter {

    MemberAutocompleteTextViewAdapter(Context context, List<Member> members) {
        super(context, members);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(filteredMembers.get(position).getName());
        return convertView;
    }
}
