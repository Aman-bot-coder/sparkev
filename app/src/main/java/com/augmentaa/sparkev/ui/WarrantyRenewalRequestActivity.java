package com.augmentaa.sparkev.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.adapter.GetAllCountryListAdapter;
import com.augmentaa.sparkev.adapter.GetAllStateListAdapter;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.model.signup.warranty_history.Data;
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

public class WarrantyRenewalRequestActivity extends AppCompatActivity implements SW_SnapAdapter.ItemClickListenerSw, GetAllStateListAdapter.ClickButton, GetAllCountryListAdapter.ClickButton {

    List<ResponseState> responseState;
    ResponseCountry responseCountry;

    ImageView img_back;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage, img_close, img_show;
    Button cameraBtn, galleryBtn;
    String currentPhotoPath;
    List<Uri> list_image_sw_update;
    Dialog dialogImageCapture, dialogShowImage;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView rv_sw_update;
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
    TextView tv_charger_serial_no, tvMobile, tvAddress, tvState, tvCountry, tvPIN;
    Button btnRequestSend;
    int state_id, country_id, zipcode;
    String mobile, address, state, country, charger_serial_no;
    TextView btnUploadImage, tv_input_state, tv_input_country;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_warranty_renewal_request);
        getSupportActionBar().hide();
//        img_back = findViewById(R.id.back);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        this.apiInterfacePP = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);

        progress = new ProgressDialog(this);
        tv_charger_serial_no = findViewById(R.id.serial_number);
        tvMobile = findViewById(R.id.mobile);
        tvAddress = findViewById(R.id.address);
        tvState = findViewById(R.id.place);
        tvCountry = findViewById(R.id.country);
        tvPIN = findViewById(R.id.zipcode);
        tv_input_state = findViewById(R.id.tv_place);
        tv_input_country = findViewById(R.id.tv_country);
        btnUploadImage = findViewById(R.id.uploadimage);
        btnRequestSend = findViewById(R.id.save);
        rv_sw_update=findViewById(R.id.rv_hw_update);
        list_image_sw_update = new ArrayList<>();
        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Station data 22  " + data.serialNo);
            charger_serial_no = data.serialNo;
            tv_charger_serial_no.setText(charger_serial_no);
            tvMobile.setText(Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
            tvAddress.setText("HFCL");
            tvState.setText("Hariyana");
            tvCountry.setText("India");
            tvPIN.setText("122011");

        } catch (Exception e) {

        }


        tvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_getAllCountryList();

            }
        });

        tv_input_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_getAllCountryList();

            }
        });

        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dialog_getAllStateList();
                } catch (Exception e) {
                    Toast.makeText(WarrantyRenewalRequestActivity.this, "Please select country first.", Toast.LENGTH_LONG).show();

                }

            }
        });

        tv_input_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dialog_getAllStateList();
                } catch (Exception e) {
                    Toast.makeText(WarrantyRenewalRequestActivity.this, "Please select country first.", Toast.LENGTH_LONG).show();

                }

            }
        });


        if (Utils.isNetworkConnected(WarrantyRenewalRequestActivity.this)) {
            progress = new ProgressDialog(WarrantyRenewalRequestActivity.this);
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getAllCountryList();
            //updateBillingAddress(Pref.getIntValue(Pref.TYPE.S_USER_ID.toString(), 0));
        } else {
            Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();


        }


        dialogImageCapture = new Dialog(WarrantyRenewalRequestActivity.this);
        dialogShowImage = new Dialog(WarrantyRenewalRequestActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
       /* img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/


        btnRequestSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
//                    Logger.e("List data " + data.action);
                    if (TextUtils.isEmpty(tvAddress.getText().toString())) {
                        Toast.makeText(WarrantyRenewalRequestActivity.this, "Please enter address", Toast.LENGTH_LONG).show();
                    } else if (list_image_sw_update.size() < 1) {
                        Toast.makeText(WarrantyRenewalRequestActivity.this, "Please add charger at least one image", Toast.LENGTH_LONG).show();

                    } else {
                        address = tvAddress.getText().toString();
                        zipcode = Integer.parseInt(tvPIN.getText().toString());
                        if (Utils.isNetworkConnected(WarrantyRenewalRequestActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            requestBody_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0)));
                            requestBody_charger_id = RequestBody.create(MediaType.parse("multipart/form-data"), "D72110382490774");
                            requestBody_mobile = RequestBody.create(MediaType.parse("multipart/form-data"), Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                            requestBody_state_id = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                            requestBody_country_id = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                            requestBody_pin = RequestBody.create(MediaType.parse("multipart/form-data"), "121212");
                            requestBody_city_id = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
                            requestBody_address1 = RequestBody.create(MediaType.parse("multipart/form-data"), "address");
                            requestBody_address2 = RequestBody.create(MediaType.parse("multipart/form-data"), "address");
                            requestBody_request_type = RequestBody.create(MediaType.parse("multipart/form-data"), "WARRANTY");
                            requestRenewalWarranty();
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                        }

                    }


                } catch (Exception e) {
                    Logger.e("Exception "+e);

                }

//                for (int i = 0; i < data.action.size(); i++) {
//                    Logger.e("action taken ==> " + list_actionTaken.get(i));
//                }


            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_image_sw_update.size() > 2) {
                    Toast.makeText(WarrantyRenewalRequestActivity.this, "Upload maximum 3 images", Toast.LENGTH_LONG).show();

                } else {
                    dialog_captureImage();
                }

            }
        });


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
                Logger.e("List_size=Gallery==> " + list_image_sw_update.size());
            }
        });

        dialogImageCapture.show();


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
                Uri contentUri = Uri.fromFile(f);
//                list_image_sw_update.add(contentUri);
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                InputStream iStream = null;
                try {
                    list_image_sw_update.add(contentUri);
                    // Add another part within the multipart request
                    requestBody_charger_id = RequestBody.create(MediaType.parse("multipart/form-data"), charger_serial_no);
                    Logger.e("Byte data == " + list_image_sw_update.size());
                    sw_imagepart = new MultipartBody.Part[list_image_sw_update.size()];
                    for (int index = 0; index < list_image_sw_update.size(); index++) {
                        File file = null;
                        try {
//                            file = new File(new URI(list_image_sw_update.get(index).toString()));
                            iStream = getContentResolver().openInputStream(list_image_sw_update.get(index));
                            sw_inputData = getBytes(iStream);
                            swBody = RequestBody.create(MediaType.parse("image/*"), sw_inputData);
                            sw_imagepart[index] = MultipartBody.Part.createFormData("gallery", "image.png", swBody);
                        } catch (Exception e) {
                            Logger.e("Exp888  " + e.getMessage());
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    Logger.e("Exp999  " + e.getMessage());
                    e.printStackTrace();
                }
//                    list_image_sw_update.add(contentUri);
                RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
                // Set LayoutManager on Recycler View
                rv_sw_update.setLayoutManager(RecyclerViewLayoutManager);
                sw_adapter = new SW_SnapAdapter(list_image_sw_update, WarrantyRenewalRequestActivity.this);
                // Set Horizontal Layout Manager
                // for Recycler view
                rv_sw_update.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                sw_adapter.setClickListener((SW_SnapAdapter.ItemClickListenerSw) this);
                // Set adapter on recycler view
                rv_sw_update.setAdapter(sw_adapter);
                dialogImageCapture.dismiss();
            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                list_image_sw_update.add(contentUri);

                list_image_sw_update.add(contentUri);
                InputStream iStream = null;
                try {
                    requestBody_charger_id = RequestBody.create(MediaType.parse("multipart/form-data"), charger_serial_no);
                    Logger.e("Byte data == " + list_image_sw_update.size());
                    sw_imagepart = new MultipartBody.Part[list_image_sw_update.size()];
                    for (int index = 0; index < list_image_sw_update.size(); index++) {
                        try {
                            iStream = getContentResolver().openInputStream(list_image_sw_update.get(index));
                            sw_inputData = getBytes(iStream);
                            swBody = RequestBody.create(MediaType.parse("image/*"), sw_inputData);
                            sw_imagepart[index] = MultipartBody.Part.createFormData("gallery", "image.png", swBody);
                        } catch (Exception e) {
                            Logger.e("Exp888  " + e.getMessage());
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    Logger.e("Exp33  " + e.getMessage());
                    e.printStackTrace();
                }
                RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
                // Set LayoutManager on Recycler View
                rv_sw_update.setLayoutManager(RecyclerViewLayoutManager);
                sw_adapter = new SW_SnapAdapter(list_image_sw_update, WarrantyRenewalRequestActivity.this);
                // Set Horizontal Layout Manager
                // for Recycler view
                rv_sw_update.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                sw_adapter.setClickListener((SW_SnapAdapter.ItemClickListenerSw) this);
                // Set adapter on recycler view
                rv_sw_update.setAdapter(sw_adapter);
                dialogImageCapture.dismiss();
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


    @Override
    public void onItemClickSw(View view, int position) {
        Logger.e("3333333333===" + list_image_sw_update.get(position));
        dialog_showImage(list_image_sw_update.get(position));

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


    private void requestRenewalWarranty() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.uploadImage(hashMap1, sw_imagepart,
                requestBody_user_id,
                requestBody_charger_id,
                requestBody_mobile,
                requestBody_address1,
                requestBody_address2,
                requestBody_pin,
                requestBody_city_id,
                requestBody_state_id,
                requestBody_country_id,
                requestBody_request_type).enqueue(new Callback<ResponseWarrantyRenewal>() {
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
                            Toast.makeText(WarrantyRenewalRequestActivity.this, "Request Sent Successfully.", Toast.LENGTH_LONG).show();

                        } else {

                            if ("Token is not valid".equalsIgnoreCase(loginResponse.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(WarrantyRenewalRequestActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(WarrantyRenewalRequestActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();

                            }

                        }

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<ResponseWarrantyRenewal> call, Throwable t) {
                Logger.e("Exception  " + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
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
                        Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseCountry> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


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
                        Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<List<ResponseState>> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("TAG" + t.getMessage());
                Toast.makeText(WarrantyRenewalRequestActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


            }
        });

    }


    void dialog_getAllCountryList() {
        View view = getLayoutInflater().inflate(R.layout.dialog_countrylist, null);
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(WarrantyRenewalRequestActivity.this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(false);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllCountryListAdapter adapter = new GetAllCountryListAdapter(WarrantyRenewalRequestActivity.this, responseCountry.data,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                country_id = responseCountry.data.get(position).id;
                tvCountry.setText(responseCountry.data.get(position).name);
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
        final BottomSheetDialog dialog_Distance = new BottomSheetDialog(WarrantyRenewalRequestActivity.this);
        dialog_Distance.setContentView(view);
//        dialog_Distance = new Dialog(MainActivity.this);
//        dialog_Distance.setContentView(R.layout.dialog_distance);
        dialog_Distance.setCancelable(true);
        ListView lv_model = (ListView) dialog_Distance.findViewById(R.id.listview);
        GetAllStateListAdapter adapter = new GetAllStateListAdapter(WarrantyRenewalRequestActivity.this, responseState,this);
        lv_model.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_model.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_Distance.dismiss();
                state_id = responseState.get(position).id;
                tvState.setText(responseState.get(position).name);
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

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(WarrantyRenewalRequestActivity.this, MyService.class);
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