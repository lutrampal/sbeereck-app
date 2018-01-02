package com.sbeereck.lutrampal.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new PartiesFragment())
                .commit();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.sbeereck_art).setOnClickListener(getSbeerEckArtClickListener());
    }

    private View.OnClickListener getSbeerEckArtClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    byte[] b = {(byte) 0xF0, (byte) 0x9F, (byte) 0x8D, (byte) 0xBB};
                    String beers = new String(b, "UTF8");
                    Toast.makeText(getApplicationContext(), "!! " + beers + beers + beers + " !!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment f = null;
        String fragmentName = null;
        switch (item.getItemId()) {
            case R.id.action_members:
                f = new MembersFragment();
                fragmentName = "members";
                break;
            case R.id.action_parties:
                f = new PartiesFragment();
                fragmentName = "parties";
                break;
            case R.id.action_products:
                f = new BeersAndOthersFragment();
                fragmentName = "products";
                break;
            case R.id.action_settings:
                return true;
            default:
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
