package com.augmentaa.sparkev.ui;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;


import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augmentaa.sparkev.Globals;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.DurationListAdapter;
import com.augmentaa.sparkev.model.signup.charger_status.RequestChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_status.ResponseChargerStatus;
import com.augmentaa.sparkev.model.signup.data_transfer.RequestCurrent;
import com.augmentaa.sparkev.model.signup.data_transfer.ResponseCurrent;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.one_time_schedule.ResponseCreateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.RequestCreateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.Schedule;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;


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

public class AddScheduleDetailsOneTimeActivity extends AppCompatActivity {

    TextView tvSelectTime, tvSelectDate, tvSelectDurarion;
    Button btnSubmit;
    String cTime, cDate, cDuration;
    String sTime, sDate, currentDateandTime, dTime;
    int sDuration = 1;
    final Calendar currentDate = Calendar.getInstance();
    int selected_hour, selected_min;
    Calendar date;
    List<Integer> list_durations = new ArrayList<>();
    APIInterface apiInterface, apiInterfaceCharger;
    ProgressDialog progress;
    String charger_status;
    List<Schedule> list_schedule;
    EditText et_session_name;
    ImageView img_back;



    String cmd_duration;
    String cmd_date;
    String cmd_time;
    boolean appointBtn_clicked = true;

    int dYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_details_one_time);
        getSupportActionBar().hide();

        tvSelectDate = findViewById(R.id.date);
        tvSelectTime = findViewById(R.id.time);
        tvSelectDurarion = findViewById(R.id.duration);
        et_session_name = findViewById(R.id.name);
        btnSubmit = findViewById(R.id.btn_add_schedule);
        img_back = findViewById(R.id.back);

        this.progress = new ProgressDialog(this);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfaceCharger = (APIInterface) APIClient.getChargerURL().create(APIInterface.class);
        list_schedule = new ArrayList<>();

        for (int i = 1; i <= 24; i++) {
            list_durations.add(i);
        }


        tvSelectDurarion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_duration();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        currentDateandTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        String[] parts = currentDateandTime.split("_");
        cDate = parts[0]; // Date
        cTime = parts[1]; // Time

        Logger.e("Current date==="+cDate);


        Logger.e("Date and Time " + cDate + "  " + cTime);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_session_name.getText().toString().trim().length() > 0) {
//                    if ("Preparing".equalsIgnoreCase(charger_status)) {
                    Logger.e("====Set schedule==="+Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null));
                    if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
                        if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
                            if (Utils.isNetworkConnected(AddScheduleDetailsOneTimeActivity.this)) {
                                progress.setMessage(getResources().getString(R.string.loading));
                                progress.show();
                                getChargerStatus();
                            } else {
                                appointBtn_clicked = true;

//                        Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                            }
                        } else {
                            appointBtn_clicked = true;

                        }
                    } else {
                        appointBtn_clicked = true;
                    }


//                    } else {
//                        Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();
//
//                    }

                } else {

                    Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Please enter session name", Toast.LENGTH_LONG).show();


                }


            }
        });


        sDate = cDate;

        parseDate(sDate);


        Logger.e("==Date=1111=="+parseDate(sDate));


        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = Calendar.getInstance();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//                final String currentDateandTime = sdf1.format(new Date());
//                tvSelectDate.setText(currentDateandTime);
//                Log.v("", "The choosen one " + currentDateandTime);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleDetailsOneTimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        date.set(year, monthOfYear, dayOfMonth);

                        Logger.e("==Date=2222=="+parseDate(sDate));

                        String sourceDate = new SimpleDateFormat("dd-MM-yyyy").format(date.getTime());

                        Logger.e("==Date=333=="+sourceDate);
//
                        parseDate(sourceDate);

                        tvSelectDate.setText(parseDate12(sDate));
                        
                        Logger.e("====" + date.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

                // Add 7 days to Calendar
                date.add(Calendar.DATE, 1);

                // Set the Calendar new date as maximum date of date picker
                datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());

                // Subtract 6 days from Calendar updated date
                date.add(Calendar.DATE, -1);

                // Set the Calendar new date as minimum date of date picker
                datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());

//                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

//                date.add(Calendar.HOUR_OF_DAY,2);
//                date.getTime();
                datePickerDialog.show();
            }
        });

        sTime = cTime;

        durationTime(sDuration, sDate, sTime);

        tvSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        selected_hour = hour;
                        selected_min = minute;
                        sTime = String.format("%02d", hour) + ":" + String.format("%02d", minute);



                        if (AppUtils.isDateValidationSchedule(cDate, sDate)) {
                            if (AppUtils.isTimeBetweenNineToSixValidation(cTime, sTime)) {
                                tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));

                            } else {
                            }
                            Logger.e("Date Compare" + cTime + "  " + "   " + sTime + "   " + AppUtils.isTimeBetweenNineToSixValidation(cTime, cTime));

                        } else {

                            if (AppUtils.isTimeValidation(cTime, sTime)) {
                                tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));

                            } else {
                                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }


                        }



                    }
                };

                Calendar c = Calendar.getInstance();
                final TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleDetailsOneTimeActivity.this, timePickerListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });
    }

    void dialog_duration() {
        View view = getLayoutInflater().inflate(R.layout.dialog_duration, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        dialog_Distance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_Distance.setContentView(view);
//        view.setBackgroundColor(Color.TRANSPARENT);

//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        View tv_close = dialog_Distance.findViewById(R.id.close);
        DurationListAdapter adapter = new DurationListAdapter(AddScheduleDetailsOneTimeActivity.this, list_durations);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sDuration = list_durations.get(position);
                tvSelectDurarion.setText(list_durations.get(position) + " Hour");
                String hexTime = Integer.toHexString(list_durations.get(position) * 60);
                String finalHexTime = ("0000" + hexTime).substring(hexTime.length());
                cmd_duration = Globals.SET_CHARGE_TIME_PREFIX + finalHexTime;
                Logger.e("duration " + cmd_duration);
                durationTime(sDuration, sDate, sTime);
                dialog_Distance.dismiss();

            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Distance.dismiss();
            }
        });
        dialog_Distance.show();

    }

    String durationTime(int duration, String date, String time) {
        String newTime = null;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(sTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.HOUR, sDuration);
            newTime = df.format(cal.getTime());
            Logger.e("Duration Time" + newTime);
            dTime = newTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    String parseDate(String inputDate) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
            dateString = outputFormat.format(date);
            Logger.e("New Date=1111=> " + dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;

    }



    String parseDate12(String inputDate) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
            dateString = outputFormat.format(date);
            Logger.e("New Date=1111=> " + dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;

    }

    String parseTime(String inputDate) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
            dateString = outputFormat.format(date);
            Logger.e("New Time==> " + dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    private void createSetScheduleBLE() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        list_schedule.add(new Schedule("", "Y"));
        Logger.e("Request data: " + list_schedule.toString() + " " + Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0) + "  " + parseDate(sDate) + "  " + sDate + "   " + sDuration + " " + dTime + "   " + sTime);
        this.apiInterface.createScheduleBLE(hashMap1, new RequestCreateSchedule(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_schedule, "Y", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "ONE_TIME", et_session_name.getText().toString(), "Y", parseDate(sDate) + " " + sTime + ":00", parseDate(sDate) + " " + dTime + ":00", (sDuration * 60),1)).enqueue(new Callback<ResponseCreateSchedule>() {
            public void onResponse(Call<ResponseCreateSchedule> call, Response<ResponseCreateSchedule> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCreateSchedule responseCreateSchedule = (ResponseCreateSchedule) response.body();
                    if (responseCreateSchedule.status) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                set_cmd_duration();
                            }
                        }, 3000);


                    } else {
                        progress.dismiss();
                        Toast.makeText(AddScheduleDetailsOneTimeActivity.this, responseCreateSchedule.message, Toast.LENGTH_LONG).show();

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
                            response.code(),
                            "SERVER ERROR",
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    progress.dismiss();
                    Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseCreateSchedule> call, Throwable t) {
                AppUtils.bodyToString(call.request().body());
                Logger.e("Exception  " + t.getMessage());
                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                        200,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    private void getChargerStatus() {
        this.apiInterfaceCharger.getChargerStatus(new RequestChargerStatus("STATUS", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "1")).enqueue(new Callback<ResponseChargerStatus>() {
            public void onResponse(Call<ResponseChargerStatus> call, Response<ResponseChargerStatus> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseChargerStatus chargerStatus = (ResponseChargerStatus) response.body();
                    charger_status = chargerStatus.message;
                    Logger.e("Response value: " + chargerStatus);
                    if (chargerStatus.status) {
                        if ("Preparing".equalsIgnoreCase(charger_status)) {
                            createSetScheduleBLE();
                        } else {
                            Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();
                            progress.dismiss();

                        }
                    }else {
                        progress.dismiss();
                        Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();

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
                            response.code(),
                            "COMMAND NOT SENT",
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }

            public void onFailure(Call<ResponseChargerStatus> call, Throwable t) {
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
                        200,
                        "JSON PARSING",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }


    private void set_cmd_duration() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, cmd_duration)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    if (responseCurrent.status) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                set_cmd_time();
                            }
                        }, 3000);


                    } else {
                        progress.dismiss();
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
                                "COMMAND NOT SENT",
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
//                    Toast.makeText(AddScheduleDetailsOneTimeActivity.this, responseCurrent.message, Toast.LENGTH_LONG).show();

                    Logger.e("Response duration current: " + responseCurrent);


                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
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
                        200,
                        "JSON PARSING",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }

    private void set_cmd_date() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, cmd_date)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    if (responseCurrent.status) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                set_cmd_appointment_enable();
                            }
                        }, 3000);

                    } else {
                        progress.dismiss();

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
                                200,
                                "COMMAND NOT SEND",
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
                    Logger.e("Response Set date: " + responseCurrent);

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
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
                        200,
                        "JSON PARSING",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }

    private void set_cmd_time() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, cmd_time)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    if (responseCurrent.status) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                set_cmd_date();
                            }
                        }, 3000);


                    } else {
                        progress.dismiss();
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
                                200,
                                "COMMAND NOT SENT",
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }
                    Logger.e("Response Set time: " + responseCurrent);


                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
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
                        200,
                        "JSON PARSING",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }

    private void set_cmd_appointment_enable() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, Globals.ENABLE_APPOINTMENT)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    if (responseCurrent.status) {
                        Toast.makeText(AddScheduleDetailsOneTimeActivity.this, "Schedule created successfully.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddScheduleDetailsOneTimeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
                                response.code(),
                                "COMMAND NOT SENT",
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                    }

                    Logger.e("Response appointment current: " + responseCurrent);

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(AddScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                        200,
                        "JSON PARSING",
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


            }
        });

    }




    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(AddScheduleDetailsOneTimeActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

}