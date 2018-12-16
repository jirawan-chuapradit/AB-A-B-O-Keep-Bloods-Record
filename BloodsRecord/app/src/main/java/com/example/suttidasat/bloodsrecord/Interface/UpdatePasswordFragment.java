package com.example.suttidasat.bloodsrecord.Interface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/*************************************************
 *intent: Update password                        *
 *pre-condition: User must login with role Donor *
 *post-condition: User sign out and go to login  *
 *************************************************/

public class UpdatePasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_password,container,false);
    }

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private Button saveBtn;
    private EditText newPassword, reNewPassword,oldPassword;
    private FirebaseUser firebaseUser;
    private String userPasswordNew, userRePasswordNew,uid,userOldPassword;
    ProgressDialog progressDialog;


    //menu
    private TextView textCartItemCount;
    private int mCartItemCount;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();


        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();
        SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt("_countNotify", 0);
        Log.d("prefs Update", String.valueOf(mCartItemCount));

        setHasOptionsMenu(true);

        saveBtn = getView().findViewById(R.id.btnUpdatePassword);
        oldPassword = getView().findViewById(R.id.update_oldPassword);
        newPassword = getView().findViewById(R.id.update_newPassword);
        reNewPassword = getView().findViewById(R.id.update_re_newPassword);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOldPassword = oldPassword.getText().toString();
                userPasswordNew = newPassword.getText().toString();
                userRePasswordNew = reNewPassword.getText().toString();


                if (userPasswordNew.length() <= 5 || userRePasswordNew.length() <= 5){
                    Log.d("UPDATE", "PASSWORD LESS THAN 6");
                    Toast.makeText(getActivity(),"กรุณาระบุรหัสผ่านมากกว่า 5 ตัว",Toast.LENGTH_SHORT).show();
                }else if(userPasswordNew.isEmpty() || userRePasswordNew.isEmpty()){
                    Log.d("UPDATE", "VALUE IS EMPTY");
                    Toast.makeText(getActivity(),"กรุณาระบุข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                }else if(!userPasswordNew.equals(userRePasswordNew)){
                    Log.d("UPDATE", "PASSWORD NOT EQUALS RE PASSWORD");
                    Toast.makeText(getActivity(),"รหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord", Context.MODE_PRIVATE);
                    String email = prefs.getString("email", "empty email");
                    Log.d("Email: ", email);

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,userOldPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            confirmDialog();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("UPDATE", "OLD PASSWORD WAS WRONG");
                                    Toast.makeText(
                                            getActivity(),
                                            "รหัสผ่านไม่ถูกต้อง",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });

                }

            }
        });

    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("ยืนยันที่จะเปลี่ยนรหัสผ่าน ใช่ หรือ ไม่")
                .setPositiveButton("ใช่",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        updatePassword(userPasswordNew);
                    }
                })
                .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    private void updatePassword(String userPasswordNew) {
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

        firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();


                    //FORCE USER SIGGOUT
                    FirebaseAuth.getInstance().signOut();

                    //starting service
                    getActivity().stopService(new Intent(getActivity(),MyService.class));
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(myIntent);
                    Log.d("USER", "GOTO LOGIN");
                    Toast.makeText(
                            getActivity(),
                            "Password has been Changed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d("UPDATE PASSWORD : ", e.getMessage());
                        Toast.makeText(
                                getActivity(),
                                "Password Update Failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                });
    }


    /**********************************
     *   intent: สร้าง popup ระบบกำลังประมวลผล  *
     **********************************/

    //menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.nofity_bell);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .addToBackStack(null)
                        .commit();
                System.out.println("CLICK NOTIFY BELL");

                Log.d("USER ", "CLICK NOTIFY BELL");

                SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE).edit();
                prefs.putInt("_countNotify",0);
                prefs.apply();

                setupBadge();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
