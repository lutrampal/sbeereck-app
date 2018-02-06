package com.sbeereck.lutrampal.view;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.controller.ProductController;
import com.sbeereck.lutrampal.controller.TransactionController;
import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;
import com.sbeereck.lutrampal.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NewTransactionActivity extends AppCompatActivity {

    private Party party = null;
    private Member selectedMember = null;
    private RadioGroup radioGroup;
    private TextView tvBalance;
    private TextView tvBalanceTitle;
    private AutoCompleteTextView tvMemberName;
    private float balanceTooLowThreshold = 0;
    private MemberController memberController;
    private ProductController productController;
    private TransactionController transactionController;
    private Fragment currentTransactionFragment = null;

    private TextView quantityTv = null;
    private ListView productLv = null;
    private RadioGroup glassSizeRg = null;
    private RadioGroup depositRg = null;
    private RadioButton depositRb = null;
    private RadioButton foodRb = null;
    private EditText labelEt = null;
    private EditText amountEt = null;

    private LinkedList<Product> food = new LinkedList<>();
    private LinkedList<Product> deposits = new LinkedList<>();

    private class GetProductsAndEnableRadioButtonTask
            extends AsyncTask<Void, Integer, List<Product>> {
        private Exception e = null;
        private List<Product> listToAddTo = null;
        private RadioButton rbToEnable = null;
        private int errorStringId = 0;
        private ProductType type = null;

        public GetProductsAndEnableRadioButtonTask(List<Product> listToAddTo,
                                                   RadioButton rbToEnable, int errorStringId,
                                                   ProductType type) {
            this.listToAddTo = listToAddTo;
            this.rbToEnable = rbToEnable;
            this.errorStringId = errorStringId;
            this.type = type;
        }

        @Override
        protected List<Product> doInBackground(Void ... voids) {
            List<Product> products = null;
            try {
                products = productController.getProductsByType(type);
            } catch (Exception e) {
                this.e = e;
                products = new LinkedList<>();
            }
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (e != null) {
                Toast.makeText(NewTransactionActivity.this,
                        getString(errorStringId) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            listToAddTo.addAll(products);
            rbToEnable.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        party = (Party) getIntent().getSerializableExtra("party");
        tvBalance = findViewById(R.id.member_balance);
        tvBalanceTitle = findViewById(R.id.balance_title);

        radioGroup = findViewById(R.id.type_rg);
        depositRb = findViewById(R.id.deposit_rb);
        foodRb = findViewById(R.id.food_rb);
        radioGroup.setOnCheckedChangeListener(getCheckedTypeChangedListener());
        memberController = new MemberController(Placeholders.getPlaceHolderDataManager());
        productController = new ProductController(Placeholders.getPlaceHolderDataManager());
        transactionController = new TransactionController(Placeholders.getPlaceHolderDataManager());
        new GetBalanceThresholdTask().execute();
        new GetProductsAndEnableRadioButtonTask(food, foodRb,
                R.string.food_loading_error, ProductType.FOOD).execute();
        new GetProductsAndEnableRadioButtonTask(deposits, depositRb,
                R.string.deposits_loading_error, ProductType.DEPOSIT).execute();
        new SetUpMemberSelectionTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // executePendingTransactions() won't work if called inside onCreate
        swapTransactionFragment(radioGroup.getCheckedRadioButtonId());
    }

    private class SetUpMemberSelectionTask extends AsyncTask<Void, Integer, List<Member>> {

        private Exception e = null;

        @Override
        protected List<Member> doInBackground(Void ... voids) {
            List<Member> members = null;
            try {
                members = memberController.getAllMembers();
            } catch (Exception e) {
                this.e = e;
                members = new ArrayList<>();
            }
            return members;
        }

        @Override
        protected void onPostExecute(List<Member> members) {
            if (e != null) {
                Toast.makeText(NewTransactionActivity.this,
                        getString(R.string.parties_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            tvMemberName = findViewById(R.id.member_name);
            tvMemberName.setAdapter(new MemberAutocompleteTextViewAdapter(NewTransactionActivity.this,
                    members));
            tvMemberName.setOnItemClickListener(getOnAutocompleteItemClickListener());
            tvMemberName.addTextChangedListener(getMemberNameTextChangedListener());
        }
    }

    private class GetBalanceThresholdTask extends AsyncTask<Void, Integer, Float> {

        private Exception e = null;

        @Override
        protected Float doInBackground(Void ... voids) {
            try {
                return transactionController.getBalanceThreshold();
            } catch (Exception e) {
                this.e = e;
                return 0f;
            }
        }

        @Override
        protected void onPostExecute(Float threshold) {
            if (e != null) {
                Toast.makeText(NewTransactionActivity.this,
                        getString(R.string.threshold_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            balanceTooLowThreshold = threshold;
        }
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
                        !(selectedMember.getFirstName() + " " + selectedMember.getLastName())
                                .toLowerCase().equals(s.toString().toLowerCase().trim())) {
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
                tvMemberName.setText(selectedMember.getFirstName() + " "
                        + selectedMember.getLastName());
                tvMemberName.setSelection(tvMemberName.getText().toString().length());
                selectedMember = (Member) tvMemberName.getAdapter().getItem(position);
                updateBalanceTextView();
                InputMethodManager inputManager =
                        (InputMethodManager) NewTransactionActivity.this.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        NewTransactionActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
    }

    private void updateBalanceTextView() {
        String sign = "+";
        if (selectedMember.getBalance() < 0) {
            sign = "";
        }
        if (!selectedMember.isMembershipValid()) {
            tvMemberName.setTextColor(getResources().getColor(
                    R.color.colorOutdatedMembership));
        } else if (selectedMember.getBalance() < balanceTooLowThreshold) {
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
                swapTransactionFragment(checkedId);
            }
        };
    }

    private void swapTransactionFragment(int rbId) {
        currentTransactionFragment = null;
        Bundle args = new Bundle();
        switch (rbId) {
            case R.id.beer_rb:
                currentTransactionFragment = new BeerTransactionFragment();
                args.putSerializable("servedBeers",
                        (HashMap< Product, BeerCategory>) party.getServedBeers());
                args.putFloat("normalPrice", party.getNormalBeerPrice());
                args.putFloat("specialPrice", party.getSpecialBeerPrice());
                break;
            case R.id.deposit_rb:
                currentTransactionFragment = new DepositTransactionFragment();
                args.putSerializable("deposits", deposits);
                break;
            case R.id.food_rb:
                currentTransactionFragment = new FoodTransactionFragment();
                args.putSerializable("food", food);
                break;
            case R.id.refill_or_other_rb:
                currentTransactionFragment = new OtherTransactionFragment();
                break;
            default:
                return;
        }
        currentTransactionFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.type_content_frame, currentTransactionFragment)
                .commit();
        getFragmentManager().executePendingTransactions();
        View fragmentView = currentTransactionFragment.getView();
        quantityTv = fragmentView.findViewById(R.id.quantity_tv);
        productLv = fragmentView.findViewById(R.id.products_listview);
        switch (rbId) {
            case R.id.beer_rb:
                glassSizeRg = fragmentView.findViewById(R.id.glass_size_rg);
                break;
            case R.id.deposit_rb:
                depositRg = fragmentView.findViewById(R.id.deposit_rg);
                break;
            case R.id.refill_or_other_rb:
                labelEt = fragmentView.findViewById(R.id.label_et);
                amountEt = fragmentView.findViewById(R.id.amount_et);
                break;
        }
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
                validateTransaction();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateTransaction() {
        if (selectedMember == null) {
            return;
        }
        final Transaction newTransaction = buildTransactionFromForm();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.add_transaction_anyway, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AddTransactionTask(newTransaction).execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        if (!selectedMember.isMembershipValid()) {
            builder.setTitle(R.string.transaction_outdated_membership_title)
                    .setMessage(selectedMember.getFirstName() + " n'a pas payé sa cotisation...")
                    .create()
                    .show();

        } else if (selectedMember.getBalance() < balanceTooLowThreshold
                && newTransaction.getAmount() <= 0) {
            builder.setTitle(R.string.transaction_balance_too_low_title)
                    .setMessage(selectedMember.getFirstName()
                            + " devrait penser à recharger son compte... ("
                            + String.format("%.2f", selectedMember.getBalance()) + "€)")
                    .create()
                    .show();
        } else {
            new AddTransactionTask(newTransaction).execute();;
        }
    }

    private class AddTransactionTask extends AsyncTask<Void, Integer, Void> {

        private Exception e = null;
        private Transaction newTransaction;

        public AddTransactionTask(Transaction newTransaction) {
            this.newTransaction = newTransaction;
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                newTransaction.setId(transactionController.addTransaction(newTransaction));
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            if (e != null) {
                Toast.makeText(NewTransactionActivity.this,
                        getString(R.string.transaction_add_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("transaction", newTransaction);
            setResult(1, resultIntent);
            NewTransactionActivity.this.finish();
        }
    }

    private Transaction buildTransactionFromForm() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.beer_rb:
                return buildBeerTransactionFromForm();
            case R.id.deposit_rb:
                return buildDepositTransactionFromForm();
            case R.id.food_rb:
                return buildFoodTransactionFromForm();
            case R.id.refill_or_other_rb:
                return buildOtherTransactionFromForm();
        }
        return null;
    }

    private Transaction buildDepositTransactionFromForm() {
        int quantity = Integer.parseInt(quantityTv.getText().toString());
        String s = "";
        String rendue = "";
        int cashBack = -1;
        if (quantity > 1) {
            s = "s";
        }
        Product selectedDeposit = (Product) productLv.getItemAtPosition(
                productLv.getCheckedItemPosition());
        if (depositRg.getCheckedRadioButtonId() == R.id.cash_out_deposit_rb) {
            rendue = " rendue" + s;
            cashBack = 1;
        }
        return new Transaction(selectedMember, party,
                cashBack*quantity*selectedDeposit.getPrice(),
                quantity + " caution" + s + " " + selectedDeposit.getName() + rendue);
    }

    private Transaction buildBeerTransactionFromForm() {
        float beerPrice = 0;
        int quantity = Integer.parseInt(quantityTv.getText().toString());
        int glassSizeMultiplier = 1;
        String glassType = "";
        String s = "";
        if (quantity > 1) {
            s = "s";
        }
        Map.Entry<Product, BeerCategory> selectedBeer =
                (Map.Entry<Product, BeerCategory>) productLv.getItemAtPosition(
                        productLv.getCheckedItemPosition());
        switch (glassSizeRg.getCheckedRadioButtonId()) {
            case R.id.half_pint_rb:
                glassType = "demi";
                break;
            case R.id.pint_rb:
                glassSizeMultiplier = Product.NB_HALF_PINTS_FOR_A_PINT;
                glassType = "pinte";
                break;
            case R.id.pitcher_rb:
                glassSizeMultiplier = Product.NB_HALF_PINTS_FOR_A_PITCHER;
                glassType = "pichet";
                break;
        }
        switch (selectedBeer.getValue()) {
            case NORMAL:
                beerPrice = party.getNormalBeerPrice();
                break;
            case SPECIAL:
                beerPrice = party.getSpecialBeerPrice();
                break;
        }
        return new Transaction(selectedMember, party,
                -quantity*beerPrice*glassSizeMultiplier,
                quantity + " " + glassType + s + " de " + selectedBeer.getKey().getName());
    }

    private Transaction buildFoodTransactionFromForm() {
        int quantity = Integer.parseInt(quantityTv.getText().toString());
        Product selectedFood = (Product) productLv.getItemAtPosition(
                productLv.getCheckedItemPosition());
        return new Transaction(selectedMember, party,
                -quantity*selectedFood.getPrice(),
                quantity + " \"" + selectedFood.getName() + "\"");
    }

    private Transaction buildOtherTransactionFromForm() {
        return new Transaction(selectedMember, party,
                Float.parseFloat(amountEt.getText().toString()), labelEt.getText().toString());
    }
}
