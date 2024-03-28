package com.augmentaa.sparkev.ui.fragment;


import static com.augmentaa.sparkev.ui.MainActivity.isProgressShow;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.augmentaa.sparkev.Globals;
import com.augmentaa.sparkev.R;

import com.augmentaa.sparkev.adapter.HomeChargerListAdapter;
import com.augmentaa.sparkev.model.signup.charger_details.ChargerDetails;
import com.augmentaa.sparkev.model.signup.charger_status.RequestChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_status.ResponseChargerStatus;
import com.augmentaa.sparkev.model.signup.data_transfer.RequestCurrent;
import com.augmentaa.sparkev.model.signup.data_transfer.ResponseCurrent;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.model.signup.remote_start_stop.RequestChargerStartStop;
import com.augmentaa.sparkev.model.signup.remote_start_stop.ResponseChargerStartStop;
import com.augmentaa.sparkev.model.signup.sessionmeter_value.ChargerSessionValue;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.ui.AddScheduleDetailsOneTimeActivity;

import com.augmentaa.sparkev.ui.MainActivity;
import com.augmentaa.sparkev.ui.MyChargersActivity;
import com.augmentaa.sparkev.ui.NotificationActivity;
import com.augmentaa.sparkev.ui.ScheduleListActivity;
import com.augmentaa.sparkev.ui.SignInActivity;

import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.NetworkUtil;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {


    APIInterface apiInterface, apiInterfaceCharger, apiInterfacePP;
    public ProgressDialog progress;
    TextView tv_charging_time, tv_progress, tv_charging_energy, tv_total_kWh_time, tv_unit, tv_status, tv_charger_list;
    ImageView img_connected, img_charger_list, img_schedule, img_start_stop, img_charger_status, img_notification;
    RadioButton rb_charging_time, rb_charging_energy;
    RadioGroup radioGroup;
    SeekBar sb_current;
    ListView listView;
    String charger_sr_no;
    ResponseGetChargerList list_chargerdata;
    boolean connectedMedia = false;
    String charger_status, station_id, idTag;
    Timer swipeTimer = new Timer();
    List<ChargerDetails> chargerDetails_data;

    String sessionTimeValue, sessionStartTimeValue, sessionStopTimeValue, currentValue,
            startChargingValue, stopChargingValue, sessionUnitValue, initialSoC, endSoc,
            startSessionUnit, stopsessionValue, voltageValue, startMeterValue, stopMeterValue, meterReadingValue;
    String initial_metervalue;

    int diff_time;
    int total_time;
    private static DecimalFormat df = new DecimalFormat("0.00");
    boolean isCheck = false;
    CountDownTimer countDownTimer;
    int counter = 0;
    private ProgressBar progressCharge;

    int charging_counter = 0;
    String total_kWh;
    ImageView img_anim;
    Dialog dialog_stop_charger;

    boolean isSchedule_time = false;
    boolean isSchedule_kwh = false;
    String set_current;
    String cmd_kwh, cmd_time;

String stateText="";
String deviceID,finalHexPass;
Dialog dialog_ble;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home, container, false);
        tv_charging_time = view.findViewById(R.id.tv_charging_time);
        tv_charging_energy = view.findViewById(R.id.tv_charging_energy);
        tv_total_kWh_time = view.findViewById(R.id.time_kwh);
        tv_unit = view.findViewById(R.id.units);
        tv_status = view.findViewById(R.id.charger_status);
        tv_charger_list = view.findViewById(R.id.charger_list);
        progressCharge = view.findViewById(R.id.progress);
        tv_progress = view.findViewById(R.id.tv_progress);
        rb_charging_time = view.findViewById(R.id.rb_charging_time);
        rb_charging_energy = view.findViewById(R.id.rb_charging_energy);
        radioGroup = view.findViewById(R.id.radioGroup);
        sb_current = view.findViewById(R.id.sb_amp);
        img_charger_status = view.findViewById(R.id.img_charger_status);
        img_notification = view.findViewById(R.id.img_notification);
        img_schedule = view.findViewById(R.id.schedule);
        img_connected = view.findViewById(R.id.img_connect);
        img_charger_list = view.findViewById(R.id.my_charger);
        img_start_stop = view.findViewById(R.id.start_stop);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfaceCharger = (APIInterface) APIClient.getChargerURL().create(APIInterface.class);
        this.apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        progress = new ProgressDialog(getActivity());
        img_anim = view.findViewById(R.id.img_anim);

        try {
            Glide.with(this).load(R.mipmap.charging_animation_low).into(img_anim);
            if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) != null) {
                charger_sr_no = Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null);
            }
        } catch (Exception e) {
            Logger.e("Excption  " + e);
        }


        img_notification.setOnClickListener(new View.OnClickListener() {
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

        if (Utils.isNetworkConnected(getActivity())) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));
            img_connected.setImageResource(R.mipmap.ic_web);
            getChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));

            if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                } else {
                    img_connected.setImageResource(R.mipmap.bluetooth);

                    DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));

                }

            } else {
                img_connected.setImageResource(R.mipmap.bluetooth);

                DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));

            }


        } else {
//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            img_connected.setImageResource(R.mipmap.bluetooth);

            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));

        }


        try {
            deviceID = Pref.getValue(Pref.TYPE.SYSTEM_NUMBER.toString(), null);

        } catch (Exception e) {

        }

        tv_charging_energy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                rb_charging_energy.setChecked(true);
                rb_charging_time.setChecked(false);
                return false;
            }
        });


        img_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isConnectivityOk(getActivity())) {
                    try {
                        if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                            if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                Intent intent = new Intent(getActivity(), ScheduleListActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), AddScheduleDetailsOneTimeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), AddScheduleDetailsOneTimeActivity.class);
                            startActivity(intent);
                        }


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Charger does not exist", Toast.LENGTH_LONG).show();

                    }


                } else {

                    Intent intent = new Intent(getActivity(), AddScheduleDetailsOneTimeActivity.class);
                    startActivity(intent);
//                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });


        tv_charging_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                rb_charging_energy.setChecked(false);
                rb_charging_time.setChecked(true);
                return false;
            }
        });


        try {

            if (Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null) != null) {
                if ("".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null)) || " ".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null))) {
                    tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null));

                } else {
                    tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

                }

            } else {
                tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

            }
        } catch (Exception e) {

        }


        tv_charger_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogChargerList();

                } catch (Exception e) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Charger does not exist", Toast.LENGTH_LONG).show();

                }


            }
        });

        img_charger_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (NetworkUtil.isConnectivityOk(getActivity())) {
                try {
                    Intent intent = new Intent(getActivity(), MyChargersActivity.class);
                    startActivity(intent);




                } catch (Exception e) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Charger does not exist", Toast.LENGTH_LONG).show();

                }


//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
//
//                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    try {

                        if (charger_status != null ) {
                            if ("Charging Time".equalsIgnoreCase(rb.getText().toString())) {
                                isSchedule_kwh = false;
                                isSchedule_time = true;

                                tv_charging_time.setFocusable(true);
                                tv_charging_time.setFocusableInTouchMode(true);
                                tv_charging_time.setEnabled(true);

                                tv_charging_energy.setFocusableInTouchMode(false);
                                tv_charging_energy.setFocusable(false);
                                tv_charging_energy.setEnabled(false);
                                tv_charging_energy.setText(null);
                                tv_charging_energy.setHint("0 kWh");

                                if (Utils.isNetworkConnected(getActivity())) {
                                    Logger.e("Charger Status" + charger_status);
                                    if ("Preparing".equalsIgnoreCase(charger_status) || "Plug Connected".equalsIgnoreCase(stateText)) {
                                        tv_charging_time.setFocusable(true);
                                        tv_charging_time.setFocusableInTouchMode(true);
                                        tv_charging_time.setEnabled(true);
                                    } else {
                                        tv_charging_time.setFocusable(false);
                                        tv_charging_time.setFocusableInTouchMode(false);
                                        tv_charging_time.setEnabled(false);

                                    }
                                } else {

                                    if ("Plug Connected".equalsIgnoreCase(stateText)) {
                                        tv_charging_time.setFocusable(true);
                                        tv_charging_time.setFocusableInTouchMode(true);
                                        tv_charging_time.setEnabled(true);

                                    } else {
                                        tv_charging_time.setFocusable(false);
                                        tv_charging_time.setFocusableInTouchMode(false);
                                        tv_charging_time.setEnabled(false);

                                    }

                                }
                            } else {

                                isSchedule_time = false;
                                isSchedule_kwh = true;

                                tv_charging_energy.setFocusableInTouchMode(true);
                                tv_charging_energy.setFocusable(true);
                                tv_charging_energy.setEnabled(true);

                                tv_charging_time.setFocusable(false);
                                tv_charging_time.setFocusableInTouchMode(false);
                                tv_charging_time.setEnabled(false);
                                tv_charging_time.setText(null);
                                tv_charging_time.setHint("0 hour");
                                if (Utils.isNetworkConnected(getActivity())) {
                                    if ("Preparing".equalsIgnoreCase(charger_status) || "Plug Connected".equalsIgnoreCase(stateText)) {
                                        tv_charging_energy.setFocusableInTouchMode(true);
                                        tv_charging_energy.setFocusable(true);
                                        tv_charging_energy.setEnabled(true);
                                    } else {
                                        tv_charging_energy.setFocusableInTouchMode(false);
                                        tv_charging_energy.setFocusable(false);
                                        tv_charging_energy.setEnabled(false);
                                    }
                                } else {
                                    if ("Plug Connected".equalsIgnoreCase(stateText)) {
                                        tv_charging_energy.setFocusableInTouchMode(true);
                                        tv_charging_energy.setFocusable(true);
                                        tv_charging_energy.setEnabled(true);

                                    } else {
                                        tv_charging_energy.setFocusableInTouchMode(false);
                                        tv_charging_energy.setFocusable(false);
                                        tv_charging_energy.setEnabled(false);

                                    }
                                }

                            }
                        }
                    } catch (Exception e) {

                    }


                }

            }
        });


        img_start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(getActivity())) {
//                    if (charger_status != null) {
                    if ("Charging".equalsIgnoreCase(charger_status)) {
                        if (Utils.isNetworkConnected(getActivity())) {
                            dialog_stopCharger();


                        } else {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if (Utils.isNetworkConnected(getActivity())) {
                            progress = new ProgressDialog(getActivity());
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            Logger.e("Click on button:");
                            getChargerData(charger_sr_no);


                        } else {

                        }
                    }


                } else {




                }
            }


        });

//        sb_current.setMin(16);
        sb_current.setMax(32);
        sb_current.setProgress(32);
        tv_progress.setText("32 A");
        Float f = Float.parseFloat("16");

        int i = Float.floatToIntBits(f);
        String Hex = Integer.toHexString(i);
        Hex = "10AC4F01" + Hex;
//        Logger.e(" Data ==" + Hex.toUpperCase());
        set_current = Hex.toUpperCase();
        sb_current.setProgressDrawable(getActivity().getResources().getDrawable(R.drawable.custom_seekbar));

        sb_current.setClickable(false);
        sb_current.setEnabled(false);



        return view;
    }


    void dialogChargerList() {
        final BottomSheetDialog dialogChargerList = new BottomSheetDialog(getActivity());
        dialogChargerList.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.frag_charger_list, null);
        dialogChargerList.setContentView(view);

//        dialogChargerList = new Dialog(getActivity());
        listView = dialogChargerList.findViewById(R.id.listview);

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        dialogChargerList.show();


        try {
            Gson gson = new Gson();
//            String json = gson.toJson(list_chargerdata);
            list_chargerdata = gson.fromJson(Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null), ResponseGetChargerList.class);


        } catch (Exception e) {
            progress.dismiss();
            Logger.e("Response" + e.getMessage());

        }
        if (list_chargerdata.data.size() > 0) {
            charger_sr_no = list_chargerdata.data.get(0).serialNo;
            HomeChargerListAdapter chargerAdapter = new HomeChargerListAdapter(getActivity(), list_chargerdata.data);
            listView.setAdapter(chargerAdapter);
            chargerAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    charger_sr_no = list_chargerdata.data.get(position).serialNo;
                    Pref.setValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), charger_sr_no);

                    Pref.setIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), list_chargerdata.data.get(position).mapAsChild);


                    Pref.setValue(Pref.TYPE.NICK_NAME.toString(), list_chargerdata.data.get(position).nickName);
                    Pref.setValue(Pref.TYPE.B_ADDRESS.toString(), list_chargerdata.data.get(position).address);
                    Pref.setValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), list_chargerdata.data.get(position).partNo);
                    Pref.setIntValue(Pref.TYPE.CHARGER_CRED.toString(), list_chargerdata.data.get(position).client_certificate);
                    Pref.setValue(Pref.TYPE.SYSTEM_NUMBER.toString(), list_chargerdata.data.get(position).partNo + "#" + list_chargerdata.data.get(position).serialNo);
                    Pref.setValue(Pref.TYPE.IS_OCCP.toString(), list_chargerdata.data.get(position).is_OCPP_enabled);

                    deviceID = list_chargerdata.data.get(position).partNo + "#" + list_chargerdata.data.get(position).serialNo;
                    if (list_chargerdata.data.get(position).nickName != null) {
                        if ("".equalsIgnoreCase(list_chargerdata.data.get(position).nickName) || " ".equalsIgnoreCase(list_chargerdata.data.get(position).nickName)) {
                            tv_charger_list.setText("" + list_chargerdata.data.get(position).serialNo);

                        } else {
                            tv_charger_list.setText("" + list_chargerdata.data.get(position).nickName);

                        }

                    } else {
                        tv_charger_list.setText("" + list_chargerdata.data.get(position).serialNo);

                    }

                    Logger.e("Charger Serial Number:  " + charger_sr_no);
                    dialogChargerList.dismiss();
                }
            });
        } else {

        }

    }


    private void getChargerList(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGetChargerList> call = apiInterfacePP.getChargerListforWarranty(hashMap1, userId);
        call.enqueue(new Callback<ResponseGetChargerList>() {
            @Override
            public void onResponse(Call<ResponseGetChargerList> call, Response<ResponseGetChargerList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        list_chargerdata = (ResponseGetChargerList) response.body();
                        Logger.e("Response data Home screen" + list_chargerdata.toString());
                        if (list_chargerdata.status) {
                            if (list_chargerdata.data.size() > 0) {
                                list_chargerdata.user_id = Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0);
                                list_chargerdata.vehicle_id = Pref.getIntValue(Pref.TYPE.VEHICLE_ID.toString(), 0);
                                list_chargerdata.registration_number = Pref.getValue(Pref.TYPE.VEHICLE_REG.toString(), null);
                                list_chargerdata.serial_number = Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null);
                                list_chargerdata.mobile_number = Pref.getValue(Pref.TYPE.MOBILE.toString(), null);
                                list_chargerdata.is_OCPP_enabled = Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null);
                                Gson gson = new Gson();
                                String json = gson.toJson(list_chargerdata);
                                Pref.setValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), json);

                                Logger.e("Charger list data: 1111  " + Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null));


                                if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) != null) {

                                } else {

                                    Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), response.body().data.size());
                                    Pref.setValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), response.body().data.get(0).serialNo);
                                    Pref.setValue(Pref.TYPE.B_ADDRESS.toString(), list_chargerdata.data.get(0).address);
                                    Pref.setValue(Pref.TYPE.IS_OCCP.toString(), list_chargerdata.data.get(0).is_OCPP_enabled);
                                    Pref.setValue(Pref.TYPE.NICK_NAME.toString(), response.body().data.get(0).nickName);
                                    Pref.setValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), response.body().data.get(0).partNo);
                                    Pref.setIntValue(Pref.TYPE.CHARGER_CRED.toString(), response.body().data.get(0).client_certificate);
                                    Pref.setIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), list_chargerdata.data.get(0).mapAsChild);

                                    tv_charger_list.setText("" + list_chargerdata.data.get(0).nickName);


                                }


                                if (Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null) != null) {
                                    if ("".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null)) || " ".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null))) {
                                        tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null));

                                    } else {
                                        tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

                                    }

                                } else {
                                    tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

                                }

                                if (list_chargerdata.data.size() == 1) {
                                    tv_charger_list.setClickable(false);
                                    tv_charger_list.setFocusable(false);
                                    tv_charger_list.setFocusableInTouchMode(false);
                                    tv_charger_list.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                    Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), response.body().data.size());
                                    Pref.setValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), response.body().data.get(0).serialNo);
                                    Pref.setValue(Pref.TYPE.B_ADDRESS.toString(), list_chargerdata.data.get(0).address);
                                    Pref.setValue(Pref.TYPE.IS_OCCP.toString(), list_chargerdata.data.get(0).is_OCPP_enabled);
                                    Pref.setValue(Pref.TYPE.NICK_NAME.toString(), response.body().data.get(0).nickName);
                                    Pref.setValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), response.body().data.get(0).partNo);
                                    Pref.setIntValue(Pref.TYPE.CHARGER_CRED.toString(), response.body().data.get(0).client_certificate);
                                    Pref.setIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), list_chargerdata.data.get(0).mapAsChild);


                                } else {
                                    tv_charger_list.setClickable(true);
                                    tv_charger_list.setFocusable(true);
                                    tv_charger_list.setFocusableInTouchMode(true);
                                    tv_charger_list.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                                }
                            }

                        } else {
                            if ("Token is not valid".equalsIgnoreCase(list_chargerdata.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(getActivity(), SignInActivity.class);
                                startActivity(intent21);
                                getActivity().finishAffinity();
                                Toast.makeText(getActivity(), "Your session are expired.", Toast.LENGTH_LONG).show();
                            }
                        }
//
                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
//                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 1111, Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 22222, Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                }

            }

            @Override
            public void onFailure(Call<ResponseGetChargerList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 3333, Toast.LENGTH_LONG).show();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


            }
        });

    }


    private void chargerStopCommand() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceCharger.chargerStart(hashMap1, new RequestChargerStartStop("STOP_CHARGING", charger_sr_no, charger_sr_no, 1, null, "", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                Pref.getValue(Pref.TYPE.ORIGIN.toString(), null), Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null), Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null), Pref.getValue(Pref.TYPE.STATION_ID.toString(), null), Pref.getValue(Pref.TYPE.MOBILE.toString(), null),
                Pref.getIntValue(Pref.TYPE.VEHICLE_ID.toString(), 0), Pref.getValue(Pref.TYPE.VEHICLE_REG.toString(), null), "ID_TAG", " ")).enqueue(new Callback<ResponseChargerStartStop>() {
            public void onResponse(Call<ResponseChargerStartStop> call, Response<ResponseChargerStartStop> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        ResponseChargerStartStop loginResponse = (ResponseChargerStartStop) response.body();
                        Logger.e("Response Stop Charger: " + loginResponse.toString());
                        rb_charging_time.setChecked(false);
                        rb_charging_energy.setChecked(false);
                        img_start_stop.setImageResource(R.mipmap.pause);
                        if (loginResponse.status) {
                            Toast.makeText(getActivity(), "Charger stop successfully", Toast.LENGTH_LONG).show();

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            tv_charging_energy.setText(null);
                            tv_charging_time.setText(null);
                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");

//                            tv_total_kWh_time.setText("0");
//                            tv_charging_energy.setText("0 kWh");
//                            tv_charging_time.setText("0 hour");


                            tv_charging_energy.setFocusableInTouchMode(true);
                            tv_charging_energy.setFocusable(true);
                            tv_charging_energy.setEnabled(true);

                            tv_charging_time.setFocusable(true);
                            tv_charging_time.setFocusableInTouchMode(true);
                            tv_charging_time.setEnabled(true);


                            rb_charging_time.setFocusable(true);
                            rb_charging_time.setFocusableInTouchMode(true);
                            rb_charging_time.setEnabled(true);

                            rb_charging_energy.setFocusable(true);
                            rb_charging_energy.setFocusableInTouchMode(true);
                            rb_charging_energy.setEnabled(true);
                            changeChargerStatus();

                            Pref.setBoolValue(Pref.TYPE.CHARGER_STATUS.toString(), false);
//                            if (swipeTimer != null)
//                                swipeTimer.cancel();
                        } else {
                            Toast.makeText(getActivity(), loginResponse.message, Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 444444, Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
//                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 5555555, Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseChargerStartStop> call, Throwable t) {
                Logger.e("URL: " + call.request().toString());
                AppUtils.bodyToString(call.request().body());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }


    public void getChargerData(final String chargerId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<List<ChargerDetails>> call = apiInterface.getChargerByDisplayId(hashMap1, chargerId);
        call.enqueue(new Callback<List<ChargerDetails>>() {
            @Override
            public void onResponse(Call<List<ChargerDetails>> call, Response<List<ChargerDetails>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                chargerDetails_data = response.body();
                if (response.code() == 200) {
                    try {
                        if (chargerDetails_data.size() > 0) {
                            Logger.e("Station Id " + chargerDetails_data.get(0).station_id);
                            station_id = chargerDetails_data.get(0).station_id;
                            Pref.setValue(Pref.TYPE.STATION_ID.toString(), station_id);
                            if (chargerDetails_data.get(0).connector_data.size() > 0) {
                                for (int i = 0; i < chargerDetails_data.get(0).connector_data.size(); i++) {
                                    charger_status = chargerDetails_data.get(0).connector_data.get(i).current_status;
                                    Logger.e("Charger Status:  " + charger_status);

                                }
                            } else {
                                Toast.makeText(getActivity(), "Connector not found", Toast.LENGTH_LONG).show();

                            }
//                        Logger.e("======Current=Status === " + Pref.getValue(Pref.TYPE.SET_CURRENT.toString(), null) + "   " + set_current);

//                        if (Pref.getValue(Pref.TYPE.SET_CURRENT.toString(), null) != null) {
//                            if (Pref.getValue(Pref.TYPE.SET_CURRENT.toString(), null).equalsIgnoreCase(set_current)) {
                            if (!isSchedule_time && !isSchedule_kwh) {
                                if (NetworkUtil.isConnectivityOk(getActivity())) {
                                    try {
                                        progress.dismiss();
                                        Logger.e("Charger Status" + charger_status);
                                        if (charger_status != null) {
                                            if ("Charging".equalsIgnoreCase(charger_status)) {

                                            } else {
                                                if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                                        progress = new ProgressDialog(getActivity());
                                                        progress.setMessage("Connecting...");
                                                        progress.setCancelable(false);
                                                        progress.show();
                                                        chargerStartCommand("START_CHARGING", charger_sr_no, charger_sr_no, String.valueOf(1), idTag, null);

                                                    } else {

                                                    }

                                                } else {


                                                }


                                            }

                                        } else {
                                            if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                                if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                                    progress = new ProgressDialog(getActivity());
                                                    progress.setMessage("Connecting...");
                                                    progress.setCancelable(false);
                                                    progress.show();
                                                    chargerStartCommand("START_CHARGING", charger_sr_no, charger_sr_no, String.valueOf(1), idTag, null);

                                                } else {


                                                }

                                            } else {


                                            }
                                        }


                                    } catch (Exception e) {
                                        progress.dismiss();
                                        Toast.makeText(getActivity(), "Charger does not exist", Toast.LENGTH_LONG).show();

                                    }


                                } else {
//                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                                }
//
                            } else if (isSchedule_time && !isSchedule_kwh) {

                                if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                        setTimeChargingOCCP();
                                    } else {

                                    }
                                } else {

                                }

                            } else if (!isSchedule_time && isSchedule_kwh) {
                                if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                        setKwhChargingOCCP();
                                    } else {

                                    }
                                } else {

                                }


                            }
//

                        } else {
                            Toast.makeText(getActivity(), "No Charger Available.", Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                MainActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                }

            }

            @Override
            public void onFailure(Call<List<ChargerDetails>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                AppUtils.bodyToString(call.request().body());

                Logger.e("Execption 111" + t.getMessage());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    private void chargerStartCommand(final String command, final String charger_id,
                                     final String charger_sr_no, final String conntector, final String id_tag, String
                                             tranction_id) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceCharger.chargerStart(hashMap1, new RequestChargerStartStop(command, charger_id, charger_sr_no, 1, "", null, Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                Pref.getValue(Pref.TYPE.ORIGIN.toString(), null), Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null), Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null), station_id, Pref.getValue(Pref.TYPE.MOBILE.toString(), null),
                Pref.getIntValue(Pref.TYPE.VEHICLE_ID.toString(), 0), Pref.getValue(Pref.TYPE.VEHICLE_REG.toString(), null), "ID_TAG", " ")).enqueue(new Callback<ResponseChargerStartStop>() {
            public void onResponse(Call<ResponseChargerStartStop> call, Response<ResponseChargerStartStop> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        ResponseChargerStartStop chargerDetails_data = (ResponseChargerStartStop) response.body();
                        Logger.e("Response == " + chargerDetails_data.toString());
                        if (chargerDetails_data.status) {
                            Logger.e("Response Start Charger: " + chargerDetails_data.toString());
                            Toast.makeText(getActivity(), "Charger Start successfully", Toast.LENGTH_LONG).show();
                            getSessionValue(charger_sr_no, String.valueOf(1));
                        } else {
                            Toast.makeText(getActivity(), chargerDetails_data.message, Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                MainActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                AppUtils.DATA_NOT_FOUND,
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                }


            }

            public void onFailure(Call<ResponseChargerStartStop> call, Throwable t) {
                Logger.e("URL: " + call.request().toString());
                AppUtils.bodyToString(call.request().body());
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    private void getSessionValue(final String charger_id, final String conntector) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceCharger.getActiveSessionMeterValues(hashMap1, charger_id, conntector).enqueue(new Callback<ChargerSessionValue>() {
            public void onResponse(Call<ChargerSessionValue> call, Response<ChargerSessionValue> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {

                        Logger.e("Counter value== " + charging_counter);
                        ChargerSessionValue sessionResponse = (ChargerSessionValue) response.body();
                        Logger.e("Response Stop Charger: " + sessionResponse.toString());
                        ObjectMapper mapper = new ObjectMapper();
//                        Converting the Object to JSONString
                        String jsonString = mapper.writeValueAsString(sessionResponse);
                        Logger.e("Response1233: " + jsonString);
//                        tv_data_fetch.setVisibility(View.VISIBLE);

                        //Hide today
//                        progressCharge.setVisibility(View.VISIBLE);


                        if (sessionResponse.status.equals("true")) {
                            String diffTime = "00:00";
                            progress.dismiss();
                            initial_metervalue = sessionResponse.data.get(0).meterStart;

                            sessionStartTimeValue = getTime(sessionResponse.data.get(0).startTime);
                            if (sessionResponse.data.size() >= 3) {
                                progressCharge.setVisibility(View.GONE);
                                if (sessionResponse.data.get(0).startTime != null) {
                                    sessionStartTimeValue = getTime(sessionResponse.data.get(0).startTime);
                                } else {
                                    sessionStartTimeValue = sessionResponse.data.get(0).createdOn;
                                }


                                for (int i = 0; i < sessionResponse.data.size(); i++) {
                                    if ("wh".equalsIgnoreCase(sessionResponse.data.get(i).unit)) {
                                        startChargingValue = sessionResponse.data.get(i).value;
                                    } else if ("A".equalsIgnoreCase(sessionResponse.data.get(i).unit)) {
                                        currentValue = sessionResponse.data.get(i).value;
                                    } else if ("V".equalsIgnoreCase(sessionResponse.data.get(i).unit)) {
                                        voltageValue = sessionResponse.data.get(i).value;
                                    } else if ("Percent".equalsIgnoreCase(sessionResponse.data.get(i).unit)) {
//                                        soc= sessionResponse.data.get(i).value;
                                    }

                                }

                                startMeterValue = sessionResponse.data.get(sessionResponse.data.size() - 1).meterStart;
                                meterReadingValue = sessionResponse.data.get(sessionResponse.data.size() - 1).meterReading;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
                                String currentTime = sdf.format(new Date());
                                initialSoC = sessionResponse.data.get(sessionResponse.data.size() - 1).initialSoc;
                                endSoc = sessionResponse.data.get(sessionResponse.data.size() - 1).finalSoc;
                                String[] time = sessionStartTimeValue.split("\\s");
                                int hours = 0, minutes = 0, seconds = 0;
                                long diff = 0;

//                                tv_progress.setText(Math.round(Integer.parseInt(currentValue)) + "A");

                                if (charging_counter == 0 || charging_counter == 1 || charging_counter == 2) {
                                    try {
                                        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                                        java.util.Date date1 = df.parse(sessionStartTimeValue);
                                        java.util.Date date2 = df.parse(currentTime);
                                        diff = date2.getTime() - date1.getTime();
                                        int timeInSeconds = (int) diff / 1000;
                                        diff_time = timeInSeconds;
                                        hours = timeInSeconds / 3600;
                                        timeInSeconds = timeInSeconds - (hours * 3600);
                                        minutes = timeInSeconds / 60;
                                        timeInSeconds = timeInSeconds - (minutes * 60);
                                        seconds = timeInSeconds;
//                                        diffTime = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
//
//
//                                    sessionTime.setText("" + diffTime);
                                        diffTime = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);

                                        Logger.e("isChecked  " + isCheck);

//                                        long maxTimeInMilliseconds = 20 * 60 * 60 * 1000;// in your case
//                                        startTimer(maxTimeInMilliseconds, 1000, diff_time);
//                                        tv_unit.setText("hour");

                                        tv_total_kWh_time.setText("" + diffTime);// manage it accordign to you
//                                        tv_charging_time.setText("" + diffTime + " hour");// manage it accordign to you
                                        tv_unit.setText("hour");


                                        if (!isCheck) {
                                            isCheck = true;
                                            long maxTimeInMilliseconds = 20 * 60 * 60 * 1000;// in your case
                                            startTimer(maxTimeInMilliseconds, 1000, diff_time);
//                                            tv_unit.setText("hour");

                                        }

//                                        tv_unit.setText("kWh");
                                    } catch (Exception e) {
                                        Logger.e("Exception " + e);
//                                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                                    }
                                    Logger.e("Session Time--" + diffTime + " Hour " + hours + "  min " + minutes + " sec " + seconds);
                                } else {

                                    if (currentValue != null) {
                                        if (Float.parseFloat(currentValue) > 0) {

                                        } else {
//                                        Toast.makeText(getActivity(), "Either your vehicle is not lock in proper way or Charging full.", Toast.LENGTH_LONG).show();

                                        }

                                    }


                                    Logger.e("Session Time: " + time[1] + "  " + currentTime + "  " + diff + "  " + diffTime);


                                    if (initial_metervalue != null && startChargingValue != null) {
                                        float session_unit = (Float.parseFloat(startChargingValue) - Float.parseFloat(initial_metervalue)) / 1000;
                                        Log.e("session_unit", startChargingValue + "  " + initial_metervalue);
                                        tv_total_kWh_time.setText("" + df.format(session_unit));
//                                        tv_charging_energy.setText(df.format(session_unit) + " kWh");
                                        total_kWh = df.format(session_unit);

                                        Logger.e("Session Units " + total_kWh);


                                    } else {
                                        tv_total_kWh_time.setText("0");
                                    }


                                    if (charging_counter == 4) {
                                        charging_counter = 0;
                                    }
                                    tv_unit.setText("kWh");
                                    // End kWh
//                                tvStartTime.setText("" + time[1]);


                                }

                                charging_counter++;


                            } else {
                                sessionStartTimeValue = getTime(sessionResponse.data.get(0).startTime);
                                if (sessionResponse.data.get(0).startTime != null) {
                                    sessionStartTimeValue = getTime(sessionResponse.data.get(0).startTime);

                                } else {
                                    sessionStartTimeValue = sessionResponse.data.get(0).createdOn;
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
                                String currentTime = sdf.format(new Date());
                                String[] time = sessionStartTimeValue.split("\\s");
                                int hours = 0, minutes = 0, seconds = 0;
                                long diff = 0;
                                try {
                                    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                                    java.util.Date date1 = df.parse(sessionStartTimeValue);
                                    java.util.Date date2 = df.parse(currentTime);
                                    diff = date2.getTime() - date1.getTime();
                                    int timeInSeconds = (int) diff / 1000;
                                    diff_time = timeInSeconds;
                                    hours = timeInSeconds / 3600;
                                    timeInSeconds = timeInSeconds - (hours * 3600);
                                    minutes = timeInSeconds / 60;
                                    timeInSeconds = timeInSeconds - (minutes * 60);
                                    seconds = timeInSeconds;
                                    diffTime = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
//
                                } catch (Exception e) {
                                    Logger.e("Exception " + e);
                                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                                }
                                Logger.e("Session Time--" + diffTime + " Hour " + hours + "  min " + minutes + " sec " + seconds);
                                progress.dismiss();
                                if (sessionResponse.data.size() > 0 && sessionResponse.data.size() < 4) {
//                                    sessionTime.setText("" + diffTime);
                                } else {
                                    tv_total_kWh_time.setText("00.00");
                                }
                                if (startMeterValue != null) {
                                    tv_total_kWh_time.setText(startMeterValue);
                                } else {
                                    tv_total_kWh_time.setText("0");
                                }

                                if (startChargingValue != null) {
                                    if (startChargingValue != null) {
                                        if (Float.parseFloat(startChargingValue) > 0) {
                                            float startChargingValueinkWh = Float.parseFloat(startChargingValue) / 1000;
//                                            String.format("%.02f", list_chargerdata.data.get(position).distance)
                                            tv_total_kWh_time.setText("" + df.format(startChargingValueinkWh));
                                        } else {
                                            tv_total_kWh_time.setText("0");
                                        }
                                    }

                                } else {
                                    tv_total_kWh_time.setText("0");
                                }


                            }


                        } else {
                            progress.dismiss();
//                            Toast.makeText(getActivity(), sessionResponse.message, Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e) {
                        Logger.e("Exception " + e);
//                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                }


            }

            public void onFailure(Call<ChargerSessionValue> call, Throwable t) {
                Logger.e("URL: " + call.request().toString());
                AppUtils.bodyToString(call.request().body());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    private String getTime(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
//            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    @Override
    public void onStop() {
        super.onStop();
        diff_time = 0;
//        isProgressShow = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (swipeTimer != null)
            swipeTimer.cancel();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void startTimer(final long finish, long tick, final int diff_time) {

        countDownTimer = new CountDownTimer(finish, tick) {
            int hours = 0, minutes = 0, seconds = 0;
            String diffTime;
            int timeInSeconds = 0;

            public void onTick(long millisUntilFinished) {
                timeInSeconds++;
                total_time = diff_time + timeInSeconds;
                hours = total_time / 3600;
                total_time = total_time - (hours * 3600);
                minutes = total_time / 60;
                total_time = total_time - (minutes * 60);
                seconds = total_time;
                diffTime = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
//                tv_total_kWh_time.setText("" + diffTime);// manage it accordign to you
//                tv_charging_time.setText("" + diffTime);// manage it accordign to you
//                tv_unit.setText("hour");

                Logger.e("Timer Log  " + diffTime);


            }

            public void onFinish() {
                tv_total_kWh_time.setText("00:00");
//                Toast.makeText(getActivity(), "Finish", Toast.LENGTH_SHORT).show();
                cancel();
            }
        }.start();
    }

    private void getChargerStatus(final String command, final String charger_id,
                                  final String connector) {
        this.apiInterfaceCharger.getChargerStatus(new RequestChargerStatus("STATUS", charger_id, connector)).enqueue(new Callback<ResponseChargerStatus>() {
            public void onResponse(Call<ResponseChargerStatus> call, Response<ResponseChargerStatus> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                isProgressShow = true;
                try {



                    if (response.code() == 200) {
                        ResponseChargerStatus chargerStatus = (ResponseChargerStatus) response.body();
                        Logger.e("Response value: " + chargerStatus);
                        tv_status.setText("" + chargerStatus.message);
                        charger_status = chargerStatus.message;
                        if ("Charging".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), true);
                            getSessionValue(charger_id, connector);
                            tv_status.setTextColor(getResources().getColor(R.color.themeColor));
                            tv_total_kWh_time.setTextColor(getResources().getColor(R.color.colorText));
                            tv_unit.setTextColor(getResources().getColor(R.color.colorText));
                            DrawableCompat.setTint(img_charger_status.getDrawable(), ContextCompat.getColor(getActivity(), R.color.themeColor));
                            img_start_stop.setImageResource(R.mipmap.stop);
//                        tv_charger_list.setText("" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null));
                            img_anim.setVisibility(View.VISIBLE);

                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.themeColor));
                            img_connected.setImageResource(R.mipmap.ic_web);


                            tv_charging_energy.setFocusableInTouchMode(false);
                            tv_charging_energy.setFocusable(false);
                            tv_charging_energy.setEnabled(false);

                            tv_charging_time.setFocusable(false);
                            tv_charging_time.setFocusableInTouchMode(false);
                            tv_charging_time.setEnabled(false);


                            sb_current.setFocusable(false);
                            sb_current.setFocusableInTouchMode(false);
                            sb_current.setEnabled(false);

                            rb_charging_energy.setFocusable(false);
                            rb_charging_energy.setFocusableInTouchMode(false);
                            rb_charging_energy.setEnabled(false);

                            rb_charging_time.setFocusable(false);
                            rb_charging_time.setFocusableInTouchMode(false);
                            rb_charging_time.setEnabled(false);


                        } else if ("Preparing".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            progress.dismiss();
                            isCheck = false;
                            changeChargerStatus();
//                        chargerStartCommand();
                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.themeColor));
                            img_connected.setImageResource(R.mipmap.ic_web);
                            img_start_stop.setImageResource(R.mipmap.play_active);
                            tv_charging_energy.setFocusableInTouchMode(true);
                            tv_charging_energy.setFocusable(true);
                            tv_charging_energy.setEnabled(true);

                            tv_charging_time.setFocusable(true);
                            tv_charging_time.setFocusableInTouchMode(true);
                            tv_charging_time.setEnabled(true);


                            rb_charging_time.setFocusable(true);
                            rb_charging_time.setFocusableInTouchMode(true);
                            rb_charging_time.setEnabled(true);

                            rb_charging_energy.setFocusable(true);
                            rb_charging_energy.setFocusableInTouchMode(true);
                            rb_charging_energy.setEnabled(true);

                        } else if ("CHARGER_OFFLINE".equalsIgnoreCase(chargerStatus.message)) {

                            try {
                                Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);
                                DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));
                                img_connected.setImageResource(R.mipmap.ic_web);
                                progress.dismiss();
                                tv_status.setText("" + "Disconnected");
                                changeChargerStatus();
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                isCheck = false;
                                counter++;
                                {
                                    if (counter == 4) {
                                        counter = 0;
                                    }
                                }
                            } catch (Exception e) {

                            }

                        } else if ("Finishing".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);

                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.themeColor));
                            img_connected.setImageResource(R.mipmap.ic_web);
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);
                            tv_charging_energy.setText(null);
                            tv_charging_time.setText(null);
                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);


                            tv_charging_energy.setFocusableInTouchMode(true);
                            tv_charging_energy.setFocusable(true);
                            tv_charging_energy.setEnabled(true);

                            tv_charging_time.setFocusable(true);
                            tv_charging_time.setFocusableInTouchMode(true);
                            tv_charging_time.setEnabled(true);


                            rb_charging_time.setFocusable(true);
                            rb_charging_time.setFocusableInTouchMode(true);
                            rb_charging_time.setEnabled(true);

                            rb_charging_energy.setFocusable(true);
                            rb_charging_energy.setFocusableInTouchMode(true);
                            rb_charging_energy.setEnabled(true);

                            changeChargerStatus();
                            isCheck = false;


                        } else if ("Unavailable".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);

                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);

                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));
                            img_connected.setImageResource(R.mipmap.ic_web);
                            changeChargerStatus();
                            isCheck = false;
                            progress.dismiss();
                            counter++;
                            {
                                if (counter == 4) {
                                    counter = 0;
                                }
                            }
//
                        } else if ("SuspendedEV".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);

                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));
                            img_connected.setImageResource(R.mipmap.ic_web);
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);
                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);
                            rb_charging_time.setFocusable(true);
                            rb_charging_time.setFocusableInTouchMode(true);
                            rb_charging_time.setEnabled(true);
                            rb_charging_time.setFocusable(true);
                            rb_charging_time.setFocusableInTouchMode(true);
                            rb_charging_time.setEnabled(true);

                            changeChargerStatus();
                            isCheck = false;
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Either your vehicle is not lock in proper way or Charging full.", Toast.LENGTH_LONG).show();

                        } else if ("Available".equalsIgnoreCase(chargerStatus.message)) {
                            Pref.setBoolValue(Pref.TYPE.IS_CHARGING.toString(), false);

                            DrawableCompat.setTint(img_connected.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorTextLight));
                            img_connected.setImageResource(R.mipmap.ic_web);
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            changeChargerStatus();
                            progress.dismiss();

                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);
                            tv_charging_energy.setText(null);
                            tv_charging_time.setText(null);
                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");
                            tv_charging_time.setHint("0 hour");
                            tv_charging_energy.setHint("0 kWh");
                            rb_charging_energy.setChecked(false);
                            rb_charging_time.setChecked(false);


                            isCheck = false;
                            counter++;
                            {
                                if (counter == 4) {

                                    counter = 0;
                                }
                            }
                        }

                    } else {
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                MainActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.SERVER_ERROR_CODE,
                                AppUtils.SERVER_ERROR,
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                } catch (Exception e) {

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.DATA_NOT_FOUND_CODE,
                            AppUtils.DATA_NOT_FOUND,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                }
            }

            public void onFailure(Call<ResponseChargerStatus> call, Throwable t) {
                changeChargerStatus();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }



    private void setCharingTimebased() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", charger_sr_no, charger_sr_no, "CPV07", 1, cmd_time)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    Logger.e("Response Timer current: " + responseCurrent);
                    setPassword();

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                changeChargerStatus();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }


    private void setCharingkWhbased() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", charger_sr_no, charger_sr_no, "CPV07", 1, cmd_kwh)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    Logger.e("Response Set current: " + responseCurrent);
                    setPassword();

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                changeChargerStatus();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        permission();
        Logger.e("Timer   " + swipeTimer);

        swipeTimer = new Timer();
        try {
           /* if (countDownTimer != null) {
                countDownTimer.cancel();
            }*/
            isCheck = false;
            Logger.e("==isProgressShow  " + isProgressShow);

            if (Utils.isNetworkConnected(getActivity())) {
                if (!isProgressShow) {
                    progress.setMessage("Please wait while. fetching updates!");
                    progress.show();
                }
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Logger.e("Execute Task after 5 second");
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    if (Utils.isNetworkConnected(getActivity())) {
                                        if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                            if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                                getChargerStatus("STATUS", charger_sr_no, "1");

                                            }
                                        } else {

                                        }
                                    }


                                } catch (Exception e) {
                                    Logger.e("Timer exp");

                                }


                            }
                        });
//                getSessionValue(charger_id, connector);
//
                    }
                }, 5000, 5000);

            } else {


//                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Logger.e("Timer exp111111" + e.getMessage());

        }
    }

    void changeChargerStatus() {
        tv_status.setTextColor(getResources().getColor(R.color.colorlight));
        tv_total_kWh_time.setTextColor(getResources().getColor(R.color.colorlight));
        tv_unit.setTextColor(getResources().getColor(R.color.colorlight));
        DrawableCompat.setTint(img_charger_status.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorlight));



        if ("CHARGER_OFFLINE".equalsIgnoreCase(charger_status)) {
            tv_status.setText("disconnected");

        } else {
            tv_status.setText(charger_status);

        }


        img_anim.setVisibility(View.GONE);
        progressCharge.setVisibility(View.GONE);
//        tv_charging_time.setText("0 hour");
//        tv_charging_energy.setText("0 kWh");
        tv_unit.setText("hour");
        tv_total_kWh_time.setText("00:00");


    }










    void permission() {

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "Bluetooth not support", Toast.LENGTH_SHORT).show();

        } else if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        } else if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }


        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, 0);
            }
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 0);
            }
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADVERTISE}, 0);
            }

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, 0);
            }
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 0);
            }
        } else {

        }
    }



    void dialog_ble() {
        dialog_ble = new Dialog(getActivity());
        dialog_ble.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_ble.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_ble.setCancelable(false);

        TextView btn_ok = (TextView) dialog_ble.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_ble.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_ble.findViewById(R.id.tv_message);
        btn_cencel.setVisibility(View.GONE);
        btn_ok.setText("OK");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ble.dismiss();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ble.dismiss();

            }
        });
        dialog_ble.show();


    }


    void dialog_stopCharger() {
        dialog_stop_charger = new Dialog(getActivity());
        dialog_stop_charger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_stop_charger.setContentView(R.layout.app_dialog);
        dialog_stop_charger.setCancelable(false);
        TextView btn_ok = (TextView) dialog_stop_charger.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_stop_charger.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_stop_charger.findViewById(R.id.tv_message);
        tv_message.setText("Are you sure, you want to stop charging?");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                        if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            chargerStopCommand();
                            dialog_stop_charger.dismiss();
                        } else {

                        }
                    } else {

                    }
                } else {

                }

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_stop_charger.dismiss();

            }
        });
        dialog_stop_charger.show();


    }




    private void setTimeChargingOCCP() {
        try {
            Integer time = Integer.parseInt(tv_charging_time.getText().toString());
            if (time >= 1) {
                String hexTime = Integer.toHexString(time * 60);
                String finalHexTime = ("0000" + hexTime).substring(hexTime.length());
//                writeCommand(Globals.SET_CHARGE_TIME_PREFIX + finalHexTime);
                cmd_time = Globals.SET_CHARGE_TIME_PREFIX + finalHexTime;
                Logger.e("Command time  " + cmd_time);

                setCharingTimebased();

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.min_time), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please enter time value", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    private void setKwhChargingOCCP() {
        try {
            Integer watt = Integer.parseInt(tv_charging_energy.getText().toString());
            if (watt >= 1) {
                String hexWatt = Integer.toHexString(watt * 100);
                String finalHexWatt = ("00000000" + hexWatt).substring(hexWatt.length());
                cmd_kwh = Globals.SET_CHARGE_KWH_PREFIX + finalHexWatt;
                Logger.e("Command kwh  " + cmd_kwh);
                setCharingkWhbased();
//                writeCommand(Globals.SET_CHARGE_KWH_PREFIX + finalHexWatt);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.min_kwh), Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter kWh value", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startChargerUsingSchedule() {
        String hexPass = Integer.toHexString(Pref.getIntValue(Pref.TYPE.CHARGER_CRED.toString(), 0)).toUpperCase();
        finalHexPass = ("000000" + hexPass).substring(hexPass.length());

        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", charger_sr_no, charger_sr_no, "CPV07", 1, Globals.START_CHARGING )).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    Logger.e("Response Timer current: " + responseCurrent);
                    Toast.makeText(getActivity(), "Charger start successfully", Toast.LENGTH_LONG).show();


                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                changeChargerStatus();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }


    private void setPassword() {
        String hexPass = Integer.toHexString(Pref.getIntValue(Pref.TYPE.CHARGER_CRED.toString(), 0)).toUpperCase();
        finalHexPass = ("000000" + hexPass).substring(hexPass.length());
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", charger_sr_no, charger_sr_no, "CPV07", 1, Globals.SET_PASSWORD_PREFIX )).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    Logger.e("Response Timer current: " + responseCurrent);
                    startChargerUsingSchedule();

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                changeChargerStatus();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }





    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }



    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(getActivity(), MyService.class);
        intent.putExtra("data", errorMessage_email);
        getActivity().startService(intent);

    }


}