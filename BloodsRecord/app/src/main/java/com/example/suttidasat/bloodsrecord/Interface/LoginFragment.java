package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.AdminMainView;
import com.example.suttidasat.bloodsrecord.DonorMainView;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.DateFormatCal;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private SQLiteDatabase myDB;
    private FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    private int size = 0;
    private int diffDate,sizeofContent,mCartItemCount;
    private String type, currentDate, msg,uid;
//    UpdateNotify un = UpdateNotify.getUpdateNotifyInstance();
    //Check Notification is exist in Fire base
    boolean existNotify = false;


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

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
            progressDialog.setMessage("กรุณารอสักครู่...");
            // Progress Dialog Style Horizontal
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Display Progress Dialog
            progressDialog.show();
            // Cannot Cancel Progress Dialog
            progressDialog.setCancelable(false);

            SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
            String nationalID = prefs.getString("nationalID", "null value");
            uid = prefs.getString("uid","null value");
            Log.d("USER", "USER ALREADY LOG IN");
            Log.d("USER", "GOTO HomePage");
            getHistoryDate(nationalID);

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
                                    uid = FirebaseAuth.getInstance().getUid();
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
                                                    prefs.putString("uid", uid);
                                                    prefs.apply();

                                                    Log.d("User email: ", email);
                                                    Log.d("User fName: ", fName);
                                                    Log.d("User lName: ", lName);
                                                    Log.d("User national ID: ", nationalID);
                                                    Log.d("User blood: ", blood);
                                                    Log.d("User Address: ", address);

                                                    getHistoryDate(nationalID);



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



    private void getHistoryDate(final String nationalID) {
        Log.d("MY TASK", "GET HISTORY DONATE HAS BEEN START");
        firestore.collection("donateHistory")
                .document(nationalID).collection("history").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        size = queryDocumentSnapshots.size();
                        Log.d("GET HOSTORY DONATE", "Time of Donate : " + size);

                        if (size != 0) {
                            //operation calculate difference date
                            calculateDiffDate(nationalID);
                        }else {
                            Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                            getActivity().startActivity(myIntent);
                            progressDialog.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
            }
        });
    }

    private void calculateDiffDate(String nationalID) {
        Log.d("MY TASK", "CALCULATE DIFFERENT DATE HAS BEEN START");

        String sizeStr = String.valueOf(size);

        firestore.collection("donateHistory")
                .document(nationalID)
                .collection("history")
                .whereEqualTo("num", sizeStr)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String historyDate = (String) queryDocumentSnapshots.getDocuments()
                                .get(0).get("date");

                        Log.d("DATE: ", String.valueOf(historyDate));

                        DateFormatCal df = new DateFormatCal(historyDate);
                        diffDate = df.getDiffDays();
                        currentDate = df.getCurrentDate();
                        Log.d("CALCULATE DIFF DATE", "diffDate : " + diffDate);

                        if (diffDate > 83 && diffDate < 90) {
                            msg = "อีก 7 วัน จะถึงรอบบริจาคครั้งถัดไป";
                            System.out.println(msg);
                            setNotifyToFirebase();
                        } else if (diffDate >= 90) {
                            msg = "สามารถบริจาคเลือดได้";
                            System.out.println(msg);
                            setNotifyToFirebase();
                        } else {
                            Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                            getActivity().startActivity(myIntent);
                            progressDialog.dismiss();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERROR: ", e.getMessage());
                    }
                });
    }

    private void setNotifyToFirebase() {
        Log.d("MY TASK", "SET NOTIFY FIREBASE HAS BEEN START");
        firestore.collection("notificationContent")
                .document(uid).collection("content").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        Log.d("SET NOTIFY FIREBASE", "size Of content : " + sizeofContent);

                        final String sizeOfContentStr = String.valueOf(sizeofContent);
                        firestore.collection("notificationContent")
                                .document(uid)
                                .collection("content")
                                .whereEqualTo("num", sizeOfContentStr)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> doc = task.getResult().getDocuments();
                                String notifyDate = doc.get(0).get("date").toString();
                                Log.d("notify date: ", notifyDate);

                                DateFormatCal df = new DateFormatCal(notifyDate);
                                diffDate = df.getDiffDays();
                                Log.d("CAL DIFF DATE Notify", "diffDate : " + diffDate);

                                if (diffDate > 83) {
                                    NotifyManange nm = NotifyManange.getNotifyManangeInstance();
                                    nm.setNum(String.valueOf(sizeofContent + 1));
                                    nm.setDate(currentDate);
                                    nm.setText(msg);
                                    firestore.collection("notificationContent")
                                            .document(uid)
                                            .collection("content")
                                            .document(String.valueOf(sizeofContent + 1))
                                            .set(nm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FIRST TIME NOTIFY", "Notification has been saved!!!");
                                            SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord", Context.MODE_PRIVATE);
                                            mCartItemCount = prefs.getInt("_countNotify", 0);
                                            Log.d("mCartItemCount: ", String.valueOf(mCartItemCount));
                                            mCartItemCount++;
                                            SharedPreferences.Editor prefs2 = getContext().getSharedPreferences("BloodsRecord", MODE_PRIVATE).edit();
                                            prefs2.putInt("_countNotify", mCartItemCount);
                                            prefs2.apply();

                                            Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                                            getActivity().startActivity(myIntent);
                                            progressDialog.dismiss();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("ERROR: ", "ERROR =" + e.getMessage());
                                        }
                                    });
                                } else {
                                    Intent myIntent = new Intent(getActivity(), DonorMainView.class);
                                    getActivity().startActivity(myIntent);
                                    progressDialog.dismiss();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR: ", e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERROR =" + e.getMessage());
            }
        });
    }

}