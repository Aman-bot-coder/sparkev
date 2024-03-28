package com.augmentaa.sparkev.ui;

import static com.augmentaa.sparkev.utils.AppUtils.geDateSessionHistory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.SessionHistoryAdapter;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.session_history.RequestSessionHistory;
import com.augmentaa.sparkev.model.signup.session_history.ResponseSessionHistory;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionHistoryActivity extends AppCompatActivity {

    ListView listView;
    ImageView img_back;
    LinearLayout ly_start_date, ly_end_date;
    APIInterface apiInterface;
    ProgressDialog progress;
    TextView tvRequestNotFound, tv_start_date, tv_end_date, tv_submit;
    String current_date, select_date;
    LinearLayout ly_search;
    RelativeLayout layout;
    TextView tv_get_occp_logs;
    TextView tv_get_ble_logs;
    ToggleButton tb_ble_occp;
    String mode = "OCPP";
    boolean isCheck=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);
        getSupportActionBar().hide();
        listView = findViewById(R.id.listview);
        ly_start_date = findViewById(R.id.ly_start_date);
        ly_end_date = findViewById(R.id.ly_end_date);

        tv_start_date = findViewById(R.id.start_date);
        tv_end_date = findViewById(R.id.end_date);

        ly_search = findViewById(R.id.search);
        img_back = findViewById(R.id.back);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.progress = new ProgressDialog(this);
        tvRequestNotFound = findViewById(R.id.tv_data);

        layout = findViewById(R.id.ly_select_toggle);
        tv_get_occp_logs = findViewById(R.id.get_occp_logs);
        tv_get_ble_logs = findViewById(R.id.get_ble_logs);
        tb_ble_occp = findViewById(R.id.tb_ble);


        current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -30);
        Date d = c.getTime();
        Log.e("Date==>", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d));
        select_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d);

//        tb_ble_occp.setClickable(false);
//        tb_ble_occp.setEnabled(false);
       /* if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                getSessionHistory(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date);
            } else {
                getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date);

            }
        } else {
            Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }*/

        tb_ble_occp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                if (isCheck){

                    if (b) {
                        Logger.e("Checked");
                        if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                            tb_ble_occp.setChecked(true);
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            isCheck=true;
                            getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "", "BLE");
                        } else {
                            Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    } else {

                        if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                            tb_ble_occp.setChecked(false);
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            isCheck=false;
                            getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "", "OCCP");
                        } else {
                            Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                        Logger.e("Unchecked");
                    }
//            }

            }
        });


        if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "current_date", "OCCP");

          /*  if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "current_date", "OCCP");
                    tb_ble_occp.setChecked(false);

                } else {
                    isClickable();
                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "current_date", "BLE");


                }

            } else {
                isClickable();
//                getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");
                getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "", "BLE");


            }*/
        } else {
            Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }




        tv_get_ble_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                    tb_ble_occp.setChecked(true);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "", "BLE");
                } else {
                    Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });


        tv_get_occp_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                    tb_ble_occp.setChecked(false);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "", "", "OCCP");
                } else {
                    Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });


        ly_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_start_date.getText().toString().trim())) {
                    Toast.makeText(SessionHistoryActivity.this, "From Date empty", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(tv_end_date.getText().toString().trim())) {
                    Toast.makeText(SessionHistoryActivity.this, "To Date empty", Toast.LENGTH_LONG).show();

                } else {
                   /* if (NetworkUtil.isConnectivityOk(SessionHistoryActivity.this)) {
                        progress = new ProgressDialog(SessionHistoryActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.setCancelable(false);
                        progress.show();
                        getSessionHistory(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date);

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }*/
                    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1 = null;
                    Date d2 = null;
                    try {
                        d1 = sdformat.parse(select_date);
                        d2 = sdformat.parse(current_date);


                        if (d1.compareTo(d2) > 0) {

                            Toast.makeText(SessionHistoryActivity.this, "Please select valid date.", Toast.LENGTH_LONG).show();


                        } else if (d1.compareTo(d2) < 0) {

                            if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                                /*if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                        progress.setMessage(getResources().getString(R.string.loading));
                                        progress.show();
                                        getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "OCCP");
                                        tb_ble_occp.setChecked(false);

                                    } else {
                                        isClickable();
                                        progress.setMessage(getResources().getString(R.string.loading));
                                        progress.show();
                                        getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");


                                    }

                                } else {
                                    isClickable();
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");


                                }*/

                                if(isCheck){
                                    isClickable();
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");

                                }
                                else {
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "OCCP");

                                }
                            } else {
                                Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                            }

                        } else if (d1.compareTo(d2) == 0) {
                            if (Utils.isNetworkConnected(SessionHistoryActivity.this)) {
                               /* if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                                    if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                                        progress.setMessage(getResources().getString(R.string.loading));
                                        progress.show();
                                        getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "OCCP");
                                        tb_ble_occp.setChecked(false);

                                    } else {
                                        isClickable();
                                        progress.setMessage(getResources().getString(R.string.loading));
                                        progress.show();
                                        getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");


                                    }

                                } else {
                                    isClickable();
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");


                                }*/
                                if(isCheck){
                                    isClickable();
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "BLE");

                                }
                                else {
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    getSessionHistoryMode(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), select_date, current_date, "OCCP");

                                }
                            } else {
                                Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ly_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                final Calendar date = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDateandTime = sdf1.format(new Date());

                Logger.e("The choosen one " + geDateSessionHistory(currentDateandTime));


                tv_end_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));

                DatePickerDialog datePickerDialog = new DatePickerDialog(SessionHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        String date_time = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                        String s1 = date_time;
                        String[] words = s1.split("\\s");//splits the string based on whitespace
                        //using java foreach loop to print elements of string array
                        current_date = words[0];
//                        booking_time = words[1] + " " + words[2].toLowerCase();
                        tv_end_date.setText(geDateSessionHistory(current_date));
//


                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

                // Add 3 days to Calendar
                date.add(Calendar.DATE, 0);


            /*
                getTimeInMillis()
                    Returns the time represented by this Calendar,
                    recomputing the time from its fields if necessary.

                getDatePicker()
                Gets the DatePicker contained in this dialog.

               setMinDate(long minDate)
                    Sets the minimal date supported by this NumberPicker
                    in milliseconds since January 1, 1970 00:00:00 in getDefault() time zone.

                setMaxDate(long maxDate)
                    Sets the maximal date supported by this DatePicker in milliseconds
                    since January 1, 1970 00:00:00 in getDefault() time zone.
             */

                // Set the Calendar new date as maximum date of date picker
                datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());

                // Subtract 6 days from Calendar updated date
                date.add(Calendar.DATE, -1000);

                // Set the Calendar new date as minimum date of date picker
                datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());

//                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

//                date.add(Calendar.HOUR_OF_DAY,2);
//                date.getTime();
                datePickerDialog.show();
            }
        });
//
        ly_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                final Calendar date = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDateandTime = sdf1.format(new Date());
                Log.v("", "The choosen one " + currentDateandTime);
                tv_start_date.setText(geDateSessionHistory(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime())));
                DatePickerDialog datePickerDialog = new DatePickerDialog(SessionHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        String date_time = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                        String s1 = date_time;
                        String[] words = s1.split("\\s");//splits the string based on whitespace
                        //using java foreach loop to print elements of string array
                        select_date = words[0];
//                        booking_time = words[1] + " " + words[2].toLowerCase();
                        tv_start_date.setText(geDateSessionHistory(select_date));
//


                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

                // Add 3 days to Calendar
                date.add(Calendar.DATE, 0);


            /*
                getTimeInMillis()
                    Returns the time represented by this Calendar,
                    recomputing the time from its fields if necessary.

                getDatePicker()
                Gets the DatePicker contained in this dialog.

               setMinDate(long minDate)
                    Sets the minimal date supported by this NumberPicker
                    in milliseconds since January 1, 1970 00:00:00 in getDefault() time zone.

                setMaxDate(long maxDate)
                    Sets the maximal date supported by this DatePicker in milliseconds
                    since January 1, 1970 00:00:00 in getDefault() time zone.
             */

                // Set the Calendar new date as maximum date of date picker
                datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());

                // Subtract 6 days from Calendar updated date
                date.add(Calendar.DATE, -1000);

                // Set the Calendar new date as minimum date of date picker
                datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());

//                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

//                date.add(Calendar.HOUR_OF_DAY,2);
//                date.getTime();
                datePickerDialog.show();


            }
        });
    }


   /* public void getSessionHistory(int user_id, String start_date, String end_date) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseSessionHistory> call = apiInterface.userChargingHistoryBle(hashMap1, new RequestSessionHistory(user_id, start_date, end_date));
        call.enqueue(new Callback<ResponseSessionHistory>() {
            @Override
            public void onResponse(Call<ResponseSessionHistory> call, Response<ResponseSessionHistory> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseSessionHistory loginResponse = (ResponseSessionHistory) response.body();
                        Logger.e("Response:111111 " + loginResponse.toString() + "  " + loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {

                                SessionHistoryAdapter adapter = new SessionHistoryAdapter(SessionHistoryActivity.this, loginResponse.data);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                tvRequestNotFound.setVisibility(View.GONE);


                                listView.setVisibility(View.VISIBLE);

                            } else {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
//                                Toast.makeText(SessionHistoryActivity.this, "No call request found", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            try {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
//                                Toast.makeText(CallRequestInfoActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseSessionHistory> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }*/

    public void getSessionHistoryMode(int user_id, String start_date, String end_date, String mode) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<ResponseSessionHistory> call = apiInterface.userChargingHistoryBleMode(hashMap1, new RequestSessionHistory(user_id, start_date, end_date, mode));
        call.enqueue(new Callback<ResponseSessionHistory>() {
            @Override
            public void onResponse(Call<ResponseSessionHistory> call, Response<ResponseSessionHistory> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseSessionHistory loginResponse = (ResponseSessionHistory) response.body();
                        Logger.e("Response:111111 " + loginResponse.toString() + "  " + loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {

                                SessionHistoryAdapter adapter = new SessionHistoryAdapter(SessionHistoryActivity.this, loginResponse.data);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                tvRequestNotFound.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);

                            } else {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
//                                Toast.makeText(SessionHistoryActivity.this, "No call request found", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            try {
                                tvRequestNotFound.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);

                               /* callMailService(
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                        SessionHistoryActivity.class.getSimpleName(),
                                        "SPIN_ANDROID",
                                        AppUtils.project_id,
                                        AppUtils.bodyToString(call.request().body()),
                                        "ANDROID",
                                        call.request().url().toString(),
                                        "Y",
                                        AppUtils.DATA_NOT_FOUND_CODE,
                                        AppUtils.DATA_NOT_FOUND,
                                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));*/
//                                Toast.makeText(CallRequestInfoActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                SessionHistoryActivity.class.getSimpleName(),
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
                    Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SessionHistoryActivity.class.getSimpleName(),
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
            public void onFailure(Call<ResponseSessionHistory> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(SessionHistoryActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        SessionHistoryActivity.class.getSimpleName(),
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

    private void isClickable() {
        tv_get_occp_logs.setClickable(false);
        tv_get_ble_logs.setClickable(false);
        tv_get_occp_logs.setEnabled(false);
        tv_get_ble_logs.setEnabled(false);
//        tb_ble_occp.setClickable(false);
//        tb_ble_occp.setEnabled(false);
        tb_ble_occp.setChecked(true);
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(SessionHistoryActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


}