package com.augmentaa.sparkev.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.ui.AlexaActivity;
import com.augmentaa.sparkev.ui.CallRequestInfoActivity;
import com.augmentaa.sparkev.ui.ContactUsActivity;
import com.augmentaa.sparkev.ui.FAQActivity;
import com.augmentaa.sparkev.ui.SmartWatchActivity;
import com.augmentaa.sparkev.ui.UpgradeRequestTrackingActivity;
import com.augmentaa.sparkev.ui.UpgradeToConnectChargerActivity;
import com.augmentaa.sparkev.ui.WarrantyActivity;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

public class FragmentServices extends Fragment {

    public FragmentServices() {
        // require a empty public constructor
    }

    Dialog dialog_addcharger;
    LinearLayout lyWarrantyPlan, lyAlexa, ly_upgrade_plan,
            lyContactUs, lySmartWatch, lyCallBackRequest, ly_faq;
    ImageView img_upgrade;
    TextView tv_upgrade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        lyWarrantyPlan = view.findViewById(R.id.ly_warranty_plan);
        lyAlexa = view.findViewById(R.id.ly_alexa);
        ly_upgrade_plan = view.findViewById(R.id.ly_upgrade_plan);
        lyContactUs = view.findViewById(R.id.ly_contact_us);
        lySmartWatch = view.findViewById(R.id.ly_smart_watch);
        lyCallBackRequest = view.findViewById(R.id.ly_callback);
        ly_faq = view.findViewById(R.id.ly_faq);
        tv_upgrade = view.findViewById(R.id.tv_upgrade);
        img_upgrade = view.findViewById(R.id.img_upgrade);
        Logger.e("OCCP value:  " + Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null) + "  " + Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null));

        try {

            if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                   // You already have a Connected Charger
//                    tv_upgrade.setText("Your Charger is Internet Ready");
                    tv_upgrade.setText("Upgrade to Connected Charger");
                    img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                } else {
                    if (Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null) != null) {
                        if ("NA".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                            tv_upgrade.setText("Upgrade to Connected Charger");
                            img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                        } else if ("O".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                "IP".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                            tv_upgrade.setText("Track Request ");
                            img_upgrade.setImageResource(R.mipmap.ic_upgrade_tracking);
                        } else if ("C".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                            //tv_upgrade.setText("Your Charger is Internet Ready");
                            tv_upgrade.setText("Upgrade to Connected Charger");
                            img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                        }


                    } else {
                        tv_upgrade.setText("Upgrade to Connected Charger");
                        img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                    }
                }

            } else {
                if ("NA".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                    tv_upgrade.setText("Upgrade to Connected Charger");
                    img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                } else if ("O".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                        "IP".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                    tv_upgrade.setText("Track Request ");
                    img_upgrade.setImageResource(R.mipmap.ic_upgrade_tracking);
                } else if ("C".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
//                    tv_upgrade.setText("Your Charger is Internet Ready");
//                    img_upgrade.setImageResource(R.mipmap.ic_upgrade_ocpp);
                    tv_upgrade.setText("Upgrade to Connected Charger");
                    img_upgrade.setImageResource(R.mipmap.ic_upgrade_ble);
                }
            }


        } catch (Exception e) {

        }

        lyAlexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                        if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                            Intent intent = new Intent(getActivity(), AlexaActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dialog_services_message();

                    }

                } else {
                    dialog_services_message();

                }


            }
        });


        ly_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), FAQActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

        lyContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);

            }
        });
        ly_upgrade_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Intent intent = new Intent(getActivity(), UpgradeRequestTrackingActivity.class);
//                    startActivity(intent);

                    Logger.e("OCCP value1111:  " + Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null) + "  " + Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null));


                    if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                        if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                            Logger.e("OCCP value  101010:  ");
                            dialog_upgrade_connected_charger();
//                            Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
//                            startActivity(intent);
                        } else {

                            Logger.e("OCCP value  1111:  ");
                            if (Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null) != null) {
                                Logger.e("OCCP value  22222:  ");
                                if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                                    if ("NA".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                            "R".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                            "P".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
                                    startActivity(intent);
                                    Logger.e("OCCP value  33333:  ");
                                } else if ("O".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                        "IP".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Intent intent = new Intent(getActivity(), UpgradeRequestTrackingActivity.class);
                                    startActivity(intent);
                                    Logger.e("OCCP value  444444:  ");
                                } else if ("C".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Logger.e("OCCP value  55555:  ");
                                        dialog_upgrade_connected_charger();
                                }
                                } else {
                                    Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                                Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
                                startActivity(intent);
                                Logger.e("OCCP value  66666:  ");
                                } else {
                                    Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    } else {
                       /* if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                            Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
                            startActivity(intent);
                            Logger.e("OCCP value  66666:  ");
                        } else {
                            Toast.makeText(getActivity(), "You are not authorized as not owner of this charger.", Toast.LENGTH_LONG).show();
                        }*/


                        if (Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null) != null) {
                            Logger.e("OCCP value  22222:  ");
                            if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                                if ("NA".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                        "R".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                        "P".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
                                    startActivity(intent);
                                    Logger.e("OCCP value  33333:  ");
                                } else if ("O".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null)) ||
                                        "IP".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Intent intent = new Intent(getActivity(), UpgradeRequestTrackingActivity.class);
                                    startActivity(intent);
                                    Logger.e("OCCP value  444444:  ");
                                } else if ("C".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), null))) {
                                    Logger.e("OCCP value  55555:  ");
                                    dialog_upgrade_connected_charger();
                                }
                            } else {
                                Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                                Intent intent = new Intent(getActivity(), UpgradeToConnectChargerActivity.class);
                                startActivity(intent);
                                Logger.e("OCCP value  66666:  ");
                            } else {
                                Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                            }
                        }
                        Logger.e("OCCP value  777777:  ");
                    }


                } catch (Exception e) {

                }



            }
        });
        lySmartWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                        if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                            Intent intent = new Intent(getActivity(), SmartWatchActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dialog_services_message();

                    }

                } else {
                    dialog_services_message();

                }


            }
        });
        lyCallBackRequest.setOnClickListener(new View.OnClickListener() {
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
        lyWarrantyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(getActivity())) {
                    if (Pref.getIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), 0) == 0) {
                        Intent intent = new Intent(getActivity(), WarrantyActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getActivity(), "You are not authorized, as not being owner of this charger.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });

        return view;
    }

    void dialog_services_message() {
        dialog_addcharger = new Dialog(getActivity());
        dialog_addcharger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_addcharger.setContentView(R.layout.app_dialog);
        dialog_addcharger.setCancelable(false);
        TextView btn_ok = (TextView) dialog_addcharger.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_addcharger.findViewById(R.id.tv_cancel);
//        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dialog_addcharger.findViewById(R.id.tv_message);
        btn_ok.setText("Ok");

        tv_message.setText(getResources().getString(R.string.service_message));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                dialog_addcharger.dismiss();


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_addcharger.dismiss();

            }
        });
        dialog_addcharger.show();


    }



    void dialog_upgrade_connected_charger() {
        dialog_addcharger = new Dialog(getActivity());
        dialog_addcharger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_addcharger.setContentView(R.layout.app_dialog);
        TextView btn_ok = (TextView) dialog_addcharger.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_addcharger.findViewById(R.id.tv_cancel);
        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dialog_addcharger.findViewById(R.id.tv_message);
        btn_ok.setText("Ok");

        tv_message.setText("You already have a Connected Charger");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_addcharger.dismiss();


            }
        });
        dialog_addcharger.show();


    }

}