package com.sbeereck.lutrampal.view;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewTransactionActivity extends AppCompatActivity {

    private Party party = null;
    private List<Member> members = null;
    private Member selectedMember = null;
    private RadioGroup radioGroup;
    private TextView tvBalance;
    private TextView tvBalanceTitle;
    private AutoCompleteTextView tvMemberName;
    private float transactionAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        party = (Party) getIntent().getSerializableExtra("party");
        members = (ArrayList<Member>) getIntent().getSerializableExtra("members");
        tvBalance = findViewById(R.id.member_balance);
        tvBalanceTitle = findViewById(R.id.balance_title);

        radioGroup = findViewById(R.id.type_rg);
        radioGroup.setOnCheckedChangeListener(getCheckedTypeChangedListener());
        Fragment f = new BeerTransactionFragment();
        Bundle args = new Bundle();
        args.putSerializable("servedBeers", (HashMap< Product, BeerCategory>) party.getServedBeers());
        args.putFloat("normalPrice", party.getNormalBeerPrice());
        args.putFloat("specialPrice", party.getSpecialBeerPrice());
        f.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.type_content_frame, f)
                .commit();

        setUpMemberSelection();
    }

    private void setUpMemberSelection() {
        tvMemberName = findViewById(R.id.member_name);
        tvMemberName.setAdapter(new MemberAutocompleteTextViewAdapter(this, members));
        tvMemberName.setOnItemClickListener(getOnAutocompleteItemClickListener());
        tvMemberName.addTextChangedListener(getMemberNameTextChangedListener());
    }

    private TextWatcher getMemberNameTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (selectedMember!= null &&
                        !selectedMember.getName().toLowerCase()
                                .equals(s.toString().toLowerCase().trim())) {
                    selectedMember = null;
                    tvBalance.setText("");
                    tvBalanceTitle.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    private AdapterView.OnItemClickListener getOnAutocompleteItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMember = (Member) tvMemberName.getAdapter().getItem(position);
                tvMemberName.setText(selectedMember.getName());
                tvMemberName.setSelection(tvMemberName.getText().toString().length());
                updateBalanceTextView();
            }
        };
    }

    private void updateBalanceTextView() {
        String sign = "+";
        if (selectedMember.getBalance() < 0) {
            sign = "";
        }
        if (selectedMember.getBalance() < -10) { // this threshold should be changed dynamically
            tvBalance.setTextColor(getResources().getColor(R.color.colorBalanceTooLow));
        } else {
            tvBalance.setTextColor(getResources().getColor(R.color.colorPositiveBalance));
        }
        tvBalance.setText(sign + selectedMember.getBalance() + "€");
        tvBalanceTitle.setVisibility(View.VISIBLE);
    }


    private RadioGroup.OnCheckedChangeListener getCheckedTypeChangedListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment f = null;
                Bundle args = new Bundle();
                switch (checkedId) {
                    case R.id.beer_rb:
                        f = new BeerTransactionFragment();
                        args.putSerializable("servedBeers", (HashMap< Product, BeerCategory>) party.getServedBeers());
                        args.putFloat("normalPrice", party.getNormalBeerPrice());
                        args.putFloat("specialPrice", party.getSpecialBeerPrice());
                        break;
                    case R.id.deposit_rb:
                        f = new DepositTransactionFragment();
                        break;
                    case R.id.food_rb:
                        f = new FoodTransactionFragment();
                        break;
                    case R.id.refill_or_other_rb:
                        f = new OtherTransactionFragment();
                        break;
                }
                if (f != null) {
                    f.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.type_content_frame, f)
                            .commit();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_validate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.validate:
                if (selectedMember == null) return true;
                if (selectedMember.getBalance() < -10 && transactionAmount <= 0) { // threshold should be changed dynamically
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.transaction_balance_too_low_title)
                            .setMessage(selectedMember.getName() + " devrait penser à recharger son compte... (" + selectedMember.getBalance() + "€)")
                            .setPositiveButton(R.string.add_transaction_anyway, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    validateTransaction();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .create()
                            .show();
                } else {
                    validateTransaction();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateTransaction() {
        Intent resultIntent = new Intent();
        setResult(1, resultIntent);
        NewTransactionActivity.this.finish();
    }
}
