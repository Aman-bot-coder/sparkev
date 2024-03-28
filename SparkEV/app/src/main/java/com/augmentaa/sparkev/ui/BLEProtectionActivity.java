package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;

public class BLEProtectionActivity extends AppCompatActivity {
    String deviceID;

    ImageView img_back;
    ToggleButton tb_energy_meter_alarm, tb_earth_disconnect_alarm, tb_ne_voltage_alarm, tc_session_kwh;

    TextView tb_n_e_voltage_cut_Off;

    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleprotection);
        getSupportActionBar().hide();

        tb_energy_meter_alarm = findViewById(R.id.tb_energy_meter_alarm);
        tb_earth_disconnect_alarm = findViewById(R.id.tb_earth_disconnect_alarm);
        tb_ne_voltage_alarm = findViewById(R.id.tb_n_e_voltage_alarm);
        tc_session_kwh = findViewById(R.id.tb_session_kwh);
        tb_n_e_voltage_cut_Off = findViewById(R.id.tb_n_e_voltage_cut_Off);

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