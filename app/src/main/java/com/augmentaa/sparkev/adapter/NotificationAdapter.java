package com.augmentaa.sparkev.adapter;

import static com.augmentaa.sparkev.utils.AppUtils.getDateonNotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.notification.Data;
import com.augmentaa.sparkev.ui.ChargerSharingActivity;


import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public NotificationAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_listitem, parent, false);
            viewHolder.txt_notification_name = (TextView) convertView.findViewById(R.id.notification_name);
            viewHolder.txt_description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.img_next = convertView.findViewById(R.id.img_next);
            viewHolder.ly = convertView.findViewById(R.id.ly_notification);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {

            viewHolder.txt_date.setText(getDateonNotification(itemsModelListFiltered.get(position).createdDate));
            viewHolder.txt_description.setText(itemsModelListFiltered.get(position).notificationMessage);
            viewHolder.txt_notification_name.setText(itemsModelListFiltered.get(position).eventName);
            if("S".equalsIgnoreCase(itemsModelListFiltered.get(position).status)){
                viewHolder.ly.setBackground(context.getResources().getDrawable(R.drawable.bg_ly__gray_services));
            }else {
                viewHolder.ly.setBackground(context.getResources().getDrawable(R.drawable.bg_ly_services));

            }


            viewHolder.img_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChargerSharingActivity.class);
                    context.startActivity(intent);
                }
            });


        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txt_notification_name, txt_description, txt_date;
        ImageView img_next;
        LinearLayout ly;

    }


}
