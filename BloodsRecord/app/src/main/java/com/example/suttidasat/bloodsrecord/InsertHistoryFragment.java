package com.example.suttidasat.bloodsrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class InsertHistoryFragment extends Fragment {

    FirebaseFirestore firestore;
    DocumentReference booldsRecord;
    private TextView profileName, profileNationalID, profileBirth,profileBlood, profileEmail;


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

    }
    void insertHistory()
    {
//        final String NID = getView().findViewById(R.id.nationalID).toString();

        final String uid ;


        profileName =  getView().findViewById(R.id.sh_name_donater);
        profileNationalID = getView().findViewById(R.id.sh_nid_donater);
        profileBirth = getView().findViewById(R.id.sh_birth_donater);
        profileBlood = getView().findViewById(R.id.sh_group_donater);
        profileEmail = getView().findViewById(R.id.sh_email_donater);

        firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID","111")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> doc = task.getResult().getDocuments();
                        String name = doc.get(0).get("fName").toString() +""+ doc.get(0).get("lName").toString();

                        String nationalID = doc.get(0).get("nationalID").toString();
                        String birth = doc.get(0).get("birth").toString();
                        String blood = doc.get(0).get("bloodGroup").toString();
                        String email = doc.get(0).get("email").toString();

                        profileName.setText("First name : " +name);
                        profileNationalID.setText("National ID : " + nationalID);
                        profileBirth.setText("Birth date : " + birth);
                        profileBlood.setText("Blood Group : " + blood);
                        profileEmail.setText("E-mail : " + email);
                    }
                });


    }

}
