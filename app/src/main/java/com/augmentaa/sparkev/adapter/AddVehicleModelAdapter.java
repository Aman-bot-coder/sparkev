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
import com.augmentaa.sparkev.model.signup.Vehicle.VehicleModel;

import java.util.List;

public class AddVehicleModelAdapter extends BaseAdapter {
    Context context;
    private List<VehicleModel> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    ClickButton clickButton;

    public AddVehicleModelAdapter(Context context, List<VehicleModel> itemsModelsl,ClickButton clickButton) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.clickButton=clickButton;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.add_vehicle_model_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
//            viewHolder.img_conn = (ImageView) convertView.findViewById(R.id.img_connector);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).name);

        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton.search_model(position,itemsModelListFiltered);
            }
        });
//        viewHolder.img_conn.setImageResource(R.drawable.ic_connector);
//        viewHolder.img_conn.setVisibility(View.GONE);

        return convertView;
    }


    public class ViewHolder {
        TextView txtName;
        ImageView img_conn;



    }

    public interface ClickButton {
        void search_model(int position,List<VehicleModel> list);


    }
}
