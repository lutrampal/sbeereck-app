package com.sbeereck.lutrampal.view;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.TransactionController;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionsActivity extends ActivityWithAsyncTasks {

    protected ListView mListview;
    protected SearchView mSearchView;
    protected FloatingActionButton mFabAdd;
    private Party party = null;
    private TransactionController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new TransactionController(Placeholders.getPlaceHolderDataManager());
        party = (Party) getIntent().getSerializableExtra("party");

        mListview = findViewById(R.id.main_listview);
        registerForContextMenu(mListview);
        mListview.setTextFilterEnabled(true);

        mFabAdd = findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(getFabAddOnClickListener());

        mListview.setAdapter(new TransactionListItemAdapter(this, party.getTransactions()));
        getSupportActionBar().setTitle(party.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private View.OnClickListener getFabAddOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsActivity.this,
                        NewTransactionActivity.class);
                intent.putExtra("party", party);
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == requestCode) {
            party.getTransactions().add(0,
                    (Transaction) data.getSerializableExtra("transaction"));
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
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

    private class DeleteTransactionTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;
        private Transaction selectedTransaction;
        private int selectedTransactionIdx;

        public DeleteTransactionTask(Transaction selectedTransaction, int selectedTransactionIdx) {
            this.selectedTransaction = selectedTransaction;
            this.selectedTransactionIdx = selectedTransactionIdx;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            addTaskToRunningAsyncTasks(this);
            try {
                controller.deleteTransaction(selectedTransaction.getId());
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(TransactionsActivity.this,
                        getString(R.string.transaction_delete_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            List<Transaction> filteredTransactions = ((TransactionListItemAdapter) mListview.getAdapter())
                    .getFilteredTransactions();
            filteredTransactions.remove(selectedTransactionIdx);
            party.getTransactions().remove(selectedTransaction);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    private void deleteTransaction(final int position) {
        List<Transaction> filteredTransactions = ((TransactionListItemAdapter) mListview.getAdapter())
                .getFilteredTransactions();
        Transaction transactionToRemove = filteredTransactions.get(position);
        new DeleteTransactionTask(transactionToRemove, position).execute();
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
