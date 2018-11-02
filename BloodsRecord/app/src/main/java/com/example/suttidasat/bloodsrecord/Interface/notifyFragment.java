package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.adapter.NotifyAdapter;
import com.example.suttidasat.bloodsrecord.model.NotifyManange;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*******************************************************
 *intent: Show notification                            *
 *pre-condition: User must login with role Donor       *
 *post-condition: Time of new notifications disappears *
 *******************************************************/

public class notifyFragment extends Fragment {

    private TextView textCartItemCount;
    private  int mCartItemCount;

    UpdateNotify un = UpdateNotify.getUpdateNotifyInstance();

    ArrayList<NotifyManange> notifyMananges = new ArrayList<>();
    String uid;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private int checkFnofity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notify,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        deley();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt(uid+"_countNotify", -1);
        Log.d("prefs Notify", String.valueOf(mCartItemCount));

        checkFnofity = prefs.getInt(uid+"_checkFnotify", 2);
        Log.d("CHECK NOTIFY: " , String.valueOf(checkFnofity));


        if(checkFnofity == 1){
            TextView emptyNotify = getView().findViewById(R.id.empty_notify);
            emptyNotify.setText("");

        }

        setHasOptionsMenu(true);


        ListView notifyList = getView().findViewById(R.id.notify_list);
        final NotifyAdapter notifyAdapter = new NotifyAdapter(
               getActivity(),
               R.layout.fragment_notify_item,
               notifyMananges
        );
        notifyList.setAdapter(notifyAdapter);

        //GET VALUDE FROM FIREBASE
        firestore.collection("notificationContent")
                .document(uid)
                .collection("content")
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        notifyMananges.clear();
                        for (DocumentSnapshot d:queryDocumentSnapshots.getDocuments()){
                            notifyMananges.add(d.toObject(NotifyManange.class));
                        }
                        notifyAdapter.notifyDataSetChanged();

                    }
                });
    }


    /**********************************
     *   intent: สร้าง popup ระบบกำลังประมวลผล  *
     **********************************/
    private void deley() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(4); // Incremented By Value 1
            }
        };
        // Progress Dialog Max Value
        progressDialog.setMax(100);
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog.getMax()) {
                        Thread.sleep(100);
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }

                }catch (Exception e){
                    e.getStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.nofity_bell);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER ", "CLICK NOTIFY BELL");

                SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE).edit();
                prefs.putInt(uid+"_countNotify",0);
                prefs.apply();

                setupBadge();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
