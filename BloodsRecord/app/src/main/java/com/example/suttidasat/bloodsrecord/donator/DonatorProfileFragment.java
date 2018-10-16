package com.example.suttidasat.bloodsrecord.donator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonatorProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donator_profile,container,false);
    }

    //Firebase
    FirebaseFirestore firestore;
//    DatabaseReference profileUserRef;
    FirebaseAuth fbAuth;
    DocumentReference booldsRecord;

    private TextView profileName, profileNationalID, profileBirth,profileBlood, profileEmail;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //GET VALUDE FROM FIREBASE
        String user = fbAuth.getCurrentUser().getUid();

//        currentUserId = fbAuth.getCurrentUser().getUid();
        booldsRecord = firestore.collection("bloodsRecord")
                .document(user);

        //get textView
         profileName =  getView().findViewById(R.id.profileName);
         profileNationalID = getView().findViewById(R.id.profileNationalID);
         profileBirth = getView().findViewById(R.id.profileBirth);
         profileBlood = getView().findViewById(R.id.profileBloodsG);
         profileEmail = getView().findViewById(R.id.profileEmail);

        //GET DOCUMENT DATA
        booldsRecord.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        documentSnapshot.getData();

                        Log.d("DONATOR PROFILE","GET DOCUMENT DATA");
                        DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);

                        String name = dp.getfName() + "  "+ dp.getlName();
                        String nationalID = dp.getNationalID();
                        String birth = dp.getBirth();
                        String blood = dp.getBloodGroup();
                        String email = dp.getEmail();

                        profileName.setText("First name : " +name);
                        profileNationalID.setText("National ID : " + nationalID);
                        profileBirth.setText("Birth date : " + birth);
                        profileBlood.setText("Blood Group : " + blood);
                        profileEmail.setText("E-mail : " + email);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DONATOR PROFILE","ERROR = " + e);

            }
        });

//        loadDonatorProfile();
//        System.out.println("First name : "+dp.getfName());
//        profileName.setText(dp.getfName());


    }



}
