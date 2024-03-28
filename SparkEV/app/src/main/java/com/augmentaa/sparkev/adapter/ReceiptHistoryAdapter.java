package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.receipt_history.Data;
import com.augmentaa.sparkev.utils.AppUtils;

import java.util.List;

public class ReceiptHistoryAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public ReceiptHistoryAdapter(Context context, List<Data> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.receipt_history_listitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.charger_name);
            viewHolder.expirydate = (TextView) convertView.findViewById(R.id.session_date);
            viewHolder.tv_receipt_no = (TextView) convertView.findViewById(R.id.reciept_no);
            viewHolder.tv_plan = (TextView) convertView.findViewById(R.id.warranty_plan);
            viewHolder.tv_activation_date = (TextView) convertView.findViewById(R.id.date_of_activation);
            viewHolder.tv_purchase_date = (TextView) convertView.findViewById(R.id.date_of_purchase);
            viewHolder.tv_activation_on = (TextView) convertView.findViewById(R.id.activate_on);
            viewHolder.tv_valid_upto = (TextView) convertView.findViewById(R.id.valid_upto);
            viewHolder.tv_warranty_text = (TextView) convertView.findViewById(R.id.upgrade_text);
            viewHolder.tv_purchase_amount = (TextView) convertView.findViewById(R.id.purchase_amount);

            viewHolder.ly_upgrade =  convertView.findViewById(R.id.ly_upgrage);
            viewHolder.ly_warranty= convertView.findViewById(R.id.ly_warranty);

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



            if("UPGRADE".equalsIgnoreCase(itemsModelListFiltered.get(position).activityType)){
                viewHolder.ly_upgrade.setVisibility(View.VISIBLE);
                viewHolder.ly_warranty.setVisibility(View.GONE);
            }
            else {
                viewHolder.ly_upgrade.setVisibility(View.GONE);
                viewHolder.ly_warranty.setVisibility(View.VISIBLE);
            }


            viewHolder.expirydate.setText(AppUtils.getOnlyDate(itemsModelListFiltered.get(position).startDate) + " - " + AppUtils.getOnlyDate(itemsModelListFiltered.get(position).endDate));
            viewHolder.tv_receipt_no.setText("Receipt #"+itemsModelListFiltered.get(position).receiptNo);
            viewHolder.tv_plan.setText(itemsModelListFiltered.get(position).planValidity);


            viewHolder.tv_activation_date .setText(AppUtils.getOnlyDate(itemsModelListFiltered.get(position).startDate));
            viewHolder.tv_purchase_date.setText(AppUtils.getOnlyDate(itemsModelListFiltered.get(position).startDate));
            viewHolder.tv_activation_on.setText(AppUtils.getOnlyDate(itemsModelListFiltered.get(position).startDate));
            viewHolder.tv_valid_upto.setText(AppUtils.getOnlyDate(itemsModelListFiltered.get(position).endDate));

            viewHolder.tv_warranty_text .setText(itemsModelListFiltered.get(position).up_desc);
            viewHolder.tv_purchase_amount .setText(""+itemsModelListFiltered.get(position).mrpInctax+"/-");





        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName, expirydate, tv_receipt_no, tv_plan,
                tv_activation_date,tv_purchase_date,tv_activation_on,tv_valid_upto,tv_warranty_text,tv_purchase_amount;
        LinearLayout ly_warranty,ly_upgrade;


    }


}
