package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.Logger;


public class BLEWifiActivity extends AppCompatActivity {
    EditText et_wifi, et_password;
    ImageView img_password, img_back;
    boolean showPassword;
    String deviceID;


    Button btn_submit;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blewifi);
        et_wifi = (EditText) findViewById(R.id.wifi_name);
        et_password = (EditText) findViewById(R.id.wifi_password);
        img_password = (ImageView) findViewById(R.id.eye);
        img_back = (ImageView) findViewById(R.id.back);
        btn_submit = findViewById(R.id.btn_submit);

        getSupportActionBar().hide();


        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Charger data ===" + data.toString());
            deviceID = data.partNo + "#" + data.serialNo;
        } catch (Exception e) {

        }


        img_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword) {
                    showPassword = false;
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    img_password.setImageResource(R.mipmap.ic_eye_close);
                } else {
                    showPassword = true;
                    et_password.setTransformationMethod(null);
                    img_password.setImageResource(R.drawable.ic_eye);

                }
                Editable etext = et_password.getText();
                Selection.setSelection(etext, et_password.getText().toString().length());

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

}