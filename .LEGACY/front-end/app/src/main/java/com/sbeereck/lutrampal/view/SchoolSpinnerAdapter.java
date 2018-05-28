package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.School;

import java.util.LinkedList;
import java.util.List;


public class SchoolSpinnerAdapter extends BaseAdapter {

    private final List<String> choices;
    private final Context context;

    SchoolSpinnerAdapter(Context context) {
        this.choices = getChoices();
        this.context = context;
    }

    private static List<String> getChoices() {
        List<String> choices = new LinkedList<>();
        choices.add("École");
        for (School school : School.values()) {
            choices.add(school.toString());
        }
        return choices;
    }

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int i) {
        return choices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView vi = new TextView(context);
        vi.setText(choices.get(i));
        if (i == 0) {
            vi.setTextColor(Color.GRAY);
        } else {
            vi.setTextColor(Color.BLACK);
        }
        return vi;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }
}
