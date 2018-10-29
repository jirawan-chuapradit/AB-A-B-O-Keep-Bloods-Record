package com.example.suttidasat.bloodsrecord;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.example.suttidasat.bloodsrecord.Interface.DonatorProfileFragment;
import com.example.suttidasat.bloodsrecord.Interface.LoginFragment;
import com.example.suttidasat.bloodsrecord.Interface.TimeLineFragment;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

//    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view,
                            new LoginFragment()).commit();

        
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar
//        , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

//        if(savedInstanceState == null){
//            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(loginIntent);

        }

//        if(savedInstanceState == null) {
//        getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.login_view,
//                                new LoginFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_profile);
//        }
    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.nav_home:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container,
//                                new TimeLineFragment()).commit();
//                break;
//            case R.id.nav_profile:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view,
//                                new DonatorProfileFragment()).commit();
//                break;
//            case R.id.nav_sign_out:
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivity.this,MyService.class );
//                stopService(intent);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.login_view,
//                                new LoginFragment()).commit();
//                 break;
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else {
//            super.onBackPressed();
//        }
//
//    }
//}
