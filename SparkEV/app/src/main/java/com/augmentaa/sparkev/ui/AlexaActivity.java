package com.augmentaa.sparkev.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.Utils;

public class AlexaActivity extends AppCompatActivity {

    LinearLayout ly_charger_status_dec, ly_start_charger_dec, ly_stop_charger_dec;

    TextView tv_charger_dec;
    TextView tv_charger, tv_charger_status, tv_stop_charger, tv_start_charger;

    boolean isCheckedcharger, isCheckedCharger_status, isCheckedStop_charger, isCheckedStart_charger;
    ImageView img_back;
    Button btn_connectAlexa;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexa);
        getSupportActionBar().hide();
        btn_connectAlexa=findViewById(R.id.btn_connect_alexa);
        img_back = findViewById(R.id.back);


        tv_charger = findViewById(R.id.charger_name);
        tv_charger_dec = findViewById(R.id.what_is_alexa_des);

        tv_charger_status = findViewById(R.id.tv_charging_status);
        ly_charger_status_dec = findViewById(R.id.ly_status_charging_dec);


        tv_start_charger = findViewById(R.id.tv_start_charger);
        ly_start_charger_dec = findViewById(R.id.ly_start_charging_dec);

        tv_stop_charger = findViewById(R.id.tv_stop_charging);
        ly_stop_charger_dec = findViewById(R.id.ly_stop_charging_dec);


        tv_charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isCheckedcharger) {
                    isCheckedcharger = true;
                    tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);
                    tv_charger_dec.setVisibility(View.VISIBLE);

                } else {
                    isCheckedcharger = false;
                    tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);
                    tv_charger_dec.setVisibility(View.GONE);

                }


            }
        });


        tv_start_charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedStart_charger) {
                    isCheckedStart_charger = true;
                    ly_start_charger_dec.setVisibility(View.VISIBLE);
                    tv_start_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedStart_charger = false;
                    ly_start_charger_dec.setVisibility(View.GONE);
                    tv_start_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });

        tv_stop_charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedStop_charger) {
                    isCheckedStop_charger = true;
                    ly_stop_charger_dec.setVisibility(View.VISIBLE);
                    tv_stop_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedStop_charger = false;
                    ly_stop_charger_dec.setVisibility(View.GONE);
                    tv_stop_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });

        tv_charger_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedCharger_status) {
                    isCheckedCharger_status = true;
                    ly_charger_status_dec.setVisibility(View.VISIBLE);
                    tv_charger_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedCharger_status = false;
                    ly_charger_status_dec.setVisibility(View.GONE);
                    tv_charger_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_connectAlexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  https://play.google.com/store/apps/details?id=com.amazon.dee.app&hl=en_IN&gl=US


                if (Utils.isNetworkConnected(AlexaActivity.this)) {
                    final String appPackageName = "com.amazon.dee.app"; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else {
                    Toast.makeText(AlexaActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }




            }
        });

    }


}