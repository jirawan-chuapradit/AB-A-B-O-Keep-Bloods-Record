package com.example.suttidasat.bloodsrecord.Interface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.DateFormatCal;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DisplayDateFragment extends Fragment {

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;

    private String uid,nationalID, historyDate;
    private DocumentReference booldsRecord;

    private int size;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
//        databaseReference =  FirebaseDatabase.getInstance().getReference();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

        booldsRecord = firestore.collection("bloodsRecord")
                .document(uid);

        //GET DOCUMENT DATA from booldsRecord find National ID
        booldsRecord.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Log.d("DONATOR PROFILE", "GET DOCUMENT DATA");
                        DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);
                        nationalID = dp.getNationalID();
                        System.out.println("NATIONAL ID : "+nationalID);

                        getHistoryDate(nationalID);
//                        profileName.setText("First name : " + name);
//                        profileNationalID.setText("National ID : " + nationalID);
//                        profileBirth.setText("Birth date : " + birth);
//                        profileBlood.setText("Blood Group : " + blood);
//                        profileEmail.setText("E-mail : " + email);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DONATOR PROFILE", "ERROR = " + e.getMessage());

            }
        });

    }

    private void getHistoryDate(final String nationalID) {
        //GET DOCUMENT DATA from booldsRecord find National ID
            //getLastTime Donate
        firestore.collection("donateHistory")
                .document(nationalID)
                .collection("history")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         size = queryDocumentSnapshots.size();
                         System.out.println("Time of Donate : " + size);

                         //operation calculate difference date
                         CalculateDiffDate();
                       
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CalculateDiffDate() {
        firestore.collection("donateHistory")
                .document(nationalID)
                .collection("history")
                .document(String.valueOf(size))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DonatorHistory dh = documentSnapshot.toObject(DonatorHistory.class);
                        historyDate = dh.getDate();
                        System.out.println("Date : " + historyDate);
                        DateFormatCal df = new DateFormatCal(historyDate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Show Donator", "ERRROR =" + e.getMessage());
                        Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
