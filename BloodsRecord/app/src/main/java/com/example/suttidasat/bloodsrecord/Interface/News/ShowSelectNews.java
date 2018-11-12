package com.example.suttidasat.bloodsrecord.Interface.News;

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
        return inflater.inflate(R.layout.show_select_news, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backBtn();
        show();

    }

    void backBtn(){

        Button back = getView().findViewById(R.id.back_to_newsList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.donator_view,
                                new ViewNews()).commit();
            }
        });
    }

    void  show(){

        sp = getContext().getSharedPreferences("select_news",Context.MODE_PRIVATE);

        title = getView().findViewById(R.id.sh_title);
        date = getView().findViewById(R.id.sh_date);
        detail = getView().findViewById(R.id.sh_detail);
        link = getView().findViewById(R.id.sh_link);


        title.setText(sp.getString("title",""));
        date.setText(sp.getString("date",""));
        detail.setText(sp.getString("detail",""));
        link.setText(sp.getString("link",""));
    }
}
