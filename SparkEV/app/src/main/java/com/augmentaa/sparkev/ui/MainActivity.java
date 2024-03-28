package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.model.signup.project_update.ProjectUpdate;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.ui.fragment.AddCharger;
import com.augmentaa.sparkev.ui.fragment.FragmentHome;
import com.augmentaa.sparkev.ui.fragment.FragmentProfile;
import com.augmentaa.sparkev.ui.fragment.FragmentServices;
import com.augmentaa.sparkev.ui.fragment.FragmentStaticstics;
import com.augmentaa.sparkev.ui.fragment.ScanCharger;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.NetworkUtil;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    String activity_name;
Dialog dialog_verify_user;
    FragmentHome homeFragment;
    FragmentServices servicesFragment;
    FragmentStaticstics staticsticsFragment;
    FragmentProfile profileFragment;
    AddCharger addCharger;
    ScanCharger scanCharger;
    Dialog dialog_addcharger;
    APIInterface apiInterfacePP,apiInterface;
    ProgressDialog progress;
    ResponseGetChargerList list_chargerdata;
    String version_name;
    int version_code;
   public static boolean isProgressShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        homeFragment = new FragmentHome();
        servicesFragment = new FragmentServices();
        staticsticsFragment = new FragmentStaticstics();
        profileFragment = new FragmentProfile();
        addCharger = new AddCharger();
        scanCharger = new ScanCharger();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        this.apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        this.apiInterface= (APIInterface) APIClient.getClientNew().create(APIInterface.class);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
            version_code = pInfo.versionCode;
            Logger.e("Version Name  " + version_name + "  " + version_code);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        progress = new ProgressDialog(this);
         if (Utils.isNetworkConnected(MainActivity.this)) {
            getAppUpdate();
        }

        try {
            activity_name = getIntent().getExtras().getString("activity");

        } catch (Exception e) {

        }
        if (NetworkUtil.isConnectivityOk(this)) {
            if (Pref.getBoolValue(Pref.TYPE.REQUEST_SEND.toString(), false)) {
                getChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));
            }

        } else {

        }


        Logger.e("List Size " + Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) + "  " + activity_name);
        if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addCharger).commit();

        } else {

            if ("add_charger".equalsIgnoreCase(activity_name)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, scanCharger).commit();

            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

            }

        }


        Logger.e("activity name" + activity_name);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        activity_name = "";
        Logger.e("List Size  111 " + activity_name);
        switch (item.getItemId()) {
            case R.id.navigation_home:
              /*  if ("add_charger".equalsIgnoreCase(activity_name)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addCharger).commit();

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

                }*/
                if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addCharger).commit();

                } else {
                    if ("add_charger".equalsIgnoreCase(activity_name)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, scanCharger).commit();

                    } else {

                        Logger.e("List Size  2222 " + activity_name);
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();

                    }

                }
                return true;

            case R.id.navigation_profile:

//                if (NetworkUtil.isConnectivityOk(MainActivity.this)) {
                try {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();

                  /*  if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                        dialog_add_charger();
                    } else {
                    }*/
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Charger does not exist", Toast.LENGTH_LONG).show();

                }


//                } else {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
//
//                }

                return true;

            case R.id.navigation_staticstic:

//                if (NetworkUtil.isConnectivityOk(MainActivity.this)) {
                try {

                    if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                        dialog_add_charger();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, staticsticsFragment).commit();
                    }


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Charger does not exist", Toast.LENGTH_LONG).show();

                }


//                } else {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
//
//                }

                return true;

            case R.id.navigation_services:

//                if (NetworkUtil.isConnectivityOk(MainActivity.this)) {
                try {

                    if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                        dialog_add_charger();
                    } else {

                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, servicesFragment).commit();

                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Charger does not exist", Toast.LENGTH_LONG).show();

                }


//                } else {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
//
//                }

                return true;
        }
        return false;
    }


    void dialog_add_charger() {
        dialog_addcharger = new Dialog(MainActivity.this);
        dialog_addcharger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_addcharger.setContentView(R.layout.app_dialog);
        dialog_addcharger.setCancelable(false);
        TextView btn_ok = (TextView) dialog_addcharger.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_addcharger.findViewById(R.id.tv_cancel);
        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dialog_addcharger.findViewById(R.id.tv_message);
        btn_ok.setText("Ok");

        tv_message.setText("You have no chargers added yet.");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Logger.e("Main Screen" + list_chargerdata.toString());
                        if (list_chargerdata.status) {
                            if (list_chargerdata.data.size() > 0) {
                                Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), list_chargerdata.data.size());
                                Logger.e("List size Main" + Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0));

                                if (Pref.getIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0) == 0) {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addCharger).commit();

                                } else {

                                    if ("add_charger".equalsIgnoreCase(activity_name)) {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, scanCharger).commit();

                                    } else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                                        Pref.setBoolValue(Pref.TYPE.REQUEST_SEND.toString(), false);
                                    }

                                }

                            }
                            else {

                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        SignInActivity.class.getSimpleName(),
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
                    } catch (Exception e) {
//                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
//                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 1111, Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                SignInActivity.class.getSimpleName(),
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
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.server_not_found_please_try_again) , Toast.LENGTH_LONG).show();

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
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
//                progress.dismiss();
                Log.d("TAG", t.getMessage());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 3333, Toast.LENGTH_LONG).show();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        SignInActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));



            }
        });

    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

    void dialog_app_update(String url, String update_type) {
        dialog_verify_user = new Dialog(MainActivity.this);
        dialog_verify_user.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_verify_user.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_verify_user.setCancelable(false);
        TextView btn_ok = (TextView) dialog_verify_user.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_verify_user.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_verify_user.findViewById(R.id.tv_message);
        tv_message.setText("New version available.                                    ");
        btn_ok.setText("Update");
        btn_cencel.setText("Skip");
        if ("FORCE".equalsIgnoreCase(update_type)) {
            btn_cencel.setVisibility(View.GONE);

        } else {
            btn_cencel.setVisibility(View.VISIBLE);
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dialog_verify_user.dismiss();
                finish();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_verify_user.dismiss();

            }
        });
        dialog_verify_user.show();


    }

    private void getAppUpdate() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ProjectUpdate> call = apiInterface.getLatestProjectVersion(4, "ANDROID");
        call.enqueue(new Callback<ProjectUpdate>() {
            @Override
            public void onResponse(Call<ProjectUpdate> call, Response<ProjectUpdate> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());

                if (response.code() == 200) {
                    try {
                        ProjectUpdate response_body = (ProjectUpdate) response.body();

                        if (response_body.status) {
                            if(version_code<response_body.data.get(0).newVersionCode) {
                                dialog_app_update(response_body.data.get(0).redirectionUrl, response_body.data.get(0).updateType);
                            }

                        }
                        Logger.e("Main Screen" + response_body.toString());
                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());


                    }
                } else {
                    progress.dismiss();


                }

            }

            @Override
            public void onFailure(Call<ProjectUpdate> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());


            }
        });

    }


}
