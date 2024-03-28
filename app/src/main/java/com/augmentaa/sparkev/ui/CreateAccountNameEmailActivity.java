package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.mobile_validation.RequestMobileNumber;
import com.augmentaa.sparkev.model.signup.mobile_validation.ResponseMobileNumber;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountNameEmailActivity extends AppCompatActivity {

    EditText etEmail, etName;
    Button btnContinue;
    String email, name;
    TextView tvSignIn;
    APIInterface apiInterface;
    APIInterface apiSpinInterface;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_name_email);
        etEmail = findViewById(R.id.email);
        etName = findViewById(R.id.name);
        btnContinue = findViewById(R.id.create_account);
        tvSignIn = findViewById(R.id.signIn);
        getSupportActionBar().hide();
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress=new ProgressDialog(this);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateAccountNameEmailActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString().trim();
                name = etName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateAccountNameEmailActivity.this, "Please enter your name.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(CreateAccountNameEmailActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();

                } else if (!AppUtils.emailValidator(email)) {
                    Toast.makeText(CreateAccountNameEmailActivity.this, "Invalid email id.", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(CreateAccountNameEmailActivity.this, CreateAccountLocationPhoneActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    /*if (Utils.isNetworkConnected(CreateAccountNameEmailActivity.this)) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        checkEmail();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }*/
//                    Toast.makeText(CreateAccountNameEmailActivity.this, "Valid Email.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void checkEmail() {
        this.apiInterface.checkMobileEmail(new RequestMobileNumber(email, null, "EMAIL")).enqueue(new Callback<ResponseMobileNumber>() {
            public void onResponse(Call<ResponseMobileNumber> call, Response<ResponseMobileNumber> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                ResponseMobileNumber loginResponse = (ResponseMobileNumber) response.body();
                if (response.code() == 200) {
                    try {
                        Logger.e("Response:  " + loginResponse.toString());
                        if (loginResponse.status) {
                            Intent intent = new Intent(CreateAccountNameEmailActivity.this, CreateAccountLocationPhoneActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();

                        }

//                    return;
//                        } else {
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
//                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseMobileNumber> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();


            }
        });

    }
}