package com.example.suttidasat.bloodsrecord.Interface;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.DateFormatCal;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CountNofity extends Fragment {

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;

    private String uid,nationalID, historyDate,type,currentDate;
    private DocumentReference booldsRecord;

    private int size,diffDate,mCartItemCount,sizeofContent;

    private TextView textCartItemCount;

    UpdateNotify un = new UpdateNotify();

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
                        diffDate = df.getDiffDays();
                        currentDate = df.getCurrentDate();

                        System.out.println("diffDate : " + diffDate);

                         un.setDate(diffDate);
                        mCartItemCount = un.getCount();
                        if(mCartItemCount != 0){
                            type = un.getType();

                            if(type.equals("7days")){
                                type = "อีก 7 วัน จะถึงรอบบริจาคครั้งถัดไป";
                            }else if(type.equals("today")){
                                type = "สามารถบริจาคเลือดได้";
                            }
                            setNotifyToFirebase(type);
                        }
                        System.out.println("mCartItemCount : " + mCartItemCount);
                        setHasOptionsMenu(true);
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

    private void setNotifyToFirebase(final String type) {

        firestore.collection("notificationContent")
                .document(uid)
                .collection("content")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        System.out.println("size Of content : " + sizeofContent);

                        NotifyManange nm = new NotifyManange(currentDate,type,"");
                        firestore.collection("notificationContent")
                                .document(uid)
                                .collection("content")
                                .document(String.valueOf(sizeofContent + 1))
                                .set(nm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d("DisplayFragment", "Notification has been saved!!!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("REGISTER", "ERRROR =" + e.getMessage());
                                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    // set menu
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
        switch (item.getItemId()) {

            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new notifyFragment())
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
