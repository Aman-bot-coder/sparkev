package com.augmentaa.sparkev.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.adapter.UpgradeBLEChargerListAdapter;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_payment_mode.PaymentMode;
import com.augmentaa.sparkev.model.signup.initiate_upgrade_request.RequestUpgradeInitiate;
import com.augmentaa.sparkev.model.signup.initiate_upgrade_request.ResponseInitiateRequest;
import com.augmentaa.sparkev.model.signup.payment.PaymentRequest;
import com.augmentaa.sparkev.model.signup.payment_status.RequestToPaymentStatus;
import com.augmentaa.sparkev.model.signup.payment_status.ResponseToPayment;
import com.augmentaa.sparkev.model.signup.razorpay.CreateOrderResponse;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.model.signup.upgrage_charger_list.ResponseUpgradeChargerList;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestUpgradeActivity extends AppCompatActivity implements GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton, PaymentResultWithDataListener, ExternalWalletListener {

    EditText et_address, et_remarks;
    TextView tv_country, tv_state, tv_date, tv_time, tv_mobile, tv_name, tv_charger, tv_amount, tv_amount_text;
    Button btn_proceed;
    APIInterface apiInterface;
    ProgressDialog progress;

    ResponseUpgradeChargerList responseUpgradelist;
    int state_id, country_id;
    String charger_serial_no;
    BottomSheetDialog dialog_state, dialog_country, dialogChargerList;
    ResponseCountry responseCountry;
    List<ResponseState> responseState;
    ImageView img_back;


    int selected_hour, selected_min;
    String future_date, futureTime;
    String current_date, currentTime, postdate;
    final Calendar currentDate = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    int charger_id;


    APIInterface apiInterfacepp;
    String order_id;
    String host = "https://securegw-stage.paytm.in/";
    Integer ActivityRequestCode = 2;
    Dialog dialogPaymentSuccessfull;
    String payment_response;
    int station_id;

    Dialog dialog_warranty;
    PaymentMode paymentMode;
    List<com.augmentaa.sparkev.model.signup.get_payment_mode.Data> list;
    private AlertDialog.Builder alertDialogBuilder;
    String payment_mode;
    com.augmentaa.sparkev.model.signup.get_warrenty.Data upgrade_data;
    //    String payment_mode;
    ListView listView;

    Dialog dialog_payment_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_upgrade);
        getSupportActionBar().hide();
        tv_country = findViewById(R.id.country);
        tv_state = findViewById(R.id.state);
        tv_date = findViewById(R.id.date);
        tv_time = findViewById(R.id.time);
        tv_mobile = findViewById(R.id.mobile);
        tv_name = findViewById(R.id.name);
        tv_charger = findViewById(R.id.charger_list);
        tv_amount = findViewById(R.id.amount);
        tv_amount_text = findViewById(R.id.tv_amount);
        et_address = findViewById(R.id.address);
        et_remarks = findViewById(R.id.remarks);
        btn_proceed = findViewById(R.id.btn_request_upgrade);
        img_back = findViewById(R.id.back);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        progress = new ProgressDialog(this);
        this.apiInterfacepp = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        dialog_payment_cancel = new Dialog(RequestUpgradeActivity.this);
        list = new ArrayList<>();
        dialog_warranty = new Dialog(RequestUpgradeActivity.this);
        try {
            upgrade_data = getIntent().getParcelableExtra("data");
            tv_amount.setText(getResources().getString(R.string.Rs) + " " + upgrade_data.mrpInctax);

            Logger.e("=====Upgrade Data===" + upgrade_data.mrpInctax + "   " + upgrade_data.getSerial_no() + "  " + upgrade_data.serial_no);

        } catch (Exception e) {

        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
                    if (et_address.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_LONG).show();
                    } else if (et_remarks.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();
                    } 
                    /*else if (et_remarks.getText().toString().length() >= 200 || et_remarks.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();

                    } else if (date.get(Calendar.DAY_OF_WEEK) == date.SUNDAY) {
                        Toast.makeText(RequestUpgradeActivity.this, "Please select other day", Toast.LENGTH_LONG).show();
                        Logger.e("====" + currentDate.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                    }*/
                    else {
                        if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            postdate = future_date + " " + futureTime;
                            Logger.e("Post Date Time  " + postdate + "  " + parseDateTime(postdate));
                            postCallRequest();
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }


                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }


            }
        });
        if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
            try {

                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                getUpgradeChargerList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0));

            } catch (Exception e) {

            }


        } else {
            Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }


        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_getAllCountryList();

            }
        });

        tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (responseState.size() > 0) {
                        dialog_getAllStateList();
                    } else {
                        Toast.makeText(getApplicationContext(), "State not found", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "State not found", Toast.LENGTH_LONG).show();

                }

            }
        });

/*
        if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getPaymentMode();

        } else {
            Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }*/

        tv_charger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (responseUpgradelist != null) {
                    if (responseUpgradelist.data.size() > 1) {
                        dialogChargerList();
                    }

                }
            }
        });


        current_date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText("" + current_date);
        future_date = current_date;
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy");
//                final String currentDateandTime = sdf1.format(new Date());
//                Log.v("", "The choosen one " + currentDateandTime);
//                tv_date.setText(new SimpleDateFormat("dd MMMM yyyy").format(date.getTime()));
                date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestUpgradeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        String date_time = new SimpleDateFormat("dd MMMM yyyy-HH:mm").format(date.getTime());
                        String s1 = date_time;
                        String[] words = s1.split("-");//splits the string based on whitespace
                        //using java foreach loop to print elements of string array
                        future_date = words[0];
//                        futureTime = words[1] + " " + words[2].toLowerCase();
//
                       /* if (date.get(Calendar.DAY_OF_WEEK) == date.SUNDAY) {
                            Toast.makeText(RequestUpgradeActivity.this, "Please select other day", Toast.LENGTH_LONG).show();
                            Logger.e("====" + date.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                        } else {
                            tv_date.setText(future_date);
                            Toast.makeText(RequestUpgradeActivity.this, "Your selected date " + future_date, Toast.LENGTH_LONG).show();


                        }*/
                        tv_date.setText(future_date);
                        Logger.e("====" + date.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

                // Add 7 days to Calendar
                date.add(Calendar.DATE, 60);

                // Set the Calendar new date as maximum date of date picker
                datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());

                // Subtract 6 days from Calendar updated date
                date.add(Calendar.DATE, -60);

                // Set the Calendar new date as minimum date of date picker
                datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());

//                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

//                date.add(Calendar.HOUR_OF_DAY,2);
//                date.getTime();
                datePickerDialog.show();


            }
        });


        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        futureTime = currentTime;
        tv_time.setText("" + currentTime);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        selected_hour = hour;
                        selected_min = minute;
                        futureTime = String.format("%02d", hour) + ":" + String.format("%02d", minute);

                        if (AppUtils.isDateValidation(current_date, future_date)) {
                            if (AppUtils.isTimeBetweenNineToSixValidation(currentTime, futureTime)) {
                                tv_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                Toast.makeText(RequestUpgradeActivity.this, "Your selected time " + futureTime, Toast.LENGTH_LONG).show();

                            } else {
//                                Toast.makeText(RequestUpgradeActivity.this, "Please select time between 09:00 to 18:00", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + currentTime + "  " + "   " + futureTime + "   " + AppUtils.isTimeBetweenNineToSixValidation(currentTime, currentTime));

                        } else {

                            tv_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));

                            /*if (AppUtils.isTimeValidation(currentTime, futureTime)) {
                                tv_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
//                                Toast.makeText(RequestUpgradeActivity.this, "Your selected time " + futureTime, Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(RequestUpgradeActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + currentTime + "  " + "   " + futureTime + "   " + AppUtils.isTimeValidation(currentTime, currentTime));
*/

                        }

                        Logger.e("Date Compare" + current_date + "  " + "   " + future_date + "   " + AppUtils.isDateValidation(current_date, future_date));


                    }
                };

                Calendar c = Calendar.getInstance();
                final TimePickerDialog timePickerDialog = new TimePickerDialog(RequestUpgradeActivity.this, timePickerListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();


            }
        });


    }

    public void getUpgradeChargerList(int user_id) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseUpgradeChargerList> call = apiInterface.getBleChargersByUser(hashMap1, user_id);
        call.enqueue(new Callback<ResponseUpgradeChargerList>() {
            @Override
            public void onResponse(Call<ResponseUpgradeChargerList> call, Response<ResponseUpgradeChargerList> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                try {
                    if (response.code() == 200) {
                        try {
                            responseUpgradelist = (ResponseUpgradeChargerList) response.body();
                            Logger.e("Warranty response===" + responseUpgradelist.toString());
                            if (responseUpgradelist.status) {
                                if (responseUpgradelist.data.size() > 0) {
                                    for (int i = 0; i < responseUpgradelist.data.size(); i++) {
                                        Logger.e("====Charger details== " + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) + "  " + responseUpgradelist.data.get(i).serial_no);
                                        if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null).equalsIgnoreCase(responseUpgradelist.data.get(i).serial_no)) {
                                            et_address.setText(responseUpgradelist.data.get(i).address);
                                            tv_state.setText(responseUpgradelist.data.get(i).state_name);
                                            tv_country.setText(responseUpgradelist.data.get(i).country_name);
                                            tv_charger.setText(responseUpgradelist.data.get(i).nick_name);
                                            tv_mobile.setText(Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                                            country_id = responseUpgradelist.data.get(i).country_id;
                                            state_id = responseUpgradelist.data.get(i).state_id;
                                            charger_id = responseUpgradelist.data.get(i).charger_id;
                                            getAllCountryList();
                                            getAllStateList(country_id);
                                            if (Pref.getValue(Pref.TYPE.L_NAME.toString(), null) != null) {
                                                tv_name.setText(Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + " " + Pref.getValue(Pref.TYPE.L_NAME.toString(), null));
                                            } else {
                                                tv_name.setText(Pref.getValue(Pref.TYPE.F_NAME.toString(), null));
                                            }
                                        }
                                    }
                                    if (responseUpgradelist.data.size() == 1) {
                                        tv_charger.setClickable(false);
                                        tv_charger.setFocusable(false);
                                        tv_charger.setFocusableInTouchMode(false);
                                        tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    } else {
                                        tv_charger.setClickable(true);
                                        tv_charger.setFocusable(true);
                                        tv_charger.setFocusableInTouchMode(true);
                                        tv_charger.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);

                                    }
                                }


                            } else {
                                if ("Token is not valid".equalsIgnoreCase(responseUpgradelist.message)) {
                                    Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                    Pref.clear();
                                    Intent intent21 = new Intent(RequestUpgradeActivity.this, SignInActivity.class);
                                    startActivity(intent21);
                                    finishAffinity();
                                    Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                                }
                            }


                        } catch (Exception e) {
                            Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                    RequestUpgradeActivity.class.getSimpleName(),
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
                        Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                RequestUpgradeActivity.class.getSimpleName(),
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
            public void onFailure(Call<ResponseUpgradeChargerList> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        RequestUpgradeActivity.class.getSimpleName(),
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
        Intent intent = new Intent(RequestUpgradeActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

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
//                                sp_country.setAdapter(adapter);
//                                tvCountry.setText(responseCountry.data.get(1).name);

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

    public void getAllStateList(int country_id) {
        Call<List<ResponseState>> call = apiInterface.getAllStateList(country_id);
        call.enqueue(new Callback<List<ResponseState>>() {
            @Override
            public void onResponse(Call<List<ResponseState>> call, Response<List<ResponseState>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                responseState = response.body();

                if (response.code() == 200) {
                    try {


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<ResponseState>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    void dialog_getAllCountryList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_country = new BottomSheetDialog(this);
        dialog_country.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_country.setCancelable(true);
        ListView lv_model = (ListView) dialog_country.findViewById(R.id.listview);
        EditText et_search = dialog_country.findViewById(R.id.search);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(RequestUpgradeActivity.this, responseCountry.data, this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        et_search.addTextChangedListener(new TextWatcher() {
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
      /*  lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_state.dismiss();
                country_id = responseCountry.data.get(position).id;
                tv_country.setText(responseCountry.data.get(position).name);
                getAllStateList(country_id);

            }
        });*/

        dialog_country.show();

    }


    void dialog_getAllStateList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_state = new BottomSheetDialog(this);
        dialog_state.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_state.setCancelable(true);
        ListView lv_model = (ListView) dialog_state.findViewById(R.id.listview);
        EditText et_search = dialog_state.findViewById(R.id.search);


        GetAllStateListAdapter adapter = new GetAllStateListAdapter(RequestUpgradeActivity.this, responseState, this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        et_search.addTextChangedListener(new TextWatcher() {
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

       /* lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                state_id = responseState.get(pos).id;
                tv_state.setText(responseState.get(pos).name);


            }
        });
*/

        dialog_state.show();

    }

    @Override
    public void search_state(int position, List<ResponseState> itemsModelsl) {
        Logger.e("=====Position ===" + itemsModelsl.size() + "   " + position);

        state_id = itemsModelsl.get(position).id;
        tv_state.setText(itemsModelsl.get(position).name);
        dialog_state.dismiss();

    }

    @Override
    public void search_country(int position, List<Data> list) {

        dialog_country.dismiss();
        country_id = list.get(position).id;
        tv_country.setText(list.get(position).name);
        tv_state.setText(null);
        tv_country.setHint("Please select your state");
        tv_state.setHintTextColor(getResources().getColor(R.color.colorText));
        getAllStateList(country_id);

    }

    private String parseDateTime(String date) {
        Date initDate = null;
        String parsedTime = null;
        try {

            initDate = new SimpleDateFormat("dd MMMM yyyy HH:mm").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsedTime = formatter.format(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedTime;
    }

    private void postCallRequest() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.requestIniatedByCustomer(hashMap1, new RequestUpgradeInitiate(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.MOBILE.toString(), null), et_address.getText().toString(), charger_id, parseDateTime(postdate), et_remarks.getText().toString(), "O", state_id, country_id)).enqueue(new Callback<ResponseInitiateRequest>() {
            //        this.apiInterface.PostSignIn(new CallRequest(user_name, password)).enqueue(new Callback<CallRequest>() {
            public void onResponse(Call<ResponseInitiateRequest> call, Response<ResponseInitiateRequest> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseInitiateRequest loginResponse = (ResponseInitiateRequest) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {
                            getPaymentMode();


                        } else {
                            try {
                                Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

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
                                RequestUpgradeActivity.class.getSimpleName(),
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
                            RequestUpgradeActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseInitiateRequest> call, Throwable t) {
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
                        RequestUpgradeActivity.class.getSimpleName(),
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

    void dialog_paymentMode() {

        dialog_warranty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warranty.setContentView(R.layout.dialog_payment_mode);
        dialog_warranty.setCancelable(true);
        RadioGroup rg_payment = dialog_warranty.findViewById(R.id.radioGroup);
        RadioButton rb_paytm = dialog_warranty.findViewById(R.id.rb_paytm);
        RadioButton rb_razorpay = dialog_warranty.findViewById(R.id.rb_razorpay);
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(CallRequestInfoActivity.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();

                    if ("PayTm".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            Logger.e("Payment Mode " + list.get(0).code + "  " + rb.getText().toString());
                            payment_mode = list.get(0).code;
//                            PaymentResponse(list.get(0).code);
                            createOrderRazorPay(list.get(0).code);
                        } else {
                            Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

                    } else if ("RazorPay".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(RequestUpgradeActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            payment_mode = list.get(1).code;
                            createOrderRazorPay(list.get(1).code);

                            Logger.e("Payment Mode " + list.get(1).code + "  " + rb.getText().toString());
//                            PaymentResponse(list.get(1).code);

                        } else {
                            Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    }
                }

            }
        });


        dialog_warranty.show();


    }


    public void getPaymentMode() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<PaymentMode> call = apiInterfacepp.getWayPaymentList(hashMap1, 4);
        call.enqueue(new Callback<PaymentMode>() {
            @Override
            public void onResponse(Call<PaymentMode> call, Response<PaymentMode> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                paymentMode = (PaymentMode) response.body();
                Logger.e("Warranty response===" + paymentMode.toString());
                if (response.code() == 200) {
                    try {
                        list.clear();
                        if (paymentMode.status) {
                            list = paymentMode.data;
                            if (list.size() > 0) {
                                if (list.size() == 1) {
                                    payment_mode = list.get(0).code;
                                    createOrderRazorPay(list.get(0).code);
                                } else {
                                    dialog_paymentMode();
                                }
                            }

                        } else {
                            if ("Token is not valid".equalsIgnoreCase(paymentMode.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(RequestUpgradeActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            }
                        }


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<PaymentMode> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


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

    private void createOrderRazorPay(String paymentmode) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfacepp.createPayment(hashMap1, new PaymentRequest(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), upgrade_data.mrpInctax, upgrade_data.code, upgrade_data.id, Pref.getValue(Pref.TYPE.MOBILE.toString(), null), "SPIN_ANDROID", upgrade_data.serial_no, paymentmode)).enqueue(new Callback<CreateOrderResponse>() {
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        if (dialog_warranty != null) {
                            dialog_warranty.dismiss();
                        }
                        CreateOrderResponse createOrderResponse = (CreateOrderResponse) response.body();
                        Logger.e("Create Order Response " + createOrderResponse.toString());
                        order_id = createOrderResponse.data.orderid;

                        if ("paytm".equalsIgnoreCase(createOrderResponse.data.paymentmode)) {
                            PaytmOrder paytmOrder = new PaytmOrder(createOrderResponse.data.orderid, createOrderResponse.data.mid, createOrderResponse.data.token, createOrderResponse.data.amount, createOrderResponse.data.callbackurl + createOrderResponse.data.orderid);
                            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
                                @Override
                                public void onTransactionResponse(Bundle bundle) {
//                                    Toast.makeText(PaymentDashboardActivity.this, "Response (onTransactionResponse) : " + bundle.size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("===Trancational Response==88888=" + bundle.toString());
                                    getOrderStatus(order_id, upgrade_data.serial_no, payment_mode);

                                }

                                @Override
                                public void networkNotAvailable() {

                                }

                                @Override
                                public void onErrorProceed(String s) {
                                    Logger.e("onErrorProceed  " + s);

                                }

                                @Override
                                public void clientAuthenticationFailed(String s) {
                                    Logger.e("clientAuthenticationFailed  " + s);

                                }

                                @Override
                                public void someUIErrorOccurred(String s) {
                                    Logger.e("someUIErrorOccurred  " + s);

                                }

                                @Override
                                public void onErrorLoadingWebPage(int i, String s, String s1) {

                                    Logger.e("onErrorLoadingWebPage  " + s + "  " + s1);

                                }

                                @Override
                                public void onBackPressedCancelTransaction() {

                                }

                                @Override
                                public void onTransactionCancel(String s, Bundle bundle) {
                                    Logger.e("someUIErrorOccurred  " + s + "  " + bundle.toString());

                                }
                            });

                            transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
                            transactionManager.setAppInvokeEnabled(true);
                            Logger.e("Trans" + transactionManager.toString());
                            System.out.println("===Trancational Response==22222=");
                            transactionManager.startTransaction(RequestUpgradeActivity.this, ActivityRequestCode);
                            System.out.println("===Trancational Response==333333=");
                        } else {

                            final Checkout co = new Checkout();
                            co.setKeyID(createOrderResponse.data.mid);
                            try {
                                JSONObject options = new JSONObject();
                                options.put("name", "Exicom Power Systems Ltd.");
//                                options.put("description", "Demoing Charges");
                                options.put("order_id", createOrderResponse.data.orderid);
                                options.put("send_sms_hash", true);
                                options.put("allow_rotation", true);
                                //You can omit the image option to fetch the image from dashboard
//                                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//                                options.put("currency", createOrderResponse.data.currency);
                                options.put("amount", createOrderResponse.data.amount);

                                JSONObject preFill = new JSONObject();
                                preFill.put("email", Pref.getValue(Pref.TYPE.EMAIL.toString(), null));
                                preFill.put("contact", Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                                options.put("prefill", preFill);
                                co.open(RequestUpgradeActivity.this, options);
                            } catch (Exception e) {
                                Toast.makeText(RequestUpgradeActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                                e.printStackTrace();
                            }
                        }


                    } catch (Exception e) {
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                RequestUpgradeActivity.class.getSimpleName(),
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


                        Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            RequestUpgradeActivity.class.getSimpleName(),
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

            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        RequestUpgradeActivity.class.getSimpleName(),
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

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            getOrderStatus(order_id, payment_mode, upgrade_data.serial_no);
//            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
//            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            dialog_payment_cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOrderStatus(String order_id, String payment_mode, String charger_serial_no) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfacepp.getCheckorderstatus(hashMap1, new RequestToPaymentStatus(order_id, charger_serial_no, payment_mode)).enqueue(new Callback<ResponseToPayment>() {
            public void onResponse(Call<ResponseToPayment> call, Response<ResponseToPayment> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseToPayment loginResponse = (ResponseToPayment) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {


//                            Intent intent = new Intent(PaymentDashboardActivity.this, PaymentStatusActivity.class);
//                            intent.putExtra("data", loginResponse.data);
//                            startActivity(intent);
                            showPaymentSuccessfulDialog(loginResponse.data.receipt_no);
//                        Toast.makeText(RequestUpgradeActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();


                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                    RequestUpgradeActivity.class.getSimpleName(),
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
                        Toast.makeText(RequestUpgradeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                RequestUpgradeActivity.class.getSimpleName(),
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
                            RequestUpgradeActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseToPayment> call, Throwable t) {
                Logger.e("Excption ===" + t.getMessage());
//                Toast.makeText(RequestUpgradeActivity.this, "111111" + getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        RequestUpgradeActivity.class.getSimpleName(),
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

    void showPaymentSuccessfulDialog(String transaction_id) {
        try {
            dialogPaymentSuccessfull = new Dialog(RequestUpgradeActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialogPaymentSuccessfull.setContentView(R.layout.layout_success_upgrade_request);
            dialogPaymentSuccessfull.setCancelable(false);
            Button btn_home = dialogPaymentSuccessfull.findViewById(R.id.home_page);
            Button btn_track = dialogPaymentSuccessfull.findViewById(R.id.tracking);
            TextView tv_description = dialogPaymentSuccessfull.findViewById(R.id.success_message);
            tv_description.setText("Your request to upgrade you charger has been submitted successfully. You can track your request status by using Request ID : " + transaction_id);

            btn_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RequestUpgradeActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                    dialogPaymentSuccessfull.dismiss();
                }
            });

            btn_track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(RequestUpgradeActivity.this, UpgradeRequestTrackingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("track", "Track");
                    startActivity(intent);
                    finish();
                    dialogPaymentSuccessfull.dismiss();
                }

            });


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

//                    dialogPaymentSuccessfull.dismiss();
//
//                    Intent intent = new Intent(RequestUpgradeActivity.this, ReceiptHistoryActivity.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                   startActivity(intent);
//                    finish();


//            }
//        }, 2500);
            dialogPaymentSuccessfull.show();

        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {


            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Logger.e("Data==>" + key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    Logger.e("data=11222=>" + bundle.get("response"));
                    payment_response = bundle.get("response").toString();
                    getOrderStatus(order_id, payment_mode, upgrade_data.serial_no);


                }

                Logger.e("data=1113333=>" + bundle.get("response"));


                if (bundle.get("response") != null) {
                    if ("NULL".equalsIgnoreCase(bundle.get("response").toString()) || bundle.get("response") == null || bundle.get("response").equals("")) {
//                        Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage"), Toast.LENGTH_SHORT).show();
                    } else {


                    }
                } else {
//                    Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage"), Toast.LENGTH_SHORT).show();

                }


            }


        }


    }

    private void dismissProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    private void dismissProgressDialogCancel() {
        if (dialog_payment_cancel != null && dialog_payment_cancel.isShowing()) {
            dialog_payment_cancel.dismiss();
        }
    }

    @Override
    protected void onStop() {
        dismissProgressDialog();
        dismissProgressDialogCancel();
        super.onStop();

    }


    void dialog_payment_cancel() {

        dialog_payment_cancel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_payment_cancel.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_payment_cancel.setCancelable(false);
        TextView btn_ok = (TextView) dialog_payment_cancel.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_payment_cancel.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_payment_cancel.findViewById(R.id.tv_message);
        tv_message.setText("Payment failed/canceled.                                    ");
        btn_ok.setText("Ok");
        btn_cencel.setText("Skip");
        btn_cencel.setVisibility(View.GONE);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_payment_cancel.dismiss();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_payment_cancel.dismiss();

            }
        });
        dialog_payment_cancel.show();


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

        if (responseUpgradelist.data.size() > 0) {
            UpgradeBLEChargerListAdapter chargerAdapter = new UpgradeBLEChargerListAdapter(RequestUpgradeActivity.this, responseUpgradelist.data);
            listView.setAdapter(chargerAdapter);
            chargerAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int i, long id) {
                    try {
                        et_address.setText(responseUpgradelist.data.get(i).address);
                        tv_state.setText(responseUpgradelist.data.get(i).state_name);
                        tv_country.setText(responseUpgradelist.data.get(i).country_name);
                        tv_charger.setText(responseUpgradelist.data.get(i).nick_name);
                        tv_mobile.setText(Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                        country_id = responseUpgradelist.data.get(i).country_id;
                        state_id = responseUpgradelist.data.get(i).state_id;
                        charger_id = responseUpgradelist.data.get(i).charger_id;

//                    getAllCountryList();
//                    getAllStateList(country_id);
                    } catch (Exception e) {

                    }
                    dialogChargerList.dismiss();

                }
            });
        } else {

        }

    }
}