package com.example.suttidasat.bloodsrecord.Interface;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.init.BloodsRecordFirebase;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private String uid;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private DocumentReference documentReference;
    private TextView profileName, profileNationalID, profileBlood, profileEmail, profileAddress;
    private SharedPreferences prefs;
    private ImageView profileImage;
    //menu
    private TextView textCartItemCount;
    private int mCartItemCount;

    ProgressDialog progressDialog;



    @SuppressLint("LongLogTag")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

        //get Notify count
        prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt(uid+"_countNotify", -1);
        Log.d("prefs profile", String.valueOf(mCartItemCount));

        setHasOptionsMenu(true);
        getParamet();
        setParameter();

    }

    private void setParameter() {
        /**********************************
         *   intent: สร้าง popup ระบบกำลังประมวลผล  *
         **********************************/
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);

        getImagePic();


        profileName.setText("ชื่อ : " + prefs.getString("fName","null value") +"\t"+ prefs.getString("lName","null value"));
        profileNationalID.setText(prefs.getString("nationalID","null value"));
        profileBlood.setText(prefs.getString("blood","null value"));
        profileEmail.setText(prefs.getString("email","null value"));
        profileAddress.setText(prefs.getString("address","null value"));
    }

    private void getImagePic() {
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(uid).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                                .dontTransform()
                                .fitCenter()
                                .override(300,200)
                                .transform(new CircleCrop()))
                        .into(profileImage);
                progressDialog.dismiss();

            }
        });
    }

    private void getParamet() {
        profileImage = getView().findViewById(R.id.profilePic);
        profileName = getView().findViewById(R.id.profileName);
        profileNationalID = getView().findViewById(R.id.profileNationalID);
        profileBlood = getView().findViewById(R.id.profileBloodsG);
        profileEmail = getView().findViewById(R.id.profileEmail);
        profileAddress = getView().findViewById(R.id.profileAddress);
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
                prefs.putInt(uid+"_countNotify",0);
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

