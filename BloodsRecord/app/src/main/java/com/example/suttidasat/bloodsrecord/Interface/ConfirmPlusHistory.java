package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmPlusHistory extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    DocumentReference donateHistory;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
    final String date = mdformat.format(calendar.getTime());

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_plus_history,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        deley();



//        backBtn();
        plusBtn();

    }

    void plusBtn() {
        Button plus = getView().findViewById(R.id.cf_add_news);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                deley();


                /**********************************
                 *   intent: สร้าง popup ระบบกำลังประมวลผล  *
                 **********************************/
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
//                progressDialog.setMessage("กรุณารอสักครู่...");
//                // Progress Dialog Style Horizontal
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                // Display Progress Dialog
//                progressDialog.show();
//                // Cannot Cancel Progress Dialog
//                progressDialog.setCancelable(false);

                ConnectDB.getHistoryConnect()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                final int size = queryDocumentSnapshots.size();
                                firestore.collection("donateHistory")
                                        .document(NationaID.NID)
                                        .collection("history")
                                        .document(String.valueOf(size))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String date_last;
                                                if (size == 0)
                                                    date_last = "";
                                                else
                                                    date_last = task.getResult().get("date").toString();
                                                if (!date_last.equals(date)) {
                                                    donateHistory = firestore.collection("donateHistory")
                                                            .document(NationaID.NID);
                                                    donateHistory.get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                    if (documentSnapshot.exists() || size > 0) { //have
                                                                        ConnectDB.getHistoryConnect()
                                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                                        DonatorHistory dh = DonatorHistory.getDonatorHistoryInstance();
                                                                                        dh.setDate(date);

                                                                                        String num = Integer.toString(queryDocumentSnapshots.size() + 1);

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

                                                                    Toast.makeText(getActivity(), "เพิ่มประวัติการบริจาคสำเร็จ", Toast.LENGTH_SHORT).show();

                                                                    getActivity().getSupportFragmentManager()
                                                                            .beginTransaction()
                                                                            .addToBackStack(null)
                                                                            .replace(R.id.admin_view, new InsertHistoryFragment())
                                                                            .commit();

                                                                }

                                                            });
                                                } else {

                                                    Toast.makeText(getActivity(), "เพิ่มการบริจาควันนี้ซ้ำ", Toast.LENGTH_SHORT).show();
                                                    getActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .addToBackStack(null)
                                                            .replace(R.id.admin_view, new InsertHistoryFragment())
                                                            .commit();
                                                }
                                            }
                                        });

                            }
                        });

            }
        });
    }
}
