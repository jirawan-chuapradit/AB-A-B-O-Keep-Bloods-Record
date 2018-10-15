package com.example.suttidasat.bloodsrecord;

import android.content.Intent;
import android.icu.text.DateFormat;
import java.text.SimpleDateFormat;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class InsertHistoryFragment extends Fragment {

    FirebaseFirestore firestore;
    DocumentReference booldsRecord;
    private TextView  profileName,profileNationalID, profileBirth,profileBlood, profileEmail,currentDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.insert_history, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();


        insertHistory();
        backBtn();

    }
    void insertHistory()
    {



        profileName = getView().findViewById(R.id.sh_name_donater);
        profileNationalID = getView().findViewById(R.id.sh_nid_donater);
        profileBirth = getView().findViewById(R.id.sh_birth_donater);
        profileBlood = getView().findViewById(R.id.sh_group_donater);
        profileEmail = getView().findViewById(R.id.sh_email_donater);
        currentDate = getView().findViewById(R.id.sh_date_now);


        firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID",NationaID.NID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> doc = task.getResult().getDocuments();
                        String name = doc.get(0).get("fName").toString() +""+ doc.get(0).get("lName").toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd - MM - yyyy ");

                        String strDate = "Current Date : " + mdformat.format(calendar.getTime());

                        String nationalID = doc.get(0).get("nationalID").toString();
                        String birth = doc.get(0).get("birth").toString();
                        String blood = doc.get(0).get("bloodGroup").toString();
                        String email = doc.get(0).get("email").toString();

                        profileName.setText("First name : " +name);
                        profileNationalID.setText("National ID : " + nationalID);
                        profileBirth.setText("Birth date : " + birth);
                        profileBlood.setText("Blood Group : " + blood);
                        profileEmail.setText("E-mail : " + email);
                        currentDate.setText("Date : " + strDate);
                    }
                });


    }
    void backBtn(){
        Button back = getView().findViewById(R.id.btn_back_sertNID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new SertNationalID())
                        .commit();

            }
        });
    }

}
