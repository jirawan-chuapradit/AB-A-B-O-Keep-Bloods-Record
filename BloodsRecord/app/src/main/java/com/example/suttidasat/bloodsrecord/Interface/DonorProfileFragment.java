package com.example.suttidasat.bloodsrecord.Interface;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.init.BloodsRecordFirebase;
import com.example.suttidasat.bloodsrecord.model.CountNotify;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.example.suttidasat.bloodsrecord.model.PicassoCircleTransformation;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.concurrent.TimeUnit;

/*******************************************************
 *intent: Show User Profile                            *
 *pre-condition: User must login with role Donor       *
 *******************************************************/

public class DonorProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donator_profile, container, false);
    }

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;

    private DocumentReference documentReference;

    private TextView profileName, profileNationalID, profileBlood, profileEmail;
    private String uid;
    private ImageView profileImage;

    //menu
    private TextView textCartItemCount;
    private int mCartItemCount;


    // Loading data dialog
    ProgressDialog progressDialog;

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Loading data dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();

        SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt("countNotify", 0);
        Log.d("SharedPreferences", String.valueOf(mCartItemCount));

        setHasOptionsMenu(true);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();


        //get textView
        profileImage = getView().findViewById(R.id.profilePic);
        profileName = getView().findViewById(R.id.profileName);
        profileNationalID = getView().findViewById(R.id.profileNationalID);
        profileBlood = getView().findViewById(R.id.profileBloodsG);
        profileEmail = getView().findViewById(R.id.profileEmail);


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(uid).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .transform((Transformation) new PicassoCircleTransformation())
                        .into(profileImage);

                try {
                    TimeUnit.SECONDS.sleep(5);
                    progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        //Connect to bloodRecord
        BloodsRecordFirebase bloodsRecordConnection = new BloodsRecordFirebase(
                documentReference, firestore, uid);
        bloodsRecordConnection.getConnection();
        bloodsRecordConnection.getDocumentReference().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Log.d("DONATOR PROFILE", "GET DOCUMENT DATA");

                        DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);
                        String name = dp.getfName() + "  " + dp.getlName();
                        String nationalID = dp.getNationalID();
                        String blood = dp.getBloodGroup();
                        String email = dp.getEmail();

                        profileName.setText("Name : " + name);
                        profileNationalID.setText("National ID : " + nationalID);
                        profileBlood.setText("Blood Group : " + blood);
                        profileEmail.setText("E-mail : " + email);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DONATOR PROFILE", "ERROR = " + e.getMessage());

            }
        });


    }

    //menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.nofity_bell);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER ", "CLICK NOTIFY BELL");

                SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE).edit();
                prefs.putInt("countNotify",0);
                prefs.apply();

                setupBadge();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}

