package com.augmentaa.sparkev.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.augmentaa.sparkev.R;

public class BLEAlarmsLogsActivity extends ChargerDetailsActivity {
    ListView listView;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blealarm);
        img_back = findViewById(R.id.back);
        getSupportActionBar().hide();
        try {

            listView = findViewById(R.id.listview);


            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


        } catch (Exception e) {

        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}