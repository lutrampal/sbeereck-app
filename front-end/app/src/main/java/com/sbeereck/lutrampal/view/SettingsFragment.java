package com.sbeereck.lutrampal.view;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.PartyController;
import com.sbeereck.lutrampal.controller.TransactionController;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import java.util.LinkedList;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * Created by lutrampal on 08/02/18 for S'Beer Eck.
 */

public class SettingsFragment extends Fragment {

    private List<AsyncTask> tasks = new LinkedList<>();

    protected void addTaskToRunningAsyncTasks(AsyncTask task) {
        tasks.add(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (AsyncTask task: tasks) {
            task.cancel(true);
        }
    }

    private AppCompatActivity mActivity;
    private ActionBarDrawerToggle mDrawerToggle;
    private View view;
    private float balanceTooLowThreshold = 0;
    private float defaultNormalBeerPrice = 0;
    private float defaultSpecialBeerPrice = 0;

    private EditText serverAddressEt;
    private EditText passwordEt;
    private EditText balanceThresholdEt;
    private EditText defaultNormalPriceEt;
    private EditText defaultSpecialPriceEt;

    private TransactionController transactionController = new TransactionController(
            Placeholders.getPlaceHolderDataManager());
    private PartyController partyController = new PartyController(
            Placeholders.getPlaceHolderDataManager());

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (AppCompatActivity)getActivity();
        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = mActivity.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(mActivity, drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        mActivity.getSupportActionBar().setTitle(R.string.settings);
        setHasOptionsMenu(true);

        serverAddressEt = view.findViewById(R.id.server_address_et);
        passwordEt = view.findViewById(R.id.password_et);
        balanceThresholdEt = view.findViewById(R.id.balance_et);
        defaultNormalPriceEt = view.findViewById(R.id.normal_et);
        defaultSpecialPriceEt = view.findViewById(R.id.special_et);
        new GetPreferencesTask().execute();
        recoverCredentials();
        return view;
    }

    private void recoverCredentials() {
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        String serverAdress = sharedPref.getString(getString(R.string.saved_server_address), "");
        serverAddressEt.setText(serverAdress);


        Store store = new Store(getActivity().getApplicationContext());
        if (!store.hasKey(getString(R.string.sbeereck_key))) {
            passwordEt.setText("");
            return;
        }
        SecretKey key = store.getSymmetricKey(getString(R.string.sbeereck_key), null);
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
        String decrPass = crypto.decrypt(
                sharedPref.getString(getString(R.string.saved_server_password), ""), key);
        passwordEt.setText(decrPass);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_validate, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {
            case R.id.validate:
                try {
                    defaultNormalBeerPrice =
                            Float.parseFloat(defaultNormalPriceEt.getText().toString());
                    defaultSpecialBeerPrice =
                            Float.parseFloat(defaultSpecialPriceEt.getText().toString());
                    balanceTooLowThreshold =
                            Float.parseFloat(balanceThresholdEt.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
                    break;
                }
                new SavePreferencesTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPreferencesTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;

        @Override
        protected Void doInBackground(Void ... voids) {
            addTaskToRunningAsyncTasks(this);
            try {
                defaultNormalBeerPrice = partyController.getDefaultNormalBeerPrice();
                defaultSpecialBeerPrice = partyController.getDefaultSpecialBeerPrice();
                balanceTooLowThreshold = transactionController.getBalanceThreshold();
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (e != null) {
                Toast.makeText(getContext(),
                        getString(R.string.preferences_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            defaultNormalPriceEt.setText(String.valueOf(defaultNormalBeerPrice));
            defaultSpecialPriceEt.setText(String.valueOf(defaultSpecialBeerPrice));
            balanceThresholdEt.setText(String.valueOf(balanceTooLowThreshold));
        }
    }

    private class SavePreferencesTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;

        @Override
        protected Void doInBackground(Void ... voids) {
            addTaskToRunningAsyncTasks(this);
            try {
                partyController.setDefaultNormalBeerPrice(defaultNormalBeerPrice);
                partyController.setDefaultSpecialBeerPrice(defaultSpecialBeerPrice);
                transactionController.setBalanceThreshold(balanceTooLowThreshold);
                storeCredentials();
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (e != null) {
                Toast.makeText(getContext(),
                        getString(R.string.preferences_saving_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),
                        getString(R.string.preferences_saved), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void storeCredentials() {
        SharedPreferences sharedPref =
                getActivity().getSharedPreferences(getString(R.string.app_preferences),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_server_address),
                serverAddressEt.getText().toString());

        Store store = new Store(getActivity().getApplicationContext());
        if (!store.hasKey(getString(R.string.sbeereck_key))) {
            SecretKey key = store.generateSymmetricKey(getString(R.string.sbeereck_key),
                    null);
        }
        SecretKey key = store.getSymmetricKey(getString(R.string.sbeereck_key), null);
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
        String encrPass = crypto.encrypt(passwordEt.getText().toString(), key);
        editor.putString(getString(R.string.saved_server_password), encrPass);

        editor.apply();
    }
}
