package com.example.suttidasat.bloodsrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.example.suttidasat.bloodsrecord.Interface.DonatorProfileFragment;
import com.example.suttidasat.bloodsrecord.Interface.SertNationalID;
import com.example.suttidasat.bloodsrecord.Interface.TimeLineFragment;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // menu Admin
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_admin_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.admin_view,
                            new SertNationalID()).commit();
            navigationView.setCheckedItem(R.id.nav_insert);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_insert: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view,
                                new SertNationalID()).commit();
                break;
            }
            case R.id.nav_sign_out: {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(AdminMainView.this, MainActivity.class);
                startActivity(loginIntent);
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
