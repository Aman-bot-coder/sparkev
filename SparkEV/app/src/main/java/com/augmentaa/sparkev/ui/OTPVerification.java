package com.augmentaa.sparkev.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.login.RequestForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLoginBLE;
import com.augmentaa.sparkev.model.signup.otp.RequestForOTPVefifyUser;
import com.augmentaa.sparkev.model.signup.otp.RequestForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForVerifyOTP;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OTPVerification extends AppCompatActivity {
    private EditText[] editTexts;
    EditText etP1;
    EditText etP2;
    EditText etP3;
    EditText etP4;
    EditText etP5;
    EditText etP6;
    //    ImageView img_back;
    Button btn_Send;
    String abc;
    TextView tv_timer, tv_sendagain;
    String otp;
    boolean check = false;
    CountDownTimer countDownTimer;
    APIInterface apiInterface, spin_apiInterface;
    ProgressDialog progress;
    LinearLayout ly_otp;
    String error_message;
    TextView tv_tv_mobile_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        getSupportActionBar().hide();
//        otp = getIntent().getExtras().getString("otp");
        etP1 = (EditText) findViewById(R.id.p1);
        etP2 = (EditText) findViewById(R.id.p2);
        etP3 = (EditText) findViewById(R.id.p3);
        etP4 = (EditText) findViewById(R.id.p4);
        etP5 = (EditText) findViewById(R.id.p5);
        etP6 = (EditText) findViewById(R.id.p6);
        tv_timer = (TextView) findViewById(R.id.timer);
        ly_otp = (LinearLayout) findViewById(R.id.ly_otp);
        tv_tv_mobile_dec = findViewById(R.id.tv_mobile);

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        spin_apiInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);

        editTexts = new EditText[]{etP1, etP2, etP3, etP4, etP5, etP6};
        etP1.addTextChangedListener(new PinTextWatcher(0));
        etP2.addTextChangedListener(new PinTextWatcher(1));
        etP3.addTextChangedListener(new PinTextWatcher(2));
        etP4.addTextChangedListener(new PinTextWatcher(3));
        etP5.addTextChangedListener(new PinTextWatcher(4));
        etP6.addTextChangedListener(new PinTextWatcher(5));
        etP1.setOnKeyListener(new PinOnKeyListener(0));
        etP2.setOnKeyListener(new PinOnKeyListener(1));
        etP3.setOnKeyListener(new PinOnKeyListener(2));
        etP4.setOnKeyListener(new PinOnKeyListener(3));
        etP5.setOnKeyListener(new PinOnKeyListener(4));
        etP6.setOnKeyListener(new PinOnKeyListener(5));
        this.progress = new ProgressDialog(this);
        tv_sendagain = (TextView) findViewById(R.id.send_again);
//        img_back = (ImageView) findViewById(R.id.back);
        btn_Send = (Button) findViewById(R.id.btn_send);
        startTimer();
     /*   img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        try {
            String mobile = getIntent().getExtras().getString("mob");
            tv_tv_mobile_dec.setText("We’ve sent on your phone (**" + mobile.substring(8) + ") six-digit code, please enter for verification");
        } catch (Exception e) {

        }
        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    abc = "";
                    for (int i = 0; i < editTexts.length; i++) {
                        abc += editTexts[i].getText().toString();
                    }
                    if (Utils.isNetworkConnected(OTPVerification.this)) {

                        if ("".equalsIgnoreCase(abc)) {
                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();

                        } else {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            veryfyUser(getIntent().getExtras().getString("username"), getIntent().getExtras().getString("mob"), getIntent().getExtras().getString("email"), abc);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();

                }

            }
        });
        tv_sendagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(OTPVerification.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    reSendOTP(getIntent().getExtras().getString("username"), getIntent().getExtras().getString("mob"), getIntent().getExtras().getString("email"), abc);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();

        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);


            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
            editTexts[currentIndex].setBackground(getResources().getDrawable(R.drawable.otp_selected));
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
            editTexts[currentIndex].setBackground(getResources().getDrawable(R.drawable.otp_unselected));
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
//                calculateTime(120);

//                tv_timer.setText("" + millisUntilFinished / 1000);

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
//                tv_timer.setText(getString(R.string.you_will_recieve_otp_two_minutes) + "  " + String.format("%02d", minutes)
//                        + ":" + String.format("%02d", seconds));

                tv_timer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
//                check = false;
                ly_otp.setVisibility(View.GONE);
                tv_timer.setVisibility(View.VISIBLE);


            }

            public void onFinish() {
                tv_timer.setVisibility(View.INVISIBLE);
                ly_otp.setVisibility(View.VISIBLE);
//                check = true;
            }
        }.start();

    }


    private void veryfyUser(String username, final String mobile, final String email, String otp) {
        this.apiInterface.veryfyUserRegistration(new RequestForOTPVefifyUser(email, mobile, otp, username, Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null), Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null), Pref.getValue(Pref.TYPE.FCM_ID.toString(), null))).enqueue(new Callback<ResponseForVerifyOTP>() {
            public void onResponse(Call<ResponseForVerifyOTP> call, Response<ResponseForVerifyOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                ResponseForVerifyOTP loginResponse = (ResponseForVerifyOTP) response.body();
                Logger.e("Response: " + loginResponse.toString());
//                Logger.e("Response: " + loginResponse.data.user.email+ "  " + loginResponse.data.user.mobile + "  " + loginResponse.data.user.id + "    " + loginResponse.data.user.jwtToken);

                if (response.code() == 200) {
                    try {

                        Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                        if (loginResponse.getStatus()) {
                            postLoginData_spin(Pref.getValue(Pref.TYPE.S_EMAIL.toString(), null), Pref.getValue(Pref.TYPE.PASSWORD.toString(), null));


                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPVerification.class.getSimpleName(),
                                    "SPIN_ANDROID",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                        }
//                    return;
//                        } else {
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
//                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPVerification.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
                } else {
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           OTPVerification.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
//
                }


            }

            public void onFailure(Call<ResponseForVerifyOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPVerification.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }


    private void reSendOTP(String username, final String mobile, final String email, String otp) {
        this.apiInterface.reSendOTP(new RequestForResendOTP(username, mobile, email, otp, "RESEND_REGISTER")).enqueue(new Callback<ResponseForResendOTP>() {
            public void onResponse(Call<ResponseForResendOTP> call, Response<ResponseForResendOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {

                        ResponseForResendOTP loginResponse = (ResponseForResendOTP) response.body();
                        if (loginResponse.status) {
                            Logger.e("Response: " + loginResponse.toString());
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                            ly_otp.setVisibility(View.GONE);
                            startTimer();
                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPVerification.class.getSimpleName(),
                                    "SPIN_ANDROID",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                        }
                        Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();

//                    return;
//                        } else {
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
//                        }
                    } catch (Exception e) {

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPVerification.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           OTPVerification.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                }


            }

            public void onFailure(Call<ResponseForResendOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                error_message = t.getMessage();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPVerification.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


            }
        });

    }

    private void postLoginData(int user_id) {
        this.apiInterface.loginBLE(new RequestForLogin(user_id)).enqueue(new Callback<ResponseForLoginBLE>() {
            //        this.apiInterface.PostSignIn(new LoginData(user_name, password)).enqueue(new Callback<LoginData>() {
            public void onResponse(Call<ResponseForLoginBLE> call, Response<ResponseForLoginBLE> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForLoginBLE responseForLoginBLE = (ResponseForLoginBLE) response.body();
                        Logger.e("Response: " + responseForLoginBLE.toString());
                        if (responseForLoginBLE.status) {
                            Pref.setValue(Pref.TYPE.EMAIL.toString(), responseForLoginBLE.email);
                            Pref.setValue(Pref.TYPE.F_NAME.toString(), responseForLoginBLE.f_Name);
                            Pref.setValue(Pref.TYPE.L_NAME.toString(), responseForLoginBLE.l_Name);
                            Pref.setValue(Pref.TYPE.MOBILE.toString(), responseForLoginBLE.mobile);
                            Pref.setIntValue(Pref.TYPE.USER_ID.toString(), responseForLoginBLE.id);
                            Pref.setIntValue(Pref.TYPE.PROJECT_ID.toString(), AppUtils.project_id);
                            Pref.setValue(Pref.TYPE.TOKEN.toString(), "Bearer " + responseForLoginBLE.token);
                            Pref.setValue(Pref.TYPE.ORIGIN.toString(), "BLE_ANDROID");
                            Pref.setValue(Pref.TYPE.DEVICE_ID.toString(), Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null));
                            Pref.setValue(Pref.TYPE.APP_VERSION.toString(), Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null));
                            Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);
                            Pref.setValue(Pref.TYPE.USER_ROLE_CODE.toString(), responseForLoginBLE.role_code);
                            Pref.setValue(Pref.TYPE.USER_ROLE_ID.toString(), responseForLoginBLE.role_id);
                            Pref.setValue(Pref.TYPE.USER_ROLE_NAME.toString(), responseForLoginBLE.role_name);
                            Intent intent = new Intent(getApplicationContext(), AddVehicleActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_successfully), Toast.LENGTH_LONG).show();


//                        return;
                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPVerification.class.getSimpleName(),
                                    "SPIN_ANDROID",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPVerification.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           OTPVerification.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                }
            }

            public void onFailure(Call<ResponseForLoginBLE> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPVerification.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    private void postLoginData_spin(final String user_name, final String password) {
        this.spin_apiInterface.PostSignIn(new RequestForLogin(user_name, password, "1.0", Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null), "Normal", "12212121", 4, 0.0, 0.0, "SPIN_ANDROID")).enqueue(new Callback<ResponseForLogin>() {
            //        this.apiInterface.PostSignIn(new LoginData(user_name, password)).enqueue(new Callback<LoginData>() {
            public void onResponse(Call<ResponseForLogin> call, Response<ResponseForLogin> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                    ResponseForLogin loginResponse = (ResponseForLogin) response.body();
                    Logger.e("Response: " + loginResponse.toString());
                    if (loginResponse.status == 0) {
                        postLoginData(loginResponse.user.id);

                        Pref.setValue(Pref.TYPE.S_EMAIL.toString(), loginResponse.user.email);
//                            Pref.setValue(Pref.TYPE.S_F_NAME.toString(), loginResponse.data);
//                            Pref.setValue(Pref.TYPE.S_L_NAME.toString(), loginResponse.l_Name);
                        Pref.setValue(Pref.TYPE.S_MOBILE.toString(), loginResponse.user.mobile);
                        Pref.setIntValue(Pref.TYPE.S_USER_ID.toString(), loginResponse.user.id);
                        Pref.setValue(Pref.TYPE.S_TOKEN.toString(), "Bearer " + loginResponse.user.jwtToken);


//                        return;
                    } else {
                           /* if (loginResponse.message.equalsIgnoreCase("User not verified")) {
                                AppUtils.showDefaultDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.user_verified),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialog_verifyUser();

                                            }
                                        }, null);


                            } else {
                                try {
                                    Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    error_message = e.getMessage();
                                }
                            }*/

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPVerification.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                AppUtils.DATA_NOT_FOUND,
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPVerification.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                AppUtils.DATA_NOT_FOUND,
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                }  else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseForLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPVerification.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            }
        });

    }
    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(OTPVerification.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


}
