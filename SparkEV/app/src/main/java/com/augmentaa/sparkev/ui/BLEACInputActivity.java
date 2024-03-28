package com.augmentaa.sparkev.ui;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;

import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
public class BLEACInputActivity extends AppCompatActivity {

    TextView tv_low_voltage_cut_off;
    TextView tv_high_voltage_cut_off;
    TextView tv_low_voltage_cut_in;
    TextView tv_high_voltage_cut_in;
    TextView tv_related_current;
    TextView tv_maximum_output_current;
    TextView tv_minimum_output_current;

    ImageView img_back;


    String deviceID;

    Data data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleacinput);
        getSupportActionBar().hide();
        tv_low_voltage_cut_off = findViewById(R.id.low_voltage_cut_off);
        tv_high_voltage_cut_off = findViewById(R.id.high_voltage_cut_off);
        tv_low_voltage_cut_in = findViewById(R.id.low_voltage_cut_in);
        tv_high_voltage_cut_in = findViewById(R.id.high_voltage_cut_in);
        tv_related_current = findViewById(R.id.related_current);
        tv_maximum_output_current = findViewById(R.id.maximum_output_current);
        tv_minimum_output_current = findViewById(R.id.minimum_output_current);
        img_back = findViewById(R.id.back);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLEACInputActivity.this.finish();
            }
        });

        try {
            data = getIntent().getParcelableExtra("data");
            deviceID = data.partNo + "#" + data.serialNo;
        } catch (Exception e) {

        }




    }

}