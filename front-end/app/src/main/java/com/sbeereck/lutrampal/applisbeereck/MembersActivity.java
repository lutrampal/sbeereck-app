package com.sbeereck.lutrampal.applisbeereck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.School;

import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends GeneralDrawerActivity {

    private TextView mTotalBalanceTextView;

    private List<Member> getPlaceHolderMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("Accart Julien", -50, School.ENSE3, "acc.ju@gmail.com", "0635368817"));
        members.add(new Member("Bertaux Benjamin", 10.5f, School.ENSIMAG, "ber.ben@gmail.com", "0635368817"));
        members.add(new Member("Trampal Lucas", 100, School.ENSIMAG, "lt@gmail.com", "0635368817"));
        return members;
    }

    private List<Member> members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.activity_members;
        super.onCreate(savedInstanceState);

        members = getPlaceHolderMembers();
        mListview.setAdapter(new MemberListItemAdapter(this, members));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_members, menu);
        MenuItem balanceItem = menu.findItem(R.id.total_balance);
        mTotalBalanceTextView = (TextView) MenuItemCompat.getActionView(balanceItem);
        mTotalBalanceTextView.setText("2000€");
        return super.onCreateOptionsMenu(menu);
    }

}
