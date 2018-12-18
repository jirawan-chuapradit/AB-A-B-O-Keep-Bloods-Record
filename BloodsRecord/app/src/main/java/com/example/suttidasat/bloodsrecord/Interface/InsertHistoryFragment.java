package com.example.suttidasat.bloodsrecord.Interface;

import java.text.SimpleDateFormat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.HistoryAdapter;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.History;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.example.suttidasat.bloodsrecord.model.NumSorter;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/*******************************************************
 *intent: Show history of donor and plus donation time *
 *pre-condition: User must login with role Admin and   *
 *               User must insert national ID of donor *
 *post-condition: User go to Insert national ID page   *
 *******************************************************/

public class InsertHistoryFragment extends Fragment {


    FirebaseFirestore firestore = ConnectDB.getConnect();
    DocumentReference donateHistory;
    private TextView profileName, profileNationalID, profileBlood, profileEmail, currentDate, amount;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
    SharedPreferences.Editor sp;
    ListView listView;
    ArrayList<History> his_list = new ArrayList<>();
    String title,date,detail,link;

    final String c_date = mdformat.format(calendar.getTime());
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.insert_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profile_danate();
        history();
        backBtn();
        plusBtn();

    }

    void history(){
        listView = getView().findViewById(R.id.his_list);

        final HistoryAdapter historyAdapter = new HistoryAdapter(
                getActivity(),
                R.layout.history_item,
                his_list
        );



        ConnectDB.getHistoryConnect()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        his_list.clear();
                        for (DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                            his_list.add(d.toObject(History.class));
                        }

                        Collections.sort(his_list,new NumSorter());

                        historyAdapter.notifyDataSetChanged();
                    }
                });


        listView.setAdapter(historyAdapter);
    }

    void profile_danate() {


        profileName = getView().findViewById(R.id.sh_name_donater);
        profileNationalID = getView().findViewById(R.id.sh_nid_donater);
        profileBlood = getView().findViewById(R.id.sh_group_donater);
        profileEmail = getView().findViewById(R.id.sh_email_donater);
        amount = getView().findViewById(R.id.sh_amount);
        currentDate = getView().findViewById(R.id.sh_date_now);

        ConnectDB.getHistoryConnect()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int s = queryDocumentSnapshots.size();
                        amount.setText("จำนวนการบริจาคทั้งหมด : " + String.valueOf(s));
                        sp = getContext().getSharedPreferences("donate_amount", Context.MODE_PRIVATE).edit();
                        sp.putString("amount", String.valueOf(s)).apply();
                        sp.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID", NationaID.NID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> doc = task.getResult().getDocuments();


                        String name = doc.get(0).get("fName").toString() + " " + doc.get(0).get("lName").toString();

                        String nationalID = doc.get(0).get("nationalID").toString();
                        String blood = doc.get(0).get("bloodGroup").toString();
                        String email = doc.get(0).get("email").toString();

                        profileName.setText("ชื่อ : " + name);
                        profileNationalID.setText("หมายเลขบัตรประชาชน : " + nationalID);
                        profileBlood.setText("กรุ๊ปเลือด : " + blood);
                        profileEmail.setText("อีเมล : " + email);
                        currentDate.setText("วันที่ : " + c_date);

//                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void plusBtn() {
        Button plus = getView().findViewById(R.id.btn_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_view, new ConfirmPlusHistory())
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
                        .addToBackStack(null)
                        .replace(R.id.admin_view, new SertNationalID())
                        .commit();


            }
        });
    }


}
