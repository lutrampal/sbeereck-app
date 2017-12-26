package com.sbeereck.lutrampal.applisbeereck;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

        drawer.openDrawer(GravityCompat.START);
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
        switch (item.getItemId()) {
            case R.id.action_members:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MembersFragment())
                        .commit();
                setTitle(R.string.members_activity_name);
                break;
            case R.id.action_parties:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PartiesFragment())
                        .commit();
                setTitle(R.string.parties_activity_name);
                break;
            case R.id.action_products:
                return true;
            case R.id.action_settings:
                return true;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
