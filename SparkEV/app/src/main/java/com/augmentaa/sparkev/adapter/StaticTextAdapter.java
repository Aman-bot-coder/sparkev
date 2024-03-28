package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.mobile_static_text.Data;

import java.util.List;

public class StaticTextAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    private List<Data> itemsModelsl;
    public StaticTextAdapter(Context context, List<Data> itemsModelsl) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.itemsModelsl = itemsModelsl;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.static_text_itemlist, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
//        viewHolder.txtName.setText((position+1)+". " + itemsModelListFiltered.get(position).question);
        viewHolder.txtTitle.setText(""+itemsModelListFiltered.get(position).title1);
        viewHolder.txtDescription.setText(""+itemsModelListFiltered.get(position).desc1);


        viewHolder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.txtDescription.setVisibility(View.GONE);
            }
        });
        return convertView;
    }


    public class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;



    }


}
