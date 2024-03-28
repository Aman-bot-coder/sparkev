package com.augmentaa.sparkev.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.CallHistoryAdapter;

import com.augmentaa.sparkev.model.signup.call_request.Data;
import com.augmentaa.sparkev.model.signup.call_request.RequestCallBackRequest;
import com.augmentaa.sparkev.model.signup.call_request.ResponseCallBackRequest;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRequestInfoActivity extends AppCompatActivity {


    ImageView img_back,img_add_request;
    int position = 0;
    RadioGroup radioGroup;
    RadioButton rb_upcoming, rb_archived;
    Button btnAddRequest;
    APIInterface apiInterface;
    ProgressDialog progress;
    ListView listView;
    TextView tvRequestNotFound;
    List<Data> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callback_request_details);
        getSupportActionBar().hide();
        radioGroup = findViewById(R.id.radioGroup);
        rb_upcoming = findViewById(R.id.rb_upcoming);
        rb_archived = findViewById(R.id.rb_archived);
        btnAddRequest = findViewById(R.id.btn_add_request);
        img_add_request=findViewById(R.id.img_add_request);
        img_back = (ImageView) findViewById(R.id.back);
        list=new ArrayList<>();
        apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress = new ProgressDialog(this);
        getSupportActionBar().hide();
        tvRequestNotFound=findViewById(R.id.tv_data);
        listView=findViewById(R.id.listview);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent intent = new Intent(CallRequestInfoActivity.this, RequestToCallbackActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        btnAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallRequestInfoActivity.this, RequestToCallbackActivity.class);
                startActivity(intent);
//                finish();
            }
        });


        img_add_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallRequestInfoActivity.this, RequestToCallbackActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(CallRequestInfoActivity.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();

                    if("Upcoming".equalsIgnoreCase(rb.getText().toString())){
                        if (Utils.isNetworkConnected(CallRequestInfoActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getUpcoming();
                        } else {
                            Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

                    }
                    else {
                        if (Utils.isNetworkConnected(CallRequestInfoActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getArchived();
                        } else {
                            Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    }
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(CallRequestInfoActivity.this, RequestToCallbackActivity.class);
//        startActivity(intent);
//        finish();
    }


    private void getArchived() {
        list.clear();
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.getCallHistory(hashMap1, new RequestCallBackRequest("C", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.MOBILE.toString(), null))).enqueue(new Callback<ResponseCallBackRequest>() {
            public void onResponse(Call<ResponseCallBackRequest> call, Response<ResponseCallBackRequest> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseCallBackRequest loginResponse = (ResponseCallBackRequest) response.body();
                        Logger.e("Response:111111 " + loginResponse.toString()+"  "+loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {
                                for (int i = 0; i < loginResponse.data.size(); i++) {
                                    if (loginResponse.data.get(i).status.equalsIgnoreCase("C")) {
                                        list.add(loginResponse.data.get(i));
                                        CallHistoryAdapter adapter = new CallHistoryAdapter(CallRequestInfoActivity.this, list);
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                                if (list.size() > 0) {
                                    tvRequestNotFound.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                } else {
                                    tvRequestNotFound.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                    tvRequestNotFound.setText("You’ve got no archived requests yet");

                                    callMailService(
                                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                            CallRequestInfoActivity.class.getSimpleName(),
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
                            } else {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                tvRequestNotFound.setText("You’ve got no archived requests yet");

                                Toast.makeText(CallRequestInfoActivity.this, "No call request found", Toast.LENGTH_LONG).show();
                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        CallRequestInfoActivity.class.getSimpleName(),
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

                        } else {
                            try {
//                                Toast.makeText(CallRequestInfoActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                tvRequestNotFound.setText("You’ve got no archived requests yet");


                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        CallRequestInfoActivity.class.getSimpleName(),
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
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                CallRequestInfoActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.SUCCESS_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                } else {
                    Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            CallRequestInfoActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseCallBackRequest> call, Throwable t) {
                Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        CallRequestInfoActivity.class.getSimpleName(),
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

    private void getUpcoming() {
        list.clear();
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.getCallHistory(hashMap1, new RequestCallBackRequest("O", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.MOBILE.toString(), null))).enqueue(new Callback<ResponseCallBackRequest>() {
            public void onResponse(Call<ResponseCallBackRequest> call, Response<ResponseCallBackRequest> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseCallBackRequest loginResponse = (ResponseCallBackRequest) response.body();
                        Logger.e("Response:111111 " + loginResponse.toString()+"  "+loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {

                                for (int i = 0; i < loginResponse.data.size(); i++) {
                                    if (loginResponse.data.get(i).status.equalsIgnoreCase("O")) {
                                        list.add(loginResponse.data.get(i));
                                        CallHistoryAdapter adapter = new CallHistoryAdapter(CallRequestInfoActivity.this, list);
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                                if (list.size() > 0) {
                                    tvRequestNotFound.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                } else {
                                    tvRequestNotFound.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                    callMailService(
                                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                            CallRequestInfoActivity.class.getSimpleName(),
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
                            } else {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                Toast.makeText(CallRequestInfoActivity.this, "No call request found", Toast.LENGTH_LONG).show();
                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        CallRequestInfoActivity.class.getSimpleName(),
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

                        } else {
                            try {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        CallRequestInfoActivity.class.getSimpleName(),
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
//                                Toast.makeText(CallRequestInfoActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        CallRequestInfoActivity.class.getSimpleName(),
                                        "SPIN_ANDROID",
                                        AppUtils.project_id,
                                        AppUtils.bodyToString(call.request().body()),
                                        "ANDROID",
                                        call.request().url().toString(),
                                        "Y",
                                        AppUtils.SUCCESS_CODE,
                                        e.getMessage(),
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                CallRequestInfoActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.SUCCESS_CODE,
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
                            CallRequestInfoActivity.class.getSimpleName(),
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
                    Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseCallBackRequest> call, Throwable t) {

                Logger.e("Exception "+t.getMessage());
                Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });


    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(CallRequestInfoActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(CallRequestInfoActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getUpcoming();
        } else {
            Toast.makeText(CallRequestInfoActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }
}
