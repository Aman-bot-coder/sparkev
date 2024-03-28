package com.augmentaa.sparkev.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.Vehicle.GetAllVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.Vehicle;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVehicleAdapter extends BaseAdapter {
    Context context;
    private List<Vehicle> itemsModelsl;
    private List<Vehicle> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    Vehicle loginresponse;
    GetAllVehicle list_vehicle;
    APIInterface apiInterface;
    Dialog dialog_verify_user;


    public MyVehicleAdapter(Context context, List<Vehicle> itemsModelsl) {
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
        try {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.myvehicle_listitem, parent, false);
                viewHolder.txt_regNo = (TextView) convertView.findViewById(R.id.regNo);
                viewHolder.txt_brand = (TextView) convertView.findViewById(R.id.brand_name);
                viewHolder.txt_status = (TextView) convertView.findViewById(R.id.status);
                viewHolder.txt_connector = (TextView) convertView.findViewById(R.id.connector);
                viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            Logger.e("vehicle details=> "+itemsModelListFiltered.get(position).is_default);

            if(itemsModelListFiltered.get(position).is_default==1){
                viewHolder.txt_status.setText("Active");

            }
            else {
                viewHolder.txt_status.setText("Inactive");
            }

            viewHolder.txt_regNo.setText(itemsModelListFiltered.get(position).registration_no);
            viewHolder.txt_brand.setText( itemsModelListFiltered.get(position).brand_name+" "+ itemsModelListFiltered.get(position).model_name);
            viewHolder.txt_connector.setText(itemsModelListFiltered.get(position).connector_type_name);
            this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);



            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_verify_user(position);
                }
            });

        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txt_regNo;
        TextView txt_brand;
        TextView txt_status;
        TextView txt_connector;
        ImageView img_delete;


    }

    public void deleteVehicleDetails(int vehicle_id, final int pos) {
        //GET List Resources
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(),null));

        Call<Vehicle> call = apiInterface.deleteVehicleDetails(hashMap1,vehicle_id,Pref.getIntValue(Pref.TYPE.USER_ID.toString(),0));
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        loginresponse = response.body();
                        Logger.e("Response: " + loginresponse.toString());

//                        if(itemsModelListFiltered.get(pos).id==Pref.getIntValue(Pref.TYPE.VEHICLE_ID.toString(),0)){
//                            Pref.setValue(Pref.TYPE.VEHICLE_ID.toString(),null);
//                            Pref.setValue(Pref.TYPE.VEHICLE_REG.toString(),null);
//                            Pref.setValue(Pref.TYPE.CONN_TYPE.toString(),null);
//
//                        }
//                        String message=loginresponse.message;
                        Toast.makeText(context, itemsModelListFiltered.get(pos).registration_no + " deleted successfully", Toast.LENGTH_LONG).show();
                        itemsModelListFiltered.remove(pos);
                        notifyDataSetChanged();
                        dialog_verify_user.dismiss();
//                        getVehicleList(Pref.getIntValue(Pref.TYPE.USER_ID.toString(),0));


//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    public void getVehicleList(int userId) {
        //GET List Resources
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(),null));

        Call<GetAllVehicle> call = apiInterface.getVehicleDetailsByUserId(hashMap1,userId);
        call.enqueue(new Callback<GetAllVehicle>() {
            @Override
            public void onResponse(Call<GetAllVehicle> call, Response<GetAllVehicle> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {

                        list_vehicle = response.body();
                        Pref.setValue(Pref.TYPE.VEHICLE_DETAILS.toString(),list_vehicle.data.toString());
                        Logger.e("Response delete: 111111" + Pref.getValue(Pref.TYPE.VEHICLE_DETAILS.toString(),null));
                        Logger.e("Response delete: " + list_vehicle.data.size());


                        if(itemsModelListFiltered.size()==0){
                            ((Activity)context).finish();

                        }
                        notifyDataSetChanged();
//
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<GetAllVehicle> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    void dialog_verify_user(int position) {
        dialog_verify_user = new Dialog(context);
        dialog_verify_user.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_verify_user.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_verify_user.setCancelable(false);

        TextView btn_ok = (TextView) dialog_verify_user.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_verify_user.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_verify_user.findViewById(R.id.tv_message);
        tv_message.setText("Are you sure, you want to delete vehicle "+itemsModelListFiltered.get(position).registration_no);
        btn_ok.setText("Confirm");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVehicleDetails(itemsModelListFiltered.get(position).id,position);



            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_verify_user.dismiss();

            }
        });
        dialog_verify_user.show();


    }
}
