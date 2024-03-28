package com.augmentaa.sparkev.ui;

import static com.augmentaa.sparkev.utils.AppUtils.project_id;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.RequestSignUp;
import com.augmentaa.sparkev.model.signup.ResponseRegistration;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountPasswordActivity extends AppCompatActivity {

    EditText etPassword, etCnfPassword;
    Button btnContinue;
    String password, cnfPassword, name, mobile, email, fcm_id;
    int country_id;
    boolean showPassword = false;
    boolean showCnfPassword = false;
    ImageView img_password, img_cnf_password, img_back;


    APIInterface apiInterface;
    APIInterface apiSpinInterface;
    ProgressDialog progress;
    String device_unique_id, IMEI;
    int os_version = 0;
    String android_os_name = null;
    LinearLayout btn_login;
    String version_name;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    TextView tvSignIn,tv_term_condition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_password);
        etPassword = findViewById(R.id.password);
        etCnfPassword = findViewById(R.id.cnf_password);
        btnContinue = findViewById(R.id.btn_submit);
        img_password = findViewById(R.id.eye);
        img_cnf_password = findViewById(R.id.eye_cnf);
        img_back = findViewById(R.id.back);
        tvSignIn = findViewById(R.id.signIn);
        tv_term_condition=findViewById(R.id.tv_term_condition);
        getSupportActionBar().hide();
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiSpinInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);

        try {
            name = getIntent().getExtras().getString("name");
            email = getIntent().getExtras().getString("email");
            mobile = getIntent().getExtras().getString("mobile");
            country_id = getIntent().getExtras().getInt("country_id");

        } catch (Exception e) {

        }


        getAndroidOsVersion();
        loadIMEI();
        getFCMToken();
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
            Logger.e("Version Name  " + version_name);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(CreateAccountPasswordActivity.this)) {
                    try {
                        String url = "https://policyvoiceassistant.exicom.in/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }catch (Exception e){

                        Logger.e("Exp "+e);

                    }

                } else {
                    Toast.makeText(CreateAccountPasswordActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });
        img_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword) {
                    showPassword = false;
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    img_password.setImageResource(R.mipmap.ic_eye_close);

                } else {

                    showPassword = true;
                    etPassword.setTransformationMethod(null);
                    img_password.setImageResource(R.drawable.ic_eye);

                }
                Editable etext = etPassword.getText();
                Selection.setSelection(etext, etPassword.getText().toString().length());

            }
        });

        img_cnf_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showCnfPassword) {
                    showCnfPassword = false;
                    etCnfPassword.setTransformationMethod(new PasswordTransformationMethod());
                    img_cnf_password.setImageResource(R.mipmap.ic_eye_close);

                } else {

                    showCnfPassword = true;
                    etCnfPassword.setTransformationMethod(null);
                    img_cnf_password.setImageResource(R.drawable.ic_eye);

                }
                Editable etext = etCnfPassword.getText();
                Selection.setSelection(etext, etCnfPassword.getText().toString().length());

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = etPassword.getText().toString().trim();
                cnfPassword = etCnfPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateAccountPasswordActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(cnfPassword)) {
                    Toast.makeText(CreateAccountPasswordActivity.this, "Please enter your confirm password", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase(cnfPassword)) {

                    if (device_unique_id != null) {
                        progress = new ProgressDialog(CreateAccountPasswordActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        postLoginData();
                    } else {
                        phone_access_permission();
                    }
//                    Intent intent = new Intent(CreateAccountPasswordActivity.this, CreateAccountLocationPhoneActivity.class);
//                    intent.putExtra("name", password);
//                    intent.putExtra("email", password);
//                    startActivity(intent);
//                    Toast.makeText(CreateAccountPasswordActivity.this, "Valid Email.", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(CreateAccountPasswordActivity.this, "Password and confirm password not matched ", Toast.LENGTH_LONG).show();

                }

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
//                get_imei_data();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {

            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            IMEI = mngr.getDeviceId();
            device_unique_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            // READ_PHONE_STATE permission is already been granted.
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
//                Toast.makeText(this, "55555", Toast.LENGTH_LONG).show();
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

//                        Toast.makeText(CreateAccountPasswordActivity.this, fcm_id, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END log_reg_token]
    }

    void phone_access_permission() {
        new AlertDialog.Builder(CreateAccountPasswordActivity.this)
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

    void phone_permission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

    }

    private void postLoginData() {
        Logger.e("Country id1234" + country_id);// sending project_id 1
        this.apiSpinInterface.PostSignUp(new RequestSignUp(name, email, mobile, password, 2, country_id, 1, fcm_id, version_name, os_version, device_unique_id,"SPARKEV")).enqueue(new Callback<ResponseRegistration>() {
            public void onResponse(Call<ResponseRegistration> call, Response<ResponseRegistration> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                ResponseRegistration loginResponse = (ResponseRegistration) response.body();
//                Logger.e("Response SignUp: " + loginResponse.toString()+"  "+country_id);
                if (response.code() == 200) {
                    try {
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status == 1) {
                            Logger.e("Response SignUp11: " + loginResponse.toString());

                            Pref.setValue(Pref.TYPE.S_EMAIL.toString(), email);
                            Pref.setValue(Pref.TYPE.S_MOBILE.toString(), mobile);

                            Pref.setIntValue(Pref.TYPE.S_USER_ID.toString(), loginResponse.id);
                           // Pref.setIntValue(Pref.TYPE.PROJECT_ID.toString(), AppUtils.project_id);
                            Pref.setIntValue(Pref.TYPE.PROJECT_ID.toString(), 1);
                            Pref.setIntValue(Pref.TYPE.USER_ID.toString(), loginResponse.username);

                            if (os_version == 29) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android Q " + "  " + os_version);
                            }
                            if (os_version == 30) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android R" + "  " + os_version);
                            } else {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android " + " " + os_version);

                            }

                            if (os_version == 31) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android S" + "  " + os_version);
                            }

                            if (os_version == 32) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android" + "  " + os_version);
                            }

                            if (os_version == 33) {
                                Pref.setValue(Pref.TYPE.OS_VERSION.toString(), "Android" + "  " + os_version);
                            }

                            Pref.setValue(Pref.TYPE.ORIGIN.toString(), "BLE_ANDROID");
                            Pref.setValue(Pref.TYPE.DEVICE_ID.toString(), device_unique_id);
                            Pref.setValue(Pref.TYPE.APP_VERSION.toString(), version_name);
                            Pref.setValue(Pref.TYPE.FCM_ID.toString(), fcm_id);
                            // Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), true);

                            Intent intent = new Intent(CreateAccountPasswordActivity.this, OTPForgotPasswordVerification.class);
                            intent.putExtra("activity", "REGISTER");
                            intent.putExtra("email", Pref.getValue(Pref.TYPE.S_EMAIL.toString(), null));
                            intent.putExtra("mob", Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));
                            intent.putExtra("username", name);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), loginResponse.msg, Toast.LENGTH_LONG).show();
                            callMailService(
                                    0,
                                    email
                                    , email,
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   CreateAccountPasswordActivity.class.getSimpleName(),
                                    "SparkEV",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    0,
                                    mobile);
                        }

//                    return;
//                        } else {
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
//                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                0,
                                email
                                , email,
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               CreateAccountPasswordActivity.class.getSimpleName(),
                                "SPARKEV",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                AppUtils.DATA_NOT_FOUND,
                               0,
                              mobile);



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
                           CreateAccountPasswordActivity.class.getSimpleName(),
                            "SPARKEV",
//                            AppUtils.project_id,
                          1,

                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SYSTEM_ERROR,
                            0,
                            mobile);
                }


            }

            public void onFailure(Call<ResponseRegistration> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                Logger.e("URL: Sign up 111111" + t.getMessage());
                Logger.e("URL: Sign up 111111" + call.request().toString());
                AppUtils.bodyToString(call.request().body());

                callMailService(
                        0,
                        email
                        , email,
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       CreateAccountPasswordActivity.class.getSimpleName(),
                        "SPARKEV",
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
        Intent intent = new Intent(CreateAccountPasswordActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


}