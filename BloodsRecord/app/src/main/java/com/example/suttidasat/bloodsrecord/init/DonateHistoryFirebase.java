package com.example.suttidasat.bloodsrecord.init;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonateHistoryFirebase {

    private CollectionReference collectionReference;
    private DocumentReference documentReference;
    private FirebaseFirestore firestore;
    private String nationalID;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String size;

    public DonateHistoryFirebase(CollectionReference collectionReference, FirebaseFirestore firestore, String nationalID) {

        this.collectionReference = collectionReference;
        this.firestore = firestore;
        this.nationalID = nationalID;
    }

    public DonateHistoryFirebase() {
    }

    public DonateHistoryFirebase(DocumentReference documentReference, FirebaseFirestore firestore,String nationalID, String size) {
        this.documentReference = documentReference;
        this.firestore = firestore;
        this.nationalID = nationalID;
        this.size = size;
    }


    public CollectionReference getCollectionReference() {
        return collectionReference;
    }

    public void setCollectionReference(CollectionReference collectionReference) {
        this.collectionReference = collectionReference;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
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

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public void getConnectionCollection(){

        collectionReference = firestore.collection("donateHistory")
                .document(nationalID).collection("history");

    }

    public void getConnectionDocument(){
        documentReference = firestore.collection("donateHistory")
                .document(nationalID)
                .collection("history")
                .document(String.valueOf(size));
    }
}
