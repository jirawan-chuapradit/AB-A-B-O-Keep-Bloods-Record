package com.example.suttidasat.bloodsrecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suttidasat.bloodsrecord.Interface.LoginFragment;
import com.example.suttidasat.bloodsrecord.Interface.SertNationalID;

public class MainActivity extends AppCompatActivity {
    ///nate here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new SertNationalID())
                    .commit();
        }
    }
}
