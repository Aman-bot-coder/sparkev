package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.SendRequestAdapter;
import com.augmentaa.sparkev.adapter.RecievedRequestAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGuestAccessList;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseGuestAccessList;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListGuestAccessActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ProgressDialog progress;
    ListView listView;
    TextView btnSend, btnReceived;
    ResponseGuestAccessList loginresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_guest_access);
        listView = findViewById(R.id.listview);
        btnSend = findViewById(R.id.send);
        btnReceived = findViewById(R.id.recieve);

        this.apiInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);

        if (Utils.isNetworkConnected(ListGuestAccessActivity.this)) {
            progress = new ProgressDialog(ListGuestAccessActivity.this);
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
        } else {
            Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(ListGuestAccessActivity.this)) {
                    progress = new ProgressDialog(ListGuestAccessActivity.this);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getSentChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
                } else {
                    Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });

        btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ListGuestAccessActivity.this)) {
                    progress = new ProgressDialog(ListGuestAccessActivity.this);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
                } else {
                    Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    public void getReceivedChargerRequest(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGuestAccessList> call = apiInterface.guestAccessList(hashMap1, new RequestGuestAccessList(userId));
        call.enqueue(new Callback<ResponseGuestAccessList>() {
            @Override
            public void onResponse(Call<ResponseGuestAccessList> call, Response<ResponseGuestAccessList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        loginresponse = (ResponseGuestAccessList) response.body();
                        if (loginresponse.data.size() > 0) {
                            RecievedRequestAdapter chargerAdapter = new RecievedRequestAdapter(ListGuestAccessActivity.this, loginresponse.data);
                            listView.setAdapter(chargerAdapter);
                            chargerAdapter.notifyDataSetChanged();

                        } else {
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


//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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

    public void getSentChargerRequest(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGuestAccessList> call = apiInterface.requestSentList(hashMap1, new RequestGuestAccessList(userId));
        call.enqueue(new Callback<ResponseGuestAccessList>() {
            @Override
            public void onResponse(Call<ResponseGuestAccessList> call, Response<ResponseGuestAccessList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                    loginresponse = (ResponseGuestAccessList) response.body();
                    if (loginresponse.data.size() > 0) {
                        SendRequestAdapter chargerAdapter = new SendRequestAdapter(ListGuestAccessActivity.this, loginresponse.data);
                        listView.setAdapter(chargerAdapter);
                        chargerAdapter.notifyDataSetChanged();

                    } else {
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

//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(ListGuestAccessActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }
    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ListGuestAccessActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }




}