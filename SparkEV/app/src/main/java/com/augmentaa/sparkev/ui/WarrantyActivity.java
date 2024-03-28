package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.ChargerListAdapter;
import com.augmentaa.sparkev.model.signup.charger_details.ChargerDetails;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.model.signup.get_warrenty.RespponseGetWarranty;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyActivity extends AppCompatActivity {

    LinearLayout ly_charger, ly_benefit, ly_plan_one, ly_plan_two;
    TextView tv_charger, tv_exp_date, tv_benefit, tv_y_do_need, tv_under_cover, tv_price_one, tv_price_two, tv_plan_one, tv_plan_two;
    TextView tv_benefit_des, tv_y_do_need_des, tv_under_cover_des;

    boolean isCheckedBenefit, isCheckedWhyDoYouNeed, isCheckedUnder, isCheckedCharger,isChargerDetails;
    ImageView img_back;
    List<ChargerDetails> chargerDetails_data;

    ResponseGetChargerList list_chargerdata;
//    Dialog dialogChargerList;

    APIInterface apiInterface, apippInterface;
    ProgressDialog progress;
    ListView listView;
    String charger_sr_no, nick_name;

    RespponseGetWarranty respponseGetWarranty;
    Dialog dialog_warranty;
    BottomSheetDialog dialogChargerList;
    Data warranty_data;
    TextView tv_exiting_plan,tv_active_expired,tv_valid_upto,tv_activated,tv_click;
    LinearLayout ly_charger_details;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warranty);
        getSupportActionBar().hide();

        img_back = findViewById(R.id.back);
        ly_charger = findViewById(R.id.ly_charger);
        ly_benefit = findViewById(R.id.ly_benefits);
        ly_plan_one = findViewById(R.id.ly_warranty_one);
        ly_plan_two = findViewById(R.id.ly_warranty_two);
        ly_charger_details=findViewById(R.id.ly_charger_details);

        tv_charger = findViewById(R.id.charger_name);

        tv_exiting_plan = findViewById(R.id.exiting_plan);
        tv_active_expired = findViewById(R.id.tv_active_expired);
        tv_valid_upto = findViewById(R.id.valid_upto);
        tv_activated = findViewById(R.id.activate_on);
        tv_click = findViewById(R.id.click);

        tv_exp_date = findViewById(R.id.expirydate);
        tv_benefit = findViewById(R.id.tv_benefits);
        tv_y_do_need = findViewById(R.id.why_do_u_need);
        tv_under_cover = findViewById(R.id.tv_under_cover);
        tv_price_one = findViewById(R.id.warranty_plan_price_one);
        tv_price_two = findViewById(R.id.warranty_plan_price_two);
        tv_plan_one = findViewById(R.id.warranty_plan_one);
        tv_plan_two = findViewById(R.id.warranty_plan_two);

        tv_y_do_need_des = findViewById(R.id.why_do_u_need_des);
        tv_under_cover_des = findViewById(R.id.tv_under_cover_dec);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apippInterface = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        progress = new ProgressDialog(this);

        Logger.e("Charger id===" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) + "  " + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

      /*  try {
            if (Pref.getValue(Pref.TYPE.NICK_NAME.toString(),null) != null) {
                if ("".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(),null)) || " ".equalsIgnoreCase(Pref.getValue(Pref.TYPE.NICK_NAME.toString(),null))) {
                    tv_charger.setText("" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(),null));

                } else {
                    tv_charger.setText("" + nick_name);

                }

            } else {
                tv_charger.setText("" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(),null));

            }


        } catch (Exception e) {

        }*/
       /* if (Utils.isNetworkConnected(WarrantyActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getCheckWarrantyStatus(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "CHARGERTEST0006");

        } else {
            Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }*/


        if (Utils.isNetworkConnected(WarrantyActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));
        } else {
            Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }




       tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isChargerDetails) {
                    isChargerDetails = true;
                    tv_click.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);
                    ly_charger_details.setVisibility(View.VISIBLE);
//                    tv_exp_date.setVisibility(View.GONE);
                } else {
                    isChargerDetails = false;
                    tv_click.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);
                    ly_charger_details.setVisibility(View.GONE);
//                    tv_exp_date.setVisibility(View.VISIBLE);
                }

            }
        });

        ly_charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (!isCheckedCharger) {
                    isCheckedCharger = true;
                    tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedBenefit = false;
                    tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }
*/
                if (list_chargerdata != null) {
                    if (list_chargerdata.data.size() > 0) {
                        dialogChargerList();
                    } else {
                        Toast.makeText(WarrantyActivity.this, "No Charger Available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WarrantyActivity.this, "No Charger Available", Toast.LENGTH_LONG).show();

                }
            }
        });


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

        tv_under_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCheckedUnder) {
                    isCheckedUnder = true;
                    tv_under_cover_des.setVisibility(View.VISIBLE);
                    tv_under_cover.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.up_arrow, 0);

                } else {
                    isCheckedUnder = false;
                    tv_under_cover_des.setVisibility(View.GONE);
                    tv_under_cover.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                }

            }
        });

        ly_plan_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.e("data2222===" + warranty_data.toString());

                if ("Y".equalsIgnoreCase(warranty_data.canRenewWarranty) && "Y".equalsIgnoreCase(warranty_data.renewalPending)) {
                    dialog_check("Your charger is out of warranty now. Please click below to request.", "Request", warranty_data);

                } else if ("Y".equalsIgnoreCase(warranty_data.canRenewWarranty) && "N".equalsIgnoreCase(warranty_data.renewalPending)) {
                    respponseGetWarranty.pos = 1;
                    Intent intent = new Intent(WarrantyActivity.this, WarrantyPurchaseActivity.class);
                    intent.putExtra("data", respponseGetWarranty);
                    startActivity(intent);
                } else if ("P".equalsIgnoreCase(warranty_data.canRenewWarranty) && "Y".equalsIgnoreCase(warranty_data.renewalPending)) {
                    dialog_check("Your request is pending for closure. To know more please  reach out us. ", "Contact Us", warranty_data);

                }


            }
        });
        ly_plan_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Logger.e("data111===" + warranty_data.toString());

                if ("Y".equalsIgnoreCase(warranty_data.canRenewWarranty) && "Y".equalsIgnoreCase(warranty_data.renewalPending)) {
                    dialog_check("Your warranty has been expired, please send request for warranty renewal.", "Request", warranty_data);

                } else if ("Y".equalsIgnoreCase(warranty_data.canRenewWarranty) && "N".equalsIgnoreCase(warranty_data.renewalPending) || "Y".equalsIgnoreCase(warranty_data.canRenewWarranty) && "G".equalsIgnoreCase(warranty_data.renewalPending.toUpperCase())) {
                    respponseGetWarranty.pos = 0;
                    Intent intent = new Intent(WarrantyActivity.this, WarrantyPurchaseActivity.class);
                    intent.putExtra("data", respponseGetWarranty);
                    startActivity(intent);
                } else if ("P".equalsIgnoreCase(warranty_data.canRenewWarranty) && "Y".equalsIgnoreCase(warranty_data.renewalPending)) {
                    dialog_check("Your warranty has been expired and request sent already ", "Request sent", warranty_data);

                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    void dialogChargerList() {
        dialogChargerList = new BottomSheetDialog(this);
        dialogChargerList.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.frag_charger_list, null);
        dialogChargerList.setContentView(view);

//        dialogChargerList = new Dialog(WarrantyActivity.this);
        listView = dialogChargerList.findViewById(R.id.listview);
        dialogChargerList.setCancelable(true);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        dialogChargerList.show();

        if (list_chargerdata.data.size() > 0) {
            charger_sr_no = list_chargerdata.data.get(0).serialNo;
            ChargerListAdapter chargerAdapter = new ChargerListAdapter(WarrantyActivity.this, list_chargerdata.data);
            listView.setAdapter(chargerAdapter);
            chargerAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    charger_sr_no = list_chargerdata.data.get(position).serialNo;
                    nick_name = list_chargerdata.data.get(position).nickName;

//                    tv_exp_date.setText("Warranty (expires. " + AppUtils.getOnlyDate(list_chargerdata.data.get(position).exipry) + ")");

//                    tv_exp_date.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(position).exipry));
                    tv_valid_upto.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(position).exipry));
                    tv_activated.setText("Activate on : " + AppUtils.getDateWarranty(list_chargerdata.data.get(position).startDate));
                    tv_exiting_plan.setText("Existing Plan : " + list_chargerdata.data.get(position).name);

                    warranty_data = list_chargerdata.data.get(position);
                    respponseGetWarranty.charger_sr_no = list_chargerdata.data.get(position).serialNo;
                    respponseGetWarranty.station_id = list_chargerdata.data.get(position).station_id;

                    try {
                        if (nick_name != null) {
                            if ("".equalsIgnoreCase(nick_name) || " ".equalsIgnoreCase(nick_name)) {
                                tv_charger.setText("" + charger_sr_no);

                            } else {
                                tv_charger.setText("" + nick_name);

                            }

                        } else {
                            tv_charger.setText("" + charger_sr_no);

                        }


                    } catch (Exception e) {

                    }
                    dialogChargerList.dismiss();
                    

                  /*  if ("Y".equalsIgnoreCase(list_chargerdata.data.get(position).canRenewWarranty) && "Y".equalsIgnoreCase(list_chargerdata.data.get(position).renewalPending)) {
                        dialog_check("Your warranty has been expired, please send request for warranty renewal.", "Request", list_chargerdata.data.get(position));

                    } else if ("Y".equalsIgnoreCase(list_chargerdata.data.get(position).canRenewWarranty) && "N".equalsIgnoreCase(list_chargerdata.data.get(position).renewalPending)) {
                        dialogChargerList.dismiss();
                    } else if ("P".equalsIgnoreCase(list_chargerdata.data.get(position).canRenewWarranty) && "Y".equalsIgnoreCase(list_chargerdata.data.get(position).renewalPending)) {
                        dialog_check("Your warranty has been expired and request sent already ", "Request sent", list_chargerdata.data.get(position));

                    }
*/
                }
            });
        } else {

        }

    }


    public void getChargerList(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseGetChargerList> call = apippInterface.getChargerListforWarranty(hashMap1, userId);
        call.enqueue(new Callback<ResponseGetChargerList>() {
            @Override
            public void onResponse(Call<ResponseGetChargerList> call, Response<ResponseGetChargerList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {

                    list_chargerdata = (ResponseGetChargerList) response.body();
                    if (list_chargerdata.status) {
                        if (list_chargerdata.data.size() > 0) {
                            if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) != null) {
                                for (int i = 0; i < list_chargerdata.data.size(); i++) {
                                    if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null).equals(list_chargerdata.data.get(i).serialNo)) {
                                        Logger.e("Response data " + list_chargerdata.toString());
//                    charger_sr_no = list_chargerdata.data.get(0).serialNo;

                                        warranty_data = list_chargerdata.data.get(i);

                                        if ("Y".equalsIgnoreCase(list_chargerdata.data.get(i).canRenewWarranty) && "Y".equalsIgnoreCase(list_chargerdata.data.get(i).renewalPending)) {
                                            dialog_check("Your warranty has been expired, please send request for warranty renewal.", "Request", warranty_data);

                                        } else if ("Y".equalsIgnoreCase(list_chargerdata.data.get(i).canRenewWarranty) && "N".equalsIgnoreCase(list_chargerdata.data.get(i).renewalPending)) {

                                        } else if ("P".equalsIgnoreCase(list_chargerdata.data.get(i).canRenewWarranty) && "Y".equalsIgnoreCase(list_chargerdata.data.get(i).renewalPending)) {
                                            dialog_check("Your warranty has been expired and request sent already ", "Request sent", warranty_data);

                                        }

                                        if (list_chargerdata.data.get(i).nickName != null) {
                                            if (" ".equalsIgnoreCase(list_chargerdata.data.get(i).nickName)) {
                                                tv_charger.setText("" + list_chargerdata.data.get(i).serialNo);

                                            } else {
                                                tv_charger.setText("" + list_chargerdata.data.get(i).nickName);

                                            }
                                        } else {
                                            tv_charger.setText("" + list_chargerdata.data.get(i).serialNo);

                                        }
//                                        tv_exp_date.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(i).exipry));
                                        tv_valid_upto.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(i).exipry));
                                        tv_activated.setText("Activate on : " + AppUtils.getDateWarranty(list_chargerdata.data.get(i).startDate));
                                        tv_exiting_plan.setText("Existing Plan : " + list_chargerdata.data.get(i).name);
                                        getWarranty(Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_chargerdata.data.get(i).address, list_chargerdata.data.get(i).address1, list_chargerdata.data.get(i).address2, list_chargerdata.data.get(i).state_id, list_chargerdata.data.get(i).countryId, list_chargerdata.data.get(i).countryName, list_chargerdata.data.get(i).stateName, list_chargerdata.data.get(i).pin, list_chargerdata.data.get(i).station_id);


                                        if (list_chargerdata.data.size() == 1) {
                                            ly_charger.setClickable(false);
                                            ly_charger.setFocusable(false);
                                            ly_charger.setFocusableInTouchMode(false);
//                                            tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


                                        } else {
                                            ly_charger.setClickable(true);
                                            ly_charger.setFocusable(true);
                                            ly_charger.setFocusableInTouchMode(true);
//                                            tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                                        }


                                    }
                                }
                            } else {

                                if (list_chargerdata.data.get(0).nickName != null) {
                                    if (" ".equalsIgnoreCase(list_chargerdata.data.get(0).nickName)) {
                                        tv_charger.setText("" + list_chargerdata.data.get(0).serialNo);

                                    } else {
                                        tv_charger.setText("" + list_chargerdata.data.get(0).nickName);

                                    }
                                } else {
                                    tv_charger.setText("" + list_chargerdata.data.get(0).serialNo);

                                }
//                                tv_exp_date.setText("Warranty (expires. " + AppUtils.getOnlyDate(list_chargerdata.data.get(0).exipry) + ")");
//                                tv_exp_date.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(0).exipry));
                                tv_valid_upto.setText("Valid Upto : " + AppUtils.getDateWarranty(list_chargerdata.data.get(0).exipry));
                                tv_activated.setText("Activate on : " + AppUtils.getDateWarranty(list_chargerdata.data.get(0).startDate));
                                tv_exiting_plan.setText("Existing Plan : " +list_chargerdata.data.get(0).name);
                                getWarranty(Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_chargerdata.data.get(0).address, list_chargerdata.data.get(0).address1, list_chargerdata.data.get(0).address2, list_chargerdata.data.get(0).state_id, list_chargerdata.data.get(0).countryId, list_chargerdata.data.get(0).countryName, list_chargerdata.data.get(0).stateName, list_chargerdata.data.get(0).pin, list_chargerdata.data.get(0).station_id);


                            }
                        }

                    }


//
                    } catch (Exception e) {
                        progress.dismiss();
                        Logger.e("Response" + e.getMessage());
                        Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               WarrantyActivity.class.getSimpleName(),
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
                    Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           WarrantyActivity.class.getSimpleName(),
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
            public void onFailure(Call<ResponseGetChargerList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex  " + t.getMessage());
                Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       WarrantyActivity.class.getSimpleName(),
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


/*
    public void getCheckWarrantyStatus(int userId, String chargerId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseCheckWarranty> call = apippInterface.checkWarrantyStatus(hashMap1, userId, chargerId);
        call.enqueue(new Callback<ResponseCheckWarranty>() {
            @Override
            public void onResponse(Call<ResponseCheckWarranty> call, Response<ResponseCheckWarranty> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
//                    try {
                    ResponseCheckWarranty loginresponse = (ResponseCheckWarranty) response.body();
                    Logger.e("Response list data" + loginresponse.toString() + "   " + loginresponse.data.size());

                    if (loginresponse.data.get(0).endDate != null) {
                        tv_exp_date.setText("Warranty (expires. " + AppUtils.getOnlyDate(loginresponse.data.get(0).endDate) + ")");
                    }


//                        getWarranty(chargerId);
//                    } catch (Exception e) {
//                        Log.d("Response", e.getMessage());
//                        Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
//
//                    }
                } else {
                    Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseCheckWarranty> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       WarrantyActivity.class.getSimpleName(),
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
*/


    public void getWarranty(String charger_sr_no, String address, String address1, String address2, int state_id, int countryId, String countryName, String stateName, String pin, int station_id) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<RespponseGetWarranty> call = apippInterface.getWarranty(hashMap1, charger_sr_no);
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
                            tv_price_one.setText(getResources().getString(R.string.Rs) + " " + respponseGetWarranty.data.get(0).mrpInctax);
                            tv_price_two.setText(getResources().getString(R.string.Rs) + " " + respponseGetWarranty.data.get(1).mrpInctax);
                            respponseGetWarranty.address = address;
                            respponseGetWarranty.address1 = address1;
                            respponseGetWarranty.address2 = address2;
                            respponseGetWarranty.country_id = countryId;
                            respponseGetWarranty.country_name = countryName;
                            respponseGetWarranty.state_id = state_id;
                            respponseGetWarranty.state_name = stateName;
                            respponseGetWarranty.pincode = pin;
                            respponseGetWarranty.charger_sr_no = charger_sr_no;
                            respponseGetWarranty.station_id = station_id;
                        } else {
                            if ("Token is not valid".equalsIgnoreCase(respponseGetWarranty.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(WarrantyActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            }
                        }


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               WarrantyActivity.class.getSimpleName(),
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
                    Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           WarrantyActivity.class.getSimpleName(),
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
                Toast.makeText(WarrantyActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       WarrantyActivity.class.getSimpleName(),
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

    void dialog_check(String message, String request_type, Data data) {
        dialog_warranty = new Dialog(WarrantyActivity.this);
        dialog_warranty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warranty.setContentView(R.layout.app_dialog);
        dialog_warranty.setCancelable(false);
        TextView btn_ok = (TextView) dialog_warranty.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_warranty.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_warranty.findViewById(R.id.tv_message);
        tv_message.setText(message);
        btn_ok.setText(request_type);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Request".equalsIgnoreCase(request_type)) {
                    Intent intent = new Intent(WarrantyActivity.this, WarrantRenewalRequestActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                    dialog_warranty.dismiss();
                    if (dialogChargerList != null) {
                        dialogChargerList.dismiss();
                    }
                    Logger.e("warranty data" + data.toString());

                } else {
                    Intent intent = new Intent(WarrantyActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                    dialog_warranty.dismiss();
                    if (dialogChargerList != null) {
                        dialogChargerList.dismiss();
                    }
                }

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_warranty.dismiss();

            }
        });
        dialog_warranty.show();


    }



    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(WarrantyActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }





}