package com.example.suttidasat.bloodsrecord;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.donator.DonatorProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container,false);
    }


    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);



        registerBtn();
    }

    private void registerBtn() {
        Button registerBtn = getView().findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                  final String firstnameStr = firstnameEdt.getText().toString();
                  final String lastnameStr = lastnameEdt.getText().toString();
                  final String birthStr = birthEdt.getText().toString();
                  final String nationalIDStr = nationalIDEdt.getText().toString();
                  final String bloodsStr = bloodsEdt.getText().toString();
                  final String emailStr = emailEdt.getText().toString();
                String passwordStr = passwordEdt.getText().toString();
                String rePasswordStr = rePasswordEdt.getText().toString();

                //check value is empty
                if(firstnameStr.isEmpty() || lastnameStr.isEmpty()||birthStr.isEmpty()||nationalIDStr.isEmpty()||bloodsStr.isEmpty()
                        || emailStr.isEmpty()|| passwordStr.isEmpty()||rePasswordStr.isEmpty()){
                    Log.d("REGISTER", "VALUE IS EMPTY");
                    Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                }else if(!rePasswordStr.equals(passwordStr)){
                    Log.d("REGISTER", "PASSWORD IS NOT EQUALS REPASSWORD");
                    Toast.makeText(getActivity(),"รหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
                }else{
                    // check verify email
                    fbAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String uid = fbAuth.getCurrentUser().getUid();

//                            String birth, String fName, String lName, String nationalID, String email, String bloodGroup
                            DonatorProfile dp = new DonatorProfile(birthStr,firstnameStr,lastnameStr,nationalIDStr,emailStr
                            ,bloodsStr);

                            Log.d("REGISTER", "REGISTER SUCCESS");
                            firestore.collection("bloodsRecord")
                                    .document(uid)
                                    .collection("nationalID")
                                    .document(nationalIDStr)
                                    .set(dp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "ERRROR =" + e.getMessage());
                                    Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

                            //save donator profile into firebase
//                            firebaseDonatorProfile(birthStr,firstnameStr,lastnameStr,nationalIDStr
//                                    ,emailStr,bloodsStr);
                            //sentverifedemail
                            sendVerifiedEmail(authResult.getUser());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REGISTER", "ERROR =" + e);
                            Toast.makeText(getActivity(),"Error = " + e, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

//    private void firebaseDonatorProfile(String birth, String fName, String lName, String nationalID, String email, String bloodGroup) {
//        String uid = fbAuth.getCurrentUser().getUid();
//
////                            String birth, String fName, String lName, String nationalID, String email, String bloodGroup
//        DonatorProfile dp = new DonatorProfile(birth,fName,lName,nationalID
//                ,email,bloodGroup);
//
//        firestore.collection("bloodsRecord")
//                .document(uid)
//                .set(dp).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("REGISTER", "ERRROR =" + e.getMessage());
//                Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    void sendVerifiedEmail(FirebaseUser _user){
        _user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),"YOU HAS BEEN REGISTER PLEASE CONFIRM YOUR E-MAIL"
                        ,Toast.LENGTH_SHORT)
                        .show();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new LoginFragment())
                        .commit();

//                 user sign out
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
                Toast.makeText(getActivity(),"ERROR = " + e.getMessage()
                        ,Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
