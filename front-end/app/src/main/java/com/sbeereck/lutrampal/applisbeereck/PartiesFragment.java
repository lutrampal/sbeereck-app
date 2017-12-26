package com.sbeereck.lutrampal.applisbeereck;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sbeereck.lutrampal.model.Party;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartiesFragment extends GeneralMainViewFragment {

    private List<Party> parties;

    private List<Party> getPlaceHolderParties() {
        List<Party> parties = new ArrayList<>();
        parties.add(new Party("Soirée 1", new Date(2017, 12, 25), 200, 2000));
        parties.add(new Party("Soirée 2", new Date(2017, 12, 24), 50, 2000));
        parties.add(new Party("Soirée 3", new Date(2017, 12, 11), 50, -8000));
        return parties;
    }

    public PartiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parties, container, false);
        parties = getPlaceHolderParties();
        super.onCreateView(inflater, container, savedInstanceState);
        mListview.setAdapter(new PartyListItemAdapter(getActivity(), parties));
        mActivity.getSupportActionBar().setTitle(R.string.parties_activity_name);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog d = builder.setMessage(R.string.delete_party_confirm)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked delete button
                        List<Party> filteredParties = ((PartyListItemAdapter) mListview.getAdapter())
                                .getFilteredParties();
                        Party partyToRemove = filteredParties.get(partyIndex);
                        filteredParties.remove(partyIndex);
                        parties.remove(partyToRemove);
                        ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.show();
    }

}
