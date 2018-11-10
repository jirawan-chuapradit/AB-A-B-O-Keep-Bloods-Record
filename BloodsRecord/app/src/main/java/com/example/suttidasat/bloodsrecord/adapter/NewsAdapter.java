package com.example.suttidasat.bloodsrecord.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.News;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    List<News> news = new ArrayList<News>();
    Context context;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        this.news=objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View newsItem = LayoutInflater.from(context).inflate(R.layout.manage_news_item ,parent,false);

        TextView title = newsItem.findViewById(R.id.title);
        TextView date = newsItem.findViewById(R.id.date);
        TextView link = newsItem.findViewById(R.id.title);
        TextView detail = newsItem.findViewById(R.id.date);


        News row = news.get(position);
        title.setText(row.getTitle());
        date.setText(row.getDate());
        link.setText(row.getLink());
        detail.setText(row.getDetail());

        return newsItem;

    }

}
