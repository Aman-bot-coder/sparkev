package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.Pref;

public class SplashActivity extends AppCompatActivity {

    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        img_splash = findViewById(R.id.splash);
        Glide.with(this).load(R.mipmap.splash).into(img_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Pref.getBoolValue(Pref.TYPE.LOGIN.toString(), false)) {
//                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(getApplicationContext(), SplashTwoActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }


            }
        }, 2200);
    }
}