package com.augmentaa.sparkev.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.augmentaa.sparkev.R;

public class AddChargerName extends Fragment {

    Button btnSubmit;
    String charger_serial_number;
    EditText etName;
    String chargerName;
    ImageView img_back;

    public AddChargerName() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_charger_name, container, false);
        btnSubmit = view.findViewById(R.id.btn_submit);
        img_back=view.findViewById(R.id.back);
        etName = view.findViewById(R.id.name);

        try {
            charger_serial_number = getArguments().getString("charger_id");

        } catch (Exception e) {

        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargerName=etName.getText().toString().trim();
                if (TextUtils.isEmpty(chargerName)) {
                    Toast.makeText(getActivity(), "Please enter your charger name.", Toast.LENGTH_LONG).show();
                } else {
                    AddChargerSerialNumber fragment = new AddChargerSerialNumber();
                    FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("charger_id", charger_serial_number);
                    bundle.putString("name", chargerName);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.flFragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });
        return view;

    }

}