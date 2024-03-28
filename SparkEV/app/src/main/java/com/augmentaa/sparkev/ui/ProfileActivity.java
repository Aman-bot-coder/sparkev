package com.augmentaa.sparkev.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.profile.ProfileData;
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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton {
    ResponseCountry responseCountry;
    List<ResponseState> responseState;
    String fName, lName, email, mobile, address, gender, fullName, place, country, zipcode;
    TextView et_fName, et_lName, et_email, et_mobile, et_zipcode, et_address;

    //    RadioGroup rgGender;
//    RadioButton rbMale, rbFemale, rbNotDisclose;
    Button btnLogout, btnSkip;
    TextView tv_place, tv_country;
    LinearLayout ly_update_profile;
    ImageView img_profile;
    APIInterface apiInterface, spin_apiInterface;
    ProgressDialog progress;
    ResponseGetProfile responseGetProfile;
    GetAllCountryListAdapter adapter;
    int country_id, state_id, city_id;
    String birth_date;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    List<Uri> list_image;
    Dialog dialogImageCapture, dialogShowImage;
    ImageView img_close;
    byte[] sw_inputData;
    MultipartBody.Part body;
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
    RequestBody requestBody_birth_date;
    private RadioButton radioSexButton;
    Uri contentUri;
    ProfileData data = new ProfileData();
    ImageView img_back, img_edit;
    Dialog dialog_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.profile_activity);
        et_fName = findViewById(R.id.f_name);
        et_zipcode = findViewById(R.id.zipcode);
        et_lName = findViewById(R.id.l_name);
        et_email = findViewById(R.id.email);
        et_mobile = findViewById(R.id.mobile);
        tv_place = findViewById(R.id.state);
        tv_country = findViewById(R.id.country);
        ly_update_profile = findViewById(R.id.update_profile);
        et_address = findViewById(R.id.address);
        btnLogout = findViewById(R.id.btn_logout);
//        btnSkip = findViewById(R.id.skip);
        img_profile = findViewById(R.id.profile_image);
        img_back = findViewById(R.id.back);
        img_edit = findViewById(R.id.img_edit);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.spin_apiInterface = (APIInterface) APIClient.getSpinURL().create(APIInterface.class);
        DrawableCompat.setTint(img_edit.getDrawable(), ContextCompat.getColor(this, R.color.themeColor));
        dialogImageCapture = new Dialog(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_logout();

            }
        });

        ly_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ProfileActivity.this)) {
//                    progress = new ProgressDialog(ProfileActivity.this);
//                    progress.setMessage(getResources().getString(R.string.loading));
//                    progress.show();
                    try {
                        Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                        intent.putExtra("data", responseGetProfile.data.get(0));
                        startActivity(intent);
                    } catch (Exception e) {

                    }


                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_email.setText(Pref.getValue(Pref.TYPE.S_EMAIL.toString(), null));
        et_mobile.setText(Pref.getValue(Pref.TYPE.S_MOBILE.toString(), null));



    }


    public void getProfileDetails(int userId) {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.S_TOKEN.toString(), null));
        Call<ResponseGetProfile> call = spin_apiInterface.getProfile(hashMap1, userId);
        call.enqueue(new Callback<ResponseGetProfile>() {
            @Override
            public void onResponse(Call<ResponseGetProfile> call, Response<ResponseGetProfile> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        responseGetProfile = (ResponseGetProfile) response.body();
                        Logger.e("Response : " + responseGetProfile.toString());
                        if (responseGetProfile.status) {
                            if (responseGetProfile.data.get(0).f_Name != null) {
                                et_fName.setText(responseGetProfile.data.get(0).f_Name);

                            } else {
                                et_fName.setText(responseGetProfile.data.get(0).name);

                            }

                            if (responseGetProfile.data.get(0).l_Name != null) {
                                et_lName.setText(responseGetProfile.data.get(0).l_Name);
                            }
                            else {
                                et_lName.setHintTextColor(getResources().getColor(R.color.colorTextLight));
                            }
                            if (responseGetProfile.data.get(0).mobile != null) {
                                et_mobile.setText(responseGetProfile.data.get(0).mobile);
                            } else {
                                et_lName.setHintTextColor(getResources().getColor(R.color.colorTextLight));
                            }

                            if (responseGetProfile.data.get(0).address1 != null) {
                                et_address.setText(responseGetProfile.data.get(0).address1);
                            } else {
                                et_lName.setHintTextColor(getResources().getColor(R.color.colorTextLight));
                            }

                            if (responseGetProfile.data.get(0).email != null) {
                                et_email.setText(responseGetProfile.data.get(0).email);
                            }
                            else {
                                et_lName.setHintTextColor(getResources().getColor(R.color.colorTextLight));
                            }

                            if (responseGetProfile.data.get(0).PIN == 0) {
                            } else {
                                et_zipcode.setText("" + responseGetProfile.data.get(0).PIN);
                                et_zipcode.setHintTextColor(getResources().getColor(R.color.colorTextLight));

                            }


                            Pref.setValue(Pref.TYPE.IMAGE_URL.toString(), responseGetProfile.data.get(0).image_url);

                            Logger.e("Image url==" + responseGetProfile.data.get(0).image_url);

                            try {
                                if (responseGetProfile.data.get(0).image_url != null) {
                                    Glide.with(ProfileActivity.this)
                                            .load(responseGetProfile.data.get(0).image_url )
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(img_profile);

                                   /* Glide.with(ProfileActivity.this).load(responseGetProfile.data.get(0).image_url)
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


                               /* if(responseGetProfile.data.get(0).image_url!=null) {
                                    RequestOptions options = new RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.mipmap.profile_pic)
                                            .error(R.mipmap.profile_pic);
                                    Glide.with(ProfileActivity.this).load(responseGetProfile.data.get(0).image_url).apply(options).into(img_profile);
                                }
                                else {
                                    img_profile.setImageResource(R.mipmap.profile_pic);
                                }*/
                            } catch (Exception e) {
                                progress.dismiss();

                            }

                            getAllCountryList();


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
                        }


                    } catch (Exception e) {
                        progress.dismiss();

                        Log.d("Response", e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                    }
                } else {
                    progress.dismiss();

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseGetProfile> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("1111111111" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
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
                        AppUtils.JSON_PARSING,
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.MOBILE.toString(), null));

            }
        });

    }

    public void getAllCountryList() {
        Call<ResponseCountry> call = apiInterface.getAllCountryList();
        call.enqueue(new Callback<ResponseCountry>() {
            @Override
            public void onResponse(Call<ResponseCountry> call, Response<ResponseCountry> response) {
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                responseCountry = (ResponseCountry) response.body();
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        if (responseCountry.status) {
                            for (int i = 0; i < responseCountry.data.size(); i++) {
                                if (responseCountry.data.get(i).id == responseGetProfile.data.get(0).country_id) {
                                    tv_country.setText(responseCountry.data.get(i).name);
                                    responseGetProfile.data.get(0).country_name = responseCountry.data.get(i).name;
                                    getAllStateList(responseGetProfile.data.get(0).country_id);

                                }

                            }


                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        progress.dismiss();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    progress.dismiss();

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
                progress.dismiss();
                if (response.code() == 200) {
                    try {
                        if (responseState.size() > 0) {
                            for (int i = 0; i < responseState.size(); i++) {
                                if (responseState.get(i).id == responseGetProfile.data.get(0).state_id) {
                                    tv_place.setText(responseState.get(i).name);
                                    responseGetProfile.data.get(0).state_name = responseState.get(i).name;

                                }
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
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(ProfileActivity.this, responseCountry.data,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                country_id = responseCountry.data.get(position).id;
                tv_country.setText(responseCountry.data.get(position).name);
                getAllStateList(country_id);

            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Distance.dismiss();
            }
        });

        dialog_Distance.show();

    }


    void dialog_getAllStateList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllStateListAdapter adapter = new GetAllStateListAdapter(ProfileActivity.this, responseState,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                state_id = responseState.get(position).id;
                tv_place.setText(responseState.get(position).name);


            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Distance.dismiss();
            }
        });

        dialog_Distance.show();

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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

        dialogImageCapture.setCancelable(false);
        dialogImageCapture.setContentView(R.layout.dialog_image_capture);
        selectedImage = (ImageView) dialogImageCapture.findViewById(R.id.displayImageView);
        img_close = (ImageView) dialogImageCapture.findViewById(R.id.displayImageView);
        cameraBtn = (Button) dialogImageCapture.findViewById(R.id.cameraBtn);
        galleryBtn = (Button) dialogImageCapture.findViewById(R.id.galleryBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageCapture.dismiss();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        dialogImageCapture.show();


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


    void dialog_logout() {
        dialog_logout = new Dialog(ProfileActivity.this);
        dialog_logout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_logout.setContentView(R.layout.app_dialog);
        dialog_logout.setCancelable(false);
        TextView btn_ok = (TextView) dialog_logout.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_logout.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_logout.findViewById(R.id.tv_message);

        tv_message.setText("Are you sure you want to Logout?");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                Pref.clear();
                Intent intent21 = new Intent(ProfileActivity.this, SignInActivity.class);
                intent21.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent21);
                finishAffinity();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_logout.dismiss();

            }
        });
        dialog_logout.show();


    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(ProfileActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


    @Override
    public void search_state(int position, List<ResponseState> list) {

    }

    @Override
    public void search_country(int position, List<Data> list) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(this)) {
            progress = new ProgressDialog(this);
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getProfileDetails(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));


        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
    }
}