package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.warranty_history.History;

import java.util.List;

public class WarrantyTransactionChargerHistoryAdapter extends BaseAdapter {
    Context context;
    private List<History> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public WarrantyTransactionChargerHistoryAdapter(Context context, List<History> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.warranty_trnansaction_history_itemlist, parent, false);
            viewHolder.txtTransactionId = (TextView) convertView.findViewById(R.id.transaction_id);
            viewHolder.txtSerialNo = (TextView) convertView.findViewById(R.id.serial_number);
            viewHolder.txtExpirydate = (TextView) convertView.findViewById(R.id.amc);
            viewHolder.img_next = (ImageView) convertView.findViewById(R.id.img_next);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txtTransactionId.setText("" + itemsModelListFiltered.get(position).transactionId);
        viewHolder.txtSerialNo.setText("" + itemsModelListFiltered.get(position).serialNo);
        viewHolder.txtExpirydate.setText("" + itemsModelListFiltered.get(position).planValidity);


        viewHolder.img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Next", Toast.LENGTH_LONG).show();

            }
        });
        return convertView;
    }


    public class ViewHolder {
        TextView txtTransactionId, txtSerialNo, txtExpirydate, txtRenew;
        ImageView img_next;


    }


}
