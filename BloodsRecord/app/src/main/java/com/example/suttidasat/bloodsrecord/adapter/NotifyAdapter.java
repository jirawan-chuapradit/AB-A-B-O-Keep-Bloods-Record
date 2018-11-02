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
import com.example.suttidasat.bloodsrecord.model.NotifyManange;

import java.util.ArrayList;
import java.util.List;

public class NotifyAdapter extends ArrayAdapter<NotifyManange> {

    List<NotifyManange> notifyMananges = new ArrayList<NotifyManange>();
    Context context;


    public NotifyAdapter(@NonNull Context context, int resource, @NonNull List<NotifyManange> objects) {
        super(context, resource, objects);
        this.notifyMananges=objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View notifyItem = LayoutInflater.from(context).inflate(R.layout.fragment_notify_item ,parent,false);

        TextView date = notifyItem.findViewById(R.id.notify_item_date);
        TextView text = notifyItem.findViewById(R.id.notify_item_text);


        NotifyManange row = notifyMananges.get(position);
        date.setText(row.getDate());
        text.setText(row.getText());

        return notifyItem;

    }
}
