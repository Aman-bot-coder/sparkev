package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.ui.ChargerSharingActivity;

import java.util.List;

public class ChargerListDetialsAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public ChargerListDetialsAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.mychrger_list_listitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.charger_name);
            viewHolder.expirydate = (TextView) convertView.findViewById(R.id.expirydate);
            viewHolder.txt_active = (TextView) convertView.findViewById(R.id.status);
            viewHolder.txt_charger_mode = (TextView) convertView.findViewById(R.id.charger_owner);
            viewHolder.txt_status = (TextView) convertView.findViewById(R.id.charger_status);
            viewHolder.img_shared = convertView.findViewById(R.id.img_shared);
            viewHolder.img_view_details = convertView.findViewById(R.id.img_view_details);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {
            if (itemsModelListFiltered.get(position).nickName != null) {
                if ("".equalsIgnoreCase(itemsModelListFiltered.get(position).nickName)||" ".equalsIgnoreCase(itemsModelListFiltered.get(position).nickName)){
                    viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serialNo);

                }
                else {
                    viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).nickName);

                }

            } else {
                viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serialNo);

            }


            viewHolder.expirydate.setText("" + itemsModelListFiltered.get(position).planValidity);
            if (itemsModelListFiltered.get(position).chargingStatus.equalsIgnoreCase("Offline")) {
                viewHolder.txt_active.setText("Inactive");

            } else {
                viewHolder.txt_active.setText("Active");

            }

            if (itemsModelListFiltered.get(position).chargingStatus.equalsIgnoreCase("Offline")) {
                viewHolder.txt_status.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_offline, 0, 0, 0);

            } else {
                viewHolder.txt_status.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.circle_theme, 0, 0, 0);

            }


            viewHolder.txt_charger_mode.setText("" + itemsModelListFiltered.get(position).charger_mode);
            viewHolder.txt_status.setText("" + itemsModelListFiltered.get(position).chargingStatus);

            viewHolder.img_shared.setOnClickListener(new View.OnClickListener() {
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
        TextView txtName, expirydate, txt_active, txt_status, txt_charger_mode;
        ImageView img_shared, img_view_details;


    }


}
