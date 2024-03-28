package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.schedule.Schedule;
import com.augmentaa.sparkev.utils.Logger;

import java.util.List;

public class DayListAdapter extends BaseAdapter {
    Context context;
    private List<Schedule> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public DayListAdapter(Context context, List<Schedule> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.day_listitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.rl_view);
            convertView.setTag(viewHolder);
            viewHolder.checkBox.setTag(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int pos = Integer.parseInt(viewHolder.checkBox.getTag().toString()); //to take the actual position
                if (isChecked) {
                    itemsModelListFiltered.get(position).setChecked(true);

                } else {
                    itemsModelListFiltered.get(position).setChecked(false);
                }
                Logger.e("checked data==" + itemsModelListFiltered.get(position).isChecked() + "  " + position);
                notifyDataSetChanged();
            }


        });

        viewHolder.checkBox.setText(itemsModelListFiltered.get(position).dayName);
        viewHolder.checkBox.setChecked(itemsModelListFiltered.get(position).isChecked());
        return convertView;
    }


    public class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
        RelativeLayout layout;


    }


}
