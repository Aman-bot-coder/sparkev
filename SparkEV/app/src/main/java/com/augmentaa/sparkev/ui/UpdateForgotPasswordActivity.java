package com.augmentaa.sparkev.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.update_password.RequestForUpdatePassword;
import com.augmentaa.sparkev.model.signup.update_password.ResponseForUpdatePassword;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateForgotPasswordActivity extends AppCompatActivity {
    APIInterface apiInterface;
    ProgressDialog progress;
    int user_id;
    String mobile;
    String registration_origin;
    String modify_by;

    boolean showPassword = false;
    boolean showCnfPassword = false;
    ImageView img_password, img_cnf_password;
    TextView tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_forgot_password);
        getSupportActionBar().hide();
        user_id = getIntent().getExtras().getInt("user_id");
        mobile = getIntent().getExtras().getString("mobile");
        Logger.e("UserID==" + user_id);
        Logger.e("Mobile==" + mobile);
        img_password = findViewById(R.id.eye);
        img_cnf_password = findViewById(R.id.eye_cnf);
        final EditText txt_password = (EditText) findViewById(R.id.password);
        final EditText txt_confirm_password = (EditText) findViewById(R.id.confirm_password);
        TextView btn_ok = (TextView) findViewById(R.id.btn_submit);
        tvCancel=findViewById(R.id.cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress = new ProgressDialog(this);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(UpdateForgotPasswordActivity.this)) {
                    if (txt_password.getText().toString().length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password should be minimum 6 digit", Toast.LENGTH_LONG).show();
                    } else {
                        if (txt_confirm_password.getText().toString().length() < 6) {
                            Toast.makeText(getApplicationContext(), "Confirm password should be minimum 6 digit", Toast.LENGTH_LONG).show();

                        } else {

                            if ((txt_password.getText().toString().equals(txt_confirm_password.getText().toString()))) {
                                progress.setMessage(getResources().getString(R.string.loading));
                                progress.show();
                                forgotPassword_Confirm(user_id, txt_password.getText().toString(), user_id, "BLE_ANDROID");


                            } else {
                                Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();

                            }
                        }


                    }
//
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

            }
        });


        img_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword) {
                    showPassword = false;
                    txt_password.setTransformationMethod(new PasswordTransformationMethod());
                    img_password.setImageResource(R.mipmap.ic_eye_close);

                } else {

                    showPassword = true;
                    txt_password.setTransformationMethod(null);
                    img_password.setImageResource(R.drawable.ic_eye);

                }
                Editable etext = txt_password.getText();
                Selection.setSelection(etext, txt_password.getText().toString().length());

            }
        });

        img_cnf_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showCnfPassword) {
                    showCnfPassword = false;
                    txt_confirm_password.setTransformationMethod(new PasswordTransformationMethod());
                    img_cnf_password.setImageResource(R.mipmap.ic_eye_close);

                } else {

                    showCnfPassword = true;
                    txt_confirm_password.setTransformationMethod(null);
                    img_cnf_password.setImageResource(R.drawable.ic_eye);

                }

                Editable etext = txt_confirm_password.getText();
                Selection.setSelection(etext, txt_confirm_password.getText().toString().length());
            }
        });

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;


        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();


    }

    private void forgotPassword_Confirm(int id, String password, int modify_by, String registration_origin) {
        this.apiInterface.updatePassword(new RequestForUpdatePassword(id, password, id, registration_origin)).enqueue(new Callback<ResponseForUpdatePassword>() {
            public void onResponse(Call<ResponseForUpdatePassword> call, Response<ResponseForUpdatePassword> response) {

                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForUpdatePassword loginResponse = (ResponseForUpdatePassword) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UpdateForgotPasswordActivity.this, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            try {
                                Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {

                            }


                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseForUpdatePassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });
    }
}
