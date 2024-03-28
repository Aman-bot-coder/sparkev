package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;

public class BLEServerActivity extends AppCompatActivity {


    TextView tv_server_ip, tv_server_path, tv_charger_id, tv_server_port;
    ImageView img_back;
    String deviceID;

    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleserver);
        getSupportActionBar().hide();
        tv_server_ip = findViewById(R.id.server_ip);
        tv_server_path = findViewById(R.id.server_path);
        tv_server_port = findViewById(R.id.server_port);
        tv_charger_id = findViewById(R.id.charger_id);
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
            tv_charger_id.setText(data.partNo + "#" + data.serialNo);

        } catch (Exception e) {

        }

    }

}