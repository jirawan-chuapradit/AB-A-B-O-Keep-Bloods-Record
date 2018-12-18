package com.example.suttidasat.bloodsrecord.Interface;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.suttidasat.bloodsrecord.R;

public class PrepareBefore extends Fragment implements View.OnClickListener {

    private TextView textCartItemCount;
    private int mCartItemCount;
    private SharedPreferences prefs;
    private Button before,after;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prepare_before_donate,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get Notify count
        prefs = getContext().getSharedPreferences("BloodsRecord",Context.MODE_PRIVATE);
        mCartItemCount = prefs.getInt("_countNotify", 0);
        Log.d("prefs profile", String.valueOf(mCartItemCount));

        before = getView().findViewById(R.id.prepare_before);
        after = getView().findViewById(R.id.prepare_after);

        before.setOnClickListener(this);
        after.setOnClickListener(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View v) {
        if(v==before){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view, new PrepareBefore())
                    .addToBackStack(null)
                    .commit();
            Log.d("PREPARE ", "GOTO BEFORE DONATE");

        }else if(v==after){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.donator_view, new PrepareAfter())
                    .addToBackStack(null)
                    .commit();
            Log.d("PREPARE ", "GOTO AFTER DONATE");
        }
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
                prefs.putInt("_countNotify",0);
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
