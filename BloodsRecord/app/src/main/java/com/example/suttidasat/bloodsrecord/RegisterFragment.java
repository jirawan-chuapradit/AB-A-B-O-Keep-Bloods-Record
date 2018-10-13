package com.example.suttidasat.bloodsrecord;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.donator.DonatorProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment implements View.OnClickListener {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container,false);
    }

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;

    //Register value
    String firstnameStr,lastnameStr,birthStr,nationalIDStr
            ,bloodsStr,emailStr,passwordStr,rePasswordStr,uid;
    //ImageView
    private ImageView imageView;
    //Buttons
    private Button chooseBtn,loginBtn,registerBtn;

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //a Uri object to store file path
    private Uri filePath;

    private StorageReference mStorageRef;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //getting views from layout
        loginBtn =  getView().findViewById(R.id.login_btn);
        registerBtn = getView().findViewById(R.id.registerBtn);
        imageView = getView().findViewById(R.id.imageView);
        chooseBtn = getView().findViewById(R.id.chooseBtn);

        //attaching listener
        chooseBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private void login() {
        Log.d("REGISTER", "BACK TO LOGIN");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    private void register() {
        //GET VALUE FROM FRAGMENT
        gatRegisterValue();
        //check value is empty
        if(firstnameStr.isEmpty() || lastnameStr.isEmpty()||birthStr.isEmpty()||nationalIDStr.isEmpty()||bloodsStr.isEmpty()
                || emailStr.isEmpty()|| passwordStr.isEmpty()||rePasswordStr.isEmpty()){
            Log.d("REGISTER", "VALUE IS EMPTY");
            Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
        }else if(!rePasswordStr.equals(passwordStr)){
            Log.d("REGISTER", "PASSWORD IS NOT EQUALS REPASSWORD");
            Toast.makeText(getActivity(),"รหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
        }else{
            fbAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    uid = fbAuth.getCurrentUser().getUid();

//                            String birth, String fName, String lName, String nationalID, String email, String bloodGroup
                    DonatorProfile dp = new DonatorProfile(birthStr,firstnameStr,lastnameStr,nationalIDStr,emailStr
                            ,bloodsStr);
                    Log.d("REGISTER", "REGISTER SUCCESS");
                    firestore.collection("bloodsRecord")
                            .document(uid)
                            .set(dp).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");
                            uploadFile();

                            //FORCE USER SIGGOUT
                            FirebaseAuth.getInstance().signOut();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_view, new LoginFragment())
                                    .addToBackStack(null)
                                    .commit();
                            Log.d("USER", "YOU HAS BEEN SIGN OUT");
                            Log.d("USER", "GOTO LOGIN");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REGISTER", "ERRROR =" + e.getMessage());
                            Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void gatRegisterValue() {
        //GET INPUT FROM frament register
        EditText firstnameEdt = getView().findViewById(R.id.registerFirstname);
        EditText lastnameEdt = getView().findViewById(R.id.registerLastname);
        EditText birthEdt = getView().findViewById(R.id.registerBirth);
        EditText nationalIDEdt = getView().findViewById(R.id.registerNationalID);
        EditText bloodsEdt = getView().findViewById(R.id.registerBloodsGroup);
        EditText emailEdt = getView().findViewById(R.id.registerEmail);
        EditText passwordEdt = getView().findViewById(R.id.registerPassword);
        EditText rePasswordEdt = getView().findViewById(R.id.registerRePassword);

        //CONVERSE TO STRING
        firstnameStr = firstnameEdt.getText().toString();
        lastnameStr = lastnameEdt.getText().toString();
        birthStr = birthEdt.getText().toString();
        nationalIDStr = nationalIDEdt.getText().toString();
        bloodsStr = bloodsEdt.getText().toString();
        emailStr = emailEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        rePasswordStr = rePasswordEdt.getText().toString();
    }

    private void uploadFile(){

        if(filePath != null) {


            StorageReference riversRef = mStorageRef.child("image/profileImage/" +uid+".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Log.d("REGISTER", "FILE UPLOADED");
                            Toast.makeText(getContext(),"File Up loaded",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("REGISTER", "ERROR = " + exception);
                            Toast.makeText(getContext(),"ERROR = "+exception.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
        }else{
            Toast.makeText(getContext(),"ERROR = filePath is null",Toast.LENGTH_SHORT).show();

        }
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d("REGISTER", "ERROR = " + e.getMessage());
                e.printStackTrace();
            }
        }else {
            Log.d("REGISTER", "ERROR = handling the image chooser activity result Fail");
        }
    }

    //method to show file chooser
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/profileImage/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if(v == chooseBtn){
            //open file chooser
            Log.d("REGISTER", "CLICK = CHOOSE");
            showFileChooser();
        }else if(v == loginBtn){
            //back to login
            Log.d("REGISTER", "CLICK = LOGIN");
            login();

        }else if(v == registerBtn){
            //register
            Log.d("REGISTER", "CLICK = REGISTER");
            register();
        }
    }

}


