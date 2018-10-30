package com.example.suttidasat.bloodsrecord.Interface;

import java.text.SimpleDateFormat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

public class InsertHistoryFragment extends Fragment {

    private ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    DocumentReference donateHistory;
    private TextView profileName, profileNationalID, profileBirth, profileBlood, profileEmail, currentDate, amount;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");

    final String date = mdformat.format(calendar.getTime());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.insert_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        firestore = FirebaseFirestore.getInstance();

        insertHistory();
        backBtn();
        plusBtn();

    }

    void insertHistory() {

        // Loading data dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();

        profileName = getView().findViewById(R.id.sh_name_donater);
        profileNationalID = getView().findViewById(R.id.sh_nid_donater);
        profileBirth = getView().findViewById(R.id.sh_birth_donater);
        profileBlood = getView().findViewById(R.id.sh_group_donater);
        profileEmail = getView().findViewById(R.id.sh_email_donater);
        amount = getView().findViewById(R.id.sh_amount);
        currentDate = getView().findViewById(R.id.sh_date_now);

        firestore.collection("donateHistory")
                .document(NationaID.NID)
                .collection("history")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        amount.setText("จำนวนการบริจาคทั้งหมด : " + queryDocumentSnapshots.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID", NationaID.NID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> doc = task.getResult().getDocuments();
                        String name = doc.get(0).get("fName").toString() + "  " + doc.get(0).get("lName").toString();


                        String nationalID = doc.get(0).get("nationalID").toString();
                        String birth = doc.get(0).get("birth").toString();
                        String blood = doc.get(0).get("bloodGroup").toString();
                        String email = doc.get(0).get("email").toString();

                        profileName.setText("ชื่อ : " + name);
                        profileNationalID.setText("หมายเลขบัตรประชาชน : " + nationalID);
                        profileBirth.setText("วันเกิด : " + birth);
                        profileBlood.setText("กรุ๊ปเลือด : " + blood);
                        profileEmail.setText("อีเมล : " + email);
                        currentDate.setText("วันที่ : " + date);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    void plusBtn() {
        Button plus = getView().findViewById(R.id.btn_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateHistory = firestore.collection("donateHistory")
                        .document(NationaID.NID);
                donateHistory.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) { //have
                                    firestore.collection("donateHistory") /// get size (amount of donate)
                                            .document(NationaID.NID)
                                            .collection("history")
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                    DonatorHistory dh = DonatorHistory.getDonatorHistoryInstance();
                                                    dh.setDate(date);

                                                    String num = Integer.toString(queryDocumentSnapshots.size()+1);
                                                    firestore.collection("donateHistory")
                                                            .document(NationaID.NID)
                                                            .collection("history")
                                                            .document(num)
                                                            .set(dh)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("Insert", "Insert Success");
                                                                }
                                                            });

                                                }
                                            });

                                } else {
                                    // never donate before
                                    DonatorHistory dh = DonatorHistory.getDonatorHistoryInstance();
                                    dh.setDate(date);

                                    firestore.collection("donateHistory")
                                            .document(NationaID.NID)
                                            .collection("history")
                                            .document("1")
                                            .set(dh)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Insert", "Insert First Time Success");
                                                }
                                            });

                                }

                            }

                        });

                Toast.makeText(getActivity(), "Insert History Success", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view, new SertNationalID())
                        .commit();
            }
        });
    }

    void backBtn() {
        Button back = getView().findViewById(R.id.btn_back_sertNID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view, new SertNationalID())
                        .commit();

            }
        });
    }




}
