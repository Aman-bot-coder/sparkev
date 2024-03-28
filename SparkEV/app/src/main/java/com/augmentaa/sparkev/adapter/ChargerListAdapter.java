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
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;

import java.util.List;

public class ChargerListAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public ChargerListAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.charger_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.expirydate = (TextView) convertView.findViewById(R.id.expirydate);
            viewHolder.img_correct = convertView.findViewById(R.id.img_correct);
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
            Logger.e("Charger  "+Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(),null)+itemsModelListFiltered.get(position).serialNo);

            if (Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(),null).equalsIgnoreCase(itemsModelListFiltered.get(position).serialNo)) {
                viewHolder.img_correct.setVisibility(View.VISIBLE);
            } else {
                viewHolder.img_correct.setVisibility(View.GONE);

            }
            viewHolder.expirydate.setText("Warranty (expires " + AppUtils.getOnlyDate(itemsModelListFiltered.get(position).exipry)+ ")");



        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName, expirydate;
        ImageView img_correct;


    }


}
