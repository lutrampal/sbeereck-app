package com.sbeereck.lutrampal.applisbeereck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewPartyActivity extends AppCompatActivity {

    private ListView beersListView;
    private Party party;
    private Map<Product, BeerCategory> servedBeers;
    private Calendar calendar;
    private EditText dateEt;
    private EditText nameEt;
    private EditText normalPriceEt;
    private EditText specialPriceEt;
    private DatePickerDialog currentDatePicker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_party);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEt = findViewById(R.id.party_name);
        normalPriceEt = findViewById(R.id.normal_beer_price);
        specialPriceEt = findViewById(R.id.special_beer_price);
        dateEt = findViewById(R.id.party_date);

        if (getIntent().getSerializableExtra("party") != null) {
            makeEditPartyActivity();
        } else {
            party = null;
            servedBeers = new HashMap<>();
            getSupportActionBar().setTitle(R.string.title_activity_new_party);
        }

        beersListView = findViewById(R.id.beers_list_view);
        beersListView.setAdapter(new BeerListItemAdapter(this, servedBeers));
        registerForContextMenu(beersListView);

        setupDatePicker();

        findViewById(R.id.add_beer_button).setOnClickListener(getAddBeerButtonOnClickListener());
    }

    private View.OnClickListener getAddBeerButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPartyActivity.this, AddBeersActivity.class);
                intent.putExtra("beers", (ArrayList<Product>) Placeholders.getPlaceHolderBeers());
                intent.putExtra("alreadySelectedBeers", (HashMap<Product, BeerCategory>) servedBeers);
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == requestCode) {
            servedBeers = (Map<Product, BeerCategory>) data.getSerializableExtra("selectedBeers");
            beersListView.setAdapter(new BeerListItemAdapter(this, servedBeers));
        }
    }

    private void makeEditPartyActivity() {
        party = (Party) getIntent().getSerializableExtra("party");
        servedBeers = party.getServedBeers();
        getSupportActionBar().setTitle(R.string.title_activity_edit_party);
        nameEt.setText(party.getName());
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        dateEt.setText(df.format(party.getDate()));
        normalPriceEt.setText(String.valueOf(party.getNormalBeerPrice()));
        specialPriceEt.setText(String.valueOf(party.getSpecialBeerPrice()));
    }

    private void setupDatePicker() {
        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateEt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentDatePicker == null || !currentDatePicker.isShowing()) {
                    currentDatePicker = new DatePickerDialog(NewPartyActivity.this, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    currentDatePicker.show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        dateEt.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == R.id.beers_list_view) {
            inflater.inflate(R.menu.menu_delete, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                // user wants to delete a party
                deleteBeer(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteBeer(int position) {
        Product beerToRemove = (Product) servedBeers.keySet().toArray()[position];
        servedBeers.remove(beerToRemove);
        ((BaseAdapter) beersListView.getAdapter()).notifyDataSetChanged();
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
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
