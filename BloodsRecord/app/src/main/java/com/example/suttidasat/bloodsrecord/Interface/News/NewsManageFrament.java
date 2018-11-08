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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.suttidasat.bloodsrecord.Interface.AddNewsFragment;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.NewsAdapter;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NewsManageFrament extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    ArrayList<News> news_list = new ArrayList<>();
    ListView newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_news,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newsList = getView().findViewById(R.id.news_list_admin);

        final NewsAdapter newsAdapter = new NewsAdapter(
                getActivity(),
                R.layout.manage_news_item,
                news_list
        );

        newsList.setAdapter(newsAdapter);


        ConnectDB.getNews().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                news_list.clear();
                for (DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                    news_list.add(d.toObject(News.class));
                }

                newsAdapter.notifyDataSetChanged();
            }


        });


        plusBtn();
    }

    void plusBtn(){
        Button add = getView().findViewById(R.id.add_news_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view,
                                new AddNewsFragment()).commit();
            }
        });
    }
}
