package com.sbeereck.lutrampal.applisbeereck;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ListView;

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PartyActivity extends AppCompatActivity {

    protected ListView mListview;
    protected SearchView mSearchView;
    protected FloatingActionButton mFabAdd;
    private Party party = null;
    private ArrayList<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListview = findViewById(R.id.main_listview);
        registerForContextMenu(mListview);
        mListview.setTextFilterEnabled(true);

        mFabAdd = findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(getFabAddOnClickListener());

        party = (Party) getIntent().getSerializableExtra("party");
        members = (ArrayList<Member>) getIntent().getSerializableExtra("members");
        mListview.setAdapter(new TransactionListItemAdapter(this, party.getTransactions()));
        getSupportActionBar().setTitle(party.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private View.OnClickListener getFabAddOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartyActivity.this, NewTransactionActivity.class);
                intent.putExtra("party", party);
                intent.putExtra("members", members);
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == R.id.main_listview) {
            inflater.inflate(R.menu.menu_delete, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                // user wants to delete a party
                deleteTransaction(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteTransaction(final int position) {
        List<Transaction> filteredTransactions = ((TransactionListItemAdapter) mListview.getAdapter())
                .getFilteredTransactions();
        Transaction transactionToRemove = filteredTransactions.get(position);
        filteredTransactions.remove(position);
        party.getTransactions().remove(transactionToRemove);
        ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_general, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((Filterable) mListview.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
