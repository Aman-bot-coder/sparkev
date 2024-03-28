package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.augmentaa.sparkev.R;

import java.util.List;

public class TimeListAdapter extends BaseAdapter {
    Context context;
    private List<String> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;

    public TimeListAdapter(Context context, List<String> itemsModelsl) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.duration_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.duration);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txtName.setText(itemsModelListFiltered.get(position));

        return convertView;
    }


    public class ViewHolder {
        TextView txtName;



    }


}
