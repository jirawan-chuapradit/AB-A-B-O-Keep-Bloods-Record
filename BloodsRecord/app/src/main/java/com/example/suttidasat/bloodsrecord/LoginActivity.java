package com.example.suttidasat.bloodsrecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suttidasat.bloodsrecord.Interface.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_view,
                                new LoginFragment()).commit();

    }
}
