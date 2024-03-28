package com.augmentaa.sparkev.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.tracking.Data;

import java.util.List;

public class TrackingListAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ViewHolder viewHolder;
    private List<Data> itemsModelsl;

    public TrackingListAdapter(Context context, List<Data> itemsModelsl) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.itemsModelsl = itemsModelsl;

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

        try {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.tracking_itemlist, parent, false);
                viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date);
                viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.status);
                viewHolder.v_statusLine = (View) convertView.findViewById(R.id.status_line);
                viewHolder.ly_status_checked = convertView.findViewById(R.id.ly_status_checked);
                viewHolder.ly_status_unchecked = convertView.findViewById(R.id.ly_status_unchecked);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }



        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtDate;
        TextView txtStatus;
        View v_statusLine;
        LinearLayout ly_status_checked, ly_status_unchecked;


    }


}
