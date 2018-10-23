package com.example.suttidasat.bloodsrecord.Interface;

import android.os.Bundle;
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

public class notifyFragment extends Fragment {

    private TextView textCartItemCount;
    private  int mCartItemCount;

    UpdateNotify un = new UpdateNotify();

    ArrayList<NotifyManange> notifyMananges = new ArrayList<>();

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notify,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCartItemCount = un.getCount();
        setHasOptionsMenu(true);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ListView notifyList = getView().findViewById(R.id.notify_list);
        final NotifyAdapter notifyAdapter = new NotifyAdapter(
               getActivity(),
               R.layout.fragment_notify_item,
               notifyMananges
        );
        notifyList.setAdapter(notifyAdapter);

        //GET VALUDE FROM FIREBASE
        String uid = auth.getCurrentUser().getUid();
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
            case R.id.sigOut:{

                FirebaseAuth.getInstance().signOut();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER", "GOTO LOGIN");
                break;
            }
            case R.id.donatorProfile:{

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new DonatorProfileFragment())
                        .commit();
                Log.d("MENU", "GOTO DONATOR PROFILE");
                break;
            }
            case R.id.timeline:{
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new CountNofity())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER", "GOTO Timeline");
                break;
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
