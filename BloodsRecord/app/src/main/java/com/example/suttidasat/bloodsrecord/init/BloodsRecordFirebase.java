package com.example.suttidasat.bloodsrecord.init;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BloodsRecordFirebase {

    private DocumentReference documentReference;
    private FirebaseFirestore firestore;
    private String uid;

    public BloodsRecordFirebase() {
    }

    public BloodsRecordFirebase(DocumentReference documentReference, FirebaseFirestore firestore, String uid) {
        this.documentReference = documentReference;
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
        return documentReference;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    public void getConnection(){
        documentReference = firestore.collection("bloodsRecord").document(uid);
    }
}
