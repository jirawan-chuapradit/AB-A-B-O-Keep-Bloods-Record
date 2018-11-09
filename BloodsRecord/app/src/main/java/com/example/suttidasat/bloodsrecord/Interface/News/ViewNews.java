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
import android.widget.ListView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.NewsAdapter;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewNews extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    ArrayList<News> news_list_arr = new ArrayList<>();
    ListView listView;
    String title,date,detail,link;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_news,container,false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = getView().findViewById(R.id.news_list_danate);

        final NewsAdapter newsAdapter = new NewsAdapter(
                getActivity(),
                R.layout.view_news_item,
                news_list_arr
        );

        listView.setAdapter(newsAdapter);


        ConnectDB.getNews().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                news_list_arr.clear();
                for (DocumentSnapshot d: queryDocumentSnapshots.getDocuments()){
                    news_list_arr.add(d.toObject(News.class));
                }

                newsAdapter.notifyDataSetChanged();
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

                SharedPreferences.Editor sp = getContext().getSharedPreferences("select_news",Context.MODE_PRIVATE).edit();
                sp.putString("date",date).apply();
                sp.putString("title",title).apply();
                sp.putString("detail",detail).apply();
                sp.putString("link",link).apply();

                sp.commit();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.donator_view, new ShowSelectNews())
                        .commit();

            }

        });


    }

}
