package com.augmentaa.sparkev.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.VehicleBrandsAdapter;
import com.augmentaa.sparkev.adapter.VehicleConnectorTypeAdapter;
import com.augmentaa.sparkev.adapter.VehicleModelAdapter;
import com.augmentaa.sparkev.adapter.VehicleYEarAdapter;
import com.augmentaa.sparkev.model.signup.Vehicle.ConnectorTypeDetails;
import com.augmentaa.sparkev.model.signup.Vehicle.ManufacturingYear;
import com.augmentaa.sparkev.model.signup.Vehicle.RequestAddVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.ResponseUpdateVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.Vehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.VehicleBrand;
import com.augmentaa.sparkev.model.signup.Vehicle.VehicleModel;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateVehicleActivity extends AppCompatActivity implements VehicleBrandsAdapter.ClickButton, VehicleModelAdapter.ClickButton, VehicleYEarAdapter.ClickButton {
    EditText et_regNo, et_yearOfMan;
    TextView btn_add;
    String regNo, engineNo, chassisNo, yearOfMan;
    int brandId, modelId, connectorId;
    ImageView img_back;
    APIInterface apiInterface;
    ProgressDialog progress;
    //    LinearLayout btn_login;
    List<VehicleBrand> brand_response;
    List<VehicleModel> model_response;
    List<ConnectorTypeDetails> connectorType_response;
    List<ManufacturingYear> list_year;
    String error_message;
    TextView tvBrand, tvModel, tvConnectorType, tvYear;
    ListView listView;
    Dialog dialogCountry;
    EditText etSearch;

    VehicleBrandsAdapter vehicleBrandsAdapter;
    VehicleConnectorTypeAdapter vehicleConnectorTypeAdapter;
    VehicleModelAdapter vehicleModelAdapter;
    VehicleYEarAdapter vehicleYEarAdapter;
    String activity,connector_type_name;
    Vehicle data;
    int vehicle_id, is_default;
   Dialog dialog_addcharger;
     BottomSheetDialog dialog_brand,dialog_model,dialog_year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);
        getSupportActionBar().hide();
        et_regNo = (EditText) findViewById(R.id.regNo);
        tvBrand = findViewById(R.id.brand);
        tvConnectorType = findViewById(R.id.connector);
        tvModel = findViewById(R.id.model);
        tvYear = findViewById(R.id.year);
        img_back = (ImageView) findViewById(R.id.back);
        btn_add = findViewById(R.id.edit_vehicle);
        this.progress = new ProgressDialog(this);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);

//        btn_login = (LinearLayout) findViewById(R.id.ly_login);

        brand_response = new ArrayList<>();
        list_year = new ArrayList<>();
        model_response = new ArrayList<>();
        connectorType_response = new ArrayList<>();
        list_year.add(new ManufacturingYear(1, "2022"));
        list_year.add(new ManufacturingYear(2, "2021"));
        list_year.add(new ManufacturingYear(3, "2020"));
        list_year.add(new ManufacturingYear(4, "2019"));
        list_year.add(new ManufacturingYear(5, "2018"));
        list_year.add(new ManufacturingYear(6, "2017"));
        list_year.add(new ManufacturingYear(7, "2016"));
        list_year.add(new ManufacturingYear(8, "2015"));
        list_year.add(new ManufacturingYear(9, "2014"));
        list_year.add(new ManufacturingYear(10, "2013"));
        list_year.add(new ManufacturingYear(11, "2012"));
        list_year.add(new ManufacturingYear(12, "2011"));
        list_year.add(new ManufacturingYear(13, "2010"));

        try {
            data = getIntent().getParcelableExtra("data");
            regNo = data.registration_no;
            brandId = data.brand_id;
            modelId = data.model_id;
            connectorId = data.connector_type_id;
            vehicle_id = data.id;
            is_default = data.is_default;
            yearOfMan = data.year_of_manufacture;
            connector_type_name=data.connector_type_name;
            et_regNo.setText(data.registration_no);
            tvBrand.setText(data.brand_name);
            tvModel.setText(data.model_name);
            tvConnectorType.setText(data.connector_type_name);
            tvYear.setText(data.year_of_manufacture);


        } catch (Exception e) {

        }

        et_regNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        try {
            activity = getIntent().getExtras().getString("activity");
        } catch (Exception e) {

        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


       /* tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(UpdateVehicleActivity.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getVehicleBrand();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });

        tvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brand_response.size() > 0) {
                    if (Utils.isNetworkConnected(UpdateVehicleActivity.this)) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        getVehicleModel(brandId);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select vehicle brands", Toast.LENGTH_LONG).show();

                }
            }
        });

        tvConnectorType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_year.size() > 0) {
                    if (Utils.isNetworkConnected(UpdateVehicleActivity.this)) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        getVehicleConnectorType(modelId);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please select manufacturing year ", Toast.LENGTH_LONG).show();

                }
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model_response.size() > 0) {
                    dialogYear();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select vehicle model ", Toast.LENGTH_LONG).show();

                }
            }
        });
*/

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    regNo = et_regNo.getText().toString().trim();
                    yearOfMan = tvYear.getText().toString().trim();
                    if (TextUtils.isEmpty(regNo)) {
                        Toast.makeText(getApplicationContext(), "Please enter registration number ", Toast.LENGTH_LONG).show();
                    } /*else if (!validateRegistrationNumber(regNo)) {
                        Toast.makeText(getApplicationContext(), "Please enter valid registration number", Toast.LENGTH_LONG).show();

                    }*/ else {
//
                        if (Utils.isNetworkConnected(UpdateVehicleActivity.this)) {

                            dialog_setVehicle();

                           /* AppUtils.showDefaultDialog(UpdateVehicleActivity.this, getString(R.string.app_name), "Are you sure select " + regNo + " Vehicle for charging?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progress.setMessage(getResources().getString(R.string.loading));
                                    progress.show();
                                    addVehicle();
                                }
                            }, null);*/

                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }


                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                    Toast.makeText(getApplicationContext(), "Please enter year of manufacturing", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public boolean validateRegistrationNumber(String str) {
        Matcher matcher;
        String EMAIL_PATTERN = "^[A-Z]{2}[0-9]{1,2}(?:[A-Z])?(?:[A-Z]*)?[0-9]{4}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public void getVehicleBrand() {
        Call<List<VehicleBrand>> call = apiInterface.getVehicleBrands();
        call.enqueue(new Callback<List<VehicleBrand>>() {
            @Override
            public void onResponse(Call<List<VehicleBrand>> call, Response<List<VehicleBrand>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        brand_response = response.body();
                        Logger.e("Response: " + brand_response.toString());
                        dialogBrand();

                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<VehicleBrand>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    public void getVehicleModel(int id) {
        Call<List<VehicleModel>> call = apiInterface.getVehicleModel(id);
        call.enqueue(new Callback<List<VehicleModel>>() {
            @Override
            public void onResponse(Call<List<VehicleModel>> call, Response<List<VehicleModel>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        model_response = response.body();
                        dialog_model();
                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        error_message = e.getMessage();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<VehicleModel>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                error_message = t.getMessage();

            }
        });

    }


    private void addVehicle() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.updateVehicleDetails(hashMap1, new RequestAddVehicle(brandId, modelId, connectorId, regNo, yearOfMan, " ", "", "", "Y", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), 1, vehicle_id, Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0))).enqueue(new Callback<ResponseUpdateVehicle>() {
            public void onResponse(Call<ResponseUpdateVehicle> call, final Response<ResponseUpdateVehicle> response) {
                AppUtils.bodyToString(call.request().body());
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        ResponseUpdateVehicle responseAddVehicle = (ResponseUpdateVehicle) response.body();
                        Logger.e("Response: " + responseAddVehicle.toString());

                        if (responseAddVehicle.status) {

                            Pref.setValue(Pref.TYPE.VEHICLE_REG.toString(), regNo);
                            Pref.setIntValue(Pref.TYPE.VEHICLE_ID.toString(), vehicle_id);
                            Pref.setValue(Pref.TYPE.CONN_TYPE.toString(), connector_type_name);
                            Pref.setIntValue(Pref.TYPE.CONN_TYPE_ID.toString(), connectorId);

                            Intent intent = new Intent(UpdateVehicleActivity.this, MyVehicleActivity.class);
                            startActivity(intent);
                            UpdateVehicleActivity.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), responseAddVehicle.message, Toast.LENGTH_LONG).show();

                        }
//


                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseUpdateVehicle> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Vehicle is already added, Please add new vehicle", Toast.LENGTH_LONG).show();
                progress.dismiss();
                Logger.e("Exception: " + t.getMessage());
                call.cancel();
            }
        });

    }

    public void getVehicleConnectorType(int id) {
        Call<List<ConnectorTypeDetails>> call = apiInterface.getConnectorType(id);
        call.enqueue(new Callback<List<ConnectorTypeDetails>>() {
            @Override
            public void onResponse(Call<List<ConnectorTypeDetails>> call, Response<List<ConnectorTypeDetails>> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        connectorType_response = response.body();
                        dialogConnector();
                        Logger.e("Response: " + connectorType_response.toString());


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<ConnectorTypeDetails>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Log.d("TAG", t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });


    }


    void dialogBrand() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);
        dialog_brand = new BottomSheetDialog(this);
        dialog_brand.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_brand.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_brand.setCancelable(true);
        listView = dialog_brand.findViewById(R.id.listview);
        etSearch = dialog_brand.findViewById(R.id.search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                vehicleBrandsAdapter.getFilter().filter(s.toString());
                vehicleBrandsAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (brand_response.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            vehicleBrandsAdapter = new VehicleBrandsAdapter(UpdateVehicleActivity.this, brand_response,this);
            listView.setAdapter(vehicleBrandsAdapter);
//            country.setData(responseCountry.data);
            vehicleBrandsAdapter.notifyDataSetChanged();

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    brandId = brand_response.get(i).id;
                    tvBrand.setText(brand_response.get(i).name);
                    dialog_brand.dismiss();
                }
            });*/
        } else {
            dialog_brand.dismiss();
            Toast.makeText(UpdateVehicleActivity.this, "Vehicle brand not found", Toast.LENGTH_LONG).show();
        }
        dialog_brand.show();


    }


    void dialog_model() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);
      dialog_model = new BottomSheetDialog(this);
        dialog_model.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_model.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_model.setCancelable(true);
        listView = dialog_model.findViewById(R.id.listview);
        etSearch = dialog_model.findViewById(R.id.search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                vehicleModelAdapter.getFilter().filter(s.toString());
                vehicleModelAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (model_response.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            vehicleModelAdapter = new VehicleModelAdapter(UpdateVehicleActivity.this, model_response,this);
            listView.setAdapter(vehicleModelAdapter);
//            country.setData(responseCountry.data);
            vehicleModelAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    modelId = model_response.get(i).id;
                    tvModel.setText(model_response.get(i).name);

                    dialog_model.dismiss();
                }
            });
        } else {
            dialog_model.dismiss();
            Toast.makeText(UpdateVehicleActivity.this, "Model not found", Toast.LENGTH_LONG).show();
        }
        dialog_model.show();


    }


    void dialogConnector() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);
        final BottomSheetDialog dialog_connector = new BottomSheetDialog(this);
        dialog_connector.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_connector.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_connector.setCancelable(true);
        listView = dialog_connector.findViewById(R.id.listview);
        etSearch = dialog_connector.findViewById(R.id.search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                vehicleConnectorTypeAdapter.getFilter().filter(s.toString());
                vehicleConnectorTypeAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (connectorType_response.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            vehicleConnectorTypeAdapter = new VehicleConnectorTypeAdapter(UpdateVehicleActivity.this, connectorType_response);
            listView.setAdapter(vehicleConnectorTypeAdapter);
//            country.setData(responseCountry.data);
            vehicleConnectorTypeAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    connectorId = connectorType_response.get(i).ct_id;
                    tvConnectorType.setText(connectorType_response.get(i).name);
                    dialog_connector.dismiss();
                }
            });
        } else {
            dialog_connector.dismiss();
            Toast.makeText(UpdateVehicleActivity.this, "Connector not found", Toast.LENGTH_LONG).show();
        }
        dialog_connector.show();


    }

    void dialogYear() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);
         dialog_year = new BottomSheetDialog(this);
        dialog_year.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_year.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_year.setCancelable(true);
        listView = dialog_year.findViewById(R.id.listview);
        etSearch = dialog_year.findViewById(R.id.search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                vehicleBrandsAdapter.getFilter().filter(s.toString());
                vehicleBrandsAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (list_year.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            vehicleYEarAdapter = new VehicleYEarAdapter(UpdateVehicleActivity.this, list_year,this);
            listView.setAdapter(vehicleYEarAdapter);
//            country.setData(responseCountry.data);
            vehicleYEarAdapter.notifyDataSetChanged();

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    yearOfMan = list_year.get(i).year;
                    tvYear.setText(yearOfMan);
                    dialog_year.dismiss();
                }
            });*/
        } else {
            dialog_year.dismiss();
            Toast.makeText(UpdateVehicleActivity.this, "Country not found", Toast.LENGTH_LONG).show();
        }
        dialog_year.show();


    }


    void dialog_setVehicle() {
        dialog_addcharger = new Dialog(UpdateVehicleActivity.this);
        dialog_addcharger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_addcharger.setContentView(R.layout.app_dialog);
        dialog_addcharger.setCancelable(false);
        TextView btn_ok = (TextView) dialog_addcharger.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_addcharger.findViewById(R.id.tv_cancel);
//        btn_cencel.setVisibility(View.INVISIBLE);
        TextView tv_message = dialog_addcharger.findViewById(R.id.tv_message);
//        btn_ok.setText("Ok");

        tv_message.setText("Are you sure select " + regNo + " Vehicle for charging?");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_addcharger.dismiss();
                progress.setMessage(getResources().getString(R.string.loading));
                progress.show();
                addVehicle();



            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_addcharger.dismiss();

            }
        });
        dialog_addcharger.show();


    }


    @Override
    public void search_brand(int position, List<VehicleBrand> list) {
        brandId = list.get(position).id;
        tvBrand.setText(list.get(position).name);
        dialog_brand.dismiss();
    }
    @Override
    public void search_model(int position, List<VehicleModel> list) {
        modelId = list.get(position).id;
        tvModel.setText(list.get(position).name);
        dialog_model.dismiss();

    }

    @Override
    public void search_year(int position, List<ManufacturingYear> list) {
        yearOfMan = list.get(position).year;
        tvYear.setText(yearOfMan);
        dialog_year.dismiss();
    }
}
