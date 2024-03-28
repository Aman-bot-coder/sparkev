package com.augmentaa.sparkev.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.ui.AboutUsActivity;
import com.augmentaa.sparkev.ui.CallRequestInfoActivity;
import com.augmentaa.sparkev.ui.ContactUsActivity;
import com.augmentaa.sparkev.ui.MyChargersActivity;
import com.augmentaa.sparkev.ui.MyVehicleActivity;
import com.augmentaa.sparkev.ui.NotificationActivity;
import com.augmentaa.sparkev.ui.ProfileActivity;
import com.augmentaa.sparkev.ui.ReceiptHistoryActivity;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

public class FragmentProfile extends Fragment {

    TextView tv_notification, tv_receipt, tv_my_chargers, tv_my_vehicles,
            tv_contact_us, tv_about_us, tv_callback_request, tv_mobile, tv_name;
    LinearLayout ly_profile;
    ImageView img_profile;

    public FragmentProfile() {
        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ly_profile = view.findViewById(R.id.ly_profile);
        tv_notification = view.findViewById(R.id.notification);
        tv_receipt = view.findViewById(R.id.my_receipt);
        tv_my_chargers = view.findViewById(R.id.my_charger);
        tv_my_vehicles = view.findViewById(R.id.my_vehicle);
        tv_contact_us = view.findViewById(R.id.contactus);
        tv_about_us = view.findViewById(R.id.about_us);
        tv_callback_request = view.findViewById(R.id.callback_request);
        tv_mobile = view.findViewById(R.id.mobile);
        tv_name = view.findViewById(R.id.name);
        img_profile = view.findViewById(R.id.profile_image);

        ly_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });



        tv_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        tv_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);

            }
        });
        tv_callback_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), CallRequestInfoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), NotificationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });
        tv_my_chargers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                    Toast.makeText(getActivity(), "You have no chargers added yet.", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(getActivity(), MyChargersActivity.class);
                    startActivity(intent);
                }

            }
        });
        tv_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                        Toast.makeText(getActivity(), "You have no chargers added yet.", Toast.LENGTH_LONG).show();

                    } else {
                        if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                            Intent intent = new Intent(getActivity(), ReceiptHistoryActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

        tv_my_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), MyVehicleActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            tv_mobile.setText("" + Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));
            tv_name.setText("" + Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + " " + Pref.getValue(Pref.TYPE.L_NAME.toString(), null));

                if (Pref.getValue(Pref.TYPE.IMAGE_URL.toString(), null) != null) {


                    Glide.with(getActivity())
                            .load(Pref.getValue(Pref.TYPE.IMAGE_URL.toString(), null) )
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(img_profile);

                    /*RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.profile_pic)
                            .error(R.mipmap.profile_pic);
                    Glide.with(getActivity()).load(Pref.getValue(Pref.TYPE.IMAGE_URL.toString(), null)).apply(options).into(img_profile);
                */} else {
                    img_profile.setImageResource(R.mipmap.profile_pic);
                }

        }
        catch (Exception e){

        }
    }
}