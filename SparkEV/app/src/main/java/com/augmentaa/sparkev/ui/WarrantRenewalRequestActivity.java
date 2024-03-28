package com.augmentaa.sparkev.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.Data;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.model.signup.warranty_renewal_request.ResponseWarrantyRenewal;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.ui.fragment.SW_SnapAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantRenewalRequestActivity extends AppCompatActivity implements SW_SnapAdapter.ItemClickListenerSw, GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton {
    Button btn_submit;
    TextView tv_country, tv_state, tv_address, tv_zipcode, tv_add_photo, tv_charger_serial_number, tv_mobile;
    ImageView img_back;
    Data warranty_data;
    BottomSheetDialog dialogAddPhoto;
    File upload_file;

    List<ResponseState> responseState;
    ResponseCountry responseCountry;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage, img_close, img_show;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    List<Uri> list_image_sw_update;
    Dialog dialogImageCapture, dialogShowImage;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView rv_image;
    SW_SnapAdapter sw_adapter;
    APIInterface apiInterface, apiInterfacePP;
    ProgressDialog progress;
    ScrollView scView;
    byte[] sw_inputData;
    RequestBody swBody;

    MultipartBody.Part[] sw_imagepart;

    RequestBody requestBody_user_id;
    RequestBody requestBody_charger_id;
    RequestBody requestBody_mobile;
    RequestBody requestBody_address1;
    RequestBody requestBody_address2;
    RequestBody requestBody_pin;
    RequestBody requestBody_city_id;
    RequestBody requestBody_state_id;
    RequestBody requestBody_country_id;
    RequestBody requestBody_request_type;
    RequestBody requestBody_landmark;

    MultipartBody.Part body;
    Uri contentUri;

    TextView tv_charger_serial_no, tvMobile, tvAddress, tvState, tvCountry, tvPIN;
    Button btnRequestSend;

    int state_id, country_id;
    String zipcode;

    String mobile, address, state, country, charger_serial_no;
    TextView btnUploadImage, tv_input_state, tv_input_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrant_renewal_request);
        tv_add_photo = findViewById(R.id.add_photo);
        tv_address = findViewById(R.id.address);
        tv_country = findViewById(R.id.country);
        tv_state = findViewById(R.id.state);
        tv_charger_serial_number = findViewById(R.id.serial_number);
        tv_mobile = findViewById(R.id.mobile);
        tv_zipcode = findViewById(R.id.zipcode);
        btn_submit = findViewById(R.id.btn_submit);
        img_back = findViewById(R.id.back);
        rv_image = findViewById(R.id.rv_image);
        progress = new ProgressDialog(this);
        dialogImageCapture = new Dialog(WarrantRenewalRequestActivity.this);
        dialogShowImage = new Dialog(WarrantRenewalRequestActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        list_image_sw_update = new ArrayList<>();

        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        getSupportActionBar().hide();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.e("List size " + list_image_sw_update.size());

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(WarrantRenewalRequestActivity.this, "Invalid address", Toast.LENGTH_LONG);
                } else if (TextUtils.isEmpty(zipcode)) {
                    Toast.makeText(WarrantRenewalRequestActivity.this, "Invalid zipcode", Toast.LENGTH_LONG);
                } else {
                    if (contentUri != null) {
                        if (Utils.isNetworkConnected(WarrantRenewalRequestActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.setCancelable(false);
                            progress.show();
                            InputStream iStream = null;
                            try {
                                requestBody_user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(2954));
                                requestBody_charger_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(802));
                                requestBody_mobile = RequestBody.create(MediaType.parse("text/plain"), "90090900909");
                                requestBody_address1 = RequestBody.create(MediaType.parse("text/plain"), "Aaaaaa1121212121");
                                requestBody_address2 = RequestBody.create(MediaType.parse("text/plain"), "aaaaaaaaa");
                                requestBody_pin = RequestBody.create(MediaType.parse("text/plain"), "12112212");
                                requestBody_city_id = RequestBody.create(MediaType.parse("text/plain"), "0");
                                requestBody_state_id = RequestBody.create(MediaType.parse("text/plain"), "1");
                                requestBody_country_id = RequestBody.create(MediaType.parse("text/plain"), "1");
                                requestBody_request_type = RequestBody.create(MediaType.parse("text/plain"), "WARRANTY");
                                requestBody_landmark = RequestBody.create(MediaType.parse("text/plain"), "LANDMARK");
/*
                               String file_path= getRealPathFromUri(WarrantRenewalRequestActivity.this,contentUri);
                                File file=new File(file_path);

                                RequestBody requesFile = RequestBody.create(MediaType.parse("multipart.form- data"), file);
                                body = MultipartBody.Part.createFormData("gallery", "image.png", requesFile);*/

                                if (contentUri != null) {
                                    iStream = getContentResolver().openInputStream(contentUri);
                                    sw_inputData = getBytes(iStream);
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), sw_inputData);
                                    body = MultipartBody.Part.createFormData("gallery", "image.png", requestFile);


                                }

                                Logger.e(" IMAGE  " + contentUri.toString() + "   " + body);


                            } catch (Exception e) {
                                Logger.e("Exp33  " + e.getMessage());
                                e.printStackTrace();
                            }


                            requestRenewalWarranty();
                        } else {
                            Toast.makeText(WarrantRenewalRequestActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(WarrantRenewalRequestActivity.this, "Please upload charger image", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });
        tv_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAddPhoto();

            }
        });


        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllCountryList();

            }
        });


        tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    getAllStateList(country_id);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please select country first.", Toast.LENGTH_LONG).show();

                }

            }
        });

        try {
            warranty_data = getIntent().getParcelableExtra("data");
            Logger.e("Warranty data" + warranty_data.toString());

            tv_address.setText(warranty_data.address);
            tv_country.setText(warranty_data.countryName);
            tv_state.setText(warranty_data.stateName);
            tv_charger_serial_number.setText(warranty_data.nickName);
            tv_mobile.setText(Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            tv_zipcode.setText(warranty_data.pin);
            zipcode = warranty_data.pin;
            country_id = warranty_data.countryId;
            state_id = warranty_data.state_id;
            address = warranty_data.address;


        } catch (Exception e) {

        }
    }

    void dialogAddPhoto() {
        dialogAddPhoto = new BottomSheetDialog(this);
        dialogAddPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_add_photo, null);
        dialogAddPhoto.setContentView(view);
        dialogAddPhoto.setCancelable(true);
        Button btn_camera = dialogAddPhoto.findViewById(R.id.cameraBtn);
        Button btn_gallery = dialogAddPhoto.findViewById(R.id.galleryBtn);

        dialogAddPhoto.show();
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                Logger.e("List_size=Gallery==> " + list_image_sw_update.size());

            }
        });


    }


    void dialog_showImage(Uri image) {
        Logger.e("Image path==" + image);
//        dialogImageCapture.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogShowImage.setCancelable(false);
        dialogShowImage.setContentView(R.layout.dialog_show_image);

        img_show = dialogShowImage.findViewById(R.id.displayImageView);
        img_close = dialogShowImage.findViewById(R.id.cancel_button);
        img_show.setImageURI(image);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowImage.dismiss();

            }
        });


        dialogShowImage.show();


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
                upload_file = f;
                contentUri = Uri.fromFile(f);
                Logger.e("File=11111==" + upload_file);
                Logger.e("ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                dialogAddPhoto.dismiss();

            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);

                upload_file = new File(contentUri.getPath());

                Logger.e("File=22222==" + upload_file);
                Logger.e("onActivityResult: Gallery Image Uri:  " + imageFileName);
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
                ".png",         /* suffix */
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
                upload_file = photoFile;

                Logger.e("File===" + upload_file);

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


    @Override
    public void onItemClickSw(View view, int position) {
        Logger.e("3333333333===" + list_image_sw_update.get(position));
        dialog_showImage(list_image_sw_update.get(position));

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
                                dialog_getAllCountryList();

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
                            dialog_getAllStateList();

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
        dialog_Distance.setCancelable(false);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(WarrantRenewalRequestActivity.this, responseCountry.data,this);
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

        dialog_Distance.show();

    }


    void dialog_getAllStateList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(false);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllStateListAdapter adapter = new GetAllStateListAdapter(WarrantRenewalRequestActivity.this, responseState,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                state_id = responseState.get(position).id;
                tv_state.setText(responseState.get(position).name);


            }
        });


        dialog_Distance.show();

    }

    private void requestRenewalWarranty() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));


        this.apiInterface.uploadImageNew(hashMap1,
                body,
                requestBody_user_id,
                requestBody_charger_id,
                requestBody_mobile,
                requestBody_address1,
                requestBody_address2,
                requestBody_pin,
                requestBody_city_id,
                requestBody_state_id,
                requestBody_country_id,
                requestBody_request_type,
                requestBody_landmark).enqueue(new Callback<ResponseWarrantyRenewal>() {
            public void onResponse(Call<ResponseWarrantyRenewal> call, Response<ResponseWarrantyRenewal> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToStringCharger(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseWarrantyRenewal loginResponse = (ResponseWarrantyRenewal) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {
                            Toast.makeText(WarrantRenewalRequestActivity.this, "Request Sent Successfully.", Toast.LENGTH_LONG).show();
                            Intent intent21 = new Intent(WarrantRenewalRequestActivity.this, MainActivity.class);
                            startActivity(intent21);
                            finishAffinity();
                        } else {
                            if ("Token is not valid".equalsIgnoreCase(loginResponse.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(WarrantRenewalRequestActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(WarrantRenewalRequestActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();

                            }


                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                   WarrantRenewalRequestActivity.class.getSimpleName(),
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
                        Logger.e("Exception 222 " + e.getMessage());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                               WarrantRenewalRequestActivity.class.getSimpleName(),
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

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                           WarrantRenewalRequestActivity.class.getSimpleName(),
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

            public void onFailure(Call<ResponseWarrantyRenewal> call, Throwable t) {
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
                       WarrantRenewalRequestActivity.class.getSimpleName(),
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

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(WarrantRenewalRequestActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }


    @Override
    public void search_state(int position, List<ResponseState> list) {

    }

    @Override
    public void search_country(int position, List<com.augmentaa.sparkev.model.signup.country.Data> list) {

    }
}