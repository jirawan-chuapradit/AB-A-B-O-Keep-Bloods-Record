package com.example.suttidasat.bloodsrecord.donator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.example.suttidasat.bloodsrecord.R;

import java.util.ArrayList;
import java.util.List;

public class DonatorAdapter extends ArrayAdapter<DonatorProfile> {
//
//
    List<DonatorProfile> donatorProfile = new ArrayList<DonatorProfile>();
    Context context;
//
    public DonatorAdapter(@NonNull Context context, int resource, @NonNull List<DonatorProfile> objects) {
        super(context, resource, objects);
        this.donatorProfile = objects;
        this.context = context;
    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View donator = LayoutInflater.from(context).inflate(R.layout.fragment_register,parent,false);
//
//        DonatorProfile row = donatorProfile.get(position);
//        donatorProfile.g
//
//        Weight _row = weights.get(position);
//        _date.setText(_row.getDate());
//        _weight.setText(Integer.toString(_row.getWeight()));
//
//
//        if (position > 0) {
//            Weight _prevRow = weights.get(position - 1);
//            if (_prevRow.weight > _row.weight) {
//                _status.setText("DOWN");
//            } else if (_row.weight > _prevRow.weight) {
//                _status.setText("UP");
//            }else{
//                _status.setText(" ");
//            }
//        }else {
//            _status.setText(" ");
//        }
//        return _weightItem;
//    }

}
