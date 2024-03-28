package com.augmentaa.sparkev.ui;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.model.signup.login.RequestForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLoginBLE;
import com.augmentaa.sparkev.model.signup.otp.RequestForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForResendOTP;
import com.augmentaa.sparkev.model.signup.project_update.ProjectUpdate;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    EditText et_mobile, et_password;
    Button btn_signIn;
    APIInterface apiInterface, spin_apiInterface, apiInterfacePP;
    ProgressDialog progress;
    String device_unique_id, IMEI;
    String android_os_name;
    LinearLayout btn_signup;
    int os_version;
    String version_name;
    int version_code;
    String origin;
    String mobile, password;
    TextView btn_forgotPassword;
    String req_id;
    Dialog dialog_confirmPassword, dialog_passwordInput;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    ImageView img_password;
    boolean showPassword = false;
    TextView tvCreateAccount;
    String fcm_id;
    CheckBox cb_login;
    boolean check_login = false;
    ResponseForLoginBLE responseForLoginBLE;
    Dialog dialog_verify_user;
    ArrayList<String> permissionsList;
    String[] permissionsStr;
    int permissionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide(); //hide the title bar
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        spin_apiInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);
        apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        et_mobile = (EditText) findViewById(R.id.mobile);
        et_password = (EditText) findViewById(R.id.password);
        btn_signIn = (Button) findViewById(R.id.create_account);
        tvCreateAccount = findViewById(R.id.signIn);
        btn_forgotPassword = (TextView) findViewById(R.id.forgot_password);
        img_password = (ImageView) findViewById(R.id.eye);
        cb_login = findViewById(R.id.cb_login);
        this.progress = new ProgressDialog(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsStr = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_SCAN,

            };


        } else {
            permissionsStr = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,


            };
        }
        FirebaseApp.initializeApp(this);
//        loadIMEI();
        getAndroidOsVersion();
        getFCMToken();

        permissionsList = new ArrayList<>();

        permissionsList.addAll(Arrays.asList(permissionsStr));
        askForPermissions(permissionsList);

        cb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_login.isChecked()) {
                    check_login = false;
                    Logger.e("Check Value if " + cb_login.isChecked());
                    Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);

                } else {
                    check_login = false;
                    Logger.e("Check Value else " + cb_login.isChecked());
                    Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);

                }
            }
        });

        img_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (et_password.getText().toString().length() > 0) {
//                    et_password.requestFocus();
//                }
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
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
            version_code = pInfo.versionCode;
            Logger.e("Version Name  " + version_name + "  " + version_code);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, CreateAccountNameEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if (Utils.isNetworkConnected(SignInActivity.this)) {
            getAppUpdate();
        }

        btn_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

//                dialog_forgotPasswordInput();

            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(SignInActivity.this)) {
                    TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                IMEI = mngr.getDeviceId();
                    device_unique_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    mobile = et_mobile.getText().toString().trim();
                    password = et_password.getText().toString().trim();
                    if (et_mobile.getText().toString().length() <= 0) {
                        Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                    } else if (et_password.getText().toString().length() <= 0) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_enter_password), Toast.LENGTH_LONG).show();
                    } else if (device_unique_id == null) {
                        phone_access_permission();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.phone_permission), Toast.LENGTH_SHORT).show();
                    } else {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        progress.setCancelable(false);
                        postLoginData_spin(mobile, password);

                    }

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void postLoginData_spin(final String user_name, final String password) {
        if (fcm_id != null) {

        } else {
            fcm_id = "eexjkdshjkmcxbvjchvcjbvjcbjbjccbvjcbvjvjcbvjcbvjdsvsjkvjskjj1jkjkn211";
        }
        this.spin_apiInterface.PostSignIn(new RequestForLogin(user_name, password, "1.0", device_unique_id, "Normal", fcm_id, 4, 0.0, 0.0, "SPIN_ANDROID")).enqueue(new Callback<ResponseForLogin>() {
            //        this.apiInterface.PostSignIn(new LoginData(user_name, password)).enqueue(new Callback<LoginData>() {
            public void onResponse(Call<ResponseForLogin> call, Response<ResponseForLogin> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());

                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForLogin loginResponse = (ResponseForLogin) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (os_version == 29) {
                            Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android Q " + "  " + os_version);
                        }
                        if (os_version == 30) {
                            Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android R" + "  " + os_version);
                        } else {
                            Pref.setValue(Pref.TYPE.OS_VERSION.toString(), android_os_name + " " + os_version);

                        }

                        Pref.setValue(Pref.TYPE.APP_VERSION.toString(), version_name);
                        Pref.setValue(Pref.TYPE.FCM_ID.toString(), fcm_id);
                        Pref.setIntValue(Pref.TYPE.APP_VERSION_CODE.toString(), version_code);


                        if (loginResponse.data != null) {
                            progress.dismiss();
//                            Toast.makeText(SignInActivity.this, loginResponse.data, Toast.LENGTH_LONG).show();
                            Logger.e("Response: 000000");

                            if ("User is not verified.".equalsIgnoreCase(loginResponse.data)) {
                              /*  AppUtils.showDefaultDialog(SignInActivity.this, getString(R.string.app_name), getString(R.string.user_verified),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialog_verifyUser();
                                                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                                                intent.putExtra("activity", "REGISTER");
                                                startActivity(intent);


                                            }
                                        }, null);*/
                                dialog_verify_user();


                            } else {
                                try {
                                    Toast.makeText(getApplicationContext(), loginResponse.data, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }

                        } else {
                            Pref.setValue(Pref.TYPE.PASSWORD.toString(), et_password.getText().toString());
                            postLoginData(loginResponse.user.id);
                            Pref.setValue(Pref.TYPE.S_EMAIL.toString(), loginResponse.user.email);
                            Pref.setValue(Pref.TYPE.S_MOBILE.toString(), loginResponse.user.mobile);
                            Pref.setIntValue(Pref.TYPE.S_USER_ID.toString(), loginResponse.user.id);
                            Pref.setValue(Pref.TYPE.S_TOKEN.toString(), "Bearer " + loginResponse.user.jwtToken);


                        }
                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Exp 11" + e);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.S_F_NAME.name().toString(), null)
                                , Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                SignInActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                response.code(),
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));
                    }
                } else {
                    Logger.e("Exp 1122");
                    progress.dismiss();
                    Logger.e("URL: " + call.request().toString());
                    Logger.e("Response Code: " + response.code());
                    AppUtils.bodyToString(call.request().body());
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.S_F_NAME.name().toString(), null)
                            , Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            response.code(),
                            "SERVER ERROR",
                            Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));
                }
            }

            public void onFailure(Call<ResponseForLogin> call, Throwable t) {
                Logger.e("Exp 1133" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.S_F_NAME.name().toString(), null)
                        , Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        SignInActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        200,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));
            }
        });

    }

    private void postLoginData(int user_id) {

        if (fcm_id != null) {

        } else {
            fcm_id = "eexjkdshjkmcxbvjchvcjbvjcbjbjccbvjcbvjvjcbvjcbvjdsvsjkvjskjj1jkjkn211";
        }
        this.apiInterface.loginBLE(new RequestForLogin(user_id, version_name, device_unique_id, fcm_id, "SPIN_ANDROID")).enqueue(new Callback<ResponseForLoginBLE>() {
            //        this.apiInterface.PostSignIn(new LoginData(user_name, password)).enqueue(new Callback<LoginData>() {
            public void onResponse(Call<ResponseForLoginBLE> call, Response<ResponseForLoginBLE> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        responseForLoginBLE = (ResponseForLoginBLE) response.body();
                        Logger.e("Response: " + responseForLoginBLE.toString());
                        if (responseForLoginBLE.status) {
                            Pref.setValue(Pref.TYPE.EMAIL.toString(), responseForLoginBLE.email);
                            Pref.setValue(Pref.TYPE.F_NAME.toString(), responseForLoginBLE.f_Name);
                            Pref.setValue(Pref.TYPE.L_NAME.toString(), responseForLoginBLE.l_Name);
                            Pref.setValue(Pref.TYPE.MOBILE.toString(), responseForLoginBLE.mobile);
                            Pref.setIntValue(Pref.TYPE.USER_ID.toString(), responseForLoginBLE.id);
                            Pref.setIntValue(Pref.TYPE.PROJECT_ID.toString(), AppUtils.project_id);
                            Pref.setValue(Pref.TYPE.TOKEN.toString(), "Bearer " + responseForLoginBLE.token);

                            if (os_version == 29) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android Q " + "  " + os_version);
                            }
                            if (os_version == 30) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android R" + "  " + os_version);
                            } else {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), android_os_name + " " + os_version);

                            }

                            Pref.setValue(Pref.TYPE.ORIGIN.toString(), "BLE_ANDROID");
                            Pref.setValue(Pref.TYPE.DEVICE_ID.toString(), device_unique_id);
                            Pref.setValue(Pref.TYPE.APP_VERSION.toString(), version_name);
//                            Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);
                            Pref.setValue(Pref.TYPE.USER_ROLE_CODE.toString(), responseForLoginBLE.role_code);
                            Pref.setValue(Pref.TYPE.USER_ROLE_ID.toString(), responseForLoginBLE.role_id);
                            Pref.setValue(Pref.TYPE.USER_ROLE_NAME.toString(), responseForLoginBLE.role_name);
                            Pref.setValue(Pref.TYPE.VEHICLE_DETAILS.toString(), responseForLoginBLE.vehicles.toString());

                            if (responseForLoginBLE.vehicles.size() > 0) {
                                Pref.setIntValue(Pref.TYPE.VEHICLE_ID.toString(), responseForLoginBLE.vehicles.get(0).id);
                                Pref.setValue(Pref.TYPE.VEHICLE_REG.toString(), responseForLoginBLE.vehicles.get(0).registration_no);
                                Pref.setValue(Pref.TYPE.CONN_TYPE.toString(), responseForLoginBLE.vehicles.get(0).connector_type_name);
                                Pref.setIntValue(Pref.TYPE.CONN_TYPE_ID.toString(), responseForLoginBLE.vehicles.get(0).connector_type_id);




                               /* if (responseForLoginBLE.vehicles.size() == 1) {

                                } else {
                                    for (int i = 0; i < responseForLoginBLE.vehicles.size(); i++) {
//                                        if (responseForLoginBLE.vehicles.get(i).is_default == 1) {
                                            Pref.setIntValue(Pref.TYPE.VEHICLE_ID.toString(), responseForLoginBLE.vehicles.get(i).id);
                                            Pref.setValue(Pref.TYPE.VEHICLE_REG.toString(), responseForLoginBLE.vehicles.get(i).registration_no);
                                            Pref.setValue(Pref.TYPE.CONN_TYPE.toString(), responseForLoginBLE.vehicles.get(i).connector_type_name);
                                            Pref.setIntValue(Pref.TYPE.CONN_TYPE_ID.toString(), responseForLoginBLE.vehicles.get(i).connector_type_id);
//                                        }
                                    }
                                }
*/
                            }


                            getChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));


//                        return;
                        } else {
                            if (responseForLoginBLE.message.equalsIgnoreCase("User is not verified.")) {
                                AppUtils.showDefaultDialog(SignInActivity.this, getString(R.string.app_name), getString(R.string.user_verified),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialog_verifyUser();
                                                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                                                startActivity(intent);


                                            }
                                        }, null);


                            } else {
//                                try {
//                                    Toast.makeText(getApplicationContext(), responseForLoginBLE.message, Toast.LENGTH_LONG).show();
//                                } catch (Exception e) {
//                                }
                            }


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
                                SignInActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                response.code(),
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
                } else if (response.code() == 404) {


                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            response.code(),
                            "SERVER ERROR",
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                } else if (response.code() == 500) {

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            response.code(),
                            "SERVER ERROR",
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseForLoginBLE> call, Throwable t) {
                Logger.e("Exp 444" + t.getMessage());
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
                        SignInActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        502,
                        "SERVER ERROR",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            }
        });

    }


    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {

            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            IMEI = mngr.getDeviceId();
            device_unique_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                IMEI = mngr.getDeviceId();
                device_unique_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
            }
        }


    }

    private void getAndroidOsVersion() {
        os_version = Build.VERSION.SDK_INT;
        switch (os_version) {
            case 17:
            case 18:
                android_os_name = "Jelly Bean";
                break;
            case 19:
            case 20:
                android_os_name = "Kitkat";
                break;
            case 21:
            case 22:
                android_os_name = "Lollipop";
                break;
            case 23:
                android_os_name = "Marshmallow";
                break;
            case 24:
            case 25:
                android_os_name = "Nougat";
                break;
            case 26:
            case 27:
                android_os_name = "Oreo";
                break;
            case 28:
                android_os_name = "Pie";
                break;
            case 29:
                android_os_name = "Android 10";
                break;

            case 30:
                android_os_name = "Android 11";
                break;
            case 31:
                android_os_name = "Android 12";
                break;
            case 32:
                android_os_name = "Android 12";
                break;



        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();


    }


    void phone_access_permission() {
        new AlertDialog.Builder(SignInActivity.this)
                .setTitle("Alert")
                .setMessage(getResources().getString(R.string.phone_permission_message))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        phone_permission();

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    void dialog_forgotPasswordInput() {
        String mobile, emp_email;
        dialog_passwordInput = new Dialog(SignInActivity.this);
        dialog_passwordInput.setContentView(R.layout.forgot_password_input);
        dialog_passwordInput.setCancelable(false);
        final EditText txt_mobile = (EditText) dialog_passwordInput.findViewById(R.id.mobile);
        TextView btn_ok = (TextView) dialog_passwordInput.findViewById(R.id.btn_submit);
        TextView btn_cencel = (TextView) dialog_passwordInput.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(SignInActivity.this)) {
                    if (txt_mobile.getText().toString().length() <= 9) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.incorrect_mobile_number), Toast.LENGTH_LONG).show();
                    } else {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        forgotPasswordInput(txt_mobile.getText().toString(), "FORGOT_PASSWORD");

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_passwordInput.dismiss();

            }
        });
        dialog_passwordInput.show();


    }

    void dialog_verifyUser() {
        String mobile, emp_email;
        dialog_passwordInput = new Dialog(SignInActivity.this);
        dialog_passwordInput.setContentView(R.layout.verifyuser_input);
        dialog_passwordInput.setCancelable(false);
        final EditText txt_mobile = (EditText) dialog_passwordInput.findViewById(R.id.mobile);
        TextView btn_ok = (TextView) dialog_passwordInput.findViewById(R.id.btn_submit);
        TextView btn_cencel = (TextView) dialog_passwordInput.findViewById(R.id.btn_cancel);

//        txt_message.setText("" + message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(SignInActivity.this)) {
                    if (txt_mobile.getText().toString().length() <= 9) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.incorrect_mobile_number), Toast.LENGTH_LONG).show();
                    } else {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        forgotPasswordInput(txt_mobile.getText().toString(), "REGISTER");

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_passwordInput.dismiss();

            }
        });
        dialog_passwordInput.show();


    }

    void dialog_forgotPasswordConfirm(final String req_id) {
        String password, confirm_password;
        dialog_confirmPassword = new Dialog(SignInActivity.this);
        dialog_confirmPassword.setContentView(R.layout.forgot_password_confirm);
        dialog_confirmPassword.setCancelable(false);
        final EditText txt_password = (EditText) dialog_confirmPassword.findViewById(R.id.password);
        final EditText txt_confirm_password = (EditText) dialog_confirmPassword.findViewById(R.id.confirm_password);
        TextView btn_ok = (TextView) dialog_confirmPassword.findViewById(R.id.btn_submit);
        TextView btn_cencel = (TextView) dialog_confirmPassword.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(SignInActivity.this)) {
                    if (txt_password.getText().toString().length() <= 7 && !isValidPassword(et_password.getText().toString())) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.pass_msg), Toast.LENGTH_LONG).show();
                    } else if ((txt_password.getText().toString().equals(txt_confirm_password.getText().toString()))) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        //forgotPassword_Confirm(req_id, txt_password.getText().toString(), txt_confirm_password.getText().toString());


                    } else {
                        Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirmPassword.dismiss();

            }
        });
        dialog_confirmPassword.show();


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
                            dialog_passwordInput.dismiss();
                            Toast.makeText(getApplicationContext(), "OTP sent successfully.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OTPForgotPasswordVerification.class);
                            intent.putExtra("email", "xyz");
                            intent.putExtra("mob", mobile);
                            intent.putExtra("username", "xyz");
                            intent.putExtra("activity", activity);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();


                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseForResendOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });
    }


    void phone_permission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    public boolean isValidMobileNumber(String s) {

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0|91)?[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));


    }

    private void reSendOTP(String mobile) {
        this.apiInterface.reSendOTP(new RequestForResendOTP("", mobile, "", "", "RESEND_REGISTER")).enqueue(new Callback<ResponseForResendOTP>() {
            public void onResponse(Call<ResponseForResendOTP> call, Response<ResponseForResendOTP> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForResendOTP loginResponse = (ResponseForResendOTP) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {

                        }
//                        Intent intent = new Intent(SignInActivity.this, ChargerOTPVerification.class);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    }
                } else {
                }


            }

            public void onFailure(Call<ResponseForResendOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();

                call.cancel();
            }
        });

    }

    void getFCMToken() {
        // Get token
        // [START log_reg_token]
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Logger.w("Fetching FCM registration token failed" + task.getException().toString());
                            return;
                        }

                        // Get new FCM registration token
                        fcm_id = task.getResult();

//                        Toast.makeText(SignInActivity.this, fcm_id, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END log_reg_token]


    /*    FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Toast.makeText(SignInActivity.this, "Successfull", Toast.LENGTH_SHORT).show();

                    }
                });*/
    }

    public void getChargerList(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGetChargerList> call = apiInterfacePP.getChargerListforWarranty(hashMap1, userId);
        call.enqueue(new Callback<ResponseGetChargerList>() {
            @Override
            public void onResponse(Call<ResponseGetChargerList> call, Response<ResponseGetChargerList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        if (response.body().status) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_successfully), Toast.LENGTH_LONG).show();

                            if (response.body().data.size() > 0) {
                                Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), response.body().data.size());
                                Pref.setValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), response.body().data.get(0).serialNo);
                                Pref.setValue(Pref.TYPE.NICK_NAME.toString(), response.body().data.get(0).nickName);
                                Pref.setValue(Pref.TYPE.IS_OCCP.toString(), response.body().data.get(0).is_OCPP_enabled);
                                Pref.setValue(Pref.TYPE.IS_UPGRADE_CHARGER_STATUS.toString(), response.body().data.get(0).ocpp_upgrade_status);
                                Pref.setValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), response.body().data.get(0).partNo);
                                Pref.setIntValue(Pref.TYPE.CHARGER_CRED.toString(), response.body().data.get(0).client_certificate);
                                Pref.setValue(Pref.TYPE.STATION_ID.toString(), String.valueOf(response.body().data.get(0).station_id));
                                Pref.setValue(Pref.TYPE.SYSTEM_NUMBER.toString(), response.body().data.get(0).partNo + "#" + response.body().data.get(0).serialNo);
                                Pref.setValue(Pref.TYPE.B_ADDRESS.toString(), response.body().data.get(0).address);
                                Pref.setIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), response.body().data.get(0).mapAsChild);
                                Logger.e("Address  " + response.body().data.get(0).address);
                                Pref.setIntValue(Pref.TYPE.MAP_AS_CHILD.toString(), response.body().data.get(0).mapAsChild);

                                response.body().user_id = Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0);
                                response.body().vehicle_id = Pref.getIntValue(Pref.TYPE.VEHICLE_ID.toString(), 0);
                                response.body().registration_number = Pref.getValue(Pref.TYPE.VEHICLE_REG.toString(), null);
                                Gson gson = new Gson();
//                                String json = gson.toJson(response.body().data);
                                String json = gson.toJson(response.body());
                                Pref.setValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), json);

                                Logger.e("Data charger ===" + Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null));


                            } else {
                                Pref.setIntValue(Pref.TYPE.MYCHARGERLIST.toString(), 0);

                            }

                            if (responseForLoginBLE.vehicles.size() > 0) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);
                                startActivity(intent);
                                finish();
                            } else {

                                Intent intent = new Intent(getApplicationContext(), AddVehicleLoginActivity.class);
                                intent.putExtra("activity", "LOGIN");
//                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseGetChargerList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }

    void dialog_verify_user() {
        dialog_verify_user = new Dialog(SignInActivity.this);
        dialog_verify_user.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_verify_user.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_verify_user.setCancelable(false);

        TextView btn_ok = (TextView) dialog_verify_user.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_verify_user.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_verify_user.findViewById(R.id.tv_message);
        tv_message.setText("You are not verified user, Please verify.");
        btn_ok.setText("Verify");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("activity", "REGISTER");
                startActivity(intent);
                dialog_verify_user.dismiss();

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


    void dialog_app_update(String url, String update_type) {
        dialog_verify_user = new Dialog(SignInActivity.this);
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

    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;

                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                                    permissionsList.add(permissionsStr[i]);
                                } else if (!hasPermission(SignInActivity.this, permissionsStr[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                //All permissions granted. Do your stuff 🤞
//                                binding.txtStatus.setText("All permissions are granted!");
                            }
                        }
                    });

    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    androidx.appcompat.app.AlertDialog alertDialog;

    private void showPermissionDialog() {
//        binding.txtStatus.setText("Showing settings dialog");
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    askForPermissions(permissionsList);

//                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
//            binding.txtStatus.setText("Asking for permissions");
            permissionsLauncher.launch(newPermissionStr);
        } else {
            /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
            which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }


    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(SignInActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

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
                            if (version_code < response_body.data.get(0).newVersionCode) {
                                dialog_app_update(response_body.data.get(0).redirectionUrl, response_body.data.get(0).updateType);
                            }

                        }
                        Logger.e("Main Screen" + response_body.toString());
                    } catch (Exception e) {
//                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
//                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 1111, Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                SignInActivity.class.getSimpleName(),
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
                    progress.dismiss();
//                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_not_found_please_try_again) + 22222, Toast.LENGTH_LONG).show();

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
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

            @Override
            public void onFailure(Call<ProjectUpdate> call, Throwable t) {
                call.cancel();
//                progress.dismiss();
                Log.d("TAG", t.getMessage());
//                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again) + 3333, Toast.LENGTH_LONG).show();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        SignInActivity.class.getSimpleName(),
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
}