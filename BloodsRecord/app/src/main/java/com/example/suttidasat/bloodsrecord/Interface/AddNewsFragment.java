package com.example.suttidasat.bloodsrecord.Interface;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;

public class AddNewsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_news, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backBtn();
        cfBtn();
    }

    void backBtn() {
        Button back = getView().findViewById(R.id.back_to_news);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view,
                                new NewsManageFrament()).commit();
            }
        });
    }

    void cfBtn() {

//        Button cf = getView().findViewById(R.id.cf_add_news);
//        cf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder builder =
//                        new AlertDialog.Builder(AddNewsFragment.this);
//            }
//        });

    }

}
