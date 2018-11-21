package com.example.suttidasat.bloodsrecord.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.History;
import com.example.suttidasat.bloodsrecord.model.News;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History> {

    List<History> history = new ArrayList<History>();
    Context context;

    public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<History> objects) {
        super(context, resource, objects);
        this.history=objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View hisItem = LayoutInflater.from(context).inflate(R.layout.history_item ,parent,false);

        TextView num = hisItem.findViewById(R.id.num);
        TextView date = hisItem.findViewById(R.id.date);
        TextView place = hisItem.findViewById(R.id.place);
        TextView sign = hisItem.findViewById(R.id.sign);



        History row = history.get(position);

        num.setText(row.getNum());
        date.setText(row.getDate());
        place.setText(row.getPlace());
        sign.setText(row.getSign());

        return hisItem;

    }

}

