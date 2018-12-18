package com.example.suttidasat.bloodsrecord.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
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
    private int size, diffDate, mCartItemCount, sizeofContent;
    private SharedPreferences prefs;


    //menu
    ScheduledExecutorService executorService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MY SERVICE", "HAS BEEN START...");
        //Start Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        //GET UID of Currnet user
        uid = fbAuth.getCurrentUser().getUid();
        prefs = getSharedPreferences("BloodsRecord", Context.MODE_PRIVATE);
        nationalID = prefs.getString("nationalID", "null value");
        //loop
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d("MY TASK", "MY TASK HAS BEEN START IN SPRINT");
                getHistoryDate(nationalID);
                Log.d("MY TASK", "MY TASK HAS BEEN DONE IN SPRINT");

                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        return START_STICKY;
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

                        if (size != 0) {
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

        String sizeStr = String.valueOf(size);

        firestore.collection("donateHistory")
                .document(nationalID)
                .collection("history")
                .whereEqualTo("num", sizeStr)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String historyDate = (String) queryDocumentSnapshots.getDocuments()
                                .get(0).get("date");

                        Log.d("DATE: ", String.valueOf(historyDate));

                        DateFormatCal df = new DateFormatCal(historyDate);
                        diffDate = df.getDiffDays();
                        currentDate = df.getCurrentDate();
                        Log.d("CALCULATE DIFF DATE", "diffDate : " + diffDate);

                        if (diffDate > 83 && diffDate < 90) {
                            msg = "อีก 7 วัน จะถึงรอบบริจาคครั้งถัดไป";
                            System.out.println(msg);
                            setNotifyToFirebase();
                        } else if (diffDate >= 90) {
                            msg = "สามารถบริจาคเลือดได้";
                            System.out.println(msg);
                            setNotifyToFirebase();
                        } else {
                            return;
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERROR: ", e.getMessage());
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

                        if(sizeofContent != 0){
                            final String sizeOfContentStr = String.valueOf(sizeofContent);
                            firestore.collection("notificationContent")
                                    .document(uid)
                                    .collection("content")
                                    .whereEqualTo("num", sizeOfContentStr)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<DocumentSnapshot> doc = task.getResult().getDocuments();
                                    String notifyDate = doc.get(0).get("date").toString();
                                    Log.d("notify date: ", notifyDate);

                                    DateFormatCal df = new DateFormatCal(notifyDate);
                                    diffDate = df.getDiffDays();
                                    Log.d("CAL DIFF DATE Notify", "diffDate : " + diffDate);

                                    if (diffDate > 83) {
                                        NotifyManange nm = NotifyManange.getNotifyManangeInstance();
                                        nm.setNum(String.valueOf(sizeofContent + 1));
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
                                                SharedPreferences prefs = getBaseContext().getSharedPreferences("BloodsRecord", Context.MODE_PRIVATE);
                                                mCartItemCount = prefs.getInt("_countNotify", 0);
                                                Log.d("mCartItemCount: ", String.valueOf(mCartItemCount));
                                                mCartItemCount++;
                                                SharedPreferences.Editor prefs2 = getBaseContext().getSharedPreferences("BloodsRecord", MODE_PRIVATE).edit();
                                                prefs2.putInt("_countNotify", mCartItemCount);
                                                prefs2.apply();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("ERROR: ", "ERROR =" + e.getMessage());
                                            }
                                        });
                                    } else {
                                        return;
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ERROR: ", e.getMessage());
                                }
                            });
                        }else {
                            return;
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERROR =" + e.getMessage());
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
