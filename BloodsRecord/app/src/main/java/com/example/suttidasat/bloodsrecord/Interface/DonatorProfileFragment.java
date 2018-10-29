package com.example.suttidasat.bloodsrecord.Interface;

import android.content.Intent;
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

public class DonatorProfileFragment extends Fragment {

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

    private TextView profileName, profileNationalID, profileBirth, profileBlood, profileEmail;
    private Button changePassword;
    private String uid;
    private ImageView profileImage;

    //menu
    UpdateNotify un = new UpdateNotify();
    private TextView textCartItemCount;
    private int mCartItemCount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//menu
        mCartItemCount = un.getCount();
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
        profileBirth = getView().findViewById(R.id.profileBirth);
        profileBlood = getView().findViewById(R.id.profileBloodsG);
        profileEmail = getView().findViewById(R.id.profileEmail);
        changePassword = getView().findViewById(R.id.btnChangePassword);


        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(uid).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .transform((Transformation) new PicassoCircleTransformation())
                        .into(profileImage);
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
                        String birth = dp.getBirth();
                        String blood = dp.getBloodGroup();
                        String email = dp.getEmail();

                        profileName.setText("First name : " + name);
                        profileNationalID.setText("National ID : " + nationalID);
                        profileBirth.setText("Birth date : " + birth);
                        profileBlood.setText("Blood Group : " + blood);
                        profileEmail.setText("E-mail : " + email);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DONATOR PROFILE", "ERROR = " + e.getMessage());

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new UpdatePasswordFragment())
                        .commit();
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
//            case R.id.sigOut:{
//
//                FirebaseAuth.getInstance().signOut();
//                getActivity().stopService(new Intent(getActivity(),MyService.class));
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new LoginFragment())
//                        .addToBackStack(null)
//                        .commit();
//                Log.d("USER", "GOTO LOGIN");
//                break;
//            }
//            case R.id.donatorProfile:{
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view,new DonatorProfileFragment())
//                        .commit();
//                Log.d("MENU", "GOTO DONATOR PROFILE");
//                break;
//            }
//            case R.id.timeline:{
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new CountNofity())
//                        .addToBackStack(null)
//                        .commit();
//                Log.d("USER", "GOTO Timeline");
//                break;
//            }
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .commit();
                System.out.println("CLICK NOTIFY BELL");
                un.setCount(0);
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

