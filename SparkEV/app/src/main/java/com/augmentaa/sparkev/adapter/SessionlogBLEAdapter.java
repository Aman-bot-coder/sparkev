package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.session_logs_ble.Data;

import java.util.List;

public class SessionlogBLEAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public SessionlogBLEAdapter(Context context, List<Data> itemsModelsl) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.progress = new ProgressDialog(context);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.session_log_ble_itemlist, parent, false);
            viewHolder.tv_start_date = (TextView) convertView.findViewById(R.id.start_date);
            viewHolder.tv_stop_date = (TextView) convertView.findViewById(R.id.stop_date);
            viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.tv_stop_time = (TextView) convertView.findViewById(R.id.stop_time);
            viewHolder.tv_kwh = (TextView) convertView.findViewById(R.id.kwh);
            viewHolder.tv_s_no = (TextView) convertView.findViewById(R.id.s_no);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        String[] start_data = itemsModelListFiltered.get(position).start_time.split("\\s");
        String start_date = start_data[0]; // 004
        String start_time = start_data[1]; // 034556

        String[] stop_data = itemsModelListFiltered.get(position).stop_time.split("\\s");
        String stop_date = stop_data[0]; // 004
        String stop_time= stop_data[1]; // 034556

        viewHolder.tv_start_date.setText("" + start_date);
        viewHolder.tv_stop_date.setText("" +stop_date);
        viewHolder.tv_start_time.setText("" + start_time);
        viewHolder.tv_stop_time.setText("" + stop_time);
        viewHolder.tv_kwh.setText("" + itemsModelListFiltered.get(position).session_kwh);
        viewHolder.tv_s_no.setText("S.No. " + (position+1));


        return convertView;
    }


    public class ViewHolder {
        TextView tv_start_date;
        TextView tv_stop_date;
        TextView tv_start_time;
        TextView tv_stop_time;
        TextView tv_kwh;
        TextView tv_s_no;


    }


}
