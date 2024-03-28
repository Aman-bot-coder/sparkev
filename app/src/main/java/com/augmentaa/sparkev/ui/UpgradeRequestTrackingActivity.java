package com.augmentaa.sparkev.ui;

import static com.augmentaa.sparkev.utils.AppUtils.geDateSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.TrackingRequestAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.tracking.ResponseUpgradeTracking;
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

public class UpgradeRequestTrackingActivity extends AppCompatActivity {
TextView tv_request_id,tv_nick_name,tv_request_date,tv_charger_id;
ListView listView;
ImageView img_back;
TextView tv_description;
    APIInterface apiInterface;
    ProgressDialog progress;
    ResponseUpgradeTracking responseUpgradelist;
    String track;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_request_tracking);
        getSupportActionBar().hide();
        tv_charger_id=findViewById(R.id.charger_id);
        tv_request_date=findViewById(R.id.request_date);
        tv_nick_name=findViewById(R.id.nick_name);
        tv_request_id=findViewById(R.id.request_id);
        listView=findViewById(R.id.listview);
        img_back=findViewById(R.id.back);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress = new ProgressDialog(this);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpgradeRequestTrackingActivity.super.onBackPressed();
            }


        });

        try {
            track=getIntent().getExtras().getString("track");
        }
        catch (Exception e){

        }

        if (Utils.isNetworkConnected(UpgradeRequestTrackingActivity.this)) {
            try {

                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                getUpgradeChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));

            } catch (Exception e) {

            }


        } else {
            Toast.makeText(UpgradeRequestTrackingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }


    public void getUpgradeChargerList(int user_id) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseUpgradeTracking> call = apiInterface.getBleupgradedetailsByUser(hashMap1, user_id);
        call.enqueue(new Callback<ResponseUpgradeTracking>() {
            @Override
            public void onResponse(Call<ResponseUpgradeTracking> call, Response<ResponseUpgradeTracking> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());

                try {
                    if (response.code() == 200) {
                        try {
                            responseUpgradelist = (ResponseUpgradeTracking) response.body();
                            Logger.e("Warranty response===" + responseUpgradelist.toString());

                            if (responseUpgradelist.status) {
                                for (int i = 0; i < responseUpgradelist.data.size(); i++) {
                                    Logger.e("====Charger details== " + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) + "  " + responseUpgradelist.data.get(i).chargerSerialNo);
                                    if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null).equalsIgnoreCase(responseUpgradelist.data.get(i).chargerSerialNo)) {
                                        tv_charger_id.setText("Charger ID : "+responseUpgradelist.data.get(i).chargerSerialNo);
                                        tv_nick_name.setText("Nick Name : "+responseUpgradelist.data.get(i).chargerNickName);
                                        if(responseUpgradelist.data.get(i).requestDt!=null) {
                                            tv_request_date.setText("Request Date : " + geDateSummary(responseUpgradelist.data.get(i).requestDt));
                                        }
                                        else {
                                            tv_request_date.setText("Request Date : " );

                                        }
                                        tv_request_id.setText("Requested ID : "+responseUpgradelist.data.get(i).requestNo);

                                        TrackingRequestAdapter adapter=new TrackingRequestAdapter(UpgradeRequestTrackingActivity.this,responseUpgradelist.data.get(i).details);
                                        listView.setAdapter(adapter);
//                                        adapter.notifyDataSetChanged();
                                        ListUtils.setDynamicHeight(listView);
                                        break;

                                    }
                                }


                            } else {
                                if ("Token is not valid".equalsIgnoreCase(responseUpgradelist.message)) {
                                    Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                    Pref.clear();
                                    Intent intent21 = new Intent(UpgradeRequestTrackingActivity.this, SignInActivity.class);
                                    startActivity(intent21);
                                    finishAffinity();
                                    Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                                }
                            }


                        } catch (Exception e) {
                            Toast.makeText(UpgradeRequestTrackingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                    UpgradeRequestTrackingActivity.class.getSimpleName(),
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
                        Toast.makeText(UpgradeRequestTrackingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                UpgradeRequestTrackingActivity.class.getSimpleName(),
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
                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseUpgradeTracking> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(UpgradeRequestTrackingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        UpgradeRequestTrackingActivity.class.getSimpleName(),
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
        Intent intent = new Intent(UpgradeRequestTrackingActivity.this, MyService.class);
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

    @Override
    public void onBackPressed() {
        if(track!=null){
            Intent intent=new Intent(UpgradeRequestTrackingActivity.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        else {
            super.onBackPressed();
        }
    }
}