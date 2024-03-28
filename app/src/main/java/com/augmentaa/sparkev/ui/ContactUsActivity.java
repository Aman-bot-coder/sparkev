package com.augmentaa.sparkev.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;

import java.net.URLEncoder;

public class ContactUsActivity extends AppCompatActivity {

    LinearLayout lyWhatsup,lyCall,lyEmail,lyWeb;
    ImageView img_back;
    Button btnCallbaclRequest;
    String charger_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().hide();
        lyCall=findViewById(R.id.ly_phone);
        lyWhatsup=findViewById(R.id.ly_whatup);
        lyEmail=findViewById(R.id.ly_email);
        lyWeb=findViewById(R.id.ly_web);
        img_back=findViewById(R.id.back);
        btnCallbaclRequest=findViewById(R.id.btn_callbackrequest);
try {
   charger_id=getIntent().getExtras().toString();
}catch (Exception e){

}



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lyWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(ContactUsActivity.this)) {
                    String url = "https://www.exicom-ps.com/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }





            }
        });

        lyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18005725845"));
                startActivity(intent);
            }
        });
        lyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ContactUsActivity.this)) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"evsupport@exicom.in"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Your Request");
                    i.putExtra(Intent.EXTRA_TEXT   , "");

                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }



            }
        });
        lyWhatsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(ContactUsActivity.this)) {

                    String contact = "+918330083300"; // use country code with your phone number
                    String url = null;

                    try {

                        if(charger_id!=null){
                            url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + URLEncoder.encode(Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + "\n" +  getIntent().getExtras().getString("charger_id")+ " \n" +
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null) + "\n" + "-----Do not remove----", "UTF-8");

                        }
                        else {
                            url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + URLEncoder.encode(Pref.getValue(Pref.TYPE.F_NAME.toString(), null) + "\n" +
                                    Pref.getValue(Pref.TYPE.B_ADDRESS.toString(), null) + "\n" + Pref.getValue(Pref.TYPE.CHARGER_PART_NUMBER.toString(), null) + "#" + Pref.getValue(Pref.TYPE.CHARGER_SERIAL_NO.toString(), null) + " \n" +
                                    Pref.getValue(Pref.TYPE.MOBILE.toString(), null) + "\n" + "-----Do not remove----", "UTF-8");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                  /*  PackageManager pm = getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);*/


                        PackageManager packageManager = getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        try {
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            Logger.e("Ex  " + e);
                            Toast.makeText(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    catch (Exception e){
                        Logger.e("Ex  " + e);
                    }
                } else {
                    Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }





            }
        });

        btnCallbaclRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(ContactUsActivity.this)) {
                    Intent intent=new Intent(ContactUsActivity.this, CallRequestInfoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

    }
}