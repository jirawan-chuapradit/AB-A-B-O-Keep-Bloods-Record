package com.example.suttidasat.bloodsrecord.donator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.suttidasat.bloodsrecord.R;

import java.util.ArrayList;
import java.util.List;

public class DonatorAdapter extends ArrayAdapter<DonatorProfile> {
//
//
    List<DonatorProfile> donatorProfiles = new ArrayList<DonatorProfile>();
    Context context;
//
    public DonatorAdapter(@NonNull Context context, int resource, @NonNull List<DonatorProfile> objects) {
        super(context, resource, objects);
        this.donatorProfiles = objects;
        this.context = context;
    }

//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View donatorItem = LayoutInflater.from(context).inflate(R.layout.fragment_donator_profile,parent,false);
//
//        //get textView
//         TextView profileName =  donatorItem.findViewById(R.id.profileName);
//        TextView profileNationalID = donatorItem.findViewById(R.id.profileNationalID);
//        TextView profileBirth = donatorItem.findViewById(R.id.profileBirth);
//        TextView profileBlood = donatorItem.findViewById(R.id.profileBloodsG);
//
//        DonatorProfile rowDP = donatorProfiles.get(position);
//        System.out.println("First name : "+rowDP.getfName());
//        profileName.setText(rowDP.getfName());
//
//        return donatorItem;
//    }

}
