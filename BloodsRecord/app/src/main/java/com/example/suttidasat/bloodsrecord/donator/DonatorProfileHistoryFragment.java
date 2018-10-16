package com.example.suttidasat.bloodsrecord.donator;

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

import com.example.suttidasat.bloodsrecord.LoginFragment;
import com.example.suttidasat.bloodsrecord.R;
import com.example.suttidasat.bloodsrecord.TimeLineFragment;
import com.google.firebase.auth.FirebaseAuth;

public class DonatorProfileHistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donator_profile_history, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
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
            case R.id.donatHistory:{

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new DonatorProfileHistoryFragment())
                        .commit();
                Log.d("MENU", "GOTO DONATOR PROFILE HISTORY");
                break;

            }
            case R.id.timeline:{
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new TimeLineFragment())
                        .addToBackStack(null)
                        .commit();
                Log.d("USER", "GOTO Timeline");
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
