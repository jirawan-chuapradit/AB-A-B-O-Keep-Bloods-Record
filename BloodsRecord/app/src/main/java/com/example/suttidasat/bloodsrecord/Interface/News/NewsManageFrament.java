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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.NewsAdapter;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.example.suttidasat.bloodsrecord.model.SortNews;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class NewsManageFrament extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    ArrayList<News> news_list = new ArrayList<>();
    ListView listView;
    String title,date,detail,link;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_news,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);

        listView = getView().findViewById(R.id.news_list_admin);

        final NewsAdapter newsAdapter = new NewsAdapter(
                getActivity(),
                R.layout.view_news_item,
                news_list
        );

        listView.setAdapter(newsAdapter);


        ConnectDB.getNews().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                news_list.clear();
                for (DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                    news_list.add(d.toObject(News.class));
                }

                Collections.sort(news_list,new SortNews());

                newsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }


        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News news = new News();

                news = (News) parent.getItemAtPosition(position);

                title = news.getTitle();
                date = news.getDate();
                link = news.getLink();
                detail = news.getDetail();

                SharedPreferences.Editor sp = getContext().getSharedPreferences("select_news", Context.MODE_PRIVATE).edit();
                sp.putString("date", date).apply();
                sp.putString("title", title).apply();
                sp.putString("detail", detail).apply();
                sp.putString("link", link).apply();

                sp.commit();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.admin_view, new ShowSelectNewsAdmin())
                        .commit();


            }
        });
        plusBtn();
    }

            void plusBtn() {
                Button add = getView().findViewById(R.id.add_news_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.admin_view, new AddNewsFragment())
                                .commit();


                    }
                });
            }


}
