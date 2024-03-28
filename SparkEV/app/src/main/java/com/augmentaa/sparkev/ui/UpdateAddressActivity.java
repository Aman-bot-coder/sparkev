package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.model.signup.billing_address.RequestBillingAddress;
import com.augmentaa.sparkev.model.signup.billing_address.ResponseBillingAddress;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_warrenty.RespponseGetWarranty;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAddressActivity extends AppCompatActivity implements GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton {

    TextView tv_country, tv_state, et_address, et_zipcode;
    Button btn_submit;
    String address, pin, country, state;
    int country_id, state_id;
    ResponseCountry responseCountry;
    List<ResponseState> responseState;
    APIInterface apiInterface;
    ProgressDialog progress;
    RespponseGetWarranty data;
    int station_id;
    APIInterface apiInterfacepp;
    String order_id;
    String host = "https://securegw-stage.paytm.in/";
    Integer ActivityRequestCode = 2;
    Dialog dialogPaymentSuccessfull;
    String payment_response;
    ImageView img_back;
    BottomSheetDialog dialog_state,dialog_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        getSupportActionBar().hide();
        tv_country = findViewById(R.id.country);
        tv_state = findViewById(R.id.state);
        et_address = findViewById(R.id.address);
        et_zipcode = findViewById(R.id.pin);
        btn_submit = findViewById(R.id.btn_submit);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        apiInterfacepp = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        img_back = findViewById(R.id.back);
        progress = new ProgressDialog(this);
        try {
            data = getIntent().getParcelableExtra("data");
            et_address.setText(data.address);
            et_zipcode.setText(data.pincode);
            tv_country.setText("" + data.country_name);
            tv_state.setText(data.state_name);

            state_id = data.state_id;
            country_id = data.country_id;
            country = data.country_name;
            state = data.state_name;
            pin = data.pincode;
            station_id = data.station_id;

            Logger.e("Warranty data11   " + data.toString());
            Logger.e("Station _id  " + station_id);
        } catch (Exception e) {

        }
        tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(UpdateAddressActivity.this)) {
                    progress = new ProgressDialog(UpdateAddressActivity.this);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    try {
                        getAllStateList(country_id);

                    } catch (Exception e) {
                        Toast.makeText(UpdateAddressActivity.this, "Please select your country", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(UpdateAddressActivity.this)) {
                    progress = new ProgressDialog(UpdateAddressActivity.this);
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    try {
                        getAllCountryList();

                    } catch (Exception e) {
                        Toast.makeText(UpdateAddressActivity.this, "Please select your country", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country = tv_country.getText().toString();
                state = tv_state.getText().toString();
                address = et_address.getText().toString();
                pin = et_zipcode.getText().toString();

                if (TextUtils.isEmpty(country)) {
                    Toast.makeText(UpdateAddressActivity.this, "Please select your country", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(state)) {
                    Toast.makeText(UpdateAddressActivity.this, "Please select your state", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(UpdateAddressActivity.this, "Please enter your address", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(pin)) {
                    Toast.makeText(UpdateAddressActivity.this, "Please enter your zipcode", Toast.LENGTH_LONG).show();

                } else {
                    if (Utils.isNetworkConnected(UpdateAddressActivity.this)) {
                        progress = new ProgressDialog(UpdateAddressActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        try {
                            updateBillingAddress();

                        } catch (Exception e) {
                            Toast.makeText(UpdateAddressActivity.this, "Please select your country", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                }

            }
        });

    }

    public void getAllCountryList() {
        Call<ResponseCountry> call = apiInterface.getAllCountryList();
        call.enqueue(new Callback<ResponseCountry>() {
            @Override
            public void onResponse(Call<ResponseCountry> call, Response<ResponseCountry> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                responseCountry = (ResponseCountry) response.body();

                if (response.code() == 200) {
                    try {
                        if (responseCountry.status) {
                            dialog_getAllCountryList();

                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseCountry> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }

    public void getAllStateList(int country_id) {
        Call<List<ResponseState>> call = apiInterface.getAllStateList(country_id);
        call.enqueue(new Callback<List<ResponseState>>() {
            @Override
            public void onResponse(Call<List<ResponseState>> call, Response<List<ResponseState>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                responseState = response.body();

                if (response.code() == 200) {
                    try {
                        if (responseState.size() > 0) {
                            dialog_getAllStateList();

                        } else {
                            Toast.makeText(getApplicationContext(), "State not found", Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<ResponseState>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    void dialog_getAllCountryList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_country = new BottomSheetDialog(this);
        dialog_country.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_country.setCancelable(true);
        ListView lv_model = (ListView) dialog_country.findViewById(R.id.listview);
        View tv_view = dialog_country.findViewById(R.id.view);
        EditText et_search = dialog_country.findViewById(R.id.search);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(UpdateAddressActivity.this, responseCountry.data,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        /*lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                country_id = responseCountry.data.get(position).id;
                tv_country.setText(responseCountry.data.get(position).name);
                tv_state.setText(null);
                tv_country.setHint("Please select your state");
                tv_state.setHintTextColor(getResources().getColor(R.color.colorText));
//                getAllStateList(country_id);



            }
        });*/
        tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_country.dismiss();
            }
        });

        dialog_country.show();

    }


    void dialog_getAllStateList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
         dialog_state = new BottomSheetDialog(this);
        dialog_state.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_state.setCancelable(true);
        ListView lv_model = (ListView) dialog_state.findViewById(R.id.listview);
        EditText et_search=dialog_state.findViewById(R.id.search);
        View tv_view = dialog_state.findViewById(R.id.view);

        GetAllStateListAdapter adapter = new GetAllStateListAdapter(UpdateAddressActivity.this, responseState,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
       /* lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_state.dismiss();
                state_id = responseState.get(position).id;
                tv_state.setText(responseState.get(position).name);


            }
        });*/
        tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_state.dismiss();
            }
        });

        dialog_state.show();

    }

    private void updateBillingAddress() {
        //GET List Resources

        Logger.e("station_id  in " + station_id);
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.updateBillingAddress(hashMap1, new RequestBillingAddress(data.charger_sr_no, et_address.getText().toString(), et_address.getText().toString(), "", Integer.parseInt(pin), 0, state_id, country_id, "", 0.0, 0.0, Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), station_id)).enqueue(new Callback<ResponseBillingAddress>() {
            public void onResponse(Call<ResponseBillingAddress> call, Response<ResponseBillingAddress> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseBillingAddress loginResponse = (ResponseBillingAddress) response.body();
                        Logger.e("Response: " + loginResponse.toString());


                        if (loginResponse.status) {
                            data.pincode = pin;
                            data.state_id = state_id;
                            data.country_id = country_id;
                            data.country_name = country;
                            data.state_name = state;
                            data.address = et_address.getText().toString();
                            data.station_id = station_id;
                            Logger.e("station_id  in2222   " + data.station_id);
                            Intent intent = new Intent(UpdateAddressActivity.this, WarrantyPurchaseActivity.class);
                            intent.putExtra("data", data);
                            startActivity(intent);
                            finish();
                            Toast.makeText(UpdateAddressActivity.this, "Address updated successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(UpdateAddressActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   UpdateAddressActivity.class.getSimpleName(),
                                    "SPIN_ANDROID",
                                    AppUtils.project_id,
                                    AppUtils.bodyToString(call.request().body()),
                                    "ANDROID",
                                    call.request().url().toString(),
                                    "Y",
                                    AppUtils.DATA_NOT_FOUND_CODE,
                                    AppUtils.DATA_NOT_FOUND,
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

                        }

                    } catch (Exception e) {

                        Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           UpdateAddressActivity.class.getSimpleName(),
                            "SPIN_ANDROID",
                            AppUtils.project_id,
                            AppUtils.bodyToString(call.request().body()),
                            "ANDROID",
                            call.request().url().toString(),
                            "Y",
                            AppUtils.SERVER_ERROR_CODE,
                            AppUtils.SERVER_ERROR,
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                    Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseBillingAddress> call, Throwable t) {
                Logger.e("Exception  " + t.getMessage());
                Toast.makeText(UpdateAddressActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();

                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                       UpdateAddressActivity.class.getSimpleName(),
                        "SPIN_ANDROID",
                        AppUtils.project_id,
                        AppUtils.bodyToString(call.request().body()),
                        "ANDROID",
                        call.request().url().toString(),
                        "Y",
                        AppUtils.SUCCESS_CODE,
                        t.getMessage(),
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


            }
        });
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(UpdateAddressActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


    @Override
    public void search_state(int position, List<ResponseState> itemsModelsl) {
        Logger.e("=====Position ==="+itemsModelsl.size()+"   "+position);
        state_id = itemsModelsl.get(position).id;
        tv_state.setText(itemsModelsl.get(position).name);
        dialog_state.dismiss();

    }

    @Override
    public void search_country(int position, List<Data> list) {
        dialog_country.dismiss();
        country_id = list.get(position).id;
        tv_country.setText(list.get(position).name);
        tv_state.setText(null);
        tv_country.setHint("Please select your state");
        tv_state.setHintTextColor(getResources().getColor(R.color.colorText));
    }
}