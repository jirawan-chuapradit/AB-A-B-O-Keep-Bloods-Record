package com.example.suttidasat.bloodsrecord.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.suttidasat.bloodsrecord.DonatorMainView;
import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.google.firebase.auth.FirebaseAuth;


public class notifyBGProcess  extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            //starting service
            getActivity().startService(new Intent(getActivity(),MyService.class));


        Intent myIntent = new Intent(getActivity(), DonatorMainView.class);
        getActivity().startActivity(myIntent);

    }



}
