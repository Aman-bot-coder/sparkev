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
import com.augmentaa.sparkev.model.signup.upgrage_charger_list.Data;
import java.util.List;

public class UpgradeBLEChargerListAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public UpgradeBLEChargerListAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.upgrade_ble_charger_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
//            viewHolder.expirydate = (TextView) convertView.findViewById(R.id.expirydate);
//            viewHolder.img_correct = convertView.findViewById(R.id.img_correct);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        try {
            if (itemsModelListFiltered.get(position).nick_name != null) {
                if ("".equalsIgnoreCase(itemsModelListFiltered.get(position).nick_name) || " ".equalsIgnoreCase(itemsModelListFiltered.get(position).nick_name)) {
                    viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serial_no);

                } else {
                    viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).nick_name);

                }

            } else {
                viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).serial_no);

            }


        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName, expirydate;
        ImageView img_correct;


    }


}
