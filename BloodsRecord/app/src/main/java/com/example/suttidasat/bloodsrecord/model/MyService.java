package com.example.suttidasat.bloodsrecord.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
   UpdateNotify un = UpdateNotify.getUpdateNotifyInstance();
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
                getNationalId();

                checkNotifyIsEmpty();

                Log.d("MY TASK", "MY TASK HAS BEEN DONE IN SPRINT");

                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    private void checkNotifyIsEmpty() {
        notificationContentFirebase = new NotificationContentFirebase(
                collectionReference, firestore, uid);
        notificationContentFirebase.getConnectionCollection();
        notificationContentFirebase.getCollectionReference().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        Log.d("CHECK NOTIFY EMPTY", "size Of content : " + sizeofContent);
                        if(sizeofContent!= 0){
                            SharedPreferences.Editor prefs = getBaseContext().getSharedPreferences("BloodsRecord",MODE_PRIVATE).edit();
                            prefs.putInt("_checkFnotify", 1);
                            prefs.apply();
                            Log.d("CHECK NOTIFY EMPTY", "working...");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
            }
        });
    }

    private void getNationalId() {
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
         firestore.collection("donateHistory")
                .document(nationalID).collection("history").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        size = queryDocumentSnapshots.size();
                        Log.d("GET HOSTORY DONATE", "Time of Donate : " + size);

                        if(size != 0){
                            //operation calculate difference date
                            calculateDiffDate();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
            }
        });
    }

    private void calculateDiffDate() {
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
                        type = un.countNotify(type);
                        if (type.equals("7days")) {
                            msg = "อีก 7 วัน จะถึงรอบบริจาคครั้งถัดไป";
                        } else if (type.equals("today")) {
                            msg = "สามารถบริจาคเลือดได้";
                        }
                        else if (type.equals("not change")){
                            return;
                        }
                        setNotifyToFirebase();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Show Donator", "ERRROR =" + e.getMessage());
                    }
                });
    }

    private void setNotifyToFirebase() {
        Log.d("MY TASK", "SET NOTIFY FIREBASE HAS BEEN START");
        firestore.collection("notificationContent")
                .document(uid).collection("content").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeofContent = queryDocumentSnapshots.size();
                        Log.d("SET NOTIFY FIREBASE", "size Of content : " + sizeofContent);

                        if(sizeofContent!= 0){
                            checkNotify(sizeofContent);
                        }
                        else {
                            Log.d("SET NOTIFY TO FIREBASE", "SIZE OF CONTENT = 0");
                            NotifyManange nm = NotifyManange.getNotifyManangeInstance();
                            nm.setDate(currentDate);
                            nm.setText(msg);
                            firestore.collection("notificationContent")
                                    .document(uid)
                                    .collection("content")
                                    .document(String.valueOf(sizeofContent + 1))
                                    .set(nm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("FIRST TIME NOTIFY", "Notification has been saved!!!");

                                    SharedPreferences.Editor prefs = getBaseContext().getSharedPreferences("BloodsRecord",MODE_PRIVATE).edit();
                                    prefs.putInt("_countNotify",1);
                                    prefs.apply();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "ERROR =" + e.getMessage());
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERROR =" + e.getMessage());
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
                                    Log.d("NEW NOTIFY NOT EXIST ", "Notification has been saved!!!");


                                    SharedPreferences prefs = getBaseContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
                                    mCartItemCount = prefs.getInt("_countNotify", 0);
                                    mCartItemCount++;
                                    SharedPreferences.Editor prefs2 = getBaseContext().getSharedPreferences("BloodsRecord",MODE_PRIVATE).edit();
                                    prefs2.putInt("_countNotify",mCartItemCount);
                                    prefs2.apply();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "ERROR =" + e.getMessage());
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("COUNT NOTIFY ", "ERROR =" + e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the time Check when service is destroyed
        Log.d("MY SERVICE", "HAS BEEN DESTROY...");
        executorService.shutdown();
    }

}
