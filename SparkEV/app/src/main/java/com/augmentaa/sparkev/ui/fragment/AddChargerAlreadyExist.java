package com.augmentaa.sparkev.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.augmentaa.sparkev.R;

public class AddChargerAlreadyExist extends Fragment {

    Button btnAddCharger;


    public AddChargerAlreadyExist() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_add_charger, container, false);
        btnAddCharger=view.findViewById(R.id.addCharger);
        btnAddCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCharger fragment = new ScanCharger();
                FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

//                Intent intent=new Intent(getActivity(), ListGuestAccessActivity.class);
//                startActivity(intent);
            }
        });
        return view;

    }

}