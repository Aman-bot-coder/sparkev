package com.augmentaa.sparkev.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.get_warrenty.Data;
import com.augmentaa.sparkev.utils.Logger;

import java.util.ArrayList;
import java.util.List;

// The adapter class which 
// extends RecyclerView Adapter 
public class PolicyandServiceAdapter
        extends RecyclerView.Adapter<PolicyandServiceAdapter.MyView> {
    private ItemClickListenerClient mClickListener;
    int selectedPosition = -1;
    ArrayList<String> checkedValue;

    // List with String type
    private List<Data> list;
    Context context;
    boolean check_date;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvPlan,tvPrice,tvDuration;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            this.tvPlan = (TextView) itemView.findViewById(R.id.amc);
            this.tvPrice = (TextView) itemView.findViewById(R.id.price);
            this.tvDuration = (TextView) itemView.findViewById(R.id.duration);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClickClient(v, getAdapterPosition());
            Logger.e("Click on item  "+getAdapterPosition());

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public PolicyandServiceAdapter(List<Data> list, Context context, int selectedPosition) {
        this.list = list;
        this.context = context;
        this.selectedPosition = selectedPosition;


    }

    // Override onCreateViewHolder which deals 
    // with the inflation of the card layout 
    // as an item for the RecyclerView. 
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator 
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.warranty_plan_itemlist, parent, false);
        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals 
    // with the setting of different data 
    // and methods related to clicks on 
    // particular items of the RecyclerView. 
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        final Data myListData = list.get(position);
        holder.tvDuration.setText(myListData.planValidity);
        holder.tvPlan.setText("Plan "+(position+1));
        holder.tvPrice.setText("Rs. "+myListData.mrpInctax);

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

    // Override getItemCount which Returns 
    // the length of the RecyclerView. 
    @Override
    public int getItemCount() {
        return list.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListenerClient itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerClient {
        void onItemClickClient(View view, int position);
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return list.get(id).name;
    }
} 