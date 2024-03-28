package com.augmentaa.sparkev.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.DayListAdapter;
import com.augmentaa.sparkev.adapter.DurationListAdapter;
import com.augmentaa.sparkev.adapter.TimeListAdapter;
import com.augmentaa.sparkev.model.signup.charger_status.RequestChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_status.ResponseChargerStatus;
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

public class AddScheduleDetailsRecurringActivity extends AppCompatActivity {
    Dialog dialogSelectDay;

    TextView tvSelectTime, tvSelectDay, tvSelectDurarion, tv_seesion_name;
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
    TextView tvSelectName;
    List<Schedule> list_day;
    ImageView img_back;
    List<com.augmentaa.sparkev.model.signup.schedule.Schedule> list_schedule;

    List<String>list_show_day_name;

    List <String>list_time;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_details_recurring);
        tvSelectDay = findViewById(R.id.date);
        tvSelectTime = findViewById(R.id.time);
        tvSelectDurarion = findViewById(R.id.duration);
        tv_seesion_name = findViewById(R.id.name);
//        tvSelectDay = findViewById(R.id.repeat);
        btnSubmit = findViewById(R.id.btn_add_schedule);
        img_back = findViewById(R.id.back);
        list_show_day_name=new ArrayList<>();
        list_day = new ArrayList<>();
        list_schedule = new ArrayList<>();
        list_time=new ArrayList<>();
        this.progress = new ProgressDialog(this);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfaceCharger = (APIInterface) APIClient.getChargerURL().create(APIInterface.class);
        getSupportActionBar().hide();

       /* if (Utils.isNetworkConnected(AddScheduleDetailsRecurringActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getChargerStatus("STATUS", "D72110382490776", "1");

        } else {
            Toast.makeText(AddScheduleDetailsRecurringActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }*/

        for (int i = 1; i <= 24; i++) {
            list_durations.add(i);
        }

        list_time.add("01:00");
        list_time.add("02:00");
        list_time.add("03:00");
        list_time.add("04:00");
        list_time.add("05:00");
        list_time.add("06:00");
        list_time.add("07:00");
        list_time.add("08:00");
        list_time.add("09:00");
        list_time.add("10:00");
        list_time.add("11:00");
        list_time.add("12:00");
        list_time.add("13:00");
        list_time.add("14:00");
        list_time.add("15:00");
        list_time.add("16:00");
        list_time.add("17:00");
        list_time.add("18:00");
        list_time.add("19:00");
        list_time.add("20:00");
        list_time.add("21:00");
        list_time.add("22:00");
        list_time.add("23:00");
        list_time.add("00:00");


        list_day.add(new Schedule("Sunday", false));
        list_day.add(new Schedule("Monday", false));
        list_day.add(new Schedule("Tuesday", false));
        list_day.add(new Schedule("Wednesday", false));
        list_day.add(new Schedule("Thursday", false));
        list_day.add(new Schedule("Friday", false));
        list_day.add(new Schedule("Saturday", false));


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

        tvSelectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogDaySelected();
            }
        });


        currentDateandTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        String[] parts = currentDateandTime.split("_");
        cDate = parts[0]; // Date
        cTime = parts[1]; // Time
//        tvSelectDurarion.setText("1 Hour");
//        tvSelectTime.setText(cTime);
//        tvSelectDay.setText(cDate);

        Logger.e("Date and Time " + cDate + "  " + cTime);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_seesion_name.getText().toString().trim().length() > 0) {
//                    if ("Preparing".equalsIgnoreCase(charger_status)) {
                    if (Utils.isNetworkConnected(AddScheduleDetailsRecurringActivity.this)) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        createSetScheduleBLE();
                    } else {
                        Toast.makeText(AddScheduleDetailsRecurringActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }

//                    } else {
//                        Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Please connect you gun.", Toast.LENGTH_LONG).show();
//
//                    }
                } else {
                    Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Please enter your session name", Toast.LENGTH_LONG).show();

                }


            }
        });


        sDate = cDate;
        parseDate(sDate);
        /*ly_selectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = Calendar.getInstance();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//                final String currentDateandTime = sdf1.format(new Date());
//                tvSelectDay.setText(currentDateandTime);
//                Log.v("", "The choosen one " + currentDateandTime);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleDetailsRecurringActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        String sourceDate = new SimpleDateFormat("dd-MM-yyyy").format(date.getTime());
                        String s1 = sourceDate;
                        String[] words = s1.split("\\s");//splits the string based on whitespace
//                        using java foreach loop to print elements of string array
                        sDate = words[0];

                        parseDate(sourceDate);
                        tvSelectDay.setText(sDate);
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
*/
//        sTime = cTime;

//        durationTime(sDuration, sDate, sTime);

        tvSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_selectTime();


              /*  TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        selected_hour = hour;
                        selected_min = minute;
                        sTime = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));

                       *//* if (AppUtils.isDateValidationSchedule(cDate, sDate)) {
                            if (AppUtils.isTimeBetweenNineToSixValidation(cTime, sTime)) {
                                tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Your selected time " + sTime, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Please select time between 09:00 to 18:00", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + cTime + "  " + "   " + sTime + "   " + AppUtils.isTimeBetweenNineToSixValidation(cTime, cTime));

                        } else {
                            if (AppUtils.isTimeValidation(cTime, sTime)) {
                                tvSelectTime.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Your selected time " + sTime, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + cTime + "  " + "   " + sTime + "   " + AppUtils.isTimeValidation(cTime, cTime));


                        }

                        Logger.e("Date Compare" + cDate + "  " + "   " + sDate + "   " + AppUtils.isDateValidation(cDate, sDate));

*//*
                    }
                };

                Calendar c = Calendar.getInstance();
                final TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleDetailsRecurringActivity.this, timePickerListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();*/
            }
        });
    }

    void dialog_duration() {
        View view = getLayoutInflater().inflate(R.layout.dialog_duration, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        DurationListAdapter adapter = new DurationListAdapter(AddScheduleDetailsRecurringActivity.this, list_durations);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sDuration = list_durations.get(position);
                tvSelectDurarion.setText(list_durations.get(position) + " Hour");
                durationTime(sDuration, sDate, sTime);
                dialog_Distance.dismiss();

            }
        });

        dialog_Distance.show();

    }

    void dialog_selectTime() {
        View view = getLayoutInflater().inflate(R.layout.dialog_duration, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        TimeListAdapter adapter = new TimeListAdapter(AddScheduleDetailsRecurringActivity.this, list_time);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sTime = list_time.get(position);
                tvSelectTime.setText(sTime);
                durationTime(sDuration, sDate, sTime);
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
        list_schedule.clear();
        for (int i = 0; i < list_day.size(); i++) {
            Logger.e("Day " + list_day.get(i).isChecked() + " " + list_day.get(i).dayName);
            if (list_day.get(i).isChecked) {
                list_schedule.add(new com.augmentaa.sparkev.model.signup.schedule.Schedule( list_day.get(i).dayName,"Y"));
            }
            else {
                list_schedule.add(new com.augmentaa.sparkev.model.signup.schedule.Schedule( list_day.get(i).dayName,"N"));

            }
        }

//        this.apiInterface.createScheduleBLE(hashMap1, new RequestCreateSchedule(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_schedule, "Y", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "RECURRING", tv_seesion_name.getText().toString(),"Y",parseDate(sDate) + " " + sTime + ":00", parseDate(sDate) + " " + dTime + ":00",(sDuration * 60))).enqueue(new Callback<ResponseCreateSchedule>() {

        this.apiInterface.createScheduleBLE(hashMap1, new RequestCreateSchedule(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), list_schedule, "Y", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "RECURRING", tv_seesion_name.getText().toString(),"Y","1900-01-01" + " " + sTime + ":00", "1900-01-01" + " " + dTime + ":00",(sDuration * 60),1)).enqueue(new Callback<ResponseCreateSchedule>() {
            public void onResponse(Call<ResponseCreateSchedule> call, Response<ResponseCreateSchedule> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCreateSchedule responseCreateSchedule = (ResponseCreateSchedule) response.body();
                    if (responseCreateSchedule.status) {
                        Toast.makeText(AddScheduleDetailsRecurringActivity.this, "Schedule created successfully.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddScheduleDetailsRecurringActivity.this, ScheduleListActivity.class);
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
                                "DATA NOT SENT",
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                        Toast.makeText(AddScheduleDetailsRecurringActivity.this, responseCreateSchedule.message, Toast.LENGTH_LONG).show();

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

                    Toast.makeText(AddScheduleDetailsRecurringActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseCreateSchedule> call, Throwable t) {
                Logger.e("Exception  "+t.getMessage());
                Toast.makeText(AddScheduleDetailsRecurringActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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

    private void getChargerStatus(final String command, final String charger_id, final String connector) {
        this.apiInterfaceCharger.getChargerStatus(new RequestChargerStatus("STATUS", charger_id, connector)).enqueue(new Callback<ResponseChargerStatus>() {
            public void onResponse(Call<ResponseChargerStatus> call, Response<ResponseChargerStatus> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {

                    ResponseChargerStatus chargerStatus = (ResponseChargerStatus) response.body();
                    charger_status = chargerStatus.message;
                    Logger.e("Response value: " + chargerStatus);


                } else {

                }
            }

            public void onFailure(Call<ResponseChargerStatus> call, Throwable t) {
                Toast.makeText(AddScheduleDetailsRecurringActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }

    void dialogDaySelected() {
        dialogSelectDay = new Dialog(AddScheduleDetailsRecurringActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogSelectDay.setContentView(R.layout.dialog_select_day);
        ListView listView = dialogSelectDay.findViewById(R.id.listview);
        ImageView imgClose = dialogSelectDay.findViewById(R.id.back);
        Button btnDialogSubmit = dialogSelectDay.findViewById(R.id.btn_submit);
        DayListAdapter adapter = new DayListAdapter(AddScheduleDetailsRecurringActivity.this, list_day);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectDay.dismiss();
            }
        });

        btnDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_show_day_name.clear();

                for (int i = 0; i < list_day.size(); i++) {
                    Logger.e("Day " + list_day.get(i).isChecked() + " " + list_day.get(i).dayName);
                    if(list_day.get(i).isChecked()){
                        String select_day=list_day.get(i).dayName.substring(0,3);
                        list_show_day_name.add(select_day.substring(0, 1).toUpperCase() + select_day.substring(1));
                    }

                    tvSelectDay.setText(list_show_day_name.toString().replaceAll("\\[","").replaceAll("\\]",""));
                }
                dialogSelectDay.dismiss();
            }
        });
        dialogSelectDay.show();


    }
    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(AddScheduleDetailsRecurringActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }




}