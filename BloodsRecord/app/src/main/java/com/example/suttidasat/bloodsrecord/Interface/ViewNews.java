package com.example.suttidasat.bloodsrecord.Interface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.NewsAdapter;
import com.example.suttidasat.bloodsrecord.model.ConnectDB;
import com.example.suttidasat.bloodsrecord.model.News;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewNews extends Fragment {

    FirebaseFirestore firestore = ConnectDB.getConnect();
    ArrayList<News> news = new ArrayList<>();
    ListView newList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_news,container,false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newList = getView().findViewById(R.id.news_list);

        final NewsAdapter _weightAdapter = new NewsAdapter(
                getActivity(),
                R.layout.manage_news_item,
                news
        );
    }
}
