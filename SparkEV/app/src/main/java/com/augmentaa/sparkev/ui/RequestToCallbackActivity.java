package com.augmentaa.sparkev.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.QuestionListAdapter;
import com.augmentaa.sparkev.model.signup.call_request.CallRequest;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.question_list.Data;
import com.augmentaa.sparkev.model.signup.question_list.Question;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestToCallbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, QuestionListAdapter.ClickButton {

    Button btn_submit;
    //    btn_callHistory, btn_incomingCall,
    LinearLayout ly;
    Spinner sp_question;
    ImageView img_back;
    APIInterface apiInterface;
    ProgressDialog progress;
    Question questionResponse;
    TextView et_name, et_mobile;
    EditText et_remarks;
    TextView tv_date, tv_time;
    String questionId;
    int selected_hour, selected_min;
    String future_date, futureTime;
    String current_date, currentTime, postdate;
    final Calendar currentDate = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    String error_message;
    TextView tvCancel;
    Dialog dialog;
    int s_year, s_month, s_day;
    ListView listView;
    EditText etSearch;
    QuestionListAdapter adapter;
   TextView tv_question;
     BottomSheetDialog dialog_Distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_to_call_back);
        getSupportActionBar().hide();
        img_back = findViewById(R.id.back);
//        btn_callHistory = findViewById(R.id.call_history);
//        btn_incomingCall =  findViewById(R.id.incoming_call);
        btn_submit = findViewById(R.id.req_call);
//        ly = findViewById(R.id.ly);
        tv_question=findViewById(R.id.tv_question);
        sp_question = findViewById(R.id.sp_question);
        et_mobile = findViewById(R.id.mobile);
        et_name = findViewById(R.id.name);
        tv_date = findViewById(R.id.date);
        tv_time = findViewById(R.id.time);
        et_remarks = findViewById(R.id.remarks);
        sp_question.setOnItemSelectedListener(this);
        this.progress = new ProgressDialog(this);
        tvCancel = findViewById(R.id.cancel);
        this.apiInterface = APIClient.getClientNew().create(APIInterface.class);
        et_name.setText(Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + " " + Pref.getValue(Pref.TYPE.L_NAME.toString(), null));
        et_mobile.setText(Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Utils.isNetworkConnected(this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getQuestionList();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
        tv_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(RequestToCallbackActivity.this)) {
                    dialogQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
      /*  btn_callHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CallRequestInfoActivity.class);
                intent.putExtra("pos", 1);
                startActivity(intent);
                finish();


            }
        });

        btn_incomingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CallRequestInfoActivity.class);
                intent.putExtra("pos", 0);
                startActivity(intent);
                finish();

            }
        });
*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(RequestToCallbackActivity.this)) {
                    if (et_name.getText().toString().length() >= 200 || et_name.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_LONG).show();
                    } else if (!isMobileNumberValid(et_mobile.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                    } else if (et_remarks.getText().toString().length() >= 200 || et_remarks.getText().toString().length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();

                    } else if (date.get(Calendar.DAY_OF_WEEK) == date.SUNDAY) {
                        Toast.makeText(RequestToCallbackActivity.this, "Please select other day", Toast.LENGTH_LONG).show();
                        Logger.e("====" + currentDate.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                    } else {
                        if (Utils.isNetworkConnected(RequestToCallbackActivity.this)) {
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


        current_date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText("" + current_date);
        future_date = current_date;

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
//                final String currentDateandTime = sdf1.format(new Date());
//                Log.v("", "The choosen one " + currentDateandTime);
//                tv_date.setText(new SimpleDateFormat("dd.MM.yyyy").format(date.getTime()));
                date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestToCallbackActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        Logger.e("===Date===" + year + "  " + monthOfYear + "  " + dayOfMonth);

                        date.set(year, monthOfYear, dayOfMonth);

//

                        String date_time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date.getTime());
                        String s1 = date_time;
                        String[] words = s1.split("\\s");//splits the string based on whitespace
                        //using java foreach loop to print elements of string array
                        future_date = words[0];
//                        futureTime = words[1] + " " + words[2].toLowerCase();
//
                        if (date.get(Calendar.DAY_OF_WEEK) == date.SUNDAY) {
                            Toast.makeText(RequestToCallbackActivity.this, "Please select other day", Toast.LENGTH_LONG).show();
                            Logger.e("====" + date.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                        } else {
                            tv_date.setText(future_date);
                            Toast.makeText(RequestToCallbackActivity.this, "Your selected date " + future_date, Toast.LENGTH_LONG).show();


                        }
                        Logger.e("====" + date.get(Calendar.DAY_OF_WEEK) + "  " + date.DAY_OF_WEEK + "  " + date.SUNDAY);

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

                // Add 7 days to Calendar
                date.add(Calendar.DATE, 7);

                // Set the Calendar new date as maximum date of date picker
                datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());

                // Subtract 6 days from Calendar updated date
                date.add(Calendar.DATE, -7);

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
//                                Toast.makeText(RequestToCallbackActivity.this, "Your selected time " + futureTime, Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(RequestToCallbackActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }
                            Logger.e("Date Compare" + currentTime + "  " + "   " + futureTime + "   " + AppUtils.isTimeBetweenNineToSixValidation(currentTime, currentTime));

                        } else {
                            tv_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                           /* if (AppUtils.isTimeValidation(currentTime, futureTime)) {
                                tv_time.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                Toast.makeText(RequestToCallbackActivity.this, "Your selected time " + futureTime, Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(RequestToCallbackActivity.this, "Please select valid time", Toast.LENGTH_LONG).show();
                            }*/
                            Logger.e("Date Compare" + currentTime + "  " + "   " + futureTime + "   " + AppUtils.isTimeValidation(currentTime, currentTime));


                        }

                        Logger.e("Date Compare" + current_date + "  " + "   " + future_date + "   " + AppUtils.isDateValidation(current_date, future_date));


                    }
                };

                Calendar c = Calendar.getInstance();
                final TimePickerDialog timePickerDialog = new TimePickerDialog(RequestToCallbackActivity.this, timePickerListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();


            }
        });


    }

    public static boolean isMobileNumberValid(String s) {

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0|91)?[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        questionId = questionResponse.data.get(position).id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getQuestionList() {
        Call<Question> call = apiInterface.getQuestionList();
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        questionResponse = response.body();
//                        Logger.e("Response: " + questionResponse.toString());
//                        QuestionListAdapter adapter = new QuestionListAdapter(RequestToCallbackActivity.this, questionResponse.data,this);
//                        sp_question.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    private void postCallRequest() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.postCallRequest(hashMap1, new CallRequest("O", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), questionId, Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + " " + Pref.getValue(Pref.TYPE.L_NAME.toString(), null), Pref.getValue(Pref.TYPE.EMAIL.toString(), null), Pref.getValue(Pref.TYPE.MOBILE.toString(), null), parseDateTime(postdate), et_remarks.getText().toString(), Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), "SPIN")).enqueue(new Callback<CallRequest>() {
            //        this.apiInterface.PostSignIn(new CallRequest(user_name, password)).enqueue(new Callback<CallRequest>() {
            public void onResponse(Call<CallRequest> call, Response<CallRequest> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        CallRequest loginResponse = (CallRequest) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        String success = loginResponse.status;
                        if (loginResponse.status.equals("true")) {
//                            Toast.makeText(getApplicationContext(), "Call Request submitted successfully", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(getApplicationContext(), CallRequestInfoActivity.class);
                            intent.putExtra("pos", 0);
                            startActivity(intent);
                            finish();*/
                            dialogSuccess();
                        } else {
                            try {
                                Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        error_message = e.getMessage();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                RequestToCallbackActivity.class.getSimpleName(),
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
                            RequestToCallbackActivity.class.getSimpleName(),
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

            public void onFailure(Call<CallRequest> call, Throwable t) {
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
                        RequestToCallbackActivity.class.getSimpleName(),
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

    private String parseDateTime(String date) {
        Date initDate = null;
        String parsedTime = null;
        try {

            initDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parsedTime = formatter.format(initDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedTime;
    }

    boolean checkSunday() {
        if (currentDate.get(Calendar.DAY_OF_WEEK) == currentDate.SUNDAY) {
            Toast.makeText(RequestToCallbackActivity.this, "Please select other day", Toast.LENGTH_LONG).show();
            Logger.e("====" + currentDate.get(Calendar.DAY_OF_WEEK) + "  " + currentDate.DAY_OF_WEEK + "  " + currentDate.SUNDAY);
            return true;
        }
        return false;
    }

    void dialogSuccess() {
        dialog = new Dialog(RequestToCallbackActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.activity_success);
        dialog.setCancelable(false);
        TextView tvSerialNumber = dialog.findViewById(R.id.success_message);
        tvSerialNumber.setText("Request sent!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
//                Intent intent = new Intent(RequestToCallbackActivity.this, CallRequestInfoActivity.class);
//                startActivity(intent);
                finish();


            }
        }, 3000);

        dialog.show();


    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(RequestToCallbackActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

    void dialogQuestion() {
        dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        listView = dialog_Distance.findViewById(R.id.listview);
        etSearch = dialog_Distance.findViewById(R.id.search);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();

//                Logger.e("======Data list=onTextChanged="+country.getData().toString()+ "       "+country.getData().size());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (questionResponse.data.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            adapter = new QuestionListAdapter(RequestToCallbackActivity.this, questionResponse.data,this);
            listView.setAdapter(adapter);
//            country.setData(responseCountry.data);
            adapter.notifyDataSetChanged();

          /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    questionId = questionResponse.data.get(i).id;
                    tv_question.setText(""+questionResponse.data.get(i).question);
                    adapter.notifyDataSetChanged();
                    dialog_Distance.dismiss();
                }
            });*/
        } else {
            dialog_Distance.dismiss();
            Toast.makeText(RequestToCallbackActivity.this, "Question not found", Toast.LENGTH_LONG).show();
        }
        dialog_Distance.show();


    }

    @Override
    public void search_question(int position, List<Data> list) {
        questionId = list.get(position).id;
        tv_question.setText(""+list.get(position).question);
        adapter.notifyDataSetChanged();
        dialog_Distance.dismiss();
    }
}
