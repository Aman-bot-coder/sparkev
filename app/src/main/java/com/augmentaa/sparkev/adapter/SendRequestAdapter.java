package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.guest_access_list.Data;

import java.util.List;

public class SendRequestAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public SendRequestAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recieved_guest_access_list, parent, false);
            viewHolder.txtnickName = (TextView) convertView.findViewById(R.id.nickName);
            viewHolder.txtSerialNo = (TextView) convertView.findViewById(R.id.serial_number);
            viewHolder.txt_permission = (TextView) convertView.findViewById(R.id.permission);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txtnickName.setText("" + itemsModelListFiltered.get(position).userEmail);
        viewHolder.txtSerialNo.setText("" + itemsModelListFiltered.get(position).deviceNumber);

        if (itemsModelListFiltered.get(position).status == 1) {
            viewHolder.txt_permission.setText("Granted");
            viewHolder.txt_permission.setTextColor(context.getResources().getColor(R.color.Green));


        } else if (itemsModelListFiltered.get(position).status == 2) {
            viewHolder.txt_permission.setText("Deny");
            viewHolder.txt_permission.setTextColor(context.getResources().getColor(R.color.colorRed));


        } else {
            viewHolder.txt_permission.setText("Pending");
            viewHolder.txt_permission.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        return convertView;
    }


    public class ViewHolder {
        TextView txtnickName, txtSerialNo, txt_permission;
        ImageView img_conn;


    }


}
