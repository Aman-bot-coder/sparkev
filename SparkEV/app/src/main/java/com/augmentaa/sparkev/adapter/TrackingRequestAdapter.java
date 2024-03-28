package com.augmentaa.sparkev.adapter;

import static com.augmentaa.sparkev.utils.AppUtils.geDateSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.tracking.Details;
import com.augmentaa.sparkev.utils.Logger;


import java.util.List;

public class TrackingRequestAdapter extends BaseAdapter {
    Context context;
    private List<Details> itemsModelListFiltered;
    ViewHolder viewHolder;
    private List<Details> itemsModelsl;

    public TrackingRequestAdapter(Context context, List<Details> itemsModelsl) {
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
            if (itemsModelListFiltered.get(position).requestDt != null) {
                viewHolder.txtDate.setText("" + geDateSummary(itemsModelListFiltered.get(position).requestDt));
            } else {
                viewHolder.txtDate.setText("");
            }
            viewHolder.txtStatus.setText("" + itemsModelListFiltered.get(position).status);

            Logger.e("==Status===" + itemsModelListFiltered.get(position).isChecked);

//

            if (itemsModelListFiltered.get(position).isChecked) {
                viewHolder.ly_status_checked.setVisibility(View.VISIBLE);
                viewHolder.ly_status_unchecked.setVisibility(View.GONE);
                    viewHolder.v_statusLine.setBackgroundColor(context.getResources().getColor(R.color.themeColor));

            } else {
                viewHolder.ly_status_checked.setVisibility(View.GONE);
                viewHolder.ly_status_unchecked.setVisibility(View.VISIBLE);
                    viewHolder.v_statusLine.setBackgroundColor(context.getResources().getColor(R.color.colorTextLight));

            }
/*
            if (position > 0) {
                if (itemsModelListFiltered.get(position - 1).isChecked) {
                    viewHolder.v_statusLine.setBackgroundColor(context.getResources().getColor(R.color.themeColor));
                } else {
                    viewHolder.v_statusLine.setBackgroundColor(context.getResources().getColor(R.color.colorTextLight));

                }
            }*/


            if (position == itemsModelListFiltered.size() - 1) {
                viewHolder.v_statusLine.setVisibility(View.GONE);
            } else {
                viewHolder.v_statusLine.setVisibility(View.VISIBLE);

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
