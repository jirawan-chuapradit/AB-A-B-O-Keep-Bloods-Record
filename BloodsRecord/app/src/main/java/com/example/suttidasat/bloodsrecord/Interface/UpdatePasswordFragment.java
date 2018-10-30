package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.suttidasat.bloodsrecord.DonatorMainView;
import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_password,container,false);
    }

    private Button saveBtn;
    private EditText newPassword, reNewPassword;
    private FirebaseUser firebaseUser;
    private String userPasswordNew, userRePasswordNew;
    private ProgressDialog progressDialog;

    //menu
    private TextView textCartItemCount;
    private int mCartItemCount;
    UpdateNotify un = UpdateNotify.getUpdateNotifyInstance();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //menu
        mCartItemCount = un.getCount();
        setHasOptionsMenu(true);

        saveBtn = getView().findViewById(R.id.btnUpdatePassword);
        newPassword = getView().findViewById(R.id.updatePassword);
        reNewPassword = getView().findViewById(R.id.re_updatePassword);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Loading data dialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please waiting...");
                progressDialog.show();

                userPasswordNew = newPassword.getText().toString();
                userRePasswordNew = reNewPassword.getText().toString();
                if (userPasswordNew.length() <= 5 || userRePasswordNew.length() <= 5){
                    Log.d("REGISTER", "รหัสผ่านน้อยกว่า 6 ตัว");
                    Toast.makeText(getActivity(),"กรุณาระบุรหัสผ่านมากกว่า 5 ตัว",Toast.LENGTH_SHORT).show();
                }else if(userPasswordNew.isEmpty() || userRePasswordNew.isEmpty()){
                    Log.d("REGISTER", "VALUE IS EMPTY");
                    Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
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
                                    Log.d("UPDATEPASSWORD : ", e.getMessage());
                                        Toast.makeText(
                                                getActivity(),
                                                "Password Update Failed",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }

                            });
                }

            }
        });

    }

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
                        .commit();
                System.out.println("CLICK NOTIFY BELL");
                un.setCount(0);
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
