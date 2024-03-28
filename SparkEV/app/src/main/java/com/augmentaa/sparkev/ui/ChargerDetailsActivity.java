package com.augmentaa.sparkev.ui;


import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.services.MyService;

import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;


public class ChargerDetailsActivity extends AppCompatActivity {

    TextView tv_shared, tv_warranty_plan, tv_software, tv_session_history, tv_wifi_config, tv_info, tv_system, tv_server,
            tv_protection, tv_ac_input, tv_live, tv_log, tv_alarm, tv_charger_name, tv_charger_serial_number;
    Button btn_delete_charger;
    Data data;

    ImageView img_back;

    String deviceID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_details);
        tv_shared = findViewById(R.id.shared);
        tv_warranty_plan = findViewById(R.id.warranty_plan);
        tv_software = findViewById(R.id.software);
        tv_session_history = findViewById(R.id.session_history);
        tv_wifi_config = findViewById(R.id.wifi_config);
        tv_info = findViewById(R.id.info);
        tv_server = findViewById(R.id.server);
        tv_system = findViewById(R.id.system);
        tv_protection = findViewById(R.id.protection);
        tv_ac_input = findViewById(R.id.ac_input);
        tv_live = findViewById(R.id.live);
        tv_log = findViewById(R.id.logs);
        tv_alarm = findViewById(R.id.alarm);
        btn_delete_charger = findViewById(R.id.btn_delete_charger);
        tv_charger_name = findViewById(R.id.charger_name);
        tv_charger_serial_number = findViewById(R.id.charger_serial_number);
        img_back = findViewById(R.id.back);
        getSupportActionBar().hide();



        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Charger data ===" + data.toString());
            tv_charger_name.setText(data.nickName);
            tv_charger_serial_number.setText(data.partNo + "#" + data.serialNo);
            deviceID = data.partNo + "#" + data.serialNo;
        } catch (Exception e) {
            callMailService(
                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                    ChargerDetailsActivity.class.getSimpleName(),
                    "SPIN_ANDROID",
                    AppUtils.project_id,
                    e.getMessage(),
                    "ANDROID",
                    "NO",
                    "Y",
                    AppUtils.DATA_NOT_FOUND_CODE,
                    AppUtils.DATA_NOT_FOUND,
                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null));



        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ChargerDetailsActivity.this)) {
                    Intent intent = new Intent(ChargerDetailsActivity.this, ChargerSharingActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ChargerDetailsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }



            }
        });


        tv_warranty_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ChargerDetailsActivity.this)) {
                    Intent intent = new Intent(ChargerDetailsActivity.this, WarrantyActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ChargerDetailsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });


        tv_session_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ChargerDetailsActivity.this)) {
                    Intent intent = new Intent(ChargerDetailsActivity.this, SessionHistoryActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ChargerDetailsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

            }
        });

        tv_wifi_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEWifiActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);

            }
        });
        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEInfoActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        tv_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLESystemActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        tv_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEServerActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
        tv_protection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEProtectionActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
        tv_ac_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEACInputActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
        tv_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLELiveDataActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });
        tv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, Session_logs.class);
                intent.putExtra("data", data);
                startActivity(intent);


            }
        });
        tv_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargerDetailsActivity.this, BLEAlarmsLogsActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });



    }


    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ChargerDetailsActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

}