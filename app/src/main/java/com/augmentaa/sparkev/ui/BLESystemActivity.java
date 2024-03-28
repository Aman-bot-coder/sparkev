package com.augmentaa.sparkev.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import android.widget.ToggleButton;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;


public class BLESystemActivity extends AppCompatActivity {

    ToggleButton tb_main_recovery, tb_rfid, tb_public_mode;
    ImageView img_back;
    String deviceID;
    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blesystem);
        getSupportActionBar().hide();
        tb_main_recovery = findViewById(R.id.tb_main_recovery);
        tb_rfid = findViewById(R.id.tb_rfid);
        tb_public_mode = findViewById(R.id.tb_public_mode);
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