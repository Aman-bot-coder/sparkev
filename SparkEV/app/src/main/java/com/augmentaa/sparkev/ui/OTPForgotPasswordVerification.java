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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.otp.RequestForOTPVefifyUser;
import com.augmentaa.sparkev.model.signup.otp.RequestForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForVerifyOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForgotVerifyOTP;
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


public class OTPForgotPasswordVerification extends AppCompatActivity {
    private EditText[] editTexts;
    EditText etP1;
    EditText etP2;
    EditText etP3;
    EditText etP4;
    EditText etP5;
    EditText etP6;
    ImageView img_back;
    Button btn_Send;
    String abc;
    TextView tv_timer, tv_sendagain;
    String otp;
    boolean check = false;
    CountDownTimer countDownTimer;
    APIInterface apiInterface;
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
        ly_otp = (LinearLayout) findViewById(R.id.ly_otp);
        tv_timer = (TextView) findViewById(R.id.timer);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
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
        tv_tv_mobile_dec = findViewById(R.id.tv_mobile);
        tv_sendagain = (TextView) findViewById(R.id.send_again);
        img_back = (ImageView) findViewById(R.id.back);
        btn_Send = (Button) findViewById(R.id.btn_send);
//        ly_otp.setVisibility(View.GONE);
        tv_sendagain.setVisibility(View.INVISIBLE);
        startTimer();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Logger.e("activity==" + getIntent().getExtras().getString("activity"));
        try {
            String mobile = getIntent().getExtras().getString("mob");
            tv_tv_mobile_dec.setText("We’ve sent on your phone (**" + mobile.substring(mobile.length() - 2) + ") six-digit code, please enter for verification");
        } catch (Exception e) {
            tv_tv_mobile_dec.setText("We’ve sent on your phone (**85) six-digit code, please enter for verification");

        }

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    abc = "";
                    for (int i = 0; i < editTexts.length; i++) {
                        abc += editTexts[i].getText().toString();
                    }

                    if ("".equalsIgnoreCase(abc)) {
                        Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();

                    } else {
                        if (Utils.isNetworkConnected(OTPForgotPasswordVerification.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            if ("REGISTER".equalsIgnoreCase(getIntent().getExtras().getString("activity"))) {
                                veryfyUser(getIntent().getExtras().getString("username"), getIntent().getExtras().getString("mob"), getIntent().getExtras().getString("email"), abc, getIntent().getExtras().getString("activity"));
                            } else {
                                veryfyOTP("xyz", getIntent().getExtras().getString("mob"), "xyz", abc, getIntent().getExtras().getString("activity"));

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();

                }


            }
        });
        tv_sendagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isNetworkConnected(OTPForgotPasswordVerification.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    reSendOTP(getIntent().getExtras().getString("mob"));

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
//                ly_otp.setVisibility(View.GONE);
                tv_sendagain.setVisibility(View.INVISIBLE);

                tv_timer.setVisibility(View.VISIBLE);


            }

            public void onFinish() {
                tv_timer.setVisibility(View.INVISIBLE);
//                ly_otp.setVisibility(View.VISIBLE);
                tv_sendagain.setVisibility(View.VISIBLE);

//                check = true;
            }
        }.start();

    }


    private void veryfyUser(String username, final String mobile, final String email, String otp, final String activity) {
        this.apiInterface.veryfyUserRegistration(new RequestForOTPVefifyUser("xyz", mobile, otp, "xyz", Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null), Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null), Pref.getValue(Pref.TYPE.FCM_ID.toString(), null))).enqueue(new Callback<ResponseForVerifyOTP>() {
            public void onResponse(Call<ResponseForVerifyOTP> call, Response<ResponseForVerifyOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForVerifyOTP loginResponse = (ResponseForVerifyOTP) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        Logger.e("Mobile==" + response.isSuccessful());

                        if (loginResponse.status) {
                            Toast.makeText(getApplicationContext(), "OTP verify successfully.", Toast.LENGTH_LONG).show();
                            if ("REGISTER".equalsIgnoreCase(activity)) {
                                Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                                intent.putExtra("activity", activity);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("activity", activity);
                                intent.putExtra("user_id", loginResponse.data.id);
                                Logger.e("UserID11==" + loginResponse.data.id);
                                Logger.e("Mobile==" + mobile);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();

                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPForgotPasswordVerification.class.getSimpleName(),
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

//
                        }
//                    return;
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
                               OTPForgotPasswordVerification.class.getSimpleName(),
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
                           OTPForgotPasswordVerification.class.getSimpleName(),
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

            public void onFailure(Call<ResponseForVerifyOTP> call, Throwable t) {
                Logger.e("URL: " + call.request().toString());
                AppUtils.bodyToString(call.request().body());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                Logger.e("Exception " + t.getMessage());
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPForgotPasswordVerification.class.getSimpleName(),
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


    private void veryfyOTP(String username, final String mobile, final String email, String otp, final String activity) {
        this.apiInterface.verifyOTPNew(new RequestForOTPVefifyUser(username, mobile, email, otp, activity)).enqueue(new Callback<ResponseForgotVerifyOTP>() {
            public void onResponse(Call<ResponseForgotVerifyOTP> call, Response<ResponseForgotVerifyOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForgotVerifyOTP loginResponse = (ResponseForgotVerifyOTP) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        Logger.e("Mobile==" + response.isSuccessful());

                        if (loginResponse.status) {
                            Toast.makeText(getApplicationContext(), "OTP verify successfully.", Toast.LENGTH_LONG).show();
                            if (activity.equalsIgnoreCase("REGISTER")) {
                                Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                                intent.putExtra("activity", activity);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("activity", activity);
                                intent.putExtra("user_id", loginResponse.data.get(0).user_id);
                                Logger.e("UserID11==" + loginResponse.data.get(0).user_id);
                                Logger.e("Mobile==" + mobile);
                                startActivity(intent);
                                finish();
                            }


                        } else {

                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPForgotPasswordVerification.class.getSimpleName(),
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

                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
                        }
//                    return;
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
                               OTPForgotPasswordVerification.class.getSimpleName(),
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
                           OTPForgotPasswordVerification.class.getSimpleName(),
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

            public void onFailure(Call<ResponseForgotVerifyOTP> call, Throwable t) {
                Logger.e("Exeption==" + t.getMessage());
                AppUtils.bodyToString(call.request().body());
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
                       OTPForgotPasswordVerification.class.getSimpleName(),
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

    private void reSendOTP(String mobile) {
        this.apiInterface.reSendOTP(new RequestForResendOTP("", mobile, "", "", "RESEND_" + getIntent().getExtras().getString("activity"))).enqueue(new Callback<ResponseForResendOTP>() {
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
//                            ly_otp.setVisibility(View.GONE);
                            tv_sendagain.setVisibility(View.INVISIBLE);

                            startTimer();
                            Toast.makeText(getApplicationContext(), "OTP sent successfully.", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   OTPForgotPasswordVerification.class.getSimpleName(),
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
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               OTPForgotPasswordVerification.class.getSimpleName(),
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
                           OTPForgotPasswordVerification.class.getSimpleName(),
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


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       OTPForgotPasswordVerification.class.getSimpleName(),
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
        Intent intent = new Intent(OTPForgotPasswordVerification.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }
}
