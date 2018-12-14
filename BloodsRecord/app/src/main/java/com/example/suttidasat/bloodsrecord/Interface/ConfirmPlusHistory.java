package com.example.suttidasat.bloodsrecord.Interface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.Interface.News.NewsManageFrament;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.History;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmPlusHistory extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    DocumentReference donateHistory;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
    final String c_date = mdformat.format(calendar.getTime());
    String place,sign;
    String date_last;
    EditText p,s;
    TextView date,amount;
    int intAmount;
    SharedPreferences sp ;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_plus_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        deley();
        sp = getContext().getSharedPreferences("donate_amount", Context.MODE_PRIVATE);
        intAmount = Integer.parseInt(sp.getString("amount",null))+1;

        date = getView().findViewById(R.id.cf_date);
        date.setText(c_date);

        amount = getView().findViewById(R.id.cf_amount);
        amount.setText(String.valueOf(intAmount));


        backBtn();
        plusBtn();



    }

    void plusBtn() {


        Button plus = getView().findViewById(R.id.cf_plus_his);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = getView().findViewById(R.id.place);
                s = getView().findViewById(R.id.sign);

                place = p.getText().toString();
                sign = s.getText().toString();

                if (place.isEmpty() || sign.isEmpty()) {

                    Toast.makeText(
                            getActivity(),
                            "กรุณากรอกข้อมูลให้ครบถ้วน",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();

                    View view = inflater.inflate(R.layout.dialog_custom, null);
                    builder.setView(view);


                    final EditText password = (EditText) view.findViewById(R.id.password);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Check password
                            if (password.getText().toString().equals("12345678")) {
                                History history = new History(String.valueOf(intAmount), c_date, place, sign);

                                firestore.collection("donateHistory")
                                        .document(NationaID.NID)
                                        .collection("history")
                                        .document(c_date)
                                        .set(history)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(
                                                        getActivity(),
                                                        "เพิ่มรายการสำเร็จ",
                                                        Toast.LENGTH_SHORT
                                                ).show();


                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.admin_view,
                                                                new InsertHistoryFragment()).commit();

                                            }

                                        });

                            } else

                            {
                                Toast.makeText(
                                        getActivity(),
                                        "รหัสผ่านไม่ถูกต้อง",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }
        });
    }

    void backBtn() {
        Button back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_view, new InsertHistoryFragment())
                        .commit();


            }
        });
    }
}
