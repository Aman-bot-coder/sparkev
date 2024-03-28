package com.augmentaa.sparkev.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.Globals;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.DurationListAdapter;
import com.augmentaa.sparkev.model.signup.charger_status.RequestChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_status.ResponseChargerStatus;
import com.augmentaa.sparkev.model.signup.data_transfer.RequestCurrent;
import com.augmentaa.sparkev.model.signup.data_transfer.ResponseCurrent;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.schedule.Data;
import com.augmentaa.sparkev.model.signup.schedule.RequestUpdateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.ResponseUpdateSchedule;
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

public class UpdateScheduleDetailsOneTimeActivity extends AppCompatActivity {

    TextView tvSelectTime, tvSelectDate, tvSelectDurarion;
    Button btnSubmit;
    String cTime, cDate, cDuration;
    String sTime, sDate, currentDateandTime, dTime;
    int sDuration = 1;
    final Calendar currentDate = Calendar.getInstance();
    //    String current_date = new SimpleDateFormat("dd-MM-yyyy_HH:mm:a",  Locale.getDefault()).format(new Date());
    int selected_hour, selected_min;
    Calendar date;
    List<Integer> list_durations = new ArrayList<>();
    APIInterface apiInterface, apiInterfaceCharger;
    ProgressDialog progress;
    String charger_status;
    List<Schedule> list_schedule;
    EditText et_session_name;
    ImageView img_back;
    Data schedule_data;
    String cmd_duration;
    String cmd_date;
    String cmd_time;
    int dYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule_details_one_time);
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
        try {
            schedule_data = getIntent().getParcelableExtra("data");
            Logger.e("days data 11111   " + schedule_data);

            String date_time = (AppUtils.geDateTime(schedule_data.startScheduleTime));
            String[] parts = AppUtils.geDateTime(schedule_data.startScheduleTime).split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
//            Logger.e("Part 1"+part1);
            tvSelectDate.setText(parseDate13(part1));
            tvSelectTime.setText(part2);
            et_session_name.setText(schedule_data.scheduleName);
//            Logger.e("data days" +schedule_data.days.toString());

        } catch (Exception e) {

            Logger.e("Excpetion " + e);

        }

        for (int i = 1; i <= 24; i++) {
            list_durations.add(i);
        }


        for (int j = 0; j < list_durations.size(); j++) {
            int duration = (schedule_data.duration / 60);
            if (duration == list_durations.get(j)) {
                tvSelectDurarion.setText("" + list_durations.get(j) + " Hours ");
            }
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
//        tvSelectDurarion.setText("1 Hour");
//        tvSelectTime.setText(cTime);
//        tvSelectDate.setText(cDate);

        Logger.e("Date and Time " + cDate + "  " + cTime);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_session_name.getText().toString().trim().length() > 0) {
//                    if ("Preparing".equalsIgnoreCase(charger_status)) {
                        if (Utils.isNetworkConnected(UpdateScheduleDetailsOneTimeActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            getChargerStatus();
                        } else {
                            Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
//                    } else {
//                        Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();
//
//                    }


                } else {
                    Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Please enter session name", Toast.LENGTH_LONG).show();

                }


            }
        });


        sDate = cDate;
        parseDate(sDate);


        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = Calendar.getInstance();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//                final String currentDateandTime = sdf1.format(new Date());
//                tvSelectDate.setText(currentDateandTime);
//                Log.v("", "The choosen one " + currentDateandTime);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateScheduleDetailsOneTimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        dYear = year - 1900;
                        date.set(year, monthOfYear, dayOfMonth);


                        String sourceDate = new SimpleDateFormat("dd-MM-yyyy").format(date.getTime());
                        String s1 = sourceDate;
                        String[] words = s1.split("\\s");//splits the string based on whitespace
//                        using java foreach loop to print elements of string array
                        sDate = words[0];

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
                                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Your selected time " + sTime, Toast.LENGTH_LONG).show();

                            } else {
//                                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Please select time between 09:00 to 18:00", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + cTime + "  " + "   " + sTime + "   " + AppUtils.isTimeBetweenNineToSixValidation(cTime, cTime));

                        } else {
                            if (AppUtils.isTimeValidation(cTime, sTime)) {
                                tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Your selected time " + sTime, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + cTime + "  " + "   " + sTime + "   " + AppUtils.isTimeValidation(cTime, cTime));


                        }

                        Logger.e("Date Compare" + cDate + "  " + "   " + sDate + "   " + AppUtils.isDateValidation(cDate, sDate));


                    }
                };

                Calendar c = Calendar.getInstance();
                final TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateScheduleDetailsOneTimeActivity.this, timePickerListener,
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
        DurationListAdapter adapter = new DurationListAdapter(UpdateScheduleDetailsOneTimeActivity.this, list_durations);
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
            Logger.e("New Date==> " + dateString);

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
    String parseDate13(String inputDate) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MM yyyy");
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
        list_schedule.add(new Schedule("", "Y", schedule_data.days.get(0).id));
        this.apiInterface.updateScheduleBLE(hashMap1, new RequestUpdateSchedule(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_schedule, "Y", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "ONE_TIME", et_session_name.getText().toString(), "Y", parseDate(sDate) + " " + sTime + ":00", parseDate(sDate) + " " + dTime + ":00", (sDuration * 60))).enqueue(new Callback<ResponseUpdateSchedule>() {
            public void onResponse(Call<ResponseUpdateSchedule> call, Response<ResponseUpdateSchedule> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseUpdateSchedule responseCreateSchedule = (ResponseUpdateSchedule) response.body();
                    if (responseCreateSchedule.status) {

                        set_disable_appointment();


                    } else {

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
                        progress.dismiss();
                        Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, responseCreateSchedule.message, Toast.LENGTH_LONG).show();

                    }

                } else {
                    progress.dismiss();
                    Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseUpdateSchedule> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
                            progress.dismiss();
                            Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        progress.dismiss();
                        Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Charger should be in preparing mode", Toast.LENGTH_LONG).show();

                    }

                } else {
                    progress.dismiss();
                    Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseChargerStatus> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
                        set_cmd_time();

                    } else {
                        progress.dismiss();
                    }
//                    Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, responseCurrent.message, Toast.LENGTH_LONG).show();

                    Logger.e("Response duration current: " + responseCurrent);


                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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


    private void set_disable_appointment() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, Globals.DISABLE_APPOINTMENT)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();

                    if (responseCurrent.status) {

                        set_cmd_duration();

                    } else {
                        progress.dismiss();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
//                    Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, responseCurrent.message, Toast.LENGTH_LONG).show();

                    Logger.e("Response duration current: " + responseCurrent);


                }
                else {
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
                        set_cmd_appointment_enable();
                    }
                    Logger.e("Response Set date: " + responseCurrent);

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
                        set_cmd_date();
                    }
                    Logger.e("Response Set time: " + responseCurrent);


                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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

    private void set_cmd_appointment_enable() {
        this.apiInterfaceCharger.setCurrent(new RequestCurrent("DATA_TRANSFER", "CC_CONFIG", Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "CPV07", 1, Globals.ENABLE_APPOINTMENT)).enqueue(new Callback<ResponseCurrent>() {
            public void onResponse(Call<ResponseCurrent> call, Response<ResponseCurrent> response) {
//                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCurrent responseCurrent = (ResponseCurrent) response.body();
                    if (responseCurrent.status) {
                        Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, "Schedule update successfully.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateScheduleDetailsOneTimeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    Logger.e("Response appointment current: " + responseCurrent);

                }
            }

            public void onFailure(Call<ResponseCurrent> call, Throwable t) {
                Toast.makeText(UpdateScheduleDetailsOneTimeActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateScheduleDetailsOneTimeActivity.class.getSimpleName(),
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
        Intent intent = new Intent(UpdateScheduleDetailsOneTimeActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }



}