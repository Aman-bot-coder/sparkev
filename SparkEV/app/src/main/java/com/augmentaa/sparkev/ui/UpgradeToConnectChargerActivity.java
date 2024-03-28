package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.StaticTextAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_warrenty.Data;
import com.augmentaa.sparkev.model.signup.get_warrenty.RespponseGetWarranty;
import com.augmentaa.sparkev.model.signup.mobile_static_text.TextStaticData;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeToConnectChargerActivity extends AppCompatActivity {
    LinearLayout ly_benefit;
    TextView tv_benefit, tv_y_do_need;
    TextView tv_benefit_des, tv_y_do_need_des;
    Button btn_request_upgrade;

    boolean isCheckedBenefit, isCheckedWhyDoYouNeed, isCheckedUnder, isCheckedCharger;
    ImageView img_back;
    TextView tv_amount;
    APIInterface apippInterface,apiInterface;
    ProgressDialog progress;
    RespponseGetWarranty respponseGetWarranty;
    TextStaticData respponse_static_data;
    float amount;
    ListView listView;
    Data data;
    LinearLayout ly_static_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_connect_charger);
        getSupportActionBar().hide();
        img_back = findViewById(R.id.back);
        btn_request_upgrade = findViewById(R.id.btn_request_upgrade);
        ly_benefit = findViewById(R.id.ly_benefits);
        tv_benefit = findViewById(R.id.tv_benefits);
        tv_y_do_need = findViewById(R.id.why_do_u_need);
        tv_y_do_need_des = findViewById(R.id.why_do_u_need_des);
        tv_amount = findViewById(R.id.amount);
        listView=findViewById(R.id.listview);
        ly_static_text=findViewById(R.id.ly_static_text);
        this.apippInterface = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress = new ProgressDialog(this);

        if (Utils.isNetworkConnected(UpgradeToConnectChargerActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getWarranty(Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null));
//            getStaticText();
        } else {
            Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }

        tv_benefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedBenefit) {
                    isCheckedBenefit = true;
                    ly_benefit.setVisibility(View.VISIBLE);
                    tv_benefit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedBenefit = false;
                    ly_benefit.setVisibility(View.GONE);
                    tv_benefit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });


        tv_y_do_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedWhyDoYouNeed) {
                    isCheckedWhyDoYouNeed = true;
                    tv_y_do_need_des.setVisibility(View.VISIBLE);
                    tv_y_do_need.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedWhyDoYouNeed = false;
                    tv_y_do_need_des.setVisibility(View.GONE);
                    tv_y_do_need.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_request_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(UpgradeToConnectChargerActivity.this)) {
                    Intent intent=new Intent(UpgradeToConnectChargerActivity.this,RequestUpgradeActivity.class);
                    intent.putExtra("data",respponseGetWarranty.data.get(0));
                    startActivity(intent);
                } else {
                    Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });


    }


    public void getWarranty(String charger_sr_no) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<RespponseGetWarranty> call = apippInterface.getOCPPUpgradePlans(hashMap1, charger_sr_no);
        call.enqueue(new Callback<RespponseGetWarranty>() {
            @Override
            public void onResponse(Call<RespponseGetWarranty> call, Response<RespponseGetWarranty> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                respponseGetWarranty = (RespponseGetWarranty) response.body();
                Logger.e("Warranty response===" + respponseGetWarranty.toString());
                if (response.code() == 200) {
                    try {
                    if (respponseGetWarranty.status) {
//                        data=respponseGetWarranty.data.get(0);
                        respponseGetWarranty.data.get(0).setSerial_no(charger_sr_no);
                        tv_amount.setText(getResources().getString(R.string.Rs)+" " + respponseGetWarranty.data.get(0).mrpInctax);
                        amount=respponseGetWarranty.data.get(0).mrpInctax;
                        tv_y_do_need_des.setText("In addition to application features, you will get 1 year extended warranty worth INR " + amount +" absolutely free with this upgrade.\n\nThis scheme is for limited time period only.");
                       
                    } else {
                        if ("Token is not valid".equalsIgnoreCase(respponseGetWarranty.message)) {
                            Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                            Pref.clear();
                            Intent intent21 = new Intent(UpgradeToConnectChargerActivity.this, SignInActivity.class);
                            startActivity(intent21);
                            finishAffinity();
                            Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                        }
                    }


                    } catch (Exception e) {
                        Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               UpgradeToConnectChargerActivity.class.getSimpleName(),
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
                    Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            UpgradeToConnectChargerActivity.class.getSimpleName(),
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
            public void onFailure(Call<RespponseGetWarranty> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        UpgradeToConnectChargerActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));



            }
        });

    }



    public void getStaticText()   {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<TextStaticData> call = apiInterface.getMobileAppStaticData(hashMap1, "UPGRADE");
        call.enqueue(new Callback<TextStaticData>() {
            @Override
            public void onResponse(Call<TextStaticData> call, Response<TextStaticData> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                respponse_static_data = (TextStaticData) response.body();
                Logger.e("Warranty response===" + respponse_static_data.toString());
                if (response.code() == 200) {
//                    try {
                        if (respponse_static_data.status) {
                            StaticTextAdapter adapter=new StaticTextAdapter(UpgradeToConnectChargerActivity.this, respponse_static_data.data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            ListUtils.setDynamicHeight(listView);


                        } else {
                            if ("Token is not valid".equalsIgnoreCase(respponse_static_data.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(UpgradeToConnectChargerActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            }
                        }


//                    } catch (Exception e) {
//                        Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
//                        callMailService(
//                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
//                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
//                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
//                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
//                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
//                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
//                                UpgradeToConnectChargerActivity.class.getSimpleName(),
//                                "SPIN_ANDROID",
//                                AppUtils.project_id,
//                                AppUtils.bodyToString(call.request().body()),
//                                "ANDROID",
//                                call.request().url().toString(),
//                                "Y",
//                                AppUtils.DATA_NOT_FOUND_CODE,
//                                e.getMessage(),
//                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
//                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
//
//
//
//                    }
                } else {
                    Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            UpgradeToConnectChargerActivity.class.getSimpleName(),
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
            public void onFailure(Call<TextStaticData> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(UpgradeToConnectChargerActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        UpgradeToConnectChargerActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));



            }
        });

    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(UpgradeToConnectChargerActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}