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

import java.util.List;

public class SessionLogsBLEAdapter extends BaseAdapter {
    Context context;
    private List<com.augmentaa.sparkev.model.signup.session_history.Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    APIInterface apiInterface;
    List<String>list_show_day_name;
    public SessionLogsBLEAdapter(Context context, List<com.augmentaa.sparkev.model.signup.session_history.Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.session_logs_itemlist, parent, false);
            viewHolder.tx_s_no = (TextView) convertView.findViewById(R.id.s_no);
            viewHolder.txt_start_time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.txt_kwh = (TextView) convertView.findViewById(R.id.energy);
            viewHolder.txt_stop_time = (TextView) convertView.findViewById(R.id.stop_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {







        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView tx_s_no, txt_start_time, txt_kwh, txt_stop_time,tv_mode;



    }



}
