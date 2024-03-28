package com.augmentaa.sparkev.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyService extends IntentService {

    private APIInterface apiInterface;
    ErrorMessage_Email errorMessage_email;
    ProgressDialog progress;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
            errorMessage_email = intent.getParcelableExtra("data");
            if (Utils.isNetworkConnected(MyService.this)) {
                ErrorMessage_Email(errorMessage_email.id, errorMessage_email.username, errorMessage_email.email, errorMessage_email.device_id, errorMessage_email.app_version, errorMessage_email.os_version, errorMessage_email.activity_name, errorMessage_email.application_name, errorMessage_email.project_id,
                        errorMessage_email.api_parameters, errorMessage_email.application_plateform,
                        errorMessage_email.url, errorMessage_email.status, errorMessage_email.error_code, errorMessage_email.error_discription, errorMessage_email.created_by, errorMessage_email.mobile);
            } else {
                Toast.makeText(MyService.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ErrorMessage_Email(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform,
                                    String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        this.apiInterface.postErrorLog(new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id,
                api_parameters, application_plateform,
                url, status, error_code, error_discription, created_by, mobile)).enqueue(new Callback<ErrorMessage_Email>() {
            public void onResponse(Call<ErrorMessage_Email> call, Response<ErrorMessage_Email> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                Logger.e("Response parameter: " + AppUtils.bodyToString(call.request().body()));
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {


                    } catch (Exception e) {
                        Toast.makeText(MyService.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(MyService.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ErrorMessage_Email> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                call.cancel();
            }

        });

    }
}
