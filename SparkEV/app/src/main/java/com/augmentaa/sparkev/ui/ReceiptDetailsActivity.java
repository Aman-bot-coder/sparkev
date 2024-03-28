package com.augmentaa.sparkev.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.receipt_history.Data;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;

public class ReceiptDetailsActivity extends AppCompatActivity {

    Button btn_download;
    TextView tv_charger_name, tv_charger_type, tv_payment_method,
            tv_transaction_id, tv_plan, tv_session, tv_address, tv_amount,tv_warranty_date,tv_reciept_details;

    ImageView img_back;
    Data data;
    LinearLayout ly_vas;
    TextView tv_vas,tv_warranty_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);
        tv_charger_name = findViewById(R.id.charger_name);
        tv_charger_type = findViewById(R.id.charger_type);
        tv_payment_method = findViewById(R.id.payment_method);
        tv_transaction_id = findViewById(R.id.transaction_id);
        tv_plan = findViewById(R.id.plan);
        tv_session = findViewById(R.id.period);
        tv_address = findViewById(R.id.address);
        btn_download = findViewById(R.id.btn_download);
        img_back = findViewById(R.id.back);
        tv_amount = findViewById(R.id.amount);
        tv_warranty_date=findViewById(R.id.date_of_warranty);
        tv_reciept_details=findViewById(R.id.reciept_details);
        tv_vas=findViewById(R.id.vas);
        ly_vas=findViewById(R.id.ly_vas);
        tv_warranty_desc=findViewById(R.id.desc_date_of_warranty);
        getSupportActionBar().hide();

        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Receipt Data" + data);
//            tv_charger_name.setText(data.serialNo);
            if("UPGRADE".equalsIgnoreCase(data.activityType)){
                ly_vas.setVisibility(View.VISIBLE);
                tv_warranty_desc.setText("Activation Date");
            }
            else {
                ly_vas.setVisibility(View.GONE);
                tv_warranty_desc.setText("Date of Warranty");
            }

            if (data.nickName != null) {
                if ("".equalsIgnoreCase(data.nickName)||" ".equalsIgnoreCase(data.nickName)){
                   tv_charger_name.setText("" + data.serialNo);

                }
                else {
                   tv_charger_name.setText("" + data.nickName);

                }

            } else {
               tv_charger_name.setText("" +data.serialNo);


            }
            tv_charger_type.setText(data.chargerModelName);
            tv_payment_method.setText(data.pgBankName);
            tv_transaction_id.setText(data.transactionId);
            tv_plan.setText(data.planValidity);
            tv_reciept_details.setText("Receipt #"+data.transactionId);
            tv_session.setText(AppUtils.getOnlyDate(data.startDate) + " - " + AppUtils.getOnlyDate(data.endDate));
            tv_address.setText(data.billingAddress);
            tv_amount.setText(getResources().getString(R.string.Rs) + "" + data.amountPaid);
            tv_vas.setText(data.up_vas);
            tv_warranty_date.setText(AppUtils.getOnlyDate(data.created_date));
            if (data.invoicePath != null) {
                if ("".equalsIgnoreCase(data.invoicePath) || " ".equalsIgnoreCase(data.invoicePath)) {
                    btn_download.setVisibility(View.GONE);

                } else {
                    btn_download.setVisibility(View.VISIBLE);

                }

            } else {
                btn_download.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Logger.e("Exception " + e);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.invoicePath != null) {
                    if ("".equalsIgnoreCase(data.invoicePath) || " ".equalsIgnoreCase(data.invoicePath)) {

                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.invoicePath));
                        startActivity(browserIntent);

                    }

                } else {
                }

            }
        });

    }
}