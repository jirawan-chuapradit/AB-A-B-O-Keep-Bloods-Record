package com.example.suttidasat.bloodsrecord.Interface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.init.BloodsRecordFirebase;
import com.example.suttidasat.bloodsrecord.init.DonateHistoryFirebase;
import com.example.suttidasat.bloodsrecord.init.NotificationContentFirebase;
import com.example.suttidasat.bloodsrecord.model.DateFormatCal;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CountNofity extends Fragment {

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;

    private String uid, nationalID, historyDate, type, currentDate, msg;
    private DocumentReference documentReference; // Used below
    private CollectionReference collectionReference; // Used below
    private int size, diffDate, mCartItemCount, sizeofContent;

    //Check Notification is exist in Fire base
    boolean existNotify = false;

    //Create Object
    DonateHistoryFirebase donateHistoryFirebase;
    NotificationContentFirebase notificationContentFirebase;

    //menu
    UpdateNotify un = new UpdateNotify();
    private TextView textCartItemCount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Start Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //GET UID of Currnet user
        uid = fbAuth.getCurrentUser().getUid();

        //Connect to bloodRecord
        BloodsRecordFirebase bloodsRecordConnection = new BloodsRecordFirebase(
                documentReference, firestore, uid);
        bloodsRecordConnection.getConnection();

        //GET DOCUMENT DATA from booldsRecord find National ID
        bloodsRecordConnection.getBloodsRecordFirebase().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("DONATOR PROFILE", "GET DOCUMENT DATA");
                        DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);
                        nationalID = dp.getNationalID();
                        System.out.println("NATIONAL ID : " + nationalID);
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
        donateHistoryFirebase = new DonateHistoryFirebase(
                collectionReference, firestore, nationalID);
        donateHistoryFirebase.getConnectionCollection();
        donateHistoryFirebase.getCollectionReference().get()
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
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CalculateDiffDate() {
        donateHistoryFirebase = new DonateHistoryFirebase(
                documentReference, firestore, nationalID, String.valueOf(size));
        donateHistoryFirebase.getConnectionDocument();
        donateHistoryFirebase.getDocumentReference().get()
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
                        if (mCartItemCount != 0) {
                            type = un.getType();
                            if (type.equals("7days")) {
                                msg = "อีก 7 วัน จะถึงรอบบริจาคครั้งถัดไป";
                            } else if (type.equals("today")) {
                                msg = "สามารถบริจาคเลือดได้";
                            }
                            setNotifyToFirebase(type);
                        }
                        System.out.println("mCartItemCount : " + mCartItemCount);
                        setHasOptionsMenu(true);
                        timelineFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Show Donator", "ERRROR =" + e.getMessage());
                        Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setNotifyToFirebase(final String type) {
        notificationContentFirebase = new NotificationContentFirebase(
                collectionReference, firestore, uid);
        notificationContentFirebase.getConnectionCollection();
        notificationContentFirebase.getCollectionReference().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        System.out.println("size Of content : " + sizeofContent);

                        checkNotify(sizeofContent);
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNotify(final int sizeofContent) {
        notificationContentFirebase = new NotificationContentFirebase(
                documentReference, firestore, uid, sizeofContent);
        notificationContentFirebase.getConnectionDocument();
        notificationContentFirebase.getDocumentReference().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        NotifyManange nm = documentSnapshot.toObject(NotifyManange.class);
                        String notifyDate = nm.getDate();
                        if (notifyDate.equals(currentDate)) {
                            existNotify = true;
                            return;
                        }
                        if (!existNotify) {
//                            NotifyManange nm = new NotifyManange(currentDate, type, "");
                            nm.setDate(currentDate);
                            nm.setText(msg);
                            firestore.collection("notificationContent")
                                    .document(uid)
                                    .collection("content")
                                    .document(String.valueOf(sizeofContent + 1))
                                    .set(nm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("DisplayFragment", "Notification has been saved!!!");
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "ERRROR =" + e.getMessage());
                                    Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void timelineFragment() {
        Log.d("Start", "GOTO TIME LINE");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new TimeLineFragment())
                .addToBackStack(null)
                .commit();
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
                        .replace(R.id.main_view, new notifyFragment())
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
