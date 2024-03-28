package com.augmentaa.sparkev.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddCharger;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddCharger;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.request_access.RequestAccess;
import com.augmentaa.sparkev.model.signup.request_access.ResponseRequestAccess;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.GPSTracker;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.ui.ContactUsActivity;
import com.augmentaa.sparkev.ui.MainActivity;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.NetworkUtil;
import com.augmentaa.sparkev.utils.PermissionUtils;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChargerSerialNumber extends Fragment {

    Button btnSubmit;
    String charger_serial_number, charger_name;
    ImageView img_back, img_help;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public static final int REQUEST_CODE = 1;

    ProgressDialog progress;
    APIInterface apiInterface, apiInterfaceSPIN;

    GPSTracker gps;
    //    SwitchCompat switchCompat;
    String l_name;
    Timer swipeTimer = new Timer();
    double lati, longi;
    private static final int PERMISSION_CODE = 99;
    String client_certificate;
    int map_as_child;
    int spinChargerId;
    String device_owner;
    String displayID;
    Dialog dialog;
    EditText etChargerSerialNumber;
    boolean isChecked = false;
    boolean isUnsuccess = false;
    TextView tv_message;
    String partNo;


    LinearLayout ly_successful, lyUnsuccessful, lyRequestSent, ly_serialNumber;
    ImageView imgUnsuccessfulBack, imgRequestSentBack;

    Button btnTryAgain, btnContactUs, btnRequestSent;
    TextView tvSuccessMsg, tvUnSuccessMgs, tvRequestSentMgs;

    public AddChargerSerialNumber() {
        // require a empty public constructor
    }

    Dialog dailog_customerCare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_charger_serial_number, container, false);
        btnSubmit = view.findViewById(R.id.btn_submit);
        img_back = view.findViewById(R.id.back);
        img_help = view.findViewById(R.id.help);
        etChargerSerialNumber = view.findViewById(R.id.serial_number);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfaceSPIN = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);
        progress = new ProgressDialog(getActivity());
        tv_message=view.findViewById(R.id.unsuccess_message);
        ly_successful = view.findViewById(R.id.ly_success);
        lyUnsuccessful = view.findViewById(R.id.ly_unsussess);
        lyRequestSent = view.findViewById(R.id.ly_request_sent);
        ly_serialNumber = view.findViewById(R.id.ly_charger_serial);

        imgRequestSentBack = view.findViewById(R.id.back_request_sent);
        imgUnsuccessfulBack = view.findViewById(R.id.back_unsuccessfull);
        btnContactUs = view.findViewById(R.id.contactus);
        btnRequestSent = view.findViewById(R.id.btn_request_sent);
        btnTryAgain = view.findViewById(R.id.try_again);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }
        });

        imgUnsuccessfulBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_serialNumber.setVisibility(View.VISIBLE);
                lyRequestSent.setVisibility(View.GONE);
                ly_successful.setVisibility(View.GONE);
                lyUnsuccessful.setVisibility(View.GONE);


            }
        });

        imgRequestSentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_serialNumber.setVisibility(View.VISIBLE);
                lyRequestSent.setVisibility(View.GONE);
                ly_successful.setVisibility(View.GONE);
                lyUnsuccessful.setVisibility(View.GONE);
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly_serialNumber.setVisibility(View.GONE);
                lyRequestSent.setVisibility(View.GONE);
                ly_successful.setVisibility(View.GONE);
                lyUnsuccessful.setVisibility(View.VISIBLE);
                if (Utils.isNetworkConnected(getActivity())) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.setCancelable(false);
                    progress.show();
                    createDevices();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });

        try {
            gps = new GPSTracker(getActivity(), getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {
                //Note: I have placed this code in onResume for demostration purpose. Be careful when you use it in
                // production code
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED) {
                    //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
                    //I will skip it for this demo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                            displayNeverAskAgainDialog();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_CODE);
                        }
                    }

                } else {

                    if (NetworkUtil.isConnectivityOk(getActivity())) {
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (lati == 0.0 && longi == 0.0) {
                                    lati = gps.getLatitude();
                                    longi = gps.getLongitude();


                                    Logger.e("Lat Long=iff=>" + lati + "    " + longi);

                                } else {
                                    if (swipeTimer != null) {
                                        swipeTimer.cancel();
                                        new Thread() {
                                            public void run() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    public void run() {

                                                        Pref.setValue(Pref.TYPE.CURR_LATI.toString(), String.valueOf(lati));
                                                        Pref.setValue(Pref.TYPE.CURR_LONGI.toString(), String.valueOf(longi));
                                                    }
                                                });
                                            }
                                        }.start();
                                    }
                                    Logger.e("Lat Long==>" + lati + "    " + longi);


                                }


                            }
                        }, 2000, 2000);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }


                }
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        catch (Exception e){

        }

        btnRequestSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ly_serialNumber.setVisibility(View.GONE);
                lyRequestSent.setVisibility(View.VISIBLE);
                ly_successful.setVisibility(View.GONE);
                lyUnsuccessful.setVisibility(View.GONE);
                if (Utils.isNetworkConnected(getActivity())) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.setCancelable(false);
                    progress.show();
                    requestAccess();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });

        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);            }
        });


        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_showChargerID();
            }
        });

        try {
            charger_serial_number = getArguments().getString("charger_id");
            charger_name = getArguments().getString("name");
            etChargerSerialNumber.setText(charger_serial_number);
        } catch (Exception e) {

        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    charger_serial_number=etChargerSerialNumber.getText().toString().trim();
                    if(!TextUtils.isEmpty(charger_serial_number) && validQrCode(charger_serial_number)) {
                        if (Utils.isNetworkConnected(getActivity())) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.setCancelable(false);
                            progress.show();
                            createDevices();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

//                    Toast.makeText(getActivity(), "Correct Charger id .", Toast.LENGTH_LONG).show();

                    }else{
                        dialog_charger_validate_alert();
//                        Toast.makeText(getActivity(), "Oops! Incorrect Charger Details. Please try again with correct format and input.", Toast.LENGTH_LONG).show();

                    }

                }
                catch (Exception e){
                    dialog_charger_validate_alert();
                }



            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            cameraPermission();
        } else {



        }


        return view;

    }

    public void cameraPermission() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                Logger.e("Permission 111");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                Logger.e("Permission 222222");
            }
        } else {
            Logger.e("Permission 33333");
            // READ_PHONE_STATE permission is already been granted.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(ScanChargerActivity.this, QRCodeScanActivity.class);
//                startActivity(intent);
            } else {
//                Toast.makeText(this, "55555", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayNeverAskAgainDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("To use the application, its mandate to provide the location access for better experiences of app and its features. Please go in settings and allow permission."
                + "Settings screen.\n\nSelect Permissions -> Enable Permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                displayNeverAskAgainDialog();

            }
        });
//        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void createDevices() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.createDevice(hashMap1, new RequestAddCharger(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0), charger_serial_number, charger_serial_number, charger_name, Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0), " ", lati, longi)).enqueue(new Callback<ResponseAddCharger>() {
            public void onResponse(Call<ResponseAddCharger> call, final Response<ResponseAddCharger> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseAddCharger responseAddCharger = (ResponseAddCharger) response.body();
                        Logger.e("Response 123: " + responseAddCharger.toString());

                        if (responseAddCharger.status == 1) {
                            isUnsuccess = false;
                            String[] parts = charger_serial_number.split("#");
                            partNo=parts[0];
                            displayID = parts[1];
                            client_certificate = responseAddCharger.client_certificate;
                            spinChargerId = responseAddCharger.id;
                            map_as_child = 0;
                            userChargerMappingBLESync(client_certificate,map_as_child);


                        } else if (responseAddCharger.status == 2) {
                            isUnsuccess = false;
                            progress.dismiss();
                            client_certificate = " ";
                            device_owner = responseAddCharger.device_owner;
                            map_as_child = 1;
                            charger_name = responseAddCharger.nickname;
//                            Toast.makeText(getActivity(), responseAddCharger.msg, Toast.LENGTH_LONG).show();
//                            dialog_sendRequest();

//                            dialogSendRequest();

                            tv_message.setText("Charger is already registered by "+device_owner);
                            ly_serialNumber.setVisibility(View.GONE);
                            lyRequestSent.setVisibility(View.VISIBLE);
                            ly_successful.setVisibility(View.GONE);
                            lyUnsuccessful.setVisibility(View.GONE);


                        } else {
//                            Toast.makeText(getActivity(), responseAddCharger.msg, Toast.LENGTH_LONG).show();

                          /*  AddChargerError fragment = new AddChargerError();
                            FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("charger_id", charger_serial_number);
                            bundle.putString("name", charger_name);
                            fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.flFragment, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();



*/

//
/*
                            if (!isUnsuccess) {
                                dialogUnSuccess();
                            }
*/

                            progress.dismiss();
//                            ly_serialNumber.setVisibility(View.GONE);
//                            lyRequestSent.setVisibility(View.GONE);
//                            ly_successful.setVisibility(View.GONE);
//                            lyUnsuccessful.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), responseAddCharger.msg, Toast.LENGTH_LONG).show();
                            dialog_services_message(charger_serial_number);

                        }


                    } catch (Exception e) {
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
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
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

            public void onFailure(Call<ResponseAddCharger> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception:22222 " + t.getMessage());
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

    private void userChargerMappingBLESync(String client_certificate, int map_as_child) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.userChargerMappingBLESync(hashMap1, new RequestAddChargerSyncBLE(displayID, client_certificate, charger_name, Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0), lati, longi, "Y", "", map_as_child)).enqueue(new Callback<ResponseAddChargerSyncBLE>() {
            public void onResponse(Call<ResponseAddChargerSyncBLE> call, final Response<ResponseAddChargerSyncBLE> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {

                    try {

                        ResponseAddChargerSyncBLE ResponseAddCharger = (ResponseAddChargerSyncBLE) response.body();
                       if(ResponseAddCharger.status) {
                           Logger.e("Response:1212 " + ResponseAddCharger.toString());
                           Pref.setValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), displayID);
                           Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 1);
                           Pref.setValue(Pref.TYPE.NICK_NAME.toString(), charger_name);
                           Pref.setValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), partNo);
                           Pref.setIntValue(Pref.TYPE.CHARGER_CRED.toString(), Integer.parseInt(client_certificate));
                           Pref.setValue(Pref.TYPE.SYSTEM_NUMBER.toString(), partNo + "#" + displayID);
                           ly_serialNumber.setVisibility(View.GONE);
                           lyRequestSent.setVisibility(View.GONE);
                           ly_successful.setVisibility(View.VISIBLE);
                           lyUnsuccessful.setVisibility(View.GONE);
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {

                                   Intent intent = new Intent(getActivity(), MainActivity.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                   startActivity(intent);
                                   getActivity().finish();

                               }
                           }, 3000);

                       }
                       else {
                           Toast.makeText(getActivity(), ResponseAddCharger.message, Toast.LENGTH_LONG).show();

                       }

                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

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
                    progress.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseAddChargerSyncBLE> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception:  1111 " + t.getMessage());
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

    private void requestAccess() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.requestAccess(hashMap1, new RequestAccess(charger_serial_number, charger_name, Pref.getValue(Pref.TYPE.S_EMAIL.toString(), null), device_owner, Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0))).enqueue(new Callback<ResponseRequestAccess>() {
            public void onResponse(Call<ResponseRequestAccess> call, final Response<ResponseRequestAccess> response) {

                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        ResponseRequestAccess responseAddCharger = (ResponseRequestAccess) response.body();
                        Logger.e("Response 123: " + responseAddCharger.toString());
                        if (responseAddCharger.status == 1) {
                            ly_serialNumber.setVisibility(View.GONE);
                            lyRequestSent.setVisibility(View.GONE);
                            ly_successful.setVisibility(View.VISIBLE);
                            lyUnsuccessful.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    Pref.setBoolValue(Pref.TYPE.REQUEST_SEND.toString(),true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    getActivity().finish();

                                }
                            }, 3000);



                        } else if (responseAddCharger.status == 2) {

                        }
                        Toast.makeText(getActivity(), responseAddCharger.msg, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        Logger.e("Exc  "+e);
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
                                AppUtils.DATA_NOT_FOUND_CODE,
                                    e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
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
                    progress.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseRequestAccess> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
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


/*    void dialog_sendRequest() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_send_request);
        dialog.setCancelable(false);
        TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_submit);
        TextView btn_cencel = (TextView) dialog.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(getActivity())) {

                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.setCancelable(false);
                    progress.show();
                    requestAccess();
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }*/


    void dialog_showChargerID() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_show_charger_id);
        dialog.setCancelable(false);
        ImageView btn_cencel = dialog.findViewById(R.id.cancel);
        TextView tvSerialNumber = dialog.findViewById(R.id.serial_number);
//        tvSerialNumber.setText(charger_serial_number);

        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();


    }


    void dialogSuccess(String message) {
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.activity_success);
        dialog.setCancelable(false);
        TextView tvSerialNumber = dialog.findViewById(R.id.success_message);
        tvSerialNumber.setText(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        }, 3000);

        dialog.show();


    }


    void dialogUnSuccess() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.fragment_error);
        dialog.setCancelable(false);
        TextView tvMessage = dialog.findViewById(R.id.success_message);
        Button btnTryAgain = dialog.findViewById(R.id.try_again);
        Button btnContactUs = dialog.findViewById(R.id.contactus);
        ImageView img_back = dialog.findViewById(R.id.back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChecked = true;
                isUnsuccess = true;
                if (Utils.isNetworkConnected(getActivity())) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.setCancelable(false);
                    progress.show();
                    createDevices();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });

        dialog.show();


    }


    void dialogSendRequest() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.frag_request_sent);
        dialog.setCancelable(false);
        TextView tvMessage = dialog.findViewById(R.id.success_message);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        ImageView img_back = dialog.findViewById(R.id.back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvMessage.setText("Charger is already registered by " + charger_name);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(getActivity())) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.setCancelable(false);
                    progress.show();
                    requestAccess();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });

        dialog.show();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(getActivity(), MyService.class);
        intent.putExtra("data", errorMessage_email);
        getActivity().startService(intent);

    }


    void dialog_services_message(String charger_id) {
        dailog_customerCare = new Dialog(getActivity());
        dailog_customerCare.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailog_customerCare.setContentView(R.layout.dialog_customer_care);
        dailog_customerCare.setCancelable(false);
        TextView btn_ok = (TextView) dailog_customerCare.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dailog_customerCare.findViewById(R.id.tv_cancel);
//        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dailog_customerCare.findViewById(R.id.tv_message);

        btn_cencel.setText("Ok");
        btn_ok.setText("Customer Care");

//        tv_message.setText(getResources().getString(R.string.service_message));

        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18005725845"));
                startActivity(intent);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                intent.putExtra("charger_id",charger_serial_number);
                startActivity(intent);
                dailog_customerCare.dismiss();


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog_customerCare.dismiss();

            }
        });
        dailog_customerCare.show();


    }


    protected boolean validQrCode(String qrCode){
        if(qrCode.length() == 24 && qrCode.substring(8, 9).equals("#")){
            return true;
        }
        return false;
    }



    void dialog_charger_validate_alert() {
        dailog_customerCare = new Dialog(getActivity());
        dailog_customerCare.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailog_customerCare.setContentView(R.layout.dialog_customer_care);
        dailog_customerCare.setCancelable(false);
        TextView btn_ok = (TextView) dailog_customerCare.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dailog_customerCare.findViewById(R.id.tv_cancel);
//        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dailog_customerCare.findViewById(R.id.tv_message);
        tv_message.setText("You have entered the invalid Charger ID, Please enter the valid format Charger ID (ex: HE51XXXX#DOXXXXXXXXXXX34)");
        btn_cencel.setVisibility(View.INVISIBLE);
        btn_ok.setText("Ok");

//        tv_message.setText(getResources().getString(R.string.service_message));


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog_customerCare.dismiss();


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog_customerCare.dismiss();

            }
        });
        dailog_customerCare.show();


    }
}