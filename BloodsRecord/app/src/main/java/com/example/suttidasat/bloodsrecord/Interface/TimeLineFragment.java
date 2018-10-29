package com.example.suttidasat.bloodsrecord.Interface;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.model.MyService;
import com.example.suttidasat.bloodsrecord.model.NationaID;
import com.example.suttidasat.bloodsrecord.model.UpdateNotify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TimeLineFragment extends Fragment {

    //menu
    UpdateNotify un = new UpdateNotify();
    private TextView textCartItemCount, donate_amount;
    private int mCartItemCount;

    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private String uid;
    private String nid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //menu
        mCartItemCount = un.getCount();
        setHasOptionsMenu(true);
        firestore = FirebaseFirestore.getInstance();
        showAmount();

    }

    void showAmount() {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        donate_amount = getView().findViewById(R.id.donate_amount);


        /// get national ID

        firestore.collection("bloodsRecord")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        nid = task.getResult().getString("nationalID");
                        // get amount

                        firestore.collection("donateHistory")
                                .document(nid)
                                .collection("history")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        donate_amount.setText(queryDocumentSnapshots.size() + " ครั้ง");
                                        int amount = queryDocumentSnapshots.size();

                                        TextView re1 = getView().findViewById(R.id.re1);
//                                        TextView re7 = getView().findViewById(R.id.re7);

                                        TextView timeline_1 = getView().findViewById(R.id.timeline_1);
//                                        TextView timeline_7 = getView().findViewById(R.id.timeline_7);

                                        /// set color
                                        if (amount >= 1) {

                                            GradientDrawable gd = (GradientDrawable) timeline_1.getBackground().mutate();
                                            GradientDrawable gdRe1 = (GradientDrawable) re1.getBackground().mutate();

                                            gd.setColor(Color.rgb(220, 80, 80));
                                            gdRe1.setColor(Color.rgb(249,225,183));
                                        }
//                                        else if (amount >= 7) {
//                                            GradientDrawable gd = (GradientDrawable) timeline_7.getBackground().mutate();
//                                            gd.setColor(Color.rgb(220, 80, 80));
//
//                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Show Donator", "ERRROR =" + e.getMessage());
                                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

    }

    //menu
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
        switch (item.getItemId()) {
//            case R.id.sigOut: {
//
//                FirebaseAuth.getInstance().signOut();
//                getActivity().stopService(new Intent(getActivity(), MyService.class));
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new LoginFragment())
//                        .addToBackStack(null)
//                        .commit();
//                Log.d("USER", "GOTO LOGIN");
//                break;
//            }
//            case R.id.donatorProfile: {
//
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new DonatorProfileFragment())
//                        .commit();
//                Log.d("MENU", "GOTO DONATOR PROFILE");
//                break;
//            }
//            case R.id.timeline: {
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_view, new notifyBGProcess())
//                        .addToBackStack(null)
//                        .commit();
//                Log.d("USER", "GOTO Timeline");
//                break;
//            }
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new notifyFragment())
                        .commit();
                System.out.println("CLICK NOTIFY BELL");
                un.setCount(0);
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
