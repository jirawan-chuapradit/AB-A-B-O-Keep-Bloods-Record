package com.example.suttidasat.bloodsrecord.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.suttidasat.bloodsrecord.DonatorMainView;
import com.example.suttidasat.bloodsrecord.MainActivity;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class SertNationalID extends Fragment {

    DocumentReference danateHistory;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sert_nationalid, container,false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSertNationalID();

    }

    void btnSertNationalID(){

        Button sertNa = getView().findViewById(R.id.btn_sert_nationalID);

        sertNa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText nid = getView().findViewById(R.id.sert_nationalID);

                NationaID.NID = nid.getText().toString();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_view, new InsertHistoryFragment())
                        .commit();


                Log.d("USER", "Go To insert Danater History");
            }
        });


    }
}
