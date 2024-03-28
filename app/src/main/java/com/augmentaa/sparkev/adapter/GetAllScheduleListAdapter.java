package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.one_time_schedule.ResponseCreateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.RequestActiveDeactiveSchecule;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.ui.UpdateScheduleDetailsOneTimeActivity;
import com.augmentaa.sparkev.ui.UpdateScheduleDetailsRecurringActivity;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllScheduleListAdapter extends BaseAdapter {
    Context context;
    private List<com.augmentaa.sparkev.model.signup.schedule.Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    APIInterface apiInterface;
    AdapterInterface buttonListener;
    List<String> list_show_day_name;

    public GetAllScheduleListAdapter(Context context, List<com.augmentaa.sparkev.model.signup.schedule.Data> itemsModelsl, AdapterInterface buttonListener) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.progress = new ProgressDialog(context);
        this.buttonListener = buttonListener;
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        list_show_day_name = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.schedule_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.charger_name);
            viewHolder.tvt_date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.txt_duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.tb_button = convertView.findViewById(R.id.chkState);
            viewHolder.img_view_details = convertView.findViewById(R.id.img_view_details);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
//        try {

        list_show_day_name.clear();
        if (itemsModelListFiltered.get(position).scheduleName != null) {
            viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).scheduleName);
        } else {
            viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).scheduleName);

        }

        Logger.e("Data and time  " + itemsModelListFiltered.get(position).startScheduleTime);

        Logger.e("Data and time Spit  " +  AppUtils.geDateTime(itemsModelListFiltered.get(position).startScheduleTime));


        String[] parts = AppUtils.geDateTime(itemsModelListFiltered.get(position).startScheduleTime).split("-");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556

        viewHolder.tvt_date.setText("" + parseDate12(part1));

        viewHolder.txt_time.setText("" + part2);
        viewHolder.txt_duration.setText("" + (itemsModelListFiltered.get(position).duration / 60) + " Hour");

        if (itemsModelListFiltered.get(position).scheduleType.equalsIgnoreCase("ONE_TIME")) {
            viewHolder.tb_button.setVisibility(View.INVISIBLE);
        } else {

            for (int i = 0; i < itemsModelListFiltered.get(position).days.size(); i++) {
                Logger.e("Day " + itemsModelListFiltered.get(position).days.get(i).status);
                if ("Y".equalsIgnoreCase(itemsModelListFiltered.get(position).days.get(i).status)) {
                    String select_day = itemsModelListFiltered.get(position).days.get(i).dayName.substring(0, 3);
                    list_show_day_name.add(select_day.substring(0, 1).toUpperCase() + select_day.substring(1));
                }

                viewHolder.tvt_date.setText(list_show_day_name.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
            }
            viewHolder.tb_button.setVisibility(View.VISIBLE);

        }

        Logger.e("Get Schedule status===" + itemsModelListFiltered.get(position).scheduleStatus + "  " + itemsModelListFiltered.get(position).scheduleName);
        if ("Y".equalsIgnoreCase(itemsModelListFiltered.get(position).scheduleStatus)) {
            viewHolder.tb_button.setChecked(true);
        } else {
            viewHolder.tb_button.setChecked(false);
        }
        viewHolder.img_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("ONE_TIME".equalsIgnoreCase(itemsModelListFiltered.get(position).scheduleType)) {
                    Intent intent = new Intent(context, UpdateScheduleDetailsOneTimeActivity.class);
                    intent.putExtra("data", itemsModelListFiltered.get(position));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, UpdateScheduleDetailsRecurringActivity.class);
                    intent.putExtra("data", itemsModelListFiltered.get(position));
                    context.startActivity(intent);
                }

                Logger.e("days data   " + itemsModelListFiltered.get(position).toString() + "           " + itemsModelListFiltered.get(position).days.toString());

            }
        });


        viewHolder.tb_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int pos = Integer.parseInt(viewHolder.checkBox.getTag().toString()); //to take the actual position
//                buttonListener.buttonPressed();
                if (isChecked) {
                    if (Utils.isNetworkConnected(context)) {
                        progress.setMessage(context.getResources().getString(R.string.loading));
                        progress.show();
                        createSetScheduleBLE(itemsModelListFiltered.get(position).scheduleId, "Y", position, isChecked);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                } else {
                    if (Utils.isNetworkConnected(context)) {
                        progress.setMessage(context.getResources().getString(R.string.loading));
                        progress.show();
                        createSetScheduleBLE(itemsModelListFiltered.get(position).scheduleId, "N", position, isChecked);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                }
                Logger.e("checked data==" + isChecked + "  " + position);

            }

        });

//        } catch (Exception e) {
//
//        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName, tvt_date, txt_time, txt_duration;
        ImageView img_view_details;
        ToggleButton tb_button;


    }

    private void createSetScheduleBLE(int schedule_id, String status, int position, boolean isChecked) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.setActiveDeactiveSchedule(hashMap1, new RequestActiveDeactiveSchecule(schedule_id, status, Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0))).enqueue(new Callback<ResponseCreateSchedule>() {
            public void onResponse(Call<ResponseCreateSchedule> call, Response<ResponseCreateSchedule> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    ResponseCreateSchedule responseCreateSchedule = (ResponseCreateSchedule) response.body();
                    Logger.e("Updated Response  " + responseCreateSchedule.toString());
                    if (responseCreateSchedule.status) {
                        Toast.makeText(context, "Schedule status updated successfully", Toast.LENGTH_LONG).show();
//                        viewHolder.tb_button.setChecked(true);

                    } else {
//                        viewHolder.tb_button.setChecked(false);
                        Toast.makeText(context, responseCreateSchedule.message, Toast.LENGTH_LONG).show();

                    }


                } else {

                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseCreateSchedule> call, Throwable t) {
                Logger.e("Exception " + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });

    }
    String parseDate12(String inputDate) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
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

    public interface AdapterInterface {
        public void buttonPressed();
    }
}
