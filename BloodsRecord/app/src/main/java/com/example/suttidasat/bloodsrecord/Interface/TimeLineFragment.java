package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.suttidasat.bloodsrecord.model.CountNotify;
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

/*************************************************
 *intent: As Home show time of donations         *
 *pre-condition: user must login with role Donor *
 *************************************************/

public class TimeLineFragment extends Fragment {

    //menu
    private TextView textCartItemCount, donate_amount;
    private int mCartItemCount;

    private FirebaseFirestore firestore;
    private String uid;
    private String nid;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //menu
        SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt("countNotify", 0);
        Log.d("SharedPreferences : ", String.valueOf(mCartItemCount));

        setHasOptionsMenu(true);
        firestore = FirebaseFirestore.getInstance();
        showAmount();

        // Loading data dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();

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
                                        TextView re7 = getView().findViewById(R.id.re7);
                                        TextView re16 = getView().findViewById(R.id.re16);
                                        TextView re18 = getView().findViewById(R.id.re18);
                                        TextView re24 = getView().findViewById(R.id.re24);
                                        TextView re36 = getView().findViewById(R.id.re36);
                                        TextView re48 = getView().findViewById(R.id.re48);
                                        TextView re50 = getView().findViewById(R.id.re50);
                                        TextView re60 = getView().findViewById(R.id.re60);
                                        TextView re72 = getView().findViewById(R.id.re72);
                                        TextView re75 = getView().findViewById(R.id.re75);
                                        TextView re84 = getView().findViewById(R.id.re84);
                                        TextView re96 = getView().findViewById(R.id.re96);
                                        TextView re100 = getView().findViewById(R.id.re100);
                                        TextView re108 = getView().findViewById(R.id.re108);

                                        TextView timeline_1 = getView().findViewById(R.id.timeline_1);
                                        TextView timeline_7 = getView().findViewById(R.id.timeline_7);
                                        TextView timeline_16 = getView().findViewById(R.id.timeline_16);
                                        TextView timeline_18 = getView().findViewById(R.id.timeline_18);
                                        TextView timeline_24 = getView().findViewById(R.id.timeline_24);
                                        TextView timeline_36 = getView().findViewById(R.id.timeline_36);
                                        TextView timeline_48 = getView().findViewById(R.id.timeline_48);
                                        TextView timeline_50 = getView().findViewById(R.id.timeline_50);
                                        TextView timeline_60 = getView().findViewById(R.id.timeline_60);
                                        TextView timeline_72 = getView().findViewById(R.id.timeline_72);
                                        TextView timeline_75 = getView().findViewById(R.id.timeline_75);
                                        TextView timeline_84 = getView().findViewById(R.id.timeline_84);
                                        TextView timeline_96 = getView().findViewById(R.id.timeline_96);
                                        TextView timeline_100 = getView().findViewById(R.id.timeline_100);
                                        TextView timeline_108 = getView().findViewById(R.id.timeline_108);


                                        /// set color
                                        if (amount >= 1) {

                                            GradientDrawable gd = (GradientDrawable) timeline_1.getBackground().mutate();
                                            GradientDrawable gdRe = (GradientDrawable) re1.getBackground().mutate();

                                            gd.setColor(Color.rgb(220, 80, 80));
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        }
                                         if (amount >= 7) {
                                            GradientDrawable gd = (GradientDrawable) timeline_7.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re7.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));

                                        }
                                         if (amount >= 16) {
                                            GradientDrawable gd = (GradientDrawable) timeline_16.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re16.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));

                                        } if (amount >= 18) {
                                            GradientDrawable gd = (GradientDrawable) timeline_18.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re18.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 24) {
                                            GradientDrawable gd = (GradientDrawable) timeline_24.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re24.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 36) {
                                            GradientDrawable gd = (GradientDrawable) timeline_36.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re36.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 48) {
                                            GradientDrawable gd = (GradientDrawable) timeline_48.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re48.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 50) {
                                            GradientDrawable gd = (GradientDrawable) timeline_50.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re50.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 60) {
                                            GradientDrawable gd = (GradientDrawable) timeline_60.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re60.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 72) {
                                            GradientDrawable gd = (GradientDrawable) timeline_72.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re72.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 75) {
                                            GradientDrawable gd = (GradientDrawable) timeline_75.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re75.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 84) {
                                            GradientDrawable gd = (GradientDrawable) timeline_84.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re84.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 96) {
                                            GradientDrawable gd = (GradientDrawable) timeline_96.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re96.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 100) {
                                            GradientDrawable gd = (GradientDrawable) timeline_100.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re100.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        } if (amount >= 108) {
                                            GradientDrawable gd = (GradientDrawable) timeline_108.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                            GradientDrawable gdRe = (GradientDrawable) re108.getBackground().mutate();
                                            gdRe.setColor(Color.rgb(249,225,183));
                                        }

                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
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
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER ", "CLICK NOTIFY BELL");

                SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE).edit();
                prefs.putInt("countNotify",0);
                prefs.apply();

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
