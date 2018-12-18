package com.example.suttidasat.bloodsrecord;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.suttidasat.bloodsrecord.Interface.News.NewsManageFrament;
import com.example.suttidasat.bloodsrecord.Interface.SertNationalID;
import com.google.firebase.auth.FirebaseAuth;


/*************************************************
 *intent: main view of Admin used every page     *
 *precondition: user must login with role Admin  *
 *************************************************/

public class AdminMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // menu Admin
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_admin_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

         navigationView = findViewById(R.id.nav_view);
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
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {
            case R.id.nav_insert: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view,
                                new SertNationalID()).commit();
                navigationView.setCheckedItem(R.id.nav_insert);
                break;
            } case R.id.nav_manage_news: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view,
                                new NewsManageFrament()).commit();

                navigationView.setCheckedItem(R.id.nav_manage_news);
                break;
            }
            case R.id.nav_sign_out: {
                confirmDialog();

            }

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainView.this);

        builder
                .setMessage("ท่านต้องการออกจากระบบ ใช่ หรือ ไม่")
                .setPositiveButton("ใช่",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sigOut();
                    }
                })
                .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void sigOut() {
        FirebaseAuth.getInstance().signOut();
        Intent loginIntent = new Intent(AdminMainView.this, MainActivity.class);
        startActivity(loginIntent);
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
