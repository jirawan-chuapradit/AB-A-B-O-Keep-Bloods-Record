package com.example.suttidasat.bloodsrecord.Interface.News;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.Interface.InsertHistoryFragment;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Dialog_news extends AppCompatActivity {
    FirebaseFirestore firestore = ConnectDB.getConnect();

    TextView date;
    EditText t,l,d;
    String title,link,detail;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
    final String date_now = mdformat.format(calendar.getTime()).toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news);


        date = findViewById(R.id.date);
        date.setText(date_now);


        cfBtn();

    }

    void  cfBtn(){

        t = findViewById(R.id.title);
        l = findViewById(R.id.link);
        d = findViewById(R.id.detail);

            Button cf = findViewById(R.id.cf_add_news);
            cf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = t.getText().toString();
                    detail = d.getText().toString();
                    link = l.getText().toString();

                    if (title.isEmpty() || detail.isEmpty() || link.isEmpty()){
                        Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบถ้วน",
                                Toast.LENGTH_SHORT).show();
                    }else {

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(Dialog_news.this);
                        LayoutInflater inflater = getLayoutInflater();

                        View view = inflater.inflate(R.layout.dialog_custom, null);
                        builder.setView(view);


                        final EditText password = (EditText) view.findViewById(R.id.password);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Check password
                                if (password.getText().toString().equals("12345678")) {
                                    News news = new News(title, date_now, detail, link);

                                    firestore.collection("news")
                                            .document(date_now)
                                            .set(news)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "เพิ่มรายการสำเร็จ",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

//                                    getSupportFragmentManager()
//                                            .beginTransaction()
//                                            .replace(R.id.admin_view,
//                                                    new NewsManageFrament()).commit();

                                } else {
                                    Toast.makeText(getApplicationContext(), "รหัสผ่านไม่ถูกต้อง",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.show();

                    }
                }

            });
        }



}
