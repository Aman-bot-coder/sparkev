package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.guest_access_list.Data;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestDenyGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGrantGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseGuestAccessList;
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

public class RecievedSharedAdapter extends BaseAdapter {
    Context context;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    APIInterface apiInterfaceSPIN;
      RevokeCharger revokeCharger;


    public RecievedSharedAdapter(Context context, List<Data> itemsModelsl, RevokeCharger revokeCharger) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.progress = new ProgressDialog(context);
        this.apiInterfaceSPIN = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);
        this.revokeCharger=revokeCharger;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.share_charger_itemlist, parent, false);
            viewHolder.txtnickName = (TextView) convertView.findViewById(R.id.username);
            viewHolder.txtRevoke= (TextView) convertView.findViewById(R.id.tv_revoke);
            viewHolder.img_profile =  convertView.findViewById(R.id.profile_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.txtnickName.setText("" + itemsModelListFiltered.get(position).userEmail);

        viewHolder.txtRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeCharger.event_revoke(position);
/*
                if (Utils.isNetworkConnected(context)) {
                    progress = new ProgressDialog(context);
                    progress.setMessage(context.getResources().getString(R.string.loading));
                    progress.show();
                    denyGuestAccess(itemsModelListFiltered.get(position).deviceNumber, itemsModelListFiltered.get(position).id, itemsModelListFiltered.get(position).userId);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }*/

            }
        });





        return convertView;
    }


    public class ViewHolder {
        TextView txtnickName, txtRevoke;
        ImageView img_profile;


    }

    private void grantGuestAccess(String device_number, int request_id, int user_id,String nickName) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.grantGuestAccess(hashMap1, new RequestGrantGuestAccess(device_number, request_id, user_id)).enqueue(new Callback<ResponseGuestAccessList>() {
            public void onResponse(Call<ResponseGuestAccessList> call, final Response<ResponseGuestAccessList> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
//                    try {
                        ResponseGuestAccessList responseAddCharger = (ResponseGuestAccessList) response.body();
                        Logger.e("Response 123: " + responseAddCharger.toString());
                        if (responseAddCharger.status == 1) {
                            userChargerMappingBLESync( device_number,  request_id,  user_id, nickName);
                        } else if (responseAddCharger.status == 2) {

                        }
                        Toast.makeText(context, responseAddCharger.msg, Toast.LENGTH_LONG).show();


//                    } catch (Exception e) {
//                        progress.dismiss();
//                        Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
//
//                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
                call.cancel();
            }
        });


    }

    private void denyGuestAccess(String device_number, int request_id, int user_id) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.denyGuestAccess(hashMap1, new RequestDenyGuestAccess(device_number, request_id, user_id)).enqueue(new Callback<ResponseGuestAccessList>() {
            public void onResponse(Call<ResponseGuestAccessList> call, final Response<ResponseGuestAccessList> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {

//                    try {
                        ResponseGuestAccessList responseAddCharger = (ResponseGuestAccessList) response.body();
                        Logger.e("Response 123: " + responseAddCharger.toString());
                       /* if (responseAddCharger.status == 1) {

                        } else if (responseAddCharger.status == 2) {

                        }*/
                        Toast.makeText(context, responseAddCharger.msg, Toast.LENGTH_LONG).show();


//                    } catch (Exception e) {
//                        progress.dismiss();
//                        Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
//
//                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseGuestAccessList> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
                call.cancel();
            }
        });


    }
    private void userChargerMappingBLESync(String device_number, int request_id, int user_id,String nickname) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfaceSPIN.userChargerMappingBLESync(hashMap1, new RequestAddChargerSyncBLE(device_number, " ", nickname,user_id, 0.0, 0.0, "Y", "", 1)).enqueue(new Callback<ResponseAddChargerSyncBLE>() {
            public void onResponse(Call<ResponseAddChargerSyncBLE> call, final Response<ResponseAddChargerSyncBLE> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {

                        ResponseAddChargerSyncBLE ResponseAddCharger = (ResponseAddChargerSyncBLE) response.body();
                        Logger.e("Response:1212 " + ResponseAddCharger.toString());


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseAddChargerSyncBLE> call, Throwable t) {
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
                call.cancel();
            }
        });

    }

    public interface RevokeCharger {
        void event_revoke(int position);
    }
}
