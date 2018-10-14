package com.example.suttidasat.bloodsrecord.donator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.io.File;
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
    private ImageView profilePic;
    private String uid;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

//        currentUserId = fbAuth.getCurrentUser().getUid();
        booldsRecord = firestore.collection("bloodsRecord")
                .document(uid);

//        showProfilePic();

        //get textView
         profileName =  getView().findViewById(R.id.profileName);
         profileNationalID = getView().findViewById(R.id.profileNationalID);
         profileBirth = getView().findViewById(R.id.profileBirth);
         profileBlood = getView().findViewById(R.id.profileBloodsG);
         profileEmail = getView().findViewById(R.id.profileEmail);
         profilePic = getView().findViewById(R.id.profilePic);



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

                        System.out.println("_________before____");

                        File imgFile = new  File("/profileImage/"+uid+ ".jpg");
                        System.out.println("_____________" + imgFile.toString());
                        if(imgFile.exists()){
                            System.out.println("______________mgFile.exists()____________");
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                            profilePic.setImageBitmap(myBitmap);

                        }else{
                            System.out.println("______________mgFile.not exists()____________");
                        }

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

//    private void showProfilePic() {
//
//    }


}
