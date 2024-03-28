package com.augmentaa.sparkev.ui;

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

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.ChargerListDetialsAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyChargersActivity extends AppCompatActivity {
    ImageView img_addCharger, img_back;
    ListView listView;
    APIInterface apiInterface;
    APIInterface apiInterfacePP;
    ProgressDialog progress;
    TextView tv_data;
    Button btnAddvCharger;
    ResponseGetChargerList list_chargerdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chargers);
        getSupportActionBar().hide();
        listView = (ListView) findViewById(R.id.listview);
        img_addCharger = (ImageView) findViewById(R.id.img_add_charger);
        img_back = (ImageView) findViewById(R.id.back);
        btnAddvCharger = findViewById(R.id.btn_add_charger);
        this.progress = new ProgressDialog(this);
        tv_data = (TextView) findViewById(R.id.tv_data);

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Utils.isNetworkConnected(this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));
        } else {

            try {

                Gson gson = new Gson();
                String json = gson.toJson(list_chargerdata);
                ResponseGetChargerList list_chargerdata = gson.fromJson(Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(),null), ResponseGetChargerList.class);
                if (list_chargerdata.data.size() > 0) {
                    ChargerListDetialsAdapter chargerAdapter = new ChargerListDetialsAdapter(MyChargersActivity.this, list_chargerdata.data);
                    listView.setAdapter(chargerAdapter);
                    chargerAdapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Intent intent = new Intent(MyChargersActivity.this, ChargerDetailsActivity.class);
                            intent.putExtra("data",list_chargerdata.data.get(position));
                            startActivity(intent);

                        }
                    });
                } else {

                }

//
            } catch (Exception e) {
                progress.dismiss();
                Logger.e("Response" + e.getMessage());
                Toast.makeText(MyChargersActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

            }

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }

        img_addCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("activity","add_charger");
                startActivity(intent);
            }
        });

        btnAddvCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("activity","add_charger");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getChargerList(int userId) {
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
                        Logger.e("Response data " + list_chargerdata.toString());
                        if (list_chargerdata.data.size() > 0) {
                            ChargerListDetialsAdapter chargerAdapter = new ChargerListDetialsAdapter(MyChargersActivity.this, list_chargerdata.data);
                            listView.setAdapter(chargerAdapter);
                            chargerAdapter.notifyDataSetChanged();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Intent intent = new Intent(MyChargersActivity.this, ChargerDetailsActivity.class);
                                    intent.putExtra("data",list_chargerdata.data.get(position));
                                    startActivity(intent);

                                }
                            });
                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   MyChargersActivity.class.getSimpleName(),
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
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               MyChargersActivity.class.getSimpleName(),
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


                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
                        Toast.makeText(MyChargersActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(MyChargersActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           MyChargersActivity.class.getSimpleName(),
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
                Toast.makeText(MyChargersActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       MyChargersActivity.class.getSimpleName(),
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
        Intent intent = new Intent(MyChargersActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

}
