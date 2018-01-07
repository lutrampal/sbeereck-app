package com.sbeereck.lutrampal.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.ProductController;
import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBeersActivity extends AppCompatActivity {

    private ListView beersListView;
    private List<Product> beers = new ArrayList<>();
    private ProductController controller;

    private class GetAllBeersTask extends AsyncTask<Void, Integer, List<Product>> {

        private Exception e = null;

        @Override
        protected List<Product> doInBackground(Void ... voids) {
            List<Product> beers = null;
            try {
                beers = controller.getProductsByType(ProductType.BEER);
            } catch (Exception e) {
                this.e = e;
                beers = new ArrayList<>();
            }
            return beers;
        }

        @Override
        protected void onPostExecute(List<Product> beers) {
            if (e != null) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.beers_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            AddBeersActivity.this.beers.clear();
            AddBeersActivity.this.beers.addAll(beers);
            ((BaseAdapter) beersListView.getAdapter()).notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(getFabOnClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller = new ProductController(Placeholders.getPlaceHolderDataManager());
        new GetAllBeersTask().execute();

        Map<Product, BeerCategory> alreadySelectedBeers;
        if (getIntent().getSerializableExtra("alreadySelectedBeers") != null) {
            alreadySelectedBeers = (Map<Product, BeerCategory>) getIntent()
                    .getSerializableExtra("alreadySelectedBeers");
        } else {
            alreadySelectedBeers = new HashMap<>();
        }
        beersListView = findViewById(R.id.beers_list_view);
        beersListView.setAdapter(new AddBeerListItemAdapter(this, beers,
                alreadySelectedBeers));
    }

    private View.OnClickListener getFabOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewProductDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewProductDialogFragment");
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedBeers", getSelectedBeersMap());
                setResult(1, resultIntent);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private HashMap<Product, BeerCategory> getSelectedBeersMap() {
        return (HashMap<Product, BeerCategory>) ((AddBeerListItemAdapter) beersListView
                .getAdapter()).getSelectedBeers();
    }

}
