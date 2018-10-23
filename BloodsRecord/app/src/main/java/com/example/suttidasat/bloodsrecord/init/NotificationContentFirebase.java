package com.example.suttidasat.bloodsrecord.init;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationContentFirebase {
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseFirestore firestore;
    private String uid;
    private int size;

    public NotificationContentFirebase() {
    }


    public NotificationContentFirebase(CollectionReference collectionReference, FirebaseFirestore firestore, String uid) {
        this.collectionReference = collectionReference;
        this.firestore = firestore;
        this.uid = uid;
    }

    public NotificationContentFirebase(DocumentReference documentReference, FirebaseFirestore firestore, String uid, int size) {
        this.documentReference = documentReference;
        this.firestore = firestore;
        this.uid = uid;
        this.size = size;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public CollectionReference getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(CollectionReference collectionReference) {
        this.collectionReference = collectionReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
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


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void getConnectionCollection(){
        collectionReference = firestore.collection("notificationContent")
                .document(uid).collection("content");
    }

    public void getConnectionDocument(){
        documentReference = firestore.collection("notificationContent")
                .document(uid)
                .collection("content")
                .document(String.valueOf(size));
    }
}
