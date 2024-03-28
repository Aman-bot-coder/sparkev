package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentaa.sparkev.R;

public class SuccessActivity extends AppCompatActivity {

    TextView tvSuccess;
    ImageView img_success;
    String activity;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        tvSuccess = findViewById(R.id.success_message);
        img_success = findViewById(R.id.img_success);
        getSupportActionBar().hide();
        tvSuccess.setText("Success!");

        try {
            activity = getIntent().getExtras().getString("activity");
            if (activity != null) {
                if ("REGISTER".equalsIgnoreCase(activity)) {

                } else if ("FORGOT_PASSWORD".equalsIgnoreCase(activity)) {

                }
            } else {

            }

        } catch (Exception e) {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
                if (activity != null) {
                    if ("REGISTER".equalsIgnoreCase(activity)) {
                        mainIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        mainIntent.putExtra("activity", activity);
                        startActivity(mainIntent);
                        finish();
                    } else if ("FORGOT_PASSWORD".equalsIgnoreCase(activity)) {
                        mainIntent = new Intent(getApplicationContext(), UpdateForgotPasswordActivity.class);
                        mainIntent.putExtra("mobile", getIntent().getExtras().getString("mobile"));
                        mainIntent.putExtra("activity", activity);
                        mainIntent.putExtra("user_id", getIntent().getExtras().getInt("user_id"));
                        startActivity(mainIntent);
                        finish();

                    } else {
                        mainIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        mainIntent.putExtra("activity", activity);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    mainIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    mainIntent.putExtra("activity", activity);
                    startActivity(mainIntent);
                    finish();
                }


            }
        }, 3000);
    }
}