package com.example.suttidasat.bloodsrecord.model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ConnectDB  {

    private static ConnectDB DBInstance;
    private ConnectDB(){}



    public static ConnectDB getDBInstance(){
        if (DBInstance == null){
            DBInstance = new ConnectDB();
        }
        return DBInstance;
    }

    public static FirebaseFirestore getConnect(){
        return FirebaseFirestore.getInstance();
    }

    public static Task<QuerySnapshot> getNews(){
        return ConnectDB.getConnect().collection("news").get();
    }


    public static Task<QuerySnapshot> getHistoryConnect(){
        int amount;
        return ConnectDB.getDBInstance().getConnect().collection("donateHistory")
                .document(NationaID.NID)
                .collection("history")
                .get();

    }


}
