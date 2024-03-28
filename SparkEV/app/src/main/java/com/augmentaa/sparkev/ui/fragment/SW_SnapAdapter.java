package com.augmentaa.sparkev.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;

import java.util.ArrayList;
import java.util.List;

// The adapter class which 
// extends RecyclerView Adapter 
public class SW_SnapAdapter extends RecyclerView.Adapter<SW_SnapAdapter.MyView> {
    private ItemClickListenerSw mClickListener;
    ArrayList<String> checkedValue;

    // List with String type
    private List<Uri> list;
    Context context;
    boolean check_date;
    int selectedPosition = -1;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_screenshot;
        ImageView img_delete;
        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            img_screenshot = (ImageView) view.findViewById(R.id.displayImageView);
            img_delete = (ImageView) view.findViewById(R.id.delete);
            checkedValue = new ArrayList<>();
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClickSw(v, getAdapterPosition());
            Logger.e("Click on item  "+getAdapterPosition());

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public SW_SnapAdapter(List<Uri> list, Context context) {
        this.list = list;
        this.context = context;


    }

    // Override onCreateViewHolder which deals 
    // with the inflation of the card layout 
    // as an item for the RecyclerView. 
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator 
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_camera, parent, false);
        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals 
    // with the setting of different data 
    // and methods related to clicks on 
    // particular items of the RecyclerView. 
    @Override
    public void onBindViewHolder(final MyView holder, @SuppressLint("RecyclerView") int position) {
       holder.img_screenshot .setImageURI(list.get(position));
       holder.img_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AppUtils.showDefaultDialog(context, context.getString(R.string.app_name), "Are you sure, you want to delete?", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       list.remove(position);
                       notifyDataSetChanged();

                   }
               }, null);

           }
       });



    }

    // Override getItemCount which Returns 
    // the length of the RecyclerView. 
    @Override
    public int getItemCount() {
        return list.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListenerSw itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerSw {
        void onItemClickSw(View view, int position);
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return list.get(id).toString();
    }
} 