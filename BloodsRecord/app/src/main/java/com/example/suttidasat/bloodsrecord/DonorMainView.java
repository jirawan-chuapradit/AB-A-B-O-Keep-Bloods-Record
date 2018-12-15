package com.example.suttidasat.bloodsrecord;


import android.content.Intent;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.example.suttidasat.bloodsrecord.Interface.DonorProfileFragment;

import com.example.suttidasat.bloodsrecord.Interface.PrepareDonate;
import com.example.suttidasat.bloodsrecord.Interface.TimeLineFragment;

import com.example.suttidasat.bloodsrecord.Interface.UpdatePasswordFragment;
import com.example.suttidasat.bloodsrecord.Interface.News.ViewNews;
import com.example.suttidasat.bloodsrecord.model.MyService;

import com.google.firebase.auth.FirebaseAuth;


/***********************************************
 *intent: main view of Donor used every page   *
 *precondition: user must login with role Donor*
 ***********************************************/

public class DonorMainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_donor_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //starting service
            Intent intent = new Intent(DonorMainView.this, MyService.class);
            startService(intent);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view,
                                new TimeLineFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view,
                            new TimeLineFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }else if (id == R.id.nav_view_news) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view,
                            new ViewNews()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_view_news);
        }
        else if (id == R.id.nav_prepare_donate) {
            navigationView.setCheckedItem(R.id.nav_prepare_donate);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view,
                            new PrepareDonate()).addToBackStack(null).commit();

        }
        else if (id == R.id.nav_profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view,
                            new DonorProfileFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
        else if (id == R.id.nav_change_password) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view, new UpdatePasswordFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_change_password);
        }
        else if (id == R.id.nav_sign_out) {
            SharedPreferences.Editor prefs = getSharedPreferences("BloodsRecord",MODE_PRIVATE).edit();
            prefs.clear();
            prefs.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DonorMainView.this, MyService.class);
            stopService(intent);
            Intent loginIntent = new Intent(DonorMainView.this, MainActivity.class);
            startActivity(loginIntent);

        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
