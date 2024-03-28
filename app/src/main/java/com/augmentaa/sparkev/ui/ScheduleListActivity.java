package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllScheduleListAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.schedule.Data;
import com.augmentaa.sparkev.model.signup.schedule.RequestGetSchedule;
import com.augmentaa.sparkev.model.signup.schedule.ResponseGetSchedule;
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

public class ScheduleListActivity extends AppCompatActivity implements GetAllScheduleListAdapter.AdapterInterface {
    RadioGroup radioGroup;
    RadioButton rb_oneTime, rb_recurring;
    Button btnAddSchedule;
    APIInterface apiInterface;
    ProgressDialog progress;
    RadioButton rb_selected;
    ImageView img_back;
    ImageView img_add_schedule;
    TextView tvRequestNotFound;
    ListView listView;
    List<Data> list_schedule;
    GetAllScheduleListAdapter.AdapterInterface adapterInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        radioGroup = findViewById(R.id.radioGroup);
        rb_recurring = findViewById(R.id.rb_recurring);
        rb_oneTime = findViewById(R.id.rb_one_time);
        btnAddSchedule = findViewById(R.id.btn_add_schedule);
        img_back = findViewById(R.id.back);
        listView = findViewById(R.id.listview);
        tvRequestNotFound = findViewById(R.id.tv_data);
        img_add_schedule = findViewById(R.id.img_add_schedule);
        getSupportActionBar().hide();
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.progress = new ProgressDialog(this);

        list_schedule = new ArrayList<>();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Utils.isNetworkConnected(ScheduleListActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getScheduleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "ONE_TIME");
        } else {
            Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    rb_selected = findViewById(selectedId);
//                    Toast.makeText(ScheduleListActivity.this, rb_selected.getText(), Toast.LENGTH_SHORT).show();
                    if ("One Time".equalsIgnoreCase(rb_selected.getText().toString().trim())) {
                        Intent intent = new Intent(getApplicationContext(), AddScheduleDetailsOneTimeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), AddScheduleDetailsRecurringActivity.class);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Toast.makeText(ScheduleListActivity.this, "Please select schedule.", Toast.LENGTH_SHORT).show();

                }


            }
        });


        img_add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    rb_selected = findViewById(selectedId);
//                    Toast.makeText(ScheduleListActivity.this, rb_selected.getText(), Toast.LENGTH_SHORT).show();
                    if ("One time".equalsIgnoreCase(rb_selected.getText().toString().trim())) {
                        Intent intent = new Intent(getApplicationContext(), AddScheduleDetailsOneTimeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), AddScheduleDetailsRecurringActivity.class);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Toast.makeText(ScheduleListActivity.this, "Please select schedule.", Toast.LENGTH_SHORT).show();

                }


            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(CallRequestInfoActivity.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();

                    if ("One time".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(ScheduleListActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getScheduleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "ONE_TIME");
                        } else {
                            Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        if (Utils.isNetworkConnected(ScheduleListActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getScheduleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "RECURRING");
                        } else {
                            Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    }
                }

            }
        });

    }


    public void getScheduleList(int user_id, String charger_serial_no, String schedule_type) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGetSchedule> call = apiInterface.getAllSchedule(hashMap1, new RequestGetSchedule(user_id, charger_serial_no, schedule_type));
        call.enqueue(new Callback<ResponseGetSchedule>() {
            @Override
            public void onResponse(Call<ResponseGetSchedule> call, Response<ResponseGetSchedule> response) {
                progress.dismiss();
                list_schedule.clear();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseGetSchedule loginResponse = (ResponseGetSchedule) response.body();
                        Logger.e("Response:111111 " + loginResponse.toString() + "  " + loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {
                                for (int i = 0; i < loginResponse.data.size(); i++) {
                                    if (loginResponse.data.get(i).scheduleType.equalsIgnoreCase(schedule_type)) {
                                        list_schedule.add(loginResponse.data.get(i));
                                    } else {

                                    }
                                    setAdapter();

                                }


                                if (list_schedule.size() > 0) {
                                    tvRequestNotFound.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                } else {
                                    tvRequestNotFound.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }
                            } else {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
//                                Toast.makeText(ScheduleListActivity.this, "No call request found", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            try {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
//                                Toast.makeText(CallRequestInfoActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
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
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                                AppUtils.DATA_NOT_FOUND_CODE, e.getMessage(),
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
                    Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.server_not_found_please_try_again) , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseGetSchedule> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(ScheduleListActivity.this, getResources().getString(R.string.server_not_found_please_try_again) , Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void buttonPressed() {

        Logger.e("Click event");

    }

    void setAdapter() {
        GetAllScheduleListAdapter adapter = new GetAllScheduleListAdapter(ScheduleListActivity.this, list_schedule, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ScheduleListActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

}