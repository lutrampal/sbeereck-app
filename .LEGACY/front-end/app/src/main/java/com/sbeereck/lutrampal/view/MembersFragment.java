package com.sbeereck.lutrampal.view;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.TransactionController;
import com.sbeereck.lutrampal.model.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends GeneralMainViewFragment
    implements OnOkButtonClickListener<Member>{

    private List<Member> members = new ArrayList<>();
    private MemberController controller;
    private float balanceTooLowThreshold = 0;
    private FloatingActionButton mFabExport = null;

    @Override
    public void onOkButtonClick(Member obj, Boolean wasEditing) {
        members.add(obj);
        Collections.sort(members);
        ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
    }

    private class GetAllMembersTask extends AsyncTaskWithLoadAnimation<Void, Integer, List<Member>> {

        private Exception e = null;

        public GetAllMembersTask(Context context) {
            super(context);
        }

        @Override
        protected List<Member> doInBackground(Void ... voids) {
            List<Member> members = null;
            try {
                RESTDataManager dataManager =
                        RESTDataManagerSingleton.getDataManager(getActivity());
                if (dataManager != null) {
                    balanceTooLowThreshold = new TransactionController(dataManager)
                            .getBalanceThreshold();
                }
            } catch (Exception e) {
                balanceTooLowThreshold = 0;
            }
            ((MemberListItemAdapter) mListview.getAdapter())
                    .setBalanceTooLowThreshold(balanceTooLowThreshold);
            try {
                members = controller.getAllMembers();
            } catch (Exception e) {
                this.e = e;
                members = new ArrayList<>();
            }
            return members;
        }

        @Override
        protected void onPostExecute(List<Member> members) {
            super.onPostExecute(members);
            if (e != null) {
                Toast.makeText(mActivity.getApplicationContext(),
                        getString(R.string.members_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            MembersFragment.this.members.clear();
            MembersFragment.this.members.addAll(members);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    public MembersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_members, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        mFabExport = view.findViewById(R.id.fab_export);
        RESTDataManager dataManager = RESTDataManagerSingleton.getDataManager(getActivity());
        if (dataManager != null) {
            controller = new MemberController(dataManager);
            new GetAllMembersTask(getActivity()).execute();
        } else {
            mFabAdd.setEnabled(false);
            mFabExport.setEnabled(false);
        }
        mListview.setAdapter(new MemberListItemAdapter(getActivity(), members));
        mListview.setOnItemClickListener(getListViewItemClickListener());
        mFabAdd.setOnClickListener(getFabAddClickListener());
        mActivity.getSupportActionBar().setTitle(R.string.members_fragment_name);
        return view;
    }

    private View.OnClickListener getFabAddClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMemberDialogFragment newMemberDialog = new NewMemberDialogFragment();
                newMemberDialog.setOnOkButtonClickListener(MembersFragment.this);
                newMemberDialog.show(mActivity.getFragmentManager(),
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
                Member m = (Member) mListview.getAdapter().getItem(i);
                args.putSerializable("id", m.getId());
                args.putSerializable("balance_too_low_threshold", balanceTooLowThreshold);
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
            Member selectedMember = (Member) mListview.getAdapter().getItem((int) info.id);
            if (selectedMember.isMembershipValid()) {
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
                renewMembership(info.position);
                return true;
            case R.id.export_member:
                // user wants to export a member
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateBalance(final int position) {
        UpdateBalanceDialogFragment infoDialog = new UpdateBalanceDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("member", (Member) mListview.getAdapter().getItem(position));
        infoDialog.setArguments(args);
        infoDialog.show(mActivity.getSupportFragmentManager(), "UpdateBalanceDialogFragment");
        infoDialog.setOnOkButtonClickListener(new OnOkButtonClickListener<Member>() {
            @Override
            public void onOkButtonClick(Member m, Boolean wasEditing) {
                new GetAllMembersTask(getActivity()).execute();
            }
        });
    }

    private void renewMembership(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        Dialog d = builder.setMessage(R.string.membership_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked yes button
                        new RenewMembershipTask(((Member) mListview.getAdapter().getItem(position))).execute();
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
                        List<Member> filteredMembers = ((MemberListItemAdapter) mListview.getAdapter())
                                .getFilteredMembers();
                        Member memberToRemove = filteredMembers.get(position);
                        new DeleteMemberTask(memberToRemove, position).execute();
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

    private class DeleteMemberTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;
        private Member selectedMember;
        private int selectedMemberIdx;

        public DeleteMemberTask(Member selectedMember, int selectedMemberIdx) {
            this.selectedMember = selectedMember;
            this.selectedMemberIdx = selectedMemberIdx;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                controller.deleteMember(selectedMember.getId());
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(mActivity.getApplicationContext(),
                        getString(R.string.member_delete_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            List<Member> filteredMembers = ((MemberListItemAdapter) mListview.getAdapter())
                    .getFilteredMembers();
            filteredMembers.remove(selectedMemberIdx);
            members.remove(selectedMember);
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

    private class RenewMembershipTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;
        private Member member;

        public RenewMembershipTask(Member member) {
            this.member = member;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                controller.renewMembership(member.getId());
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(mActivity.getApplicationContext(),
                        getString(R.string.renew_membership_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            member.setLastMembershipPayment(new Date());
            ((BaseAdapter) mListview.getAdapter()).notifyDataSetChanged();
        }
    }

}
