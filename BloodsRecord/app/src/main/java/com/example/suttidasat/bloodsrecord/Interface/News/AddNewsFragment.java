package com.example.suttidasat.bloodsrecord.Interface.News;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.Interface.SertNationalID;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewsFragment extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();

    TextView date;
    EditText t,l,d;
    String title,link,detail;
    Button back;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
    final String date_now = mdformat.format(calendar.getTime()).toString();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_news,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        date = getView().findViewById(R.id.date);
        date.setText("วันที่ : "+date_now);


        cfBtn();
        backBtn();
    }

    void backBtn(){
        back = getView().findViewById(R.id.back_to_news);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_view, new NewsManageFrament())
                        .commit();
            }
        });
    }

    void  cfBtn(){

        t = getView().findViewById(R.id.title);
        l = getView().findViewById(R.id.link);
        d = getView().findViewById(R.id.detail);

        Button cf = getView().findViewById(R.id.cf_add_news);
        cf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = t.getText().toString();
                detail = d.getText().toString();
                link = l.getText().toString();

                if (title.isEmpty() || detail.isEmpty() || link.isEmpty()){

                    Toast.makeText(
                            getActivity(),
                            "กรุณากรอกข้อมูลให้ครบถ้วน",
                            Toast.LENGTH_SHORT
                    ).show();

                }else {

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
                                News news = new News(title, date_now, detail, link);

                                firestore.collection("news")
                                        .document(date_now+title)
                                        .set(news)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(
                                                        getActivity(),
                                                        "เพิ่มรายการสำเร็จ",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                            }
                                        });

                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.admin_view,
                                                    new NewsManageFrament()).commit();

                            } else {
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
}
