package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.project_update.ProjectUpdate;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashTwoActivity extends AppCompatActivity {

    Button btnCreateAccount;
    TextView tvSignIn;

    Dialog dialog_verify_user;
    APIInterface apiInterface;
    ProgressDialog progress;
    String version_name;
    int version_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_two);
        getSupportActionBar().hide();
        btnCreateAccount = findViewById(R.id.create_account);
        tvSignIn = findViewById(R.id.signIn);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.progress = new ProgressDialog(this);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashTwoActivity.this, CreateAccountNameEmailActivity.class);
                startActivity(intent);
            }
        });
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
            version_code = pInfo.versionCode;
            Logger.e("Version Name  " + version_name + "  " + version_code);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Utils.isNetworkConnected(SplashTwoActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            progress.setCancelable(false);
            getAppUpdate();
        }

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SplashTwoActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    void dialog_app_update(String url, String update_type) {
        dialog_verify_user = new Dialog(SplashTwoActivity.this);
        dialog_verify_user.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_verify_user.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_verify_user.setCancelable(false);
        TextView btn_ok = (TextView) dialog_verify_user.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_verify_user.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_verify_user.findViewById(R.id.tv_message);
        tv_message.setText("New version available.                                    ");
        btn_ok.setText("Update");
        btn_cencel.setText("Skip");
        if ("FORCE".equalsIgnoreCase(update_type)) {
            btn_cencel.setVisibility(View.GONE);

        } else {
            btn_cencel.setVisibility(View.VISIBLE);
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dialog_verify_user.dismiss();
                finish();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_verify_user.dismiss();

            }
        });
        dialog_verify_user.show();


    }

    private void getAppUpdate() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ProjectUpdate> call = apiInterface.getLatestProjectVersion(4, "ANDROID");
        call.enqueue(new Callback<ProjectUpdate>() {
            @Override
            public void onResponse(Call<ProjectUpdate> call, Response<ProjectUpdate> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ProjectUpdate response_body = (ProjectUpdate) response.body();
                        if (response_body.status) {
                            if(version_code<response_body.data.get(0).newVersionCode) {
                                dialog_app_update(response_body.data.get(0).redirectionUrl, response_body.data.get(0).updateType);
                            }

                        }
                        Logger.e("Main Screen" + response_body.toString());
                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());


                    }
                } else {
                    progress.dismiss();


                }

            }

            @Override
            public void onFailure(Call<ProjectUpdate> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());


            }
        });

    }
}