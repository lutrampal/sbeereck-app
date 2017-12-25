package com.sbeereck.lutrampal.applisbeereck;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sbeereck.lutrampal.model.Party;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartiesActivity extends GeneralDrawerActivity {

    private List<Party> getPlaceHolderParties() {
        List<Party> parties = new ArrayList<>();
        parties.add(new Party("Soirée 1", new Date(2017, 12, 25), 200, 2000));
        parties.add(new Party("Soirée 2", new Date(2017, 12, 24), 50, 2000));
        parties.add(new Party("Soirée 3", new Date(2017, 12, 11), 50, -8000));
        return parties;
    }

    private List<Party> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.activity_parties;
        super.onCreate(savedInstanceState);

        parties = getPlaceHolderParties();
        mListview.setAdapter(new PartyListItemAdapter(this, parties));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId()==R.id.main_listview) {
            inflater.inflate(R.menu.menu_delete_edit, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                // user wants to edit a party
                return true;
            case R.id.delete:
                // user wants to delete a party
                deleteParty(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteParty(final int partyIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_party_title)
                .setMessage(R.string.delete_party_confirm)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked delete button
                parties.remove(partyIndex);
                ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked cancel button
            }
        })
                .create()
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parties, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
