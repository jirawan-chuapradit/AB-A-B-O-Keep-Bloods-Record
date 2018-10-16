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

import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirebaseAuth firebaseAuth;
    private String userPasswordNew, userRePasswordNew;




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveBtn = getView().findViewById(R.id.btnUpdatePassword);
        newPassword = getView().findViewById(R.id.updatePassword);
        reNewPassword = getView().findViewById(R.id.re_updatePassword);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            if(task.isSuccessful()){

                                //FORCE USER SIGGOUT
                                FirebaseAuth.getInstance().signOut();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_view, new LoginFragment())
                                        .addToBackStack(null)
                                        .commit();
                                Log.d("USER", "GOTO LOGIN");
                                Toast.makeText(
                                        getActivity(),
                                        "Password has been Changed",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }else{
                                Toast.makeText(
                                        getActivity(),
                                        "Password Update Failed",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
