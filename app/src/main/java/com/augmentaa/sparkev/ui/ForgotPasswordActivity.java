package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetCountryForgotPasswordListAdapter;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.otp.RequestForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForResendOTP;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etMobile;
    TextView etLocation;
    Button btnContinue;
    ImageView img_back;
    String email, name, location, mobile;
    APIInterface apiInterface;
    ProgressDialog progress;
    ResponseCountry responseCountry;
    GetCountryForgotPasswordListAdapter adapter;
    int country_id;
    ListView listView;
    Dialog dialogCountry;
    EditText etSearch;
    List<Data> list_search;
    public static ResponseCountry country;
    String activity = null;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etMobile = findViewById(R.id.mobile);
        etLocation = findViewById(R.id.location);
        btnContinue = findViewById(R.id.create_account);
        this.progress = new ProgressDialog(this);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        getSupportActionBar().hide();
        list_search = new ArrayList<>();
        country = new ResponseCountry();

        try {
            activity = getIntent().getExtras().getString("activity");
            if (activity != null) {
                if ("REGISTER".equalsIgnoreCase(activity)) {
                    tvForgotPassword.setText("Create your profile");
                } else {
                    tvForgotPassword.setText("Forgot password");
                }
            } else {
                tvForgotPassword.setText("Forgot password");

            }

        } catch (Exception e) {
            tvForgotPassword.setText("Forgot password");


        }
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ForgotPasswordActivity.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getAllCountryList();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
//

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = etMobile.getText().toString().trim();
                location = etLocation.getText().toString().trim();
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your country.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your mobile.", Toast.LENGTH_LONG).show();

                } else {
                    if (activity != null) {
                        if ("REGISTER".equalsIgnoreCase(activity)) {
                            forgotPasswordInput(mobile, "REGISTER");

                        } else {
                            forgotPasswordInput(mobile, "FORGOT_PASSWORD");

                        }

                    } else {
                        forgotPasswordInput(mobile, "FORGOT_PASSWORD");

                    }

                }

            }
        });

    }

    public void getAllCountryList() {
        Call<ResponseCountry> call = apiInterface.getAllCountryList();
        call.enqueue(new Callback<ResponseCountry>() {
            @Override
            public void onResponse(Call<ResponseCountry> call, Response<ResponseCountry> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                responseCountry = (ResponseCountry) response.body();
                if (response.code() == 200) {
                    try {
                        if (responseCountry.status) {
                            if (responseCountry.data.size() > 0) {
                                dialogCountry();
                            }


                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseCountry> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }

    void dialogCountry() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);

        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        listView = dialog_Distance.findViewById(R.id.listview);
        etSearch = dialog_Distance.findViewById(R.id.search);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (responseCountry.data.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            adapter = new GetCountryForgotPasswordListAdapter(ForgotPasswordActivity.this, responseCountry.data);
            listView.setAdapter(adapter);
//            country.setData(responseCountry.data);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.notifyDataSetChanged();
//                    Logger.e("======Data list=itemClick="+country.getData().toString()+ "       "+country.getData().size());
                    country_id = country.getData().get(i).id;
                    etLocation.setText(country.getData().get(i).name);
                    dialog_Distance.dismiss();
                }
            });
        } else {
            dialog_Distance.dismiss();
            Toast.makeText(ForgotPasswordActivity.this, "Country not found", Toast.LENGTH_LONG).show();
        }
        dialog_Distance.show();


    }


    private void forgotPasswordInput(final String mobile, final String activity) {
        this.apiInterface.reSendOTP(new RequestForResendOTP("", mobile, "", "", activity)).enqueue(new Callback<ResponseForResendOTP>() {
            public void onResponse(Call<ResponseForResendOTP> call, Response<ResponseForResendOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                ResponseForResendOTP loginResponse = (ResponseForResendOTP) response.body();
                Logger.e("Response: " + loginResponse.toString());
                if (response.code() == 200) {
                    try {

                        if (loginResponse.status) {
                            Toast.makeText(getApplicationContext(), "OTP sent successfully.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OTPForgotPasswordVerification.class);
                            intent.putExtra("email", "xyz");
                            intent.putExtra("mob", mobile);
                            intent.putExtra("username", "xyz");
                            intent.putExtra("activity", activity);
                            startActivity(intent);
                            finish();
                        } else {
                            callMailService(
                                    0,
                                    email
                                    , email,
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   ForgotPasswordActivity.class.getSimpleName(),
                                    "SPIN_ANDROID",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    0,
                                    mobile);
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();


                        }
                    } catch (Exception e) {
                        callMailService(
                                0,
                                email
                                , email,
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               ForgotPasswordActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                               e.getMessage(),
                                0,
                                mobile);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            0,
                            email
                            , email,
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           ForgotPasswordActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            0,
                            mobile);
                }
            }

            public void onFailure(Call<ResponseForResendOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        0,
                        email
                        , email,
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       ForgotPasswordActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        0,
                        mobile);
            }
        });
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ForgotPasswordActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }



}