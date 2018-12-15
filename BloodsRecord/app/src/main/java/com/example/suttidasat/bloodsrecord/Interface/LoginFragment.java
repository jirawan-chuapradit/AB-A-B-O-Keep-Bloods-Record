package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private SQLiteDatabase myDB;
    private FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();

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
                final String _userIdStr = _userId.getText().toString();
                final String _passwordStr = _password.getText().toString();


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

                                    progressDialog = new ProgressDialog(getActivity());
                                    progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
                                    progressDialog.setMessage("กรุณารอสักครู่...");
                                    // Progress Dialog Style Horizontal
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    // Display Progress Dialog
                                    progressDialog.show();
                                    // Cannot Cancel Progress Dialog
                                    progressDialog.setCancelable(false);

                                    //GET UID of Currnet user
                                    String uid = FirebaseAuth.getInstance().getUid();
                                    firestore.collection("bloodsRecord")
                                            .document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);
                                                    String fName = dp.getfName();
                                                    String lName = dp.getlName();
                                                    String nationalID = dp.getNationalID();
                                                    String blood = dp.getBloodGroup();
                                                    String email = dp.getEmail();
                                                    String address = dp.getAddress();

                                                    SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",MODE_PRIVATE).edit();
                                                    prefs.putString("email", email);
                                                    prefs.putString("fName", fName);
                                                    prefs.putString("lName", lName);
                                                    prefs.putString("nationalID", nationalID);
                                                    prefs.putString("blood", blood);
                                                    prefs.putString("address", address);
                                                    prefs.apply();

                                                    Log.d("User email: ", email);
                                                    Log.d("User fName: ", fName);
                                                    Log.d("User lName: ", lName);
                                                    Log.d("User national ID: ", nationalID);
                                                    Log.d("User blood: ", blood);
                                                    Log.d("User Address: ", address);


                                                    Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                                                    getActivity().startActivity(myIntent);
                                                    progressDialog.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("ERROR: ", e.getMessage());
                                                    progressDialog.dismiss();
                                                }
                                            });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("USER", "INVALID USER OR PASSWORD");
                            Toast.makeText(getContext(), "อีเมลล์หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        });
    }


}