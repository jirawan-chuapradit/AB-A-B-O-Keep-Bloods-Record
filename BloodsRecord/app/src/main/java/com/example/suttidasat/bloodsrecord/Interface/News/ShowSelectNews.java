package com.example.suttidasat.bloodsrecord.Interface.News;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;

public class ShowSelectNews extends Fragment {

    TextView title,date,detail,link;
    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_select_news_donat, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        show();

    }



    void  show(){

        sp = getContext().getSharedPreferences("select_news",Context.MODE_PRIVATE);

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
