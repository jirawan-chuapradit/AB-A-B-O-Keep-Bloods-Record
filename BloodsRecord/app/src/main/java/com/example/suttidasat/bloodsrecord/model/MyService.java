package com.example.suttidasat.bloodsrecord.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.init.BloodsRecordFirebase;
import com.example.suttidasat.bloodsrecord.init.DonateHistoryFirebase;
import com.example.suttidasat.bloodsrecord.init.NotificationContentFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
    ScheduledExecutorService executorService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MY SERVICE", "HAS BEEN START...");
        //Start Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //GET UID of Currnet user
        uid = fbAuth.getCurrentUser().getUid();


        //loop
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d("MY TASK", "MY TASK HAS BEEN START IN SPRINT");
                myTask();
                Log.d("MY TASK", "MY TASK HAS BEEN DONE IN SPRINT");

                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    private void myTask() {
        //getLastTime Donate
        //GET DOCUMENT DATA from booldsRecord find National ID
        //Connect to bloodRecord
        final BloodsRecordFirebase bloodsRecordConnection = new BloodsRecordFirebase(
                documentReference, firestore, uid);
        bloodsRecordConnection.getConnection();
        bloodsRecordConnection.getDocumentReference().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DonatorProfile dp = documentSnapshot.toObject(DonatorProfile.class);
                        nationalID = dp.getNationalID();
                        Log.d("MY TASK", "NATIONAL ID : " + nationalID);
                        getHistoryDate(nationalID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DONATOR PROFILE", "ERROR = " + e.getMessage());
            }
        });

    }

    private void getHistoryDate(String nationalID) {
        Log.d("MY TASK", "GET HISTORY DONATE HAS BEEN START");
        donateHistoryFirebase = new DonateHistoryFirebase(
                collectionReference, firestore, nationalID);
        donateHistoryFirebase.getConnectionCollection();
        donateHistoryFirebase.getCollectionReference().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        size = queryDocumentSnapshots.size();
                        Log.d("GET HOSTORY DONATE", "Time of Donate : " + size);

                        //operation calculate difference date
                        CalculateDiffDate();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
//                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CalculateDiffDate() {
        Log.d("MY TASK", "CALCULATE DIFFERENT DATE HAS BEEN START");
        donateHistoryFirebase = new DonateHistoryFirebase(
                documentReference, firestore, nationalID, String.valueOf(size));
        donateHistoryFirebase.getConnectionDocument();
        donateHistoryFirebase.getDocumentReference().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DonatorHistory dh = documentSnapshot.toObject(DonatorHistory.class);
                        historyDate = dh.getDate();
                        Log.d("CALCULATE DIFF DATE", "Date : " + historyDate);

                        DateFormatCal df = new DateFormatCal(historyDate);
                        diffDate = df.getDiffDays();
                        currentDate = df.getCurrentDate();
                        Log.d("CALCULATE DIFF DATE", "diffDate : " + diffDate);

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
                        Log.d("CALCULATE DIFF DATE", "mCartItemCount : " + mCartItemCount);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Show Donator", "ERRROR =" + e.getMessage());
                    }
                });
    }

    private void setNotifyToFirebase(String type) {
        Log.d("MY TASK", "SET NOTIFY FIREBASE HAS BEEN START");
        notificationContentFirebase = new NotificationContentFirebase(
                collectionReference, firestore, uid);
        notificationContentFirebase.getConnectionCollection();
        notificationContentFirebase.getCollectionReference().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        Log.d("SET NOTIFY FIREBASE", "size Of content : " + sizeofContent);

                        checkNotify(sizeofContent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
            }
        });
    }

    private void checkNotify(final int sizeofContent) {
        Log.d("MY TASK", "CHECK NOTIFY HAS BEEN START");
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
                            Log.d("CHECK NOTIFY", "RETURN!");
                            return;
                        }
                        if (!existNotify) {
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

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "ERRROR =" + e.getMessage());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("COUNT NOTIRY ", "ERRROR =" + e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the time Check when service is destroyed
        Log.d("MY SERVICE", "HAS BEEN DESTROY...");
        executorService.isShutdown();
    }

}
