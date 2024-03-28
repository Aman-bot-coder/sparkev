package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetCountryListAdapter;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.mobile_validation.RequestMobileNumber;
import com.augmentaa.sparkev.model.signup.mobile_validation.ResponseMobileNumber;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountLocationPhoneActivity extends AppCompatActivity {
    EditText etMobile;
    TextView etLocation;
    Button btnContinue;
    ImageView img_back;
    String email, name, location, mobile;
    APIInterface apiInterface;
    ProgressDialog progress;
    ResponseCountry responseCountry;
    GetCountryListAdapter adapter;
    int country_id;
    ListView listView;
    Dialog dialogCountry;
    EditText etSearch;
    List<Data> list_search;
    public static ResponseCountry country;
    TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_location_phone);
        etMobile = findViewById(R.id.mobile);
        etLocation = findViewById(R.id.location);
        btnContinue = findViewById(R.id.create_account);
        this.progress = new ProgressDialog(this);
        img_back = findViewById(R.id.back);
        tvSignIn = findViewById(R.id.signIn);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        getSupportActionBar().hide();
        list_search = new ArrayList<>();
        country = new ResponseCountry();
        try {
            name = getIntent().getExtras().getString("name");
            email = getIntent().getExtras().getString("email");

        } catch (Exception e) {

        }

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateAccountLocationPhoneActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(CreateAccountLocationPhoneActivity.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    getAllCountryList();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
//

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = etMobile.getText().toString().trim();
                location = etLocation.getText().toString().trim();
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(CreateAccountLocationPhoneActivity.this, "Please enter your country.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(CreateAccountLocationPhoneActivity.this, "Please enter your phone number.", Toast.LENGTH_LONG).show();

                } else {
                    /*if (Utils.isNetworkConnected(CreateAccountLocationPhoneActivity.this)) {
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        checkEmailMobile();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }*/
                    Intent intent = new Intent(CreateAccountLocationPhoneActivity.this, CreateAccountPasswordActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("country_id", country_id);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);

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
                            if (responseCountry.data.size() > 0) {

                                dialogCountry();
                            }


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

    void dialogCountry() {
//        dialogCountry = new Dialog(CreateAccountLocationPhoneActivity.this);
//        dialogCountry.setContentView(R.layout.dialog_countrylist);

        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        listView = dialog_Distance.findViewById(R.id.listview);
        etSearch = dialog_Distance.findViewById(R.id.search);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();

//                Logger.e("======Data list=onTextChanged="+country.getData().toString()+ "       "+country.getData().size());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (responseCountry.data.size() > 0) {
//            country.setSearch_data(responseCountry.search_data);
            adapter = new GetCountryListAdapter(CreateAccountLocationPhoneActivity.this, responseCountry.data);
            listView.setAdapter(adapter);
//            country.setData(responseCountry.data);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                    Logger.e("======Data list=itemClick="+country.getData().toString()+ "       "+country.getData().size());
                    country_id = country.getData().get(i).id;
                    etLocation.setText(country.getData().get(i).name);
//                    etMobile.setMaxEms(country.getData().get(i).max_mobile_length);

                    InputFilter[] filters = new InputFilter[1];
                    filters[0] = new InputFilter.LengthFilter(country.getData().get(i).max_mobile_length); //Filter to 10 characters
                    etMobile.setFilters(filters);
                    Logger.e("======Data list=itemClick=" + country.getData().get(i).name + "       " +country.getData().get(i).max_mobile_length );
                    adapter.notifyDataSetChanged();
                    dialog_Distance.dismiss();
                }
            });
        } else {
            dialog_Distance.dismiss();
            Toast.makeText(CreateAccountLocationPhoneActivity.this, "Country not found", Toast.LENGTH_LONG).show();
        }
        dialog_Distance.show();


    }


    private void checkEmailMobile() {
        this.apiInterface.checkMobileEmail(new RequestMobileNumber(email, mobile, "BOTH")).enqueue(new Callback<ResponseMobileNumber>() {
            public void onResponse(Call<ResponseMobileNumber> call, Response<ResponseMobileNumber> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                ResponseMobileNumber loginResponse = (ResponseMobileNumber) response.body();
                if (response.code() == 200) {
                    try {
                        Logger.e("Response:  " + loginResponse.toString());
                        if (loginResponse.status) {
                            Intent intent = new Intent(CreateAccountLocationPhoneActivity.this, CreateAccountPasswordActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("country_id", country_id);
                            intent.putExtra("mobile", mobile);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();

                        }

//                    return;
//                        } else {
//                            Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_LONG).show();
//
//                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }


            }

            public void onFailure(Call<ResponseMobileNumber> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();


            }
        });

    }

}