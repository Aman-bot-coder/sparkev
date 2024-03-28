package com.augmentaa.sparkev.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_warrenty.Data;

import java.util.List;

// The adapter class which 
// extends RecyclerView Adapter

public class WarrantyPlanAdapter extends RecyclerView.Adapter<WarrantyPlanAdapter.ViewHolder> {

    List<Data> listdata;
    Context context;
    boolean status;
    private ItemClickListenerClient mClickListener;
    int selectedPosition = -1;

    // RecyclerView recyclerView;
    public WarrantyPlanAdapter(List<Data> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.warranty_plan_itemlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Data myListData = listdata.get(position);
        holder.tvDuration.setText(myListData.planValidity);
        holder.tvPlan.setText("Plan "+(position+1));
        holder.tvPrice.setText(""+myListData.mrpInctax);

        if (selectedPosition == position) {
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.Green));
            holder.tvPlan.setTextColor(context.getResources().getColor(R.color.Green));
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.Green));

        } else {
            holder.tvDuration.setTextColor(context.getResources().getColor(R.color.colorGray));
            holder.tvPlan.setTextColor(context.getResources().getColor(R.color.colorGray));
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGray));
        }


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void setClickListener() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPlan,tvPrice,tvDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvPlan = (TextView) itemView.findViewById(R.id.amc);
            this.tvPrice = (TextView) itemView.findViewById(R.id.price);
            this.tvDuration = (TextView) itemView.findViewById(R.id.duration);
        }


    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerClient {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListenerClient itemClickListener) {
        this.mClickListener = itemClickListener;
    }


} 