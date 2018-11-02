package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.AdminMainView;
import com.example.suttidasat.bloodsrecord.DonorMainView;
import com.example.suttidasat.bloodsrecord.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {


    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("USER", "USER ALREADY LOG IN");
            Log.d("USER", "GOTO HomePage");
            Intent myIntent = new Intent(getActivity(), DonorMainView.class);
            getActivity().startActivity(myIntent);
            return;
        }


        initLoginBtn();
        initRegisterBtn();


    }

    private void initRegisterBtn() {
        TextView _regBtn = (TextView) getView().findViewById(R.id.login_register_Btn);
        _regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER", "GOTO REGISTER");
            }
        });
    }

    void initLoginBtn() {


        Button _loginBtn = getView().findViewById(R.id.login_btn);
        _loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText _userId = (EditText) getView().findViewById(R.id.login_userid);
                EditText _password = (EditText) getView().findViewById(R.id.login_password);
                String _userIdStr = _userId.getText().toString();
                String _passwordStr = _password.getText().toString();


                if (_userIdStr.isEmpty() || _passwordStr.isEmpty()) {

                    Toast.makeText(
                            getActivity(),
                            "กรุณาระบุ user or password",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d("USER", "USER OR PASSWORD IS EMPTY");

                } else if (_userIdStr.equals("admin@gmail.com") && _passwordStr.equals("12345678")) {

                    Intent myIntent = new Intent(getActivity(), AdminMainView.class);
                    getActivity().startActivity(myIntent);
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(_userIdStr, _passwordStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                                    getActivity().startActivity(myIntent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d("USER", "INVALID USER OR PASSWORD");
                            Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        });
    }


}