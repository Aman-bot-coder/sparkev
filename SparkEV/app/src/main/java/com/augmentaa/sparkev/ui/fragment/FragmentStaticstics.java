package com.augmentaa.sparkev.ui.fragment;

import static com.augmentaa.sparkev.utils.AppUtils.getDateonChartNew;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.charger_summry.Detailed;
import com.augmentaa.sparkev.model.signup.charger_summry.RequestGetSummary;
import com.augmentaa.sparkev.model.signup.charger_summry.ResponseGetAllSummary;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.ui.ContactUsActivity;
import com.augmentaa.sparkev.ui.MainActivity;
import com.augmentaa.sparkev.ui.SessionHistoryActivity;
import com.augmentaa.sparkev.ui.SignInActivity;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStaticstics extends Fragment {

    TextView tv_last_energy, tv_last_charging_time, tv_last_day, tv_co2, tv_particulate_matters;
    TextView tv_monthly_energy, tv_monthly_charging_time, tv_session, tv_session_history;
    TextView tv_total_time_or_energy, tv_unit;
    TextView tv_charger_name;
    ImageView img_chart;

    TextView tv_pie_total_time_or_energy, tv_pie_unit;
    RadioGroup radioGroup;
    RadioButton rb_energy, rb_time;
    APIInterface apiInterface;
    ProgressDialog progress;
    // variable for our bar chart
    BarChart barChart;
    // variable for our bar data.
    BarData barData;
    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    ArrayList<String> days;


    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntriesArrayList;

    boolean isChecked = false;
    List list_summary_data;

    float total_kWh;
    float total_time;
    RelativeLayout relativeLayout;
    LinearLayout ly_unit;
    TextView tv_data;
    LinearLayout scrollView;
    ImageView img_bg;
    Dialog dialog_ble;
    RelativeLayout layout;
    TextView tv_get_occp_logs;
    TextView tv_get_ble_logs;
    ToggleButton tb_ble_occp;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staticstics, container, false);
        tv_last_energy = view.findViewById(R.id.last_energy);
        layout = view.findViewById(R.id.ly_select_toggle);
        tv_get_occp_logs = view.findViewById(R.id.get_occp_logs);
        tv_get_ble_logs = view.findViewById(R.id.get_ble_logs);
        tb_ble_occp = view.findViewById(R.id.tb_ble);

        tv_last_charging_time = view.findViewById(R.id.last_charging_time);
        tv_last_day = view.findViewById(R.id.last_day);
        tv_co2 = view.findViewById(R.id.co2);
        img_bg = view.findViewById(R.id.img_bg);
        tv_particulate_matters = view.findViewById(R.id.particulate);
        tv_monthly_energy = view.findViewById(R.id.monthly_energy);
        tv_monthly_charging_time = view.findViewById(R.id.monthly_charging_time);
        tv_session = view.findViewById(R.id.total_session);
        tv_total_time_or_energy = view.findViewById(R.id.total_kwh);
        tv_unit = view.findViewById(R.id.unit);
        tv_data = view.findViewById(R.id.tv_data);
        tv_pie_total_time_or_energy = view.findViewById(R.id.pie_total_kwh);
        tv_pie_unit = view.findViewById(R.id.pie_unit);
        scrollView = view.findViewById(R.id.ly_staticsitcs);
        tv_charger_name = view.findViewById(R.id.charger_name);
        tv_session_history = view.findViewById(R.id.session_history);
        img_chart = view.findViewById(R.id.img_chart);
        relativeLayout = view.findViewById(R.id.rl_pie_chart);
        radioGroup = view.findViewById(R.id.radioGroup);
        rb_energy = view.findViewById(R.id.rb_energy);
        rb_time = view.findViewById(R.id.rb_time);

        barChart = view.findViewById(R.id.idBarChart);
        pieChart = view.findViewById(R.id.idPieChart);
        ly_unit = view.findViewById(R.id.ly_unit);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.progress = new ProgressDialog(getActivity());
        list_summary_data = new ArrayList();
//        tb_ble_occp.setClickable(false);
//        tb_ble_occp.setEnabled(false);

        Logger.e("Data  " + Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null));
        if (Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null) != null) {
            if ("Y".equalsIgnoreCase(Pref.getValue(Pref.TYPE.IS_OCCP.toString(), null))) {
//                layout.setVisibility(View.VISIBLE);
                getOccpLogs();
                tb_ble_occp.setChecked(false);

                Logger.e("====Checked 11111");

            } else {
//                layout.setVisibility(View.GONE);
//                isClickable();
                getBleLogs();
                tb_ble_occp.setChecked(true);
//                dialog_services_message();

            }

        } else {
//            isClickable();
            getBleLogs();
//            layout.setVisibility(View.GONE);
//            dialog_services_message();

        }

        try {
            tv_charger_name.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));

        }
        catch (Exception e){

        }

        tv_get_ble_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tb_ble_occp.setChecked(true);
                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                getBleLogs();
            }
        });
        tv_get_occp_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tb_ble_occp.setChecked(false);
                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                getOccpLogs();
            }
        });

        tb_ble_occp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Logger.e("====Checked");
                    tb_ble_occp.setChecked(true);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getBleLogs();
                } else {
                    Logger.e("=====Unchecked");
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    tb_ble_occp.setChecked(false);
                    getOccpLogs();
                }

            }
        });


        img_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    isChecked = false;
                    img_chart.setImageResource(R.mipmap.pie_chart);
                    barChart.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    ly_unit.setVisibility(View.VISIBLE);
                    pieChart.invalidate();
                    barChart.invalidate();

                } else {
                    img_chart.setImageResource(R.mipmap.bar_chart);
                    isChecked = true;
                    barChart.setVisibility(View.GONE);
                    ly_unit.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    pieChart.invalidate();
                    barChart.invalidate();
                }
            }
        });

        tv_session_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SessionHistoryActivity.class);
                getActivity().startActivity(intent);

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    if ("Energy".equalsIgnoreCase(rb.getText().toString())) {
//                        Toast.makeText(getActivity(), "Energy", Toast.LENGTH_LONG).show();
                        getBarChart(list_summary_data, "Energy");
                        setupPieChart(list_summary_data, "Energy");


                    } else {
//                        Toast.makeText(getActivity(), "Time", Toast.LENGTH_LONG).show();
                        getBarChart(list_summary_data, "Time");
                        setupPieChart(list_summary_data, "Time");
                    }
                }

            }
        });


//        YAxis yAxis=barChart.getAxisLeft();

        return view;
    }

    private void getBarChart(List<Detailed> list, String unit) {
        // initializing variable for bar chart.

        // calling method to get bar entries.
//        getBarEntries();

        barEntriesArrayList = new ArrayList<>();
        days = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            float j = i + 1;
            if ("Energy".equalsIgnoreCase(unit)) {
                barEntriesArrayList.add(new BarEntry(i, list.get(i).energyConsumed));
            } else {
                barEntriesArrayList.add(new BarEntry(i, list.get(i).duration));

            }
//            days.add(AppUtils.getDateonChart(list.get(i).createdDate));
            days.add(AppUtils.getDateonChartNew(list.get(i).createdDate));

        }


        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, "");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(AppUtils.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.getAxisLeft().setDrawAxisLine(false);
        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setLabelCount(barEntriesArrayList.size());
        bottomAxis.setValueFormatter(new IndexAxisValueFormatter(days));   /*for x axis values*/
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barDataSet.setDrawValues(false);
        barChart.getDescription().setEnabled(false);


        if ("Energy".equalsIgnoreCase(unit)) {
            tv_total_time_or_energy.setText("" + total_kWh);
            tv_unit.setText(" kWh");

//            tv_pie_total_time_or_energy.setText("" + total_kWh);
//            tv_pie_unit.setText(" kWh");


        } else {
            tv_total_time_or_energy.setText("" + total_time);
            tv_unit.setText(" hours");

//            tv_pie_total_time_or_energy.setText("" + total_time);
//            tv_pie_unit.setText(" Hours");
        }

//        bottomAxis.setEnabled(true);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        barChart.invalidate();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Logger.e("Bar chart value" + e.getY());

                if ("Energy".equalsIgnoreCase(unit)) {
                    tv_total_time_or_energy.setText("" + e.getY());
                    tv_unit.setText(" kWh");

//            tv_pie_total_time_or_energy.setText("" + total_kWh);
//            tv_pie_unit.setText(" kWh");


                } else {
                    tv_total_time_or_energy.setText("" + e.getY());
                    tv_unit.setText(" hours");

//            tv_pie_total_time_or_energy.setText("" + total_time);
//            tv_pie_unit.setText(" Hours");
                }


            }

            @Override
            public void onNothingSelected() {

            }
        });


    }


    private void setupPieChart(List<Detailed> list, String unit) {

        //pupulating list of PieEntires
        List<PieEntry> pieEntires = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if ("Energy".equalsIgnoreCase(unit)) {
                pieEntires.add(new PieEntry(list.get(i).energyConsumed, AppUtils.getDateonChartNew(list.get(i).createdDate)));
            } else {
                pieEntires.add(new PieEntry(list.get(i).duration, AppUtils.getDateonChartNew(list.get(i).createdDate)));

            }
        }
        PieDataSet dataSet = new PieDataSet(pieEntires, "");
        dataSet.setColors(AppUtils.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        //Get the chart
        dataSet.setSliceSpace(3f);
        dataSet.setDrawValues(false);
        pieChart.setData(data);
        pieChart.invalidate();
        if ("Energy".equalsIgnoreCase(unit)) {
            pieChart.setCenterText(total_kWh + " kWh");
        } else {
            pieChart.setCenterText(total_time + "hours ");

        }
        if ("Energy".equalsIgnoreCase(unit)) {
//            tv_total_time_or_energy.setText("" + total_kWh);
//            tv_unit.setText(" kWh");

            tv_pie_total_time_or_energy.setText("" + total_kWh);
            tv_pie_unit.setText(" kWh");


        } else {
//            tv_total_time_or_energy.setText("" + total_time);
//            tv_unit.setText(" Hours");

            tv_pie_total_time_or_energy.setText("" + total_time);
            tv_pie_unit.setText(" hours");
        }
        pieChart.setDrawEntryLabels(false);
        pieChart.setContentDescription("");
        //pieChart.setDrawMarkers(true);
        //pieChart.setMaxHighlightDistance(34);
//        pieChart.setEntryLabelTextSize(40);
        pieChart.setCenterTextSize(0);
        pieChart.setHoleRadius(90);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
        //legend attributes
//        Legend legend = pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setTextSize(20);
//        legend.setFormSize(200);
//        legend.setFormToTextSpace(2);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Logger.e("Bar chart value" + e.getY());
//                tv_total_time_or_energy.setText("" + e.getY());

                if ("Energy".equalsIgnoreCase(unit)) {

                    tv_pie_total_time_or_energy.setText("" + total_kWh);
                    tv_pie_unit.setText(" kWh");

                } else {
                    tv_pie_total_time_or_energy.setText("" + total_time);
                    tv_pie_unit.setText(" hours");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void getAllOccpSummary() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.userChargingSummaryBleMode(hashMap1, new RequestGetSummary(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "OCCP")).enqueue(new Callback<ResponseGetAllSummary>() {
            public void onResponse(Call<ResponseGetAllSummary> call, Response<ResponseGetAllSummary> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                    ResponseGetAllSummary getAllSummary = (ResponseGetAllSummary) response.body();
                    Logger.e("Summary Response  " + getAllSummary.toString() + "    " + getAllSummary.data.detailed.size());
                    if (getAllSummary.status) {
                        rb_energy.setChecked(true);
                        rb_time.setChecked(false);
                        if (getAllSummary.data.detailed.size() > 0) {
//                            tv_charger_name.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));
                            tv_total_time_or_energy.setText("" + getAllSummary.data.totalEnergy);
                            tv_unit.setText("kWh");
                            tv_last_day.setText("" + getDateonChartNew(getAllSummary.data.lastsession.createdDate));
                            tv_last_charging_time.setText("" + getAllSummary.data.lastsession.duration);
                            tv_last_energy.setText("" + getAllSummary.data.lastsession.energyConsumed + " kWh");

                            tv_session.setText("" + getAllSummary.data.month.get(0).session);
                            tv_monthly_energy.setText("" + getAllSummary.data.month.get(0).energyConsumed + " kWh");
                            tv_monthly_charging_time.setText("" + getAllSummary.data.month.get(0).duration);

                            list_summary_data = getAllSummary.data.detailed;
                            total_kWh = getAllSummary.data.totalEnergy;
                            total_time = getAllSummary.data.totaltime;


                            getBarChart(getAllSummary.data.detailed, "Energy");
//                        getPieChart(getAllSummary.data.detailed);
                            setupPieChart(getAllSummary.data.detailed, "Energy");


                            scrollView.setVisibility(View.VISIBLE);
                            tv_data.setVisibility(View.GONE);
                            img_bg.setVisibility(View.GONE);


                        } else {
                            dialog_ble("You dont have any charging records yet!");
                            scrollView.setVisibility(View.GONE);
                            img_bg.setVisibility(View.VISIBLE);
                            tv_data.setVisibility(View.VISIBLE);
                        }


                    } else {

                            tv_data.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        img_bg.setVisibility(View.VISIBLE);
                        dialog_ble("You don't have any charging records yet!");

                        if ("Token is not valid".equalsIgnoreCase(getAllSummary.message)) {
                            Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                            Pref.clear();
                            Intent intent21 = new Intent(getActivity(), SignInActivity.class);
                            startActivity(intent21);
                            getActivity().finishAffinity();
                            Toast.makeText(getActivity(), "Your session are expired.", Toast.LENGTH_LONG).show();

                        }


                    }
                    } catch (Exception e) {
                        tv_data.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        img_bg.setVisibility(View.VISIBLE);
                       /* callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                MainActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));*/
                    }


                } else {
                    tv_data.setVisibility(View.VISIBLE);
                    img_bg.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseGetAllSummary> call, Throwable t) {
                tv_data.setVisibility(View.VISIBLE);
                img_bg.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                Logger.e("URL: " + call.request().toString());
//                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                Logger.e("Exception 123  " + t.getMessage());

                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
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

    private void getAllBlepSummary() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.userChargingSummaryBleMode(hashMap1, new RequestGetSummary(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null), "BLE")).enqueue(new Callback<ResponseGetAllSummary>() {
            public void onResponse(Call<ResponseGetAllSummary> call, Response<ResponseGetAllSummary> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseGetAllSummary getAllSummary = (ResponseGetAllSummary) response.body();
                        Logger.e("Summary Response  " + getAllSummary.toString() + "    " + getAllSummary.data.detailed.size());
                        if (getAllSummary.status) {

                            rb_energy.setChecked(true);
                            rb_time.setChecked(false);
                            if (getAllSummary.data.detailed.size() > 0) {
//                                tv_charger_name.setText("" + Pref.getValue(Pref.TYPE.NICK_NAME.toString(), null));
                                tv_total_time_or_energy.setText("" + getAllSummary.data.totalEnergy);
                                tv_unit.setText("kWh");
                                tv_last_day.setText("" + getDateonChartNew(getAllSummary.data.lastsession.createdDate));
                                tv_last_charging_time.setText("" + getAllSummary.data.lastsession.duration);
                                tv_last_energy.setText("" + getAllSummary.data.lastsession.energyConsumed + " kWh");

                                tv_session.setText("" + getAllSummary.data.month.get(0).session);
                                tv_monthly_energy.setText("" + getAllSummary.data.month.get(0).energyConsumed + " kWh");
                                tv_monthly_charging_time.setText("" + getAllSummary.data.month.get(0).duration);

                                list_summary_data = getAllSummary.data.detailed;
                                total_kWh = getAllSummary.data.totalEnergy;
                                total_time = getAllSummary.data.totaltime;


                                getBarChart(getAllSummary.data.detailed, "Energy");
//                        getPieChart(getAllSummary.data.detailed);
                                setupPieChart(getAllSummary.data.detailed, "Energy");

                                scrollView.setVisibility(View.VISIBLE);
                                tv_data.setVisibility(View.GONE);
                                img_bg.setVisibility(View.GONE);


                            } else {
                                dialog_ble("You dont have any charging records yet!");
                                scrollView.setVisibility(View.GONE);
                                img_bg.setVisibility(View.VISIBLE);
                                tv_data.setVisibility(View.VISIBLE);
                            }


                        } else {

                            tv_data.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            img_bg.setVisibility(View.VISIBLE);
                            dialog_ble("You dont have any charging records yet!");

                            if ("Token is not valid".equalsIgnoreCase(getAllSummary.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(getActivity(), SignInActivity.class);
                                startActivity(intent21);
                                getActivity().finishAffinity();
                                Toast.makeText(getActivity(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            }


                        }
                    } catch (Exception e) {
                        tv_data.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        img_bg.setVisibility(View.VISIBLE);
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                MainActivity.class.getSimpleName(),
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
                    tv_data.setVisibility(View.VISIBLE);
                    img_bg.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            MainActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseGetAllSummary> call, Throwable t) {
                tv_data.setVisibility(View.VISIBLE);
                img_bg.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                Logger.e("Exception " + t.getMessage());
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        MainActivity.class.getSimpleName(),
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

    void dialog_ble(String message) {
        dialog_ble = new Dialog(getActivity());
        dialog_ble.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_ble.setContentView(R.layout.dialog_bluetooth_on_off);
//        dialog_ble.setCancelable(false);
        TextView btn_ok = (TextView) dialog_ble.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_ble.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_ble.findViewById(R.id.tv_message);
        tv_message.setText(message);
        btn_cencel.setVisibility(View.GONE);
        btn_ok.setText("OK");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ble.dismiss();


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ble.dismiss();

            }
        });
        dialog_ble.show();

    }

    void dialog_services_message() {
        dialog_ble = new Dialog(getActivity());
        dialog_ble.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_ble.setContentView(R.layout.app_dialog);
        dialog_ble.setCancelable(false);
        TextView btn_ok = (TextView) dialog_ble.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_ble.findViewById(R.id.tv_cancel);
//        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dialog_ble.findViewById(R.id.tv_message);
        btn_ok.setText("Ok");

        tv_message.setText(getResources().getString(R.string.service_message));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                dialog_ble.dismiss();


            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ble.dismiss();

            }
        });
        dialog_ble.show();


    }

    private void getOccpLogs() {
        if (Utils.isNetworkConnected(getActivity())) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getAllOccpSummary();
          /* if (Pref.getBoolValue(Pref.TYPE.IS_CHARGING.toString(), false)) {
               scrollView.setVisibility(View.GONE);
               img_bg.setVisibility(View.VISIBLE);
               dialog_ble("Please wait for charging session to be complete to view the analysis");
           } else {
               progress.setMessage(getResources().getString(R.string.loading));
               progress.show();
               getAllOccpSummary();
           }*/


        } else {
            tv_data.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            img_bg.setVisibility(View.VISIBLE);
            dialog_ble("No Internet");
//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }

    private void getBleLogs() {
        if (Utils.isNetworkConnected(getActivity())) {

            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getAllBlepSummary();
           /* if (Pref.getBoolValue(Pref.TYPE.IS_CHARGING.toString(), false)) {
                scrollView.setVisibility(View.GONE);
                img_bg.setVisibility(View.VISIBLE);
                dialog_ble("Please wait for charging session to be complete to view the analysis");
            } else {
                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                getAllBlepSummary();
            }*/


        } else {
            tv_data.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            img_bg.setVisibility(View.VISIBLE);
            dialog_ble("No Internet");
//            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }

    private void isClickable() {
        tv_get_occp_logs.setClickable(false);
        tv_get_ble_logs.setClickable(false);
        tv_get_occp_logs.setEnabled(false);
        tv_get_ble_logs.setEnabled(false);
        tb_ble_occp.setClickable(false);
        tb_ble_occp.setEnabled(false);
        tb_ble_occp.setChecked(true);
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(getActivity(), MyService.class);
        intent.putExtra("data", errorMessage_email);
        getActivity().startService(intent);

    }

    @Override
    public void onStop() {
        super.onStop();
        tb_ble_occp.setChecked(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        tb_ble_occp.setChecked(false);
    }
}