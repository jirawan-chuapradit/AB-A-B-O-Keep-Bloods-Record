package com.example.suttidasat.bloodsrecord.Interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.suttidasat.bloodsrecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**********************************
         *   intent: สร้าง popup ระบบกำลังประมวลผล  *
         **********************************/
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //menu
        SharedPreferences prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt("_countNotify", 0);



        Log.d("prefs Timeline: ", String.valueOf(mCartItemCount));
        setHasOptionsMenu(true);
        firestore = FirebaseFirestore.getInstance();
        showAmount();


    }
    

    void showAmount() {


//        donate_amount = getView().findViewById(R.id.donate_amount);


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
//                                        donate_amount.setText(queryDocumentSnapshots.size() + " ครั้ง");
                                        int amount = queryDocumentSnapshots.size();

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
                                            gd.setColor(Color.rgb(220, 80, 80));
                                        }
                                         if (amount >= 7) {
                                            GradientDrawable gd = (GradientDrawable) timeline_7.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        }
                                         if (amount >= 16) {
                                            GradientDrawable gd = (GradientDrawable) timeline_16.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 18) {
                                            GradientDrawable gd = (GradientDrawable) timeline_18.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                        } if (amount >= 24) {
                                            GradientDrawable gd = (GradientDrawable) timeline_24.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 36) {
                                            GradientDrawable gd = (GradientDrawable) timeline_36.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 48) {
                                            GradientDrawable gd = (GradientDrawable) timeline_48.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 50) {
                                            GradientDrawable gd = (GradientDrawable) timeline_50.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 60) {
                                            GradientDrawable gd = (GradientDrawable) timeline_60.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 72) {
                                            GradientDrawable gd = (GradientDrawable) timeline_72.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 75) {
                                            GradientDrawable gd = (GradientDrawable) timeline_75.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 84) {
                                            GradientDrawable gd = (GradientDrawable) timeline_84.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 96) {
                                            GradientDrawable gd = (GradientDrawable) timeline_96.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));

                                        } if (amount >= 100) {
                                            GradientDrawable gd = (GradientDrawable) timeline_100.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                        } if (amount >= 108) {
                                            GradientDrawable gd = (GradientDrawable) timeline_108.getBackground().mutate();
                                            gd.setColor(Color.rgb(220, 80, 80));
                                        }
                                        progressDialog.dismiss();
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
            case R.id.nofity_bell: {
                // Do something
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.donator_view, new notifyFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER ", "CLICK NOTIFY BELL");

                SharedPreferences.Editor prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE).edit();
                prefs.putInt("_countNotify",0);
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
