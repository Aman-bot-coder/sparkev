package com.augmentaa.sparkev.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.Logger;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCharger extends Fragment implements ZXingScannerView.ResultHandler {
    Button btnAddCharger;

    private ZXingScannerView mScannerView;
    Button btn_Submit, btn_startCharger;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    String charger_serial_number, chargerID, connectorNo, idTag, connectorId;
    ViewGroup contentFrame;
    TextView tv_or;
    String charger_sr_no;
    String error_message;
    private ToggleButton toggleFlashLightOnOff;
    private CameraManager cameraManager;
    private String getCameraID;

    TextView tv_scan, tv_skip;
    ImageView img_back;

    public ScanCharger() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_scan_charger, container, false);
        btnAddCharger = view.findViewById(R.id.addCharger);
        contentFrame = view.findViewById(R.id.content_frame);
        tv_scan = view.findViewById(R.id.tv_scan);
        tv_skip= view.findViewById(R.id.tv_skip);
        img_back = view.findViewById(R.id.back);
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        contentFrame.addView(mScannerView);
//        mScannerView.setBackgroundResource(R.mipmap.scan_qr_code);
        mScannerView.setBackgroundResource(R.drawable.bg_scanner);
        contentFrame.setBackgroundResource(R.mipmap.scan_qr_code);


        // Register the ToggleButton with specific ID
        toggleFlashLightOnOff = view.findViewById(R.id.toggle_flashlight);


        // cameraManager to interact with camera devices
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        // Exception is handled, because to check whether
        // the camera resource is being used by another
        // service or not.
        try {
            // O means back camera unit,
            // 1 means front camera unit
            getCameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        toggleFlashLightOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleFlashLightOnOff.isChecked()) {
                    // Exception is handled, because to check
                    // whether the camera resource is being used by
                    // another service or not.
                    try {
                        mScannerView.setFlash(true);

                    } catch (Exception e) {
                        // prints stack trace on standard error
                        // output error stream
                        Toast.makeText(getActivity(), "Flush light not available in your Mobile", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    // Exception is handled, because to check
                    // whether the camera resource is being used by
                    // another service or not.
                    try {
                        // true sets the torch in OFF mode
                        mScannerView.setFlash(false);

                    } catch (Exception e) {
                        // prints stack trace on standard error
                        // output error stream
                        Toast.makeText(getActivity(), "Flush light not available in your Mobile", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }
        });


        btnAddCharger.setBackgroundResource(R.drawable.bg_unselected_btn);
        btnAddCharger.setTextColor(getResources().getColor(R.color.colorText));

        btnAddCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (charger_serial_number != null) {
                    AddChargerName fragment = new AddChargerName();
                    FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("charger_id", charger_serial_number);
                    fragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

//                } else {
//
//                    Toast.makeText(getActivity(), "Please scan charger QR code", Toast.LENGTH_LONG).show();
//
//
//                }


            }
        });


        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddChargerName fragment = new AddChargerName();
                FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, fragment);
                Bundle bundle = new Bundle();
                bundle.putString("charger_id", charger_serial_number);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            cameraPermission();
            Logger.e("Permission 44444");
        } else {

            Logger.e("Permission 5555");


//            Intent intent = new Intent(ScanChargerActivity.this, QRCodeScanActivity.class);
//            startActivity(intent);
//            ScanChargerActivity.this.finish();
        }
        return view;

    }

    public void cameraPermission() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                Logger.e("Permission 111");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                Logger.e("Permission 222222");
            }
        } else {
            Logger.e("Permission 33333");
            // READ_PHONE_STATE permission is already been granted.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(ScanChargerActivity.this, QRCodeScanActivity.class);
//                startActivity(intent);
            } else {
//                Toast.makeText(this, "55555", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void handleResult(final Result rawResult) {
        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        tv_scan.setText("Qr code scan successfully.");
        btnAddCharger.setBackgroundResource(R.drawable.bg_login_btn);
        btnAddCharger.setTextColor(getResources().getColor(R.color.white));
        charger_serial_number = rawResult.getText().toString();
        Toast.makeText(getActivity(), "QR code scan successfully", Toast.LENGTH_LONG).show();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
//        charger_serial_number="HE518221#CHARGERTEST0006";
        AddChargerName fragment = new AddChargerName();
        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("charger_id", charger_serial_number);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    //
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        mScannerView.setFlash(false);
    }

}