package com.example.suttidasat.bloodsrecord.Interface;


import android.app.ProgressDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.DonatorProfile;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.example.suttidasat.bloodsrecord.model.PicassoCircleTransformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class RegisterFragment extends Fragment implements View.OnClickListener {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container,false);
    }

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private Spinner spinner1;


    //Register value
    private String firstnameStr,lastnameStr,nationalIDStr
            ,bloodsStr,emailStr,passwordStr,rePasswordStr
            ,uid,addressStr;
    private boolean nationalIdIsEmpty;

    //ImageView
    private ImageView userProfileImage;
    //Buttons
    private Button registerBtn;

    //a constant to track the file chooser intent
    private static int PICK_IMAGE = 123;

    //a Uri object to store file path
    private Uri filePath;

    ProgressDialog progressDialog;





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        //getting views from layout

        registerBtn = getView().findViewById(R.id.registerBtn);
        userProfileImage = getView().findViewById(R.id.userProfileImage);

        //attaching listener

        registerBtn.setOnClickListener(this);
        userProfileImage.setOnClickListener(this);


    }


    private void register() {

        //GET VALUE FROM FRAGMENT
        getRegisterValue();
        checkNationIdIsExist();
        //check value is empty
        if(firstnameStr.isEmpty() || lastnameStr.isEmpty()||nationalIDStr.isEmpty()||bloodsStr.isEmpty()
                || emailStr.isEmpty()|| passwordStr.isEmpty()||rePasswordStr.isEmpty()||addressStr.isEmpty()){
            Log.d("REGISTER", "VALUE IS EMPTY");
            Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
        }else if(!rePasswordStr.equals(passwordStr)){
            Log.d("REGISTER", "PASSWORD IS NOT EQUALS REPASSWORD");
            Toast.makeText(getActivity(),"รหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
        }else if(nationalIDStr.length() != 13){
            Log.d("REGISTER", "กรุณาระบุเลขบัตรประชาชนให้ครบ");
            Toast.makeText(getActivity(),"กรุณาระบุเลขบัตรประชาชนให้ครบ",Toast.LENGTH_SHORT).show();
        }
        else if (rePasswordStr.length() <= 5 || passwordStr.length() <= 5){
            Log.d("REGISTER", "รหัสผ่านน้อยกว่า 6 ตัว");
            Toast.makeText(getActivity(),"กรุณาระบุรหัสผ่านมากกว่า 5 ตัว",Toast.LENGTH_SHORT).show();
        }
        else if(filePath == null){
            Log.d("REGISTER", "ไม่ได้เลือกรูปภาพ");
            Toast.makeText(getActivity(),"กรุณาใส่รูปภาพ",Toast.LENGTH_SHORT).show();
        }else if(nationalIdIsEmpty==false){
            Log.d("REGISTER : ", "NATIONAL ID IS ALREADY EXIST");
            Toast.makeText(getActivity(),"รหัสบัตรประชาชนนี้ไม่สามารถลงทะเบียนได้",Toast.LENGTH_SHORT).show();
        }
        else{
            /**********************************
             *   intent: สร้าง popup ระบบกำลังประมวลผล  *
             **********************************/
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
            progressDialog.setMessage("กรุณารอสักครู่...");
            // Progress Dialog Style Horizontal
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Display Progress Dialog
            progressDialog.show();
            // Cannot Cancel Progress Dialog
            progressDialog.setCancelable(false);

            fbAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    uid = fbAuth.getCurrentUser().getUid();
                    sendUserData();

                }
            });
        }
    }

    private void checkNationIdIsExist() {
        Query existNationalId;
        existNationalId = firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID", nationalIDStr);
        existNationalId.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d("NATIONAL_ID : ", "CAN RESIST");
                            nationalIdIsEmpty = true;

                        }else {
                            Log.d("NATIONAL_ID : ", "IS ALREADY EXIST");
                            nationalIdIsEmpty = false;
                        }
                    }
                });

    }

    private void sendUserData() {

        //save image to storage
        StorageReference imageReference = storageReference.child(uid).child("Images").child("Profile Pic");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(filePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(
                        getActivity(),
                        "Upload failed!",
                        Toast.LENGTH_SHORT
                ).show();

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(
                        getActivity(),
                        "Upload successful!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        DonatorProfile dp = DonatorProfile.getDonatorProfileInstance();
        dp.setfName(firstnameStr);
        dp.setlName(lastnameStr);
        dp.setNationalID(nationalIDStr);
        dp.setEmail(emailStr);
        dp.setBloodGroup(bloodsStr);
        dp.setPassword(passwordStr);
        dp.setAddress(addressStr);
        Log.d("REGISTER", "REGISTER SUCCESS");

        firestore.collection("bloodsRecord")
                .document(uid)
                .set(dp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");
                progressDialog.dismiss();
                //FORCE USER SIGGOUT
                FirebaseAuth.getInstance().signOut();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
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

    private void getRegisterValue() {
        //GET INPUT FROM frament register
        EditText firstnameEdt = getView().findViewById(R.id.registerFirstname);
        EditText lastnameEdt = getView().findViewById(R.id.registerLastname);
        EditText nationalIDEdt = getView().findViewById(R.id.registerNationalID);
        EditText emailEdt = getView().findViewById(R.id.registerEmail);
        EditText passwordEdt = getView().findViewById(R.id.registerPassword);
        EditText rePasswordEdt = getView().findViewById(R.id.registerRePassword);
        EditText addressEdt = getView().findViewById(R.id.registerAddress);
        spinner1 = getView().findViewById(R.id.spinner1);



        //CONVERSE TO STRING
        firstnameStr = firstnameEdt.getText().toString().toUpperCase();
        lastnameStr = lastnameEdt.getText().toString().toUpperCase();
        nationalIDStr = nationalIDEdt.getText().toString();
        bloodsStr = (String) spinner1.getSelectedItem();
        emailStr = emailEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        rePasswordStr = rePasswordEdt.getText().toString();
        addressStr = addressEdt.getText().toString();

    }



    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            filePath = data.getData();

                Picasso.get().load(filePath).fit().centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .transform((Transformation) new PicassoCircleTransformation())
                        .into(userProfileImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //method to show file chooser
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    /**********************************
     *   intent: สร้าง popup ระบบกำลังประมวลผล  *
     **********************************/
    @Override
    public void onClick(View v) {
        if(v == userProfileImage){
            //open file chooser
            Log.d("REGISTER", "CLICK = USER_PROFIRE_IMAGE");
            showFileChooser();

        }else if(v == registerBtn){
            //register
            Log.d("REGISTER", "CLICK = REGISTER");
            register();
        }
    }

}


