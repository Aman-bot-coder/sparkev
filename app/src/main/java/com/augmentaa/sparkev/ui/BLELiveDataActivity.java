package com.augmentaa.sparkev.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;
public class BLELiveDataActivity extends AppCompatActivity {
    ImageView img_back;
    TextView tv_ac_voltage, tv_ac_current, tv_co2_saved, tv_session_kwh, tv_session_time,
            tv_com_time, tv_com_kwh, tv_ne_voltage, tv_erath_leakage_current, connector_status;

    Data data;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blelive_data);
        getSupportActionBar().hide();
        tv_ac_voltage = findViewById(R.id.ac_voltage);
        tv_ac_current = findViewById(R.id.ac_current);
        tv_co2_saved = findViewById(R.id.co2_save);
        tv_session_kwh = findViewById(R.id.session_kwh);
        tv_session_time = findViewById(R.id.sesstion_time);
        tv_com_time = findViewById(R.id.cumulative_time);
        tv_com_kwh = findViewById(R.id.cumulative_kwh);
        tv_ne_voltage = findViewById(R.id.ne_voltage);
        tv_erath_leakage_current = findViewById(R.id.earth_leakage_current);
        connector_status = findViewById(R.id.connetor_status);
        img_back = findViewById(R.id.back);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
//            Data data;
            data = getIntent().getParcelableExtra("data");
            Logger.e("Charger data ===" + data.toString());
            deviceID = data.partNo + "#" + data.serialNo;
        } catch (Exception e) {

        }



    }


}