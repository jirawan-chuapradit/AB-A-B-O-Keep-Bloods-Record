package com.example.suttidasat.bloodsrecord.Interface.News;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowSelectNewsAdmin extends Fragment {

    TextView title,date,detail,link;
    SharedPreferences sp;
    Button back,delete;
    FirebaseFirestore firestore = ConnectDB.getConnect();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_select_news, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getContext().getSharedPreferences("select_news",Context.MODE_PRIVATE);

        backBtn();
        deleteBtn();
        show();

    }

    void backBtn(){

        back = getView().findViewById(R.id.back_to_newsList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_view,
                                new NewsManageFrament()).commit();
            }
        });
    }

    void deleteBtn(){
        delete = getView().findViewById(R.id.delete_news);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                            firestore.collection("news")
                                    .document(sp.getString("date", "none") + sp.getString("title", "none")).delete();


                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.admin_view,
                                            new NewsManageFrament()).commit();
                            Toast.makeText(
                                    getActivity(),
                                    "ลบรายการเรียบร้อย",
                                    Toast.LENGTH_SHORT
                            ).show();

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
        });
    }
    void  show(){

        title = getView().findViewById(R.id.sh_title);
        date = getView().findViewById(R.id.sh_date);
        detail = getView().findViewById(R.id.sh_detail);
        link = getView().findViewById(R.id.sh_link);


        title.setText(sp.getString("title",""));
        date.setText("วันที่ : " +sp.getString("date",""));
        detail.setText(sp.getString("detail",""));
        link.setText("อ่านต่อได้ที่ : " +sp.getString("link",""));

    }
}
