package com.example.suttidasat.bloodsrecord.Interface;

import java.text.SimpleDateFormat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.DonatorHistory;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;


/*******************************************************
 *intent: Show history of donor and plus donation time *
 *pre-condition: User must login with role Admin and   *
 *               User must insert national ID of donor *
 *post-condition: User go to Insert national ID page   *
 *******************************************************/

public class InsertHistoryFragment extends Fragment {


    FirebaseFirestore firestore = ConnectDB.getConnect();
    DocumentReference donateHistory;
    private TextView profileName, profileNationalID, profileBlood, profileEmail, currentDate, amount;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");

    final String date = mdformat.format(calendar.getTime());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.insert_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        deley();

//        firestore = FirebaseFirestore.getInstance();

        insertHistory();
        backBtn();
        plusBtn();

    }

    void insertHistory() {


        profileName = getView().findViewById(R.id.sh_name_donater);
        profileNationalID = getView().findViewById(R.id.sh_nid_donater);
        profileBlood = getView().findViewById(R.id.sh_group_donater);
        profileEmail = getView().findViewById(R.id.sh_email_donater);
        amount = getView().findViewById(R.id.sh_amount);
        currentDate = getView().findViewById(R.id.sh_date_now);

        firestore.collection("donateHistory")
                .document(NationaID.NID)
                .collection("history")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        amount.setText("จำนวนการบริจาคทั้งหมด : " + queryDocumentSnapshots.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        firestore.collection("bloodsRecord")
                .whereEqualTo("nationalID", NationaID.NID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> doc = task.getResult().getDocuments();



                        String name = doc.get(0).get("fName").toString() + " " + doc.get(0).get("lName").toString();

                        String nationalID = doc.get(0).get("nationalID").toString();
                        String blood = doc.get(0).get("bloodGroup").toString();
                        String email = doc.get(0).get("email").toString();

                        profileName.setText("ชื่อ : " + name);
                        profileNationalID.setText("หมายเลขบัตรประชาชน : " + nationalID);
                        profileBlood.setText("กรุ๊ปเลือด : " + blood);
                        profileEmail.setText("อีเมล : " + email);
                        currentDate.setText("วันที่ : " + date);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void plusBtn() {
        Button plus = getView().findViewById(R.id.btn_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deley();

                firestore.collection("donateHistory")
                        .document(NationaID.NID)
                        .collection("history")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                final int size = queryDocumentSnapshots.size();
                                firestore.collection("donateHistory")
                                        .document(NationaID.NID)
                                        .collection("history")
                                        .document(String.valueOf(size))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String date_last;
                                                if (size == 0)
                                                    date_last = "";
                                                else
                                                    date_last = task.getResult().get("date").toString();
                                                if (!date_last.equals(date)) {
                                                    donateHistory = firestore.collection("donateHistory")
                                                            .document(NationaID.NID);
                                                    donateHistory.get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                    if (documentSnapshot.exists() || size > 0) { //have
                                                                        firestore.collection("donateHistory") /// get size (amount of donate)
                                                                                .document(NationaID.NID)
                                                                                .collection("history")
                                                                                .get()
                                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                                        DonatorHistory dh = DonatorHistory.getDonatorHistoryInstance();
                                                                                        dh.setDate(date);

                                                                                        String num = Integer.toString(queryDocumentSnapshots.size() + 1);
                                                                                        firestore.collection("donateHistory")
                                                                                                .document(NationaID.NID)
                                                                                                .collection("history")
                                                                                                .document(num)
                                                                                                .set(dh)
                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        Log.d("Insert", "Insert Success");
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                });

                                                                    } else {
                                                                        // never donate before
                                                                        DonatorHistory dh = DonatorHistory.getDonatorHistoryInstance();
                                                                        dh.setDate(date);

                                                                        firestore.collection("donateHistory")
                                                                                .document(NationaID.NID)
                                                                                .collection("history")
                                                                                .document("1")
                                                                                .set(dh)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Log.d("Insert", "Insert First Time Success");
                                                                                    }
                                                                                });

                                                                    }

                                                                    Toast.makeText(getActivity(), "เพิ่มประวัติการบริจาคสำเร็จ", Toast.LENGTH_SHORT).show();

                                                                    getActivity().getSupportFragmentManager()
                                                                            .beginTransaction()
                                                                            .replace(R.id.admin_view, new SertNationalID())
                                                                            .commit();

                                                                }

                                                            });
                                                } else {
                                                    System.out.println("วันซ้ำๆๆๆๆๆๆ");
                                                    Toast.makeText(getActivity(), "เพิ่มการบริจาควันนี้ซ้ำ", Toast.LENGTH_SHORT).show();
                                                    getActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.admin_view, new InsertHistoryFragment())
                                                            .commit();
                                                }
                                            }
                                        });

                            }
                        });

            }
        });
    }

    void backBtn() {
        Button back = getView().findViewById(R.id.btn_back_sertNID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view, new SertNationalID())
                        .commit();

            }
        });
    }

    /**********************************
     *   intent: สร้าง popup ระบบกำลังประมวลผล  *
     **********************************/
    private void deley() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(2); // Incremented By Value 2
            }
        };
        // Progress Dialog Max Value
        progressDialog.setMax(100);
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog.getMax()) {
                        Thread.sleep(100);
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }

                }catch (Exception e){
                    e.getStackTrace();
                }
            }
        }).start();
    }
}
