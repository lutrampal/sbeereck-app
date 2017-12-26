package com.sbeereck.lutrampal.applisbeereck;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.School;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends GeneralMainViewFragment {


    private List<Member> getPlaceHolderMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("Kerboul Thomas", -50, School.ENSE3, "kerboul@gmail.com", "0635368817", true));
        members.add(new Member("Bertaux Benjamin", 10.5f, School.ENSIMAG, "ber.ben@gmail.com", "0635368817", false));
        members.add(new Member("Trampal Lucas", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        return members;
    }

    private List<Member> members;

    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_members, container, false);
        members = getPlaceHolderMembers();
        super.onCreateView(inflater, container, savedInstanceState);
        mListview.setAdapter(new MemberListItemAdapter(getActivity(), members));
        mListview.setOnItemClickListener(getListViewItemClickListener());
        mFabAdd.setOnClickListener(getFabAddClickListener());
        mActivity.getSupportActionBar().setTitle(R.string.members_activity_name);
        return view;
    }

    private View.OnClickListener getFabAddClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newMemberDialog = new NewMemberDialogFragment();
                newMemberDialog.show(mActivity.getSupportFragmentManager(),
                        "NewMemberDialogFragment");
            }
        };
    }

    private AdapterView.OnItemClickListener getListViewItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogFragment infoDialog = new MemberInfoDialogFragment();
                Bundle args = new Bundle();
                Member m =  ((MemberListItemAdapter) mListview.getAdapter())
                        .getFilteredMembers().get(i);
                args.putString("name", m.getName());
                args.putFloat("balance", m.getBalance());
                args.putString("school", School.getName(m.getSchool()));
                args.putString("email", m.getEmail());
                args.putString("phone", m.getPhone());
                args.putBoolean("membership", m.getMembership());
                infoDialog.setArguments(args);
                infoDialog.show(mActivity.getSupportFragmentManager(),
                        "MemberInfoDialogFragment");
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        MenuInflater inflater = mActivity.getMenuInflater();
        if (v.getId()==R.id.main_listview) {
            inflater.inflate(R.menu.menu_member, menu);
            Member selectedMember = ((MemberListItemAdapter) mListview.getAdapter())
                    .getFilteredMembers().get((int)info.id);
            if (selectedMember.getMembership()) {
                menu.getItem(0).setVisible(false);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.update_balance:
                // user wants to change the balance of a member
                updateBalance(info.position);
                return true;
            case R.id.delete_member:
                // user wants to delete a member
                deleteMember(info.position);
                return true;
            case R.id.pay_membership:
                payMembership(info.position);
                return true;
            case R.id.export_member:
                // user wants to export a member
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateBalance(final int position) {
        DialogFragment infoDialog = new UpdateBalanceDialogFragment();
        infoDialog.show(mActivity.getSupportFragmentManager(), "UpdateBalanceDialogFragment");
    }

    private void payMembership(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        Dialog d = builder.setMessage(R.string.membership_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked yes button
                        ((MemberListItemAdapter) mListview.getAdapter()).getFilteredMembers()
                                .get(position).setMembership(true);
                        ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked cancel button
                    }
                })
                .create();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.show();
    }

    private void deleteMember(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        Dialog d = builder.setMessage(R.string.delete_member_confirm)
                .setPositiveButton(R.string.delete_member, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked delete button
                        List<Member> filteredMembers = ((MemberListItemAdapter) mListview.getAdapter())
                                .getFilteredMembers();
                        Member memberToRemove = filteredMembers.get(position);
                        filteredMembers.remove(position);
                        members.remove(memberToRemove);
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
