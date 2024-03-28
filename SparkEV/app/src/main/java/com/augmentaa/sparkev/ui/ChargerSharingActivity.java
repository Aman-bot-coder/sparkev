package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.RecievedRequestNewAdapter;
import com.augmentaa.sparkev.adapter.RecievedSharedAdapter;
import com.augmentaa.sparkev.adapter.SendRequestNewAdapter;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.guest_access_list.Data;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestDenyGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGrantGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGuestAccessList;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestRevokeCharger;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseGuestAccessList;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseRevokeCharger;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChargerSharingActivity extends AppCompatActivity implements RecievedRequestNewAdapter.ClickButton, RecievedSharedAdapter.RevokeCharger {

    ListView lv_sharing, lv_request;
    TextView tv_request, tv_sharing;
    RadioGroup radioGroup;
    RadioButton rb_request, rb_received;
    APIInterface apiInterface, apiInterfaceSPIN;
    ImageView img_back;
    ProgressDialog progress;
    ResponseGuestAccessList loginresponse;
    List<Data> list_sharing;
    List<Data> list_deny;
    List<Data> list_request;
    TextView tv_data;
    Dialog dialog_allow_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_sharing);
        tv_data = findViewById(R.id.tv_data);
        radioGroup = findViewById(R.id.radioGroup);
        rb_request = findViewById(R.id.rb_send);
        rb_received = findViewById(R.id.rb_receiced);
        tv_request = findViewById(R.id.request);
        tv_sharing = findViewById(R.id.sharing);
        img_back = findViewById(R.id.back);
        lv_request = findViewById(R.id.lv_request);
        lv_sharing = findViewById(R.id.lv_sharing);
        getSupportActionBar().hide();

        list_sharing = new ArrayList<>();
        list_deny = new ArrayList<>();
        list_request = new ArrayList<>();


        this.progress = new ProgressDialog(this);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfaceSPIN = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
            progress = new ProgressDialog(ChargerSharingActivity.this);
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
        } else {
            Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(CallRequestInfoActivity.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();

                    if ("Received request".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
                            progress = new ProgressDialog(ChargerSharingActivity.this);
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
                        } else {
                            Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
                            progress = new ProgressDialog(ChargerSharingActivity.this);
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getSentChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
                        } else {
                            Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    }
                }

            }
        });

    }

    public void getReceivedChargerRequest(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGuestAccessList> call = apiInterfaceSPIN.guestAccessList(hashMap1, new RequestGuestAccessList(userId));

        call.enqueue(new Callback<ResponseGuestAccessList>() {
            @Override
            public void onResponse(Call<ResponseGuestAccessList> call, Response<ResponseGuestAccessList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                    loginresponse = (ResponseGuestAccessList) response.body();
                    Logger.e("Response Received  " + loginresponse.toString());
                    list_request.clear();
                    list_sharing.clear();
                    list_deny.clear();

                    if (loginresponse.status == 1) {
                        if (loginresponse.data.size() > 0) {
                            tv_data.setVisibility(View.GONE);
                            for (int i = 0; i < loginresponse.data.size(); i++) {
                                if (loginresponse.data.get(i).status == 1) {
                                    //Revoke List
                                    list_sharing.add(new Data(loginresponse.data.get(i).id, loginresponse.data.get(i).deviceNumber, loginresponse.data.get(i).userId, loginresponse.data.get(i).userName, loginresponse.data.get(i).userEmail, loginresponse.data.get(i).deviceOwner, loginresponse.data.get(i).status, loginresponse.data.get(i).createdAt, loginresponse.data.get(i).updatedAt));
                                    //Deny list
                                } else if (loginresponse.data.get(i).status == 2) {
                                    list_request.add(new Data(loginresponse.data.get(i).id, loginresponse.data.get(i).deviceNumber, loginresponse.data.get(i).userId, loginresponse.data.get(i).userName, loginresponse.data.get(i).userEmail, loginresponse.data.get(i).deviceOwner, loginresponse.data.get(i).status, loginresponse.data.get(i).createdAt, loginresponse.data.get(i).updatedAt));

                                } else {
                                    //Pending list deny allow
                                    list_request.add(new Data(loginresponse.data.get(i).id, loginresponse.data.get(i).deviceNumber, loginresponse.data.get(i).userId, loginresponse.data.get(i).userName, loginresponse.data.get(i).userEmail, loginresponse.data.get(i).deviceOwner, loginresponse.data.get(i).status, loginresponse.data.get(i).createdAt, loginresponse.data.get(i).updatedAt));

                                }
                            }

                            Logger.e("List size==>" + list_request.size() + "  " + list_sharing.size());

                            setListview();


                        } else {

                        }
                    } else {
                        tv_data.setVisibility(View.VISIBLE);
                        lv_sharing.setVisibility(View.GONE);
                        tv_sharing.setVisibility(View.GONE);
                        lv_request.setVisibility(View.GONE);
                        tv_request.setVisibility(View.GONE);
                        Toast.makeText(ChargerSharingActivity.this, loginresponse.msg, Toast.LENGTH_LONG).show();

                    }

//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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

    public void getSentChargerRequest(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGuestAccessList> call = apiInterfaceSPIN.requestSentList(hashMap1, new RequestGuestAccessList(userId));
        call.enqueue(new Callback<ResponseGuestAccessList>() {
            @Override
            public void onResponse(Call<ResponseGuestAccessList> call, Response<ResponseGuestAccessList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {

                    try {
                    loginresponse = (ResponseGuestAccessList) response.body();
                    Logger.e("Response Send  " + loginresponse.toString());
                    if (loginresponse.status == 1) {
                        if (loginresponse.data.size() > 0) {
                            SendRequestNewAdapter chargerAdapter = new SendRequestNewAdapter(ChargerSharingActivity.this, loginresponse.data);
                            lv_request.setAdapter(chargerAdapter);
                            chargerAdapter.notifyDataSetChanged();
                            lv_sharing.setVisibility(View.GONE);
                            tv_sharing.setVisibility(View.GONE);
                            lv_request.setVisibility(View.VISIBLE);
                            tv_request.setVisibility(View.VISIBLE);
                            tv_data.setVisibility(View.GONE);

                        } else {
                            lv_request.setVisibility(View.GONE);
                            tv_request.setVisibility(View.GONE);
                            lv_sharing.setVisibility(View.GONE);
                            tv_sharing.setVisibility(View.GONE);
                            tv_data.setVisibility(View.VISIBLE);
                        }

                    } else {

                        lv_request.setVisibility(View.GONE);
                        tv_request.setVisibility(View.GONE);
                        lv_sharing.setVisibility(View.GONE);
                        tv_sharing.setVisibility(View.GONE);
                        tv_data.setVisibility(View.VISIBLE);

                    }
//                        Toast.makeText(ChargerSharingActivity.this, loginresponse.msg, Toast.LENGTH_LONG).show();

                    //set dynmic height for all listviews
                    ListUtils.setDynamicHeight(lv_request);


//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    public void getRevokeCharger(int request_id, String client_dev_no, int user_id, int modifyby, String serial_no) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseRevokeCharger> call = apiInterface.revokeCharger(hashMap1, new RequestRevokeCharger(request_id, client_dev_no, user_id, modifyby, serial_no));
        call.enqueue(new Callback<ResponseRevokeCharger>() {
            @Override
            public void onResponse(Call<ResponseRevokeCharger> call, Response<ResponseRevokeCharger> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));

                } else {
                    Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseRevokeCharger> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());

                Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public void event_allow(int position) {
        Logger.e("Click on allow");

        dialog_check("Are you sure you want to allow access to the user "+list_request.get(position).userName+" ?", "allow", position);




    }

    @Override
    public void event_deny(int position) {
        Logger.e("Click on deny");
        dialog_check("Are you sure you don't want to grant access to the user "+list_request.get(position).userName+" ?", "deny", position);


    }

    @Override
    public void event_revoke(int position) {
        Logger.e("Click Revoke");

        dialog_check("Are you sure you want to revoke access to the user "+list_request.get(position).userName+" ?", "revoke", position);

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

    void setListview() {

        if (list_sharing.size() > 0) {
            RecievedSharedAdapter chargerAdapter = new RecievedSharedAdapter(ChargerSharingActivity.this, list_sharing, this);
            lv_sharing.setAdapter(chargerAdapter);
//                            chargerAdapter.notifyDataSetChanged();
            lv_sharing.setVisibility(View.VISIBLE);
            tv_sharing.setVisibility(View.VISIBLE);

        } else {
            lv_sharing.setVisibility(View.GONE);
            tv_sharing.setVisibility(View.GONE);
        }


        if (list_request.size() > 0) {
            RecievedRequestNewAdapter chargerAdapter = new RecievedRequestNewAdapter(ChargerSharingActivity.this, list_request, this);
            lv_request.setAdapter(chargerAdapter);
//                            chargerAdapter.notifyDataSetChanged();
            lv_request.setVisibility(View.VISIBLE);
            tv_request.setVisibility(View.VISIBLE);

        } else {
            lv_request.setVisibility(View.GONE);
            tv_request.setVisibility(View.GONE);

        }


        ListUtils.setDynamicHeight(lv_request);
        ListUtils.setDynamicHeight(lv_sharing);
    }

    private void grantGuestAccess(String device_number, int request_id, int user_id, String nickName, int position) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.grantGuestAccess(hashMap1, new RequestGrantGuestAccess(device_number, request_id, user_id)).enqueue(new Callback<ResponseGuestAccessList>() {
            public void onResponse(Call<ResponseGuestAccessList> call, final Response<ResponseGuestAccessList> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                    ResponseGuestAccessList responseAddCharger = (ResponseGuestAccessList) response.body();
                    Logger.e("Response 123: " + responseAddCharger.toString());

                    if (responseAddCharger.status == 1) {
                        userChargerMappingBLESync(device_number, request_id, user_id, nickName, position);

                    } else if (responseAddCharger.status == 2) {

                    }
                    Toast.makeText(ChargerSharingActivity.this, responseAddCharger.msg, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    progress.dismiss();
                    Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
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
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            }
        });


    }

    private void denyGuestAccess(String device_number, int request_id, int user_id) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.denyGuestAccess(hashMap1, new RequestDenyGuestAccess(device_number, request_id, user_id)).enqueue(new Callback<ResponseGuestAccessList>() {
            public void onResponse(Call<ResponseGuestAccessList> call, final Response<ResponseGuestAccessList> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                    ResponseGuestAccessList responseAddCharger = (ResponseGuestAccessList) response.body();
                    Logger.e("Response 123: " + responseAddCharger.toString());

                    getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));

                       /* if (responseAddCharger.status == 1) {

                        } else if (responseAddCharger.status == 2) {

                        }*/
                    Toast.makeText(ChargerSharingActivity.this, responseAddCharger.msg, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
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
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            }
        });


    }

    private void userChargerMappingBLESync(String device_number, int request_id, int user_id, String nickname, int position) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));

        String[] parts = device_number.split("#");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556

        this.apiInterface.userChargerMappingBLESync(hashMap1, new RequestAddChargerSyncBLE(part2, " ", nickname, user_id, 0.0, 0.0, "Y", "", 1)).enqueue(new Callback<ResponseAddChargerSyncBLE>() {
            public void onResponse(Call<ResponseAddChargerSyncBLE> call, final Response<ResponseAddChargerSyncBLE> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        ResponseAddChargerSyncBLE ResponseAddCharger = (ResponseAddChargerSyncBLE) response.body();
//                        itemsModelListFiltered.remove(position);
//                        notifyDataSetChanged();
                        Logger.e("Response:1212 " + ResponseAddCharger.toString());
                        getReceivedChargerRequest(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(ChargerSharingActivity.this, ChargerSharingActivity.this.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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

            public void onFailure(Call<ResponseAddChargerSyncBLE> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
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
                        AppUtils.SUCCESS_CODE,
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            }
        });


    }

    void dialog_check(String message, String request_type, int position) {
        dialog_allow_permission = new Dialog(ChargerSharingActivity.this);
        dialog_allow_permission.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_allow_permission.setContentView(R.layout.app_dialog);
        dialog_allow_permission.setCancelable(false);
        TextView btn_ok = (TextView) dialog_allow_permission.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_allow_permission.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_allow_permission.findViewById(R.id.tv_message);
        tv_message.setText(message);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("allow".equalsIgnoreCase(request_type)) {

                    if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
                        progress = new ProgressDialog(ChargerSharingActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();

                        grantGuestAccess(list_request.get(position).deviceNumber, list_request.get(position).id, list_request.get(position).userId, list_request.get(position).userName, position);

                    } else {
                        Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                    dialog_allow_permission.dismiss();


                } else if ("deny".equalsIgnoreCase(request_type)) {


                    if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
                        progress = new ProgressDialog(ChargerSharingActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();

                        denyGuestAccess(list_request.get(position).deviceNumber, list_request.get(position).id, list_request.get(position).userId);

                    } else {
                        Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }

                    dialog_allow_permission.dismiss();

                } else if ("revoke".equalsIgnoreCase(request_type)) {

                    String[] parts = list_sharing.get(position).deviceNumber.split("#");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556

                    if (Utils.isNetworkConnected(ChargerSharingActivity.this)) {
                        progress = new ProgressDialog(ChargerSharingActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        getRevokeCharger(list_sharing.get(position).id, list_sharing.get(position).deviceNumber, list_sharing.get(position).userId, Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0), part2);

                    } else {
                        Toast.makeText(ChargerSharingActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }

                    dialog_allow_permission.dismiss();

                } else {

                    dialog_allow_permission.dismiss();

                }

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_allow_permission.dismiss();

            }
        });
        dialog_allow_permission.show();


    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ChargerSharingActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


}