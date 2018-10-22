package com.example.suttidasat.bloodsrecord.init;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BloodsRecordFirebase {

    private DocumentReference bloodsRecordFirebase;
    private FirebaseFirestore firestore;
    private String uid;

    public BloodsRecordFirebase() {
    }

    public BloodsRecordFirebase(DocumentReference bloodsRecordFirebase, FirebaseFirestore firestore, String uid) {
        this.bloodsRecordFirebase = bloodsRecordFirebase;
        this.firestore = firestore;
        this.uid = uid;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    public void setFirestore(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DocumentReference getBloodsRecordFirebase() {
        return bloodsRecordFirebase;
    }

    public void setBloodsRecordFirebase(DocumentReference bloodsRecordFirebase) {
        this.bloodsRecordFirebase = bloodsRecordFirebase;
    }

    public void getConnection(){
        bloodsRecordFirebase = firestore.collection("bloodsRecord").document(uid);
    }
}
