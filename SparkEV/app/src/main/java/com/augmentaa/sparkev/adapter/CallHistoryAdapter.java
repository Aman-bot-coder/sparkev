package com.augmentaa.sparkev.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.call_request.Data;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CallHistoryAdapter extends BaseAdapter {
    Activity context;
    private List<Data> itemsModelsl;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public CallHistoryAdapter(Activity context, List<Data> itemsModelsl) {
        this.context = context;
        this.itemsModelsl = itemsModelsl;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.call_history_request_itemlist, parent, false);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txt_question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.txt_status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.txt_time= (TextView) convertView.findViewById(R.id.time);
            viewHolder.txt_mobile = (TextView) convertView.findViewById(R.id.mobile);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        try {
            if(getTime(itemsModelListFiltered.get(position).date)!=null){
                String date_time=getTime(itemsModelListFiltered.get(position).date);
                String[] splited = date_time.split("\\s+");
                String date=splited[0];
                String time =splited[1];
                viewHolder.txt_date.setText(""+date);
                viewHolder.txt_time.setText(""+time);
            }


            viewHolder.txt_question.setText(""+itemsModelListFiltered.get(position).question);
            viewHolder.txt_mobile.setText(""+ Pref.getValue(Pref.TYPE.MOBILE.toString(),null));


            viewHolder.txt_status.setText("Close");

//        viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));


            Logger.e(" Data Question "+itemsModelListFiltered.get(position).question);
        }
        catch (Exception e){

        }



        return convertView;
    }

   /* @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                try {

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelsl.size();
                        filterResults.values = itemsModelsl;


                    } else {
                        List<CallRequest> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toLowerCase();
                        for (CallRequest itemsModel : itemsModelsl) {
//                            if (itemsModel.mobile_new == null) {
//                                itemsModel.setMobile_new("Not Applicable");
//                            }
//                            if (itemsModel.bin_new == null) {
//                                itemsModel.setBin_new("Not Applicable");
//                            }
//                            if (itemsModel.pdu_new == null) {
//                                itemsModel.setPdu_new("Not Applicable");
//                            }
//                            if (itemsModel.bms_new == null) {
//                                itemsModel.setBms_new("Not Applicable");
//                            }
//                            if (itemsModel.app_first_name == null) {
//                                itemsModel.setApp_first_name("Not Applicable");
//                            }
//                            if (itemsModel.tcu_new == null) {
//                                itemsModel.setTcu_new("Not Applicable");
//                            }
//                            if (itemsModel.sim_card_number_new == null) {
//                                itemsModel.setSim_card_number_new("Not Applicable");
//                            }
//                            if (itemsModel.part_code_new == null) {
//                                itemsModel.setPart_code_new("Not Applicable");
//                            }
//                            if (itemsModel.email == null) {
//                                itemsModel.setEmail("Not Applicable");
//                            }
//                            if (itemsModel.request_id == null) {
//                                itemsModel.setRequest_id("Not Applicable");
//
//                            }
//                            if (itemsModel.requestedDt == null) {
//                                itemsModel.setRequestedDt("Not Applicable");
//                            }
//                            if (itemsModel.status == null) {
//                                itemsModel.setStatus("Not Applicable");
//                            }


                            if (
                                    itemsModel.getChargerName().toLowerCase().contains(searchStr) ||
                                            itemsModel.getDistance().toLowerCase().contains(searchStr) ||
                                            itemsModel.getTotalCharger().toLowerCase().contains(searchStr) ||
                                            itemsModel.getActiveCharger().toLowerCase().contains(searchStr) ||
                                            itemsModel.getChargerAddress().toLowerCase().contains(searchStr)


                            ) {

                                resultsModel.add(itemsModel);

                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;


                        }
                    }


//                }
                } catch (Exception e) {

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsModelListFiltered = (List<CallRequest>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }*/

    public class ViewHolder {
        TextView txt_question;
        TextView txt_date;
        TextView txt_status;
        TextView txt_time;
        TextView txt_mobile;


    }
    private String getTime(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy HH:mm a");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm a", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

}
