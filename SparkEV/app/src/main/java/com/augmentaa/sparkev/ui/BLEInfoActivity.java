package com.augmentaa.sparkev.ui;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;
public class BLEInfoActivity extends AppCompatActivity {


    TextView tv_system_part_number;
    TextView tv_card_part_number;
    TextView tv_card_serial_number;
    TextView tv_system_serial_number;
    TextView tv_app_version;
    ImageView img_back;

    String deviceID;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleinfo);
        img_back = findViewById(R.id.back);
        getSupportActionBar().hide();
        tv_app_version = findViewById(R.id.app_version);
        tv_system_part_number = findViewById(R.id.system_part_number);
        tv_card_part_number = findViewById(R.id.card_part_number);
        tv_card_serial_number = findViewById(R.id.card_serial_number);
        tv_system_serial_number = findViewById(R.id.system_serial_number);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Charger data ===" + data.toString());
            deviceID = data.partNo + "#" + data.serialNo;
        } catch (Exception e) {
            Logger.e("Charger Expe" + data.toString());
        }


    }



}