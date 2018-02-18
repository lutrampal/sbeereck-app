package com.sbeereck.lutrampal.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sbeereck.lutrampal.controller.ProductController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AddBeersActivity extends ActivityWithAsyncTasks {

    private ListView beersListView;
    private List<Product> beers = new ArrayList<>();
    private ProductController controller;

    private class GetAllBeersTask extends AsyncTaskWithLoadAnimation<Void, Integer, List<Product>> {

        private Exception e = null;

        public GetAllBeersTask(Context context) {
            super(context);
        }

        @Override
        protected List<Product> doInBackground(Void ... voids) {
            addTaskToRunningAsyncTasks(this);
            try {
                beers.addAll(controller.getProductsByType(ProductType.BEER));
            } catch (Exception e) {
                this.e = e;
            }
            return beers;
        }

        @Override
        protected void onPostExecute(List<Product> beers) {
            super.onPostExecute(beers);
            if (e != null) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.beers_loading_error) + " : " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
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
        RESTDataManager dataManager = RESTDataManagerSingleton
                .getDataManager(getApplicationContext());
        if (dataManager == null) {
            return;
        }
        controller = new ProductController(dataManager);
        new GetAllBeersTask(this).execute();

        HashMap<Product, BeerCategory> alreadySelectedBeers;
        if (getIntent().getSerializableExtra("alreadySelectedBeers") != null) {
            alreadySelectedBeers = (HashMap<Product, BeerCategory>) getIntent()
                    .getSerializableExtra("alreadySelectedBeers");
        } else {
            alreadySelectedBeers = new HashMap<>();
        }
        beersListView = findViewById(R.id.products_listview);
        beersListView.setAdapter(new AddBeerListItemAdapter(this, beers,
                alreadySelectedBeers));
    }

    private View.OnClickListener getFabOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewProductDialogFragment dialog = new NewProductDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewProductDialogFragment");
                dialog.setOnOkButtonClickListener(new OnOkButtonClickListener() {
                    @Override
                    public void onOkButtonClick(Object obj, Boolean wasEditing) {
                        new GetAllBeersTask(getApplicationContext()).execute();
                    }
                });
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
        return (HashMap<Product, BeerCategory>) ((AddBeerListItemAdapter)
                beersListView.getAdapter()).getSelectedBeers();
    }

}
