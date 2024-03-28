package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.MyVehicleAdapter;
import com.augmentaa.sparkev.model.signup.Vehicle.GetAllVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.MainVehicle;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
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

public class MyVehicleActivity extends AppCompatActivity {
    ImageView img_addVehicle, img_back;
    ListView listView;
    APIInterface apiInterface;
    ProgressDialog progress;
    GetAllVehicle loginresponse;
    MainVehicle updateResponse;
    TextView tv_data;
    String error_message;
    Button btnAddvehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);
        getSupportActionBar().hide();
        listView = (ListView) findViewById(R.id.listview);
        img_addVehicle = (ImageView) findViewById(R.id.add_vehicle);
        img_back = (ImageView) findViewById(R.id.back);
        btnAddvehicle = findViewById(R.id.btn_add_vehicle);
        this.progress = new ProgressDialog(this);
        tv_data = (TextView) findViewById(R.id.tv_data);

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


       /* final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isNetworkConnected(MyVehicleActivity.this)) {
                    getVehicleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
                pullToRefresh.setRefreshing(false);
            }
        });
*/
        img_addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
//                intent.putExtra("check", false);
                startActivity(intent);
            }
        });

        btnAddvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
//                intent.putExtra("check", false);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getVehicleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }

    public void getVehicleList(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));

        Call<GetAllVehicle> call = apiInterface.getVehicleDetailsByUserId(hashMap1, userId);
        call.enqueue(new Callback<GetAllVehicle>() {
            @Override
            public void onResponse(Call<GetAllVehicle> call, Response<GetAllVehicle> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                    loginresponse = response.body();
//                        Logger.e("Response: " + Pref.getValue(Pref.TYPE.CONN_TYPE_ID.toString(), null));
                    if (loginresponse.data.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        tv_data.setVisibility(View.GONE);

                        MyVehicleAdapter chargerAdapter = new MyVehicleAdapter(MyVehicleActivity.this, loginresponse.data);
                        listView.setAdapter(chargerAdapter);
                        chargerAdapter.notifyDataSetChanged();


                        Pref.setValue(Pref.TYPE.VEHICLE_DETAILS.toString(), loginresponse.data.toString());
                        Logger.e("Response: " + Pref.getValue(Pref.TYPE.VEHICLE_DETAILS.toString(), null));


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                if (loginresponse.data.get(position).is_default == 1) {
                                    Toast.makeText(getApplicationContext(), "Already in active.", Toast.LENGTH_LONG).show();


                                } else {
                                   Intent intent=new Intent(MyVehicleActivity.this,UpdateVehicleActivity.class);
                                   intent.putExtra("data",loginresponse.data.get(position));
                                   startActivity(intent);
                                }


                            }
                        });
                    } else {
                        listView.setVisibility(View.GONE);
                        tv_data.setVisibility(View.VISIBLE);
                        Pref.setValue(Pref.TYPE.VEHICLE_DETAILS.toString(), loginresponse.data.toString());
                        Toast.makeText(MyVehicleActivity.this, "Vehicle not found, Please add vehicle details.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MyVehicleActivity.this, AddVehicleActivity.class);
                        intent.putExtra("check", true);
                        startActivity(intent);
                        finish();
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
                        error_message = e.getMessage();
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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




                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<GetAllVehicle> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


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
        Intent intent = new Intent(MyVehicleActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

}
