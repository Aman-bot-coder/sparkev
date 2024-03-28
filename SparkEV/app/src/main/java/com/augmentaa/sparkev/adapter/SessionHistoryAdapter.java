package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;

import java.util.List;

public class SessionHistoryAdapter extends BaseAdapter {
    Context context;
    private List<com.augmentaa.sparkev.model.signup.session_history.Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    APIInterface apiInterface;
    List<String> list_show_day_name;

    public SessionHistoryAdapter(Context context, List<com.augmentaa.sparkev.model.signup.session_history.Data> itemsModelsl) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.session_history_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.charger_name);
            viewHolder.tvt_date = (TextView) convertView.findViewById(R.id.day);
            viewHolder.txt_kwh = (TextView) convertView.findViewById(R.id.energy);
            viewHolder.txt_duration = (TextView) convertView.findViewById(R.id.duration);
//            viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.charger_mode);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {

        if ((itemsModelListFiltered.get(position).meterStartTime) != null) {

            viewHolder.tvt_date.setText("" + AppUtils.getDateonChartNew(itemsModelListFiltered.get(position).meterStartTime_char));
        } else {
            viewHolder.tvt_date.setText("NA");

        }


        if ((itemsModelListFiltered.get(position).duration) != null) {

            viewHolder.txt_duration.setText("" + (itemsModelListFiltered.get(position).duration));
        } else {
            viewHolder.txt_duration.setText("NA");

        }

        if ((itemsModelListFiltered.get(position).energyConsumed) != null) {

            viewHolder.txt_kwh.setText("" + (itemsModelListFiltered.get(position).energy_consumed_kw + " kWh"));
        } else {
            viewHolder.txt_kwh.setText("NA");

        }




        if (itemsModelListFiltered.get(position).nickName != null) {
            if ("".equalsIgnoreCase(itemsModelListFiltered.get(position).nickName) || " ".equalsIgnoreCase(itemsModelListFiltered.get(position).nickName)) {
                viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serialNo);

            } else {
                viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).nickName);

            }

        } else {
            viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serialNo);

        }


        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName, tvt_date, txt_kwh, txt_duration, tv_mode;


    }


}
