package com.sbeereck.lutrampal.view;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.PartyController;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartiesFragment extends GeneralMainViewFragment {

    private List<Party> parties = new ArrayList<>();
    private PartyController controller;

    private class GetAllPartiesTask extends AsyncTask<Void, Integer, List<Party>> {

        private Exception e = null;

        @Override
        protected List<Party> doInBackground(Void ... voids) {
            List<Party> parties = null;
            try {
                parties = controller.getAllParties();
            } catch (Exception e) {
                this.e = e;
                parties = new ArrayList<>();
            }
            return parties;
        }

        @Override
        protected void onPostExecute(List<Party> parties) {
            if (e != null) {
                Toast.makeText(mActivity.getApplicationContext(),
                        R.string.parties_loading_error + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            PartiesFragment.this.parties.clear();
            PartiesFragment.this.parties.addAll(parties);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    public PartiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parties, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        controller = new PartyController(Placeholders.getPlaceHolderDataManager());
        new GetAllPartiesTask().execute();
        mListview.setAdapter(new PartyListItemAdapter(getActivity(), parties));
        mListview.setOnItemClickListener(getListViewItemClickListener());
        mActivity.getSupportActionBar().setTitle(R.string.parties_fragment_name);
        mFabAdd.setOnClickListener(getFabAddOnClickListener());
        return view;
    }

    private View.OnClickListener getFabAddOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewPartyActivity.class);
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == requestCode) {
            Party newParty = (Party) data.getSerializableExtra("party");
            if (data.getBooleanExtra("wasEditing", false)) {
                parties.remove(newParty);
            }
            parties.add(newParty);
            Collections.sort(parties);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    private AdapterView.OnItemClickListener getListViewItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, PartyActivity.class);
                intent.putExtra("party", ((PartyListItemAdapter) mListview.getAdapter())
                        .getFilteredParties().get(i));
                intent.putExtra("members", (ArrayList<Member>) Placeholders.getPlaceHolderMembers());
                startActivity(intent);
            }
        };
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
                editParty(info.position);
                return true;
            case R.id.delete:
                // user wants to delete a party
                deleteParty(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editParty(int position) {
        List<Party> filteredParties = ((PartyListItemAdapter) mListview.getAdapter())
                .getFilteredParties();
        Party partyToEdit = filteredParties.get(position);
        Intent intent = new Intent(mActivity, NewPartyActivity.class);
        intent.putExtra("party", partyToEdit);
        startActivityForResult(intent, 1);
    }

    private class DeletePartyTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;
        private int selectedPartyId;
        private int selectedPartyIdx;

        public DeletePartyTask(int selectedPartyId, int selectedPartyIdx) {
            this.selectedPartyId = selectedPartyId;
            this.selectedPartyIdx = selectedPartyIdx;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                controller.deleteParty(selectedPartyId);
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(mActivity.getApplicationContext(),
                        R.string.party_delete_error + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            List<Party> filteredParties = ((PartyListItemAdapter) mListview.getAdapter())
                    .getFilteredParties();
            Party partyToRemove = filteredParties.get(selectedPartyIdx);
            filteredParties.remove(selectedPartyIdx);
            parties.remove(partyToRemove);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    private void deleteParty(final int partyIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog d = builder.setMessage(R.string.delete_party_confirm)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeletePartyTask(parties.get(partyIndex).getId(), partyIndex).execute();
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
