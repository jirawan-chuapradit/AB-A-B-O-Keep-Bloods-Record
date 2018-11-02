package com.example.suttidasat.bloodsrecord;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.suttidasat.bloodsrecord.Interface.InsertHistoryFragment;
import com.example.suttidasat.bloodsrecord.Interface.LoginFragment;
import com.example.suttidasat.bloodsrecord.Interface.SertNationalID;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view,
                            new LoginFragment()).commit();


        }
    }

