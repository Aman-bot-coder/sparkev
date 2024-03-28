package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.alarm_logs_ble.Data;
import com.augmentaa.sparkev.retrofit.APIInterface;

import java.util.List;

public class AlarmLogsBLEAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    APIInterface apiInterface;
    List<String>list_show_day_name;
    public AlarmLogsBLEAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_logs_itemlist, parent, false);
            viewHolder.tx_s_no = (TextView) convertView.findViewById(R.id.s_no);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tx_falg = (TextView) convertView.findViewById(R.id.flag);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.alarm_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {

            String[] parts = itemsModelListFiltered.get(position).date.split("\\s");
            String date = parts[0]; // 004
            String time = parts[1]; // 034556
            viewHolder.tx_s_no.setText("S.No "+(position+1));
            viewHolder.txt_date.setText(""+date);
            viewHolder.tx_falg.setText(itemsModelListFiltered.get(position).flag);
            viewHolder.txt_name .setText(itemsModelListFiltered.get(position).alarm_name);
            viewHolder.txt_time .setText(""+time);



        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView tx_s_no, txt_date, tx_falg, txt_name,txt_time;



    }



}
