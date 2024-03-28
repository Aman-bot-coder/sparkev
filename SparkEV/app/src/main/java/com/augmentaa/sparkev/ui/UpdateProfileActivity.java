package com.augmentaa.sparkev.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.profile.ProfileData;
import com.augmentaa.sparkev.model.signup.profile.ResponseForUpdateProfile;
import com.augmentaa.sparkev.model.signup.profile.ResponseGetProfile;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton {
    ResponseCountry responseCountry;
    List<ResponseState> responseState;
    String fName, lName, email, mobile, address, fullName, state, country, zipcode;
    EditText et_fName, et_lName, et_address, et_zipcode;
    TextView et_email, et_mobile;
    Button btnSubmit;
    TextView tv_fullName, tv_state, tv_country;

    ImageView img_profile;
    APIInterface apiInterface, spin_apiInterface;
    ProgressDialog progress;
    ResponseGetProfile responseGetProfile;
    GetAllCountryListAdapter adapter;
    int country_id, state_id, city_id;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    List<Uri> list_image;
    Dialog dialogImageCapture, dialogShowImage;
    //    ImageView img_close;
    byte[] sw_inputData;
    MultipartBody.Part body;
    Uri contentUri;
    // Add another part within the multipart request
    RequestBody requestBody_id;
    RequestBody requestBody_fullName;
    RequestBody requestBody_fName;
    RequestBody requestBody_mName;
    RequestBody requestBody_lName;
    RequestBody requestBody_address;
    RequestBody requestBody_mobile;
    RequestBody requestBody_email;
    RequestBody requestBody_cityId;
    RequestBody requestBody_stateId;
    RequestBody requestBody_gender;
    RequestBody requestBody_zipcode;
    RequestBody requestBody_country_id;
    //RequestBody requestBody_birth_date;

    ProfileData data;
    ImageView img_edit;
    BottomSheetDialog dialogAddPhoto;
    ImageView img_back;
    int pos;
    BottomSheetDialog dialog_state, dialog_country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.update_profile_activity);
        et_fName = findViewById(R.id.f_name);
        et_lName = findViewById(R.id.l_name);
        et_email = findViewById(R.id.email);
        et_mobile = findViewById(R.id.mobile);
        tv_state = findViewById(R.id.state);
        et_zipcode = findViewById(R.id.zipcode);
        tv_country = findViewById(R.id.country);
        et_address = findViewById(R.id.address);
        img_edit = findViewById(R.id.img_edit);
        btnSubmit = findViewById(R.id.btn_submit);
        img_profile = findViewById(R.id.profile_image);
        img_back = findViewById(R.id.back);

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.spin_apiInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);
        dialogImageCapture = new Dialog(this);
        DrawableCompat.setTint(img_edit.getDrawable(), ContextCompat.getColor(this, R.color.colorTextLight));

        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Profile data ===" + data.toString());

            if (data.f_Name != null) {
                et_fName.setText(data.f_Name);
            } else {
                et_fName.setText(data.name);
            }

            et_lName.setText(data.l_Name);
            et_email.setText(data.email);
            et_mobile.setText(data.mobile);


            if (data.state_name != null) {
                tv_state.setText(data.state_name);
            } else {
                tv_state.setHintTextColor(getResources().getColor(R.color.colorTextLight));

            }
            tv_country.setText(data.country_name);
            et_address.setText(data.address1);

            if (data.PIN == 0) {
            } else {
                et_zipcode.setText("" + data.PIN);
                et_zipcode.setHintTextColor(getResources().getColor(R.color.colorTextLight));

            }

            fName = data.f_Name;
            lName = data.l_Name;
            fullName = data.name;
            country_id = data.country_id;
            state_id = data.state_id;
            address = data.address1;
            email = data.email;
            mobile = data.mobile;
            zipcode = String.valueOf(data.PIN);
            Logger.e("Profile image" + data.image_url);

            try {
                getAllStateList(1);

            } catch (Exception e) {

            }

           /* if(data.image_url!=null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.profile_pic)
                        .error(R.mipmap.profile_pic);
                Glide.with(UpdateProfileActivity.this).load(data.image_url).apply(options).into(img_profile);
            }*/

            if (data.image_url != null) {

                Glide.with(UpdateProfileActivity.this)
                        .load(data.image_url )
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(img_profile);
               /* Glide.with(UpdateProfileActivity.this).load(data.image_url)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .error(R.mipmap.profile_pic)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(img_profile);*/
            }

        } catch (Exception e) {

        }


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_captureImage();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = et_fName.getText().toString().trim();
                lName = et_lName.getText().toString().trim();
                address = et_address.getText().toString().trim();
                email = et_email.getText().toString().trim();
                mobile = et_mobile.getText().toString().trim();
                zipcode = et_zipcode.getText().toString().trim();

                if (TextUtils.isEmpty(fName)) {
                    Toast.makeText(UpdateProfileActivity.this, "First name is empty.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(UpdateProfileActivity.this, "Email name is empty.", Toast.LENGTH_LONG).show();
                } else if (!emailValidator(et_email.getText().toString())) {
                    Toast.makeText(UpdateProfileActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(UpdateProfileActivity.this, "Mobile Number is empty.", Toast.LENGTH_LONG).show();
                } else if (mobile.length() <= 9) {
                    Toast.makeText(UpdateProfileActivity.this, "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                } else if (zipcode.length() < 6) {
                    Toast.makeText(UpdateProfileActivity.this, "Invalid zipcode", Toast.LENGTH_LONG).show();
                } else {

                    if (Utils.isNetworkConnected(UpdateProfileActivity.this)) {
                        progress = new ProgressDialog(UpdateProfileActivity.this);
                        progress.setMessage(getResources().getString(R.string.loading));
                        progress.show();
                        try {
                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date myDate = new Date();
                            String date = timeStampFormat.format(myDate);

                            try {
                                if (contentUri != null) {
                                    InputStream iStream = getContentResolver().openInputStream(contentUri);
                                    sw_inputData = getBytes(iStream);
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), sw_inputData);
                                    body = MultipartBody.Part.createFormData("file", "image.png", requestFile);

                                }

                                // Add another part within the multipart request
                                requestBody_id = RequestBody.create(MultipartBody.FORM, String.valueOf(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0)));
                                requestBody_fullName = RequestBody.create(MultipartBody.FORM, fName + " " + lName);
                                requestBody_fName = RequestBody.create(MultipartBody.FORM, fName);
                                requestBody_mName = RequestBody.create(MultipartBody.FORM, "");
                                requestBody_lName = RequestBody.create(MultipartBody.FORM, lName);
                                requestBody_address = RequestBody.create(MultipartBody.FORM, address);
                                requestBody_mobile = RequestBody.create(MultipartBody.FORM, mobile);
                                requestBody_email = RequestBody.create(MultipartBody.FORM, email);
                                requestBody_cityId = RequestBody.create(MultipartBody.FORM, String.valueOf(city_id));
                                requestBody_stateId = RequestBody.create(MultipartBody.FORM, String.valueOf(state_id));
                                requestBody_gender = RequestBody.create(MultipartBody.FORM, "");
                                requestBody_zipcode = RequestBody.create(MultipartBody.FORM, zipcode);
                                requestBody_country_id = RequestBody.create(MultipartBody.FORM, String.valueOf(country_id));
//                            requestBody_birth_date = RequestBody.create(MultipartBody.FORM, "");
//                   RequestBody requestBody= RequestBody.create(MediaType.parse("multipart/form-data"), charger_serial_number);

//                   RequestBody requestBody= RequestBody.create(MediaType.parse("multipart/form-data"), charger_serial_number);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            updateProfile();
                        } catch (Exception e) {
//                            Toast.makeText(ProfileActivity.this, "Please select gender", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }
                }

            }
        });

        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_getAllCountryList();

            }
        });


        tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (responseState.size() > 0) {
                        dialog_getAllStateList();
                    } else {
                        Toast.makeText(getApplicationContext(), "State not found", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "State not found", Toast.LENGTH_LONG).show();

                }

            }
        });


        if (Utils.isNetworkConnected(this)) {
            progress = new ProgressDialog(this);
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getAllCountryList();
            getProfileDetails(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }


    public void getProfileDetails(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.S_TOKEN.toString(), null));
        Call<ResponseGetProfile> call = spin_apiInterface.getProfile(hashMap1, userId);
        call.enqueue(new Callback<ResponseGetProfile>() {
            @Override
            public void onResponse(Call<ResponseGetProfile> call, Response<ResponseGetProfile> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        responseGetProfile = (ResponseGetProfile) response.body();
                        Logger.e("Response : " + responseGetProfile.toString());
                        if (responseGetProfile.status) {

                        }


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again)+111, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseGetProfile> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("1111111111" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


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
//                                sp_country.setAdapter(adapter);
//                                tvCountry.setText(responseCountry.data.get(1).name);

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
        EditText et_search = dialog_country.findViewById(R.id.search);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(UpdateProfileActivity.this, responseCountry.data, this);
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
                country_id = responseCountry.data.get(position).id;
                tv_country.setText(responseCountry.data.get(position).name);
                getAllStateList(country_id);

            }
        });*/

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
        EditText et_search = dialog_state.findViewById(R.id.search);


        GetAllStateListAdapter adapter = new GetAllStateListAdapter(UpdateProfileActivity.this, responseState, this);
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
                dialog_Distance.dismiss();
                state_id = responseState.get(pos).id;
                tv_state.setText(responseState.get(pos).name);


            }
        });
*/

        dialog_state.show();

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void updateProfile() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.S_TOKEN.toString(), null));
        this.spin_apiInterface.profileUpdate(hashMap1,
                body,
                requestBody_id,
                requestBody_fName,
                requestBody_mName,
                requestBody_lName,
                requestBody_fullName,
                requestBody_address,
                requestBody_zipcode,
                requestBody_cityId,
                requestBody_stateId,
                requestBody_country_id,
                requestBody_email,
                requestBody_gender,
                requestBody_mobile).enqueue(new Callback<ResponseForUpdateProfile>() {
            public void onResponse(Call<ResponseForUpdateProfile> call, Response<ResponseForUpdateProfile> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
//                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseForUpdateProfile loginResponse = (ResponseForUpdateProfile) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        Toast.makeText(UpdateProfileActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();

                        Pref.setValue(Pref.TYPE.IMAGE_URL.toString(), loginResponse.data.get(0).image_url);
                        Pref.setValue(Pref.TYPE.S_F_NAME.toString(), loginResponse.data.get(0).f_Name);
                        Pref.setValue(Pref.TYPE.F_NAME.toString(), loginResponse.data.get(0).f_Name);
                        Pref.setValue(Pref.TYPE.S_L_NAME.toString(), loginResponse.data.get(0).l_Name);
                        Pref.setValue(Pref.TYPE.L_NAME.toString(), loginResponse.data.get(0).l_Name);

                        if (loginResponse.status) {
                            Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();

                        } else {


                        }

                    } catch (Exception e) {
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                SignInActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.DATA_NOT_FOUND_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            SignInActivity.class.getSimpleName(),
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseForUpdateProfile> call, Throwable t) {
                Logger.e("Exception  " + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        SignInActivity.class.getSimpleName(),
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

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                img_profile.setImageURI(Uri.fromFile(f));
//                list_image.add(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                dialogAddPhoto.dismiss();


//                try {
//                    InputStream iStream = getContentResolver().openInputStream(contentUri);
//                    sw_inputData = getBytes(iStream);
//                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), sw_inputData);
//                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.png", requestFile);
//                 Add another part within the multipart request


            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                img_profile.setImageURI(contentUri);
                dialogAddPhoto.dismiss();

            }

        }


    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.exicom.android.spinev.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    void dialog_captureImage() {
//        dialogImageCapture.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddPhoto = new BottomSheetDialog(this);
        dialogAddPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_add_photo, null);
        dialogAddPhoto.setContentView(view);
        dialogAddPhoto.setCancelable(true);
        Button btn_camera = dialogAddPhoto.findViewById(R.id.cameraBtn);
        Button btn_gallery = dialogAddPhoto.findViewById(R.id.galleryBtn);
        View view1 = dialogAddPhoto.findViewById(R.id.view);
        dialogAddPhoto.show();
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddPhoto.dismiss();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

     /*   img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageCapture.dismiss();
            }
        });*/

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        dialogAddPhoto.show();


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(UpdateProfileActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


    @Override
    public void search_state(int position, List<ResponseState> itemsModelsl) {
        Logger.e("=====Position ===" + itemsModelsl.size() + "   " + position);
        pos = position;
        state_id = itemsModelsl.get(position).id;
        tv_state.setText(itemsModelsl.get(position).name);
        dialog_state.dismiss();

    }

    @Override
    public void search_country(int position, List<Data> list) {
        dialog_country.dismiss();
        try {
            country_id = list.get(position).id;
            tv_country.setText(list.get(position).name);
            tv_state.setText(null);
            tv_country.setHint("Please select your state");
            tv_state.setHintTextColor(getResources().getColor(R.color.colorText));
            getAllStateList(country_id);
        } catch (Exception e) {

        }


    }
}