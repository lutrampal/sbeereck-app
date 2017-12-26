package com.sbeereck.lutrampal.applisbeereck;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.School;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lutrampal on 25/12/2017.
 */

public class SchoolSpinnerAdapter extends BaseAdapter {

    private final List<String> choices;
    private final Context context;

    public static List<String> getChoices() {
        List<String> choices = new LinkedList<>();
        choices.add("École");
        for (School school : School.values()) {
            choices.add(School.getName(school));
        }
        return choices;
    }

    public SchoolSpinnerAdapter(Context context) {
        this.choices = getChoices();
        this.context = context;
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
        if (position == 0) {
            return false;
        }
        return true;
    }
}
