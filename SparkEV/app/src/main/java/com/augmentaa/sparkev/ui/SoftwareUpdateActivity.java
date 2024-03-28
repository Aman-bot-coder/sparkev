package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.augmentaa.sparkev.R;

public class SoftwareUpdateActivity extends AppCompatActivity {

    ImageView img_back;

    LinearLayout ly_current_version, ly_new_version;
    TextView tv_available_to_install, latest_version, tv_current_version, tv_new_version;
    Button btn_submit;
    ProgressBar  mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_update);
        ly_current_version = findViewById(R.id.ly_current_version);
        ly_new_version = findViewById(R.id.ly_new_version);
        tv_available_to_install = findViewById(R.id.available_to_install);
        latest_version = findViewById(R.id.tv_latest_version);
        tv_current_version = findViewById(R.id.current_version);
        tv_new_version = findViewById(R.id.new_version);
        btn_submit = findViewById(R.id.btn_submit);
        img_back = findViewById(R.id.back);
//        mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);

        getSupportActionBar().hide();
        btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_unselected_btn));
        btn_submit.setTextColor(getResources().getColor(R.color.colorTextLight));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
//        anim.setDuration(15000);
//        anim.setInterpolator(new DecelerateInterpolator());
//        anim.start();





    }



}