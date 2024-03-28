package com.augmentaa.sparkev.ui;

import static com.augmentaa.sparkev.utils.AppUtils.getDateWarrantyPurchase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_payment_mode.Data;
import com.augmentaa.sparkev.model.signup.get_payment_mode.PaymentMode;
import com.augmentaa.sparkev.model.signup.get_warrenty.RespponseGetWarranty;
import com.augmentaa.sparkev.model.signup.payment.PaymentRequest;
import com.augmentaa.sparkev.model.signup.payment.PaymentResponse;
import com.augmentaa.sparkev.model.signup.payment_status.RequestToPaymentStatus;
import com.augmentaa.sparkev.model.signup.payment_status.ResponseToPayment;
import com.augmentaa.sparkev.model.signup.razorpay.CreateOrderResponse;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.services.MyService;
import com.augmentaa.sparkev.utils.AppUtils;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyPurchaseActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {

    Button btn_change_address, btn_purchase;
    TextView tv_address, tv_kwh, tv_price, tv_exp_date, tv_plan;
    ImageView img_back;
    RespponseGetWarranty data;
    APIInterface apiInterfacepp;
    ProgressDialog progress;
    String order_id;
    String host = "https://securegw-stage.paytm.in/";
    Integer ActivityRequestCode = 2;
    Dialog dialogPaymentSuccessfull;
    String payment_response;
    int station_id;

    Dialog dialog_warranty;
    PaymentMode paymentMode;
    List<Data> list;
    private AlertDialog.Builder alertDialogBuilder;
    String payment_mode;
    String serial_no,source;
    TextView tv_term_condition;
    Dialog dialog_payment_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warrany_purchage);
        btn_purchase = findViewById(R.id.btn_purchase);
        btn_change_address = findViewById(R.id.btn_change_address);
        tv_address = findViewById(R.id.address);
        tv_kwh = findViewById(R.id.kwh);
        tv_price = findViewById(R.id.price);
        tv_exp_date = findViewById(R.id.date);
        img_back = findViewById(R.id.back);
        tv_plan = findViewById(R.id.plan);
        tv_term_condition=findViewById(R.id.term_condition);
        dialog_warranty = new Dialog(WarrantyPurchaseActivity.this);
        list = new ArrayList<>();
        getSupportActionBar().hide();
        progress = new ProgressDialog(this);
        Checkout.preload(getApplicationContext());
        alertDialogBuilder = new AlertDialog.Builder(WarrantyPurchaseActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        dialog_payment_cancel = new Dialog(WarrantyPurchaseActivity.this);
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });


        this.apiInterfacepp = (APIInterface) APIClient.getPaymentProcess().create(APIInterface.class);
        try {
            data = getIntent().getParcelableExtra("data");
            tv_address.setText(data.address);
            tv_kwh.setText(data.data.get(data.pos).capacity);
            tv_price.setText("" + data.data.get(data.pos).mrpInctax + " (GST Inclusive)");
            tv_exp_date.setText(data.data.get(data.pos).planValidity+"\nActivate on "+getDateWarrantyPurchase(data.data.get(data.pos).startDate) + "\nValid Upto " +getDateWarrantyPurchase( data.data.get(data.pos).endDate));
            tv_plan.setText(data.data.get(data.pos).planValidity+" plan");
            Logger.e("Warranty data" + data.toString());
            serial_no=data.charger_sr_no;
        } catch (Exception e) {

        }

        tv_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
                   try {
                       String url = "https://policyvoiceassistant.exicom.in/";
                       Intent i = new Intent(Intent.ACTION_VIEW);
                       i.setData(Uri.parse(url));
                       startActivity(i);
                   }catch (Exception e){

                       Logger.e("Exp "+e);

                   }

                } else {
                    Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }
            }
        });
        /*if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
            getPaymentMode();

        } else {
            Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }*/
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    try {
                        getPaymentMode();
                    }
                    catch (Exception e){
                        Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }

               /* if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
                    progress.setMessage(getResources().getString(R.string.loading));
                    progress.show();
                    PaymentResponse();
                } else {
                    Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }*/
            }
        });
        btn_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateAddressActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);

            }
        });
    }

    private void PaymentResponse(String paymentmode) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfacepp.initiatetransaction(hashMap1, new PaymentRequest(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), data.data.get(data.pos).mrpInctax, "WARRANTY", data.data.get(data.pos).id, Pref.getValue(Pref.TYPE.MOBILE.toString(), null), "SPIN", data.charger_sr_no, paymentmode)).enqueue(new Callback<PaymentResponse>() {
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        PaymentResponse loginResponse = (PaymentResponse) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        order_id = loginResponse.data.orderid;
//                        loginResponse.data.paymentType="Online";
                        if (loginResponse.status) {
//                            https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID
//                            String callBackUrl="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+loginResponse.data.orderid;
//                            Logger.e( "callback Url.."+callBackUrl);
                            dialog_warranty.dismiss();
                            PaytmOrder paytmOrder = new PaytmOrder(loginResponse.data.orderid, loginResponse.data.mid, loginResponse.data.token, loginResponse.data.amount, loginResponse.data.callbackurl + loginResponse.data.orderid);
                            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
                                @Override
                                public void onTransactionResponse(Bundle bundle) {
//                                    Toast.makeText(PaymentDashboardActivity.this, "Response (onTransactionResponse) : " + bundle.size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("===Trancational Response==88888=" + bundle.toString());
                                    getOrderStatus(order_id);

                                }

                                @Override
                                public void networkNotAvailable() {

                                }

                                @Override
                                public void onErrorProceed(String s) {
                                    Logger.e("onErrorProceed  " + s);

                                }

                                @Override
                                public void clientAuthenticationFailed(String s) {
                                    Logger.e("clientAuthenticationFailed  " + s);

                                }

                                @Override
                                public void someUIErrorOccurred(String s) {
                                    Logger.e("someUIErrorOccurred  " + s);

                                }

                                @Override
                                public void onErrorLoadingWebPage(int i, String s, String s1) {

                                    Logger.e("onErrorLoadingWebPage  " + s + "  " + s1);

                                }

                                @Override
                                public void onBackPressedCancelTransaction() {

                                }

                                @Override
                                public void onTransactionCancel(String s, Bundle bundle) {
                                    Logger.e("someUIErrorOccurred  " + s + "  " + bundle.toString());

                                }
                            });

                            transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
                            transactionManager.setAppInvokeEnabled(true);
                            Logger.e("Trans" + transactionManager.toString());
                            System.out.println("===Trancational Response==22222=");
                            transactionManager.startTransaction(WarrantyPurchaseActivity.this, ActivityRequestCode);
                            System.out.println("===Trancational Response==333333=");
                        }
                    } catch (Exception e) {
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                WarrantyPurchaseActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.SUCCESS_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


                        Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            WarrantyPurchaseActivity.class.getSimpleName(),
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


            }

            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        WarrantyPurchaseActivity.class.getSimpleName(),
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


    private void getOrderStatus(String order_id) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfacepp.getCheckorderstatus(hashMap1, new RequestToPaymentStatus( order_id,  serial_no,  source)).enqueue(new Callback<ResponseToPayment>() {
            public void onResponse(Call<ResponseToPayment> call, Response<ResponseToPayment> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        ResponseToPayment loginResponse = (ResponseToPayment) response.body();
                        Logger.e("Response: " + loginResponse.toString());
                        if (loginResponse.status) {


//                            Intent intent = new Intent(PaymentDashboardActivity.this, PaymentStatusActivity.class);
//                            intent.putExtra("data", loginResponse.data);
//                            startActivity(intent);
                            showPaymentSuccessfulDialog(loginResponse.data.transaction_id);
//                        Toast.makeText(WarrantyPurchaseActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();


                        } else {
                            callMailService(
                                    Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                    Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                    , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                    Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                    Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                    Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                    WarrantyPurchaseActivity.class.getSimpleName(),
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
                        Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                WarrantyPurchaseActivity.class.getSimpleName(),
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
                            WarrantyPurchaseActivity.class.getSimpleName(),
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


            }

            public void onFailure(Call<ResponseToPayment> call, Throwable t) {
                Logger.e("Excption ===" + t.getMessage());
//                Toast.makeText(WarrantyPurchaseActivity.this, "111111" + getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        WarrantyPurchaseActivity.class.getSimpleName(),
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

    void showPaymentSuccessfulDialog(String transaction_id) {
        try {
            dialogPaymentSuccessfull = new Dialog(WarrantyPurchaseActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialogPaymentSuccessfull.setContentView(R.layout.activity_success);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    dialogPaymentSuccessfull.dismiss();
                    Intent intent = new Intent(WarrantyPurchaseActivity.this, ReceiptHistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();



                }
            }, 2500);
            dialogPaymentSuccessfull.show();

        } catch (Exception e) {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Logger.e("Data==>" + key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    Logger.e("data=11222=>" + bundle.get("response"));
                    payment_response = bundle.get("response").toString();

                    getOrderStatus(order_id);


                }

                Logger.e("data=1113333=>" + bundle.get("response"));


                if (bundle.get("response") != null) {
                    if ("NULL".equalsIgnoreCase(bundle.get("response").toString()) || bundle.get("response") == null || bundle.get("response").equals("")) {
//                        Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage"), Toast.LENGTH_SHORT).show();
                    } else {


                    }
                } else {
//                    Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage"), Toast.LENGTH_SHORT).show();

                }


            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void callMailService(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        ErrorMessage_Email errorMessage_email = new ErrorMessage_Email(id, username, email, device_id, app_version, os_version, activity_name, application_name, project_id, api_parameters, application_plateform, url, status, error_code, error_discription, created_by, mobile);
        Intent intent = new Intent(WarrantyPurchaseActivity.this, MyService.class);
        intent.putExtra("data", errorMessage_email);
        startService(intent);

    }

    void dialog_paymentMode() {

        dialog_warranty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warranty.setContentView(R.layout.dialog_payment_mode);
        dialog_warranty.setCancelable(true);
        RadioGroup rg_payment = dialog_warranty.findViewById(R.id.radioGroup);
        RadioButton rb_paytm = dialog_warranty.findViewById(R.id.rb_paytm);
        RadioButton rb_razorpay = dialog_warranty.findViewById(R.id.rb_razorpay);
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(CallRequestInfoActivity.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();

                    if ("PayTm".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            Logger.e("Payment Mode " + list.get(0).code + "  " + rb.getText().toString());
//                            PaymentResponse(list.get(0).code);
                            createOrderRazorPay(list.get(0).code);
                            source=list.get(0).code;
                        } else {
                            Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }

                    } else if ("RazorPay".equalsIgnoreCase(rb.getText().toString())) {
                        if (Utils.isNetworkConnected(WarrantyPurchaseActivity.this)) {
                            progress.setMessage(getResources().getString(R.string.loading));
                            progress.show();
                            source=list.get(1).code;
                            createOrderRazorPay(list.get(1).code);

                            Logger.e("Payment Mode " + list.get(1).code + "  " + rb.getText().toString());
//                            PaymentResponse(list.get(1).code);

                        } else {
                            Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    }
                }

            }
        });


        dialog_warranty.show();


    }


    public void getPaymentMode() {
        //GET List Resources
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        Call<PaymentMode> call = apiInterfacepp.getWayPaymentList(hashMap1, 4);
        call.enqueue(new Callback<PaymentMode>() {
            @Override
            public void onResponse(Call<PaymentMode> call, Response<PaymentMode> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                paymentMode = (PaymentMode) response.body();
                Logger.e("Warranty response===" + paymentMode.toString());
                if (response.code() == 200) {
                    try {
                        list.clear();
                        if (paymentMode.status) {
                            list = paymentMode.data;
                            if(list.size()>0){
                                if(list.size()==1){
                                    createOrderRazorPay(list.get(0).code);
                                    source=list.get(0).code;
                                }
                                else {
                                    dialog_paymentMode();
                                }
                            }


                        } else {
                            if ("Token is not valid".equalsIgnoreCase(paymentMode.message)) {
                                Pref.setBoolValue(Pref.TYPE.LOGIN.toString(), false);
                                Pref.clear();
                                Intent intent21 = new Intent(WarrantyPurchaseActivity.this, SignInActivity.class);
                                startActivity(intent21);
                                finishAffinity();
                                Toast.makeText(getApplicationContext(), "Your session are expired.", Toast.LENGTH_LONG).show();

                            }
                        }


                    } catch (Exception e) {
                        Log.d("Response", e.getMessage());
                        Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                WarrantyActivity.class.getSimpleName(),
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
                    Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            WarrantyActivity.class.getSimpleName(),
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

            }

            @Override
            public void onFailure(Call<PaymentMode> call, Throwable t) {
                call.cancel();
                progress.dismiss();
                Logger.e("Ex 111==" + t.getMessage());
                Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();


                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        WarrantyActivity.class.getSimpleName(),
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

    private void createOrderRazorPay(String paymentmode) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterfacepp.createPayment(hashMap1, new PaymentRequest(Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), data.data.get(data.pos).mrpInctax, "WARRANTY", data.data.get(data.pos).id, Pref.getValue(Pref.TYPE.MOBILE.toString(), null), "SPIN_ANDROID", data.charger_sr_no, paymentmode)).enqueue(new Callback<CreateOrderResponse>() {
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        if(dialog_warranty!=null) {
                            dialog_warranty.dismiss();
                        }
                        CreateOrderResponse createOrderResponse = (CreateOrderResponse) response.body();
                        Logger.e("Create Order Response " + createOrderResponse.toString());
                        order_id=createOrderResponse.data.orderid;

                        if ("paytm".equalsIgnoreCase(createOrderResponse.data.paymentmode)) {
                            PaytmOrder paytmOrder = new PaytmOrder(createOrderResponse.data.orderid, createOrderResponse.data.mid, createOrderResponse.data.token, createOrderResponse.data.amount, createOrderResponse.data.callbackurl + createOrderResponse.data.orderid);
                            TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
                                @Override
                                public void onTransactionResponse(Bundle bundle) {
//                                    Toast.makeText(PaymentDashboardActivity.this, "Response (onTransactionResponse) : " + bundle.size(), Toast.LENGTH_SHORT).show();
                                    getOrderStatus(order_id);

                                }

                                @Override
                                public void networkNotAvailable() {

                                }

                                @Override
                                public void onErrorProceed(String s) {
                                    Logger.e("onErrorProceed  " + s);

                                }

                                @Override
                                public void clientAuthenticationFailed(String s) {
                                    Logger.e("clientAuthenticationFailed  " + s);

                                }

                                @Override
                                public void someUIErrorOccurred(String s) {
                                    Logger.e("someUIErrorOccurred  " + s);

                                }

                                @Override
                                public void onErrorLoadingWebPage(int i, String s, String s1) {

                                    Logger.e("onErrorLoadingWebPage  " + s + "  " + s1);

                                }

                                @Override
                                public void onBackPressedCancelTransaction() {

                                }

                                @Override
                                public void onTransactionCancel(String s, Bundle bundle) {
                                    Logger.e("someUIErrorOccurred  " + s + "  " + bundle.toString());

                                }
                            });

                            transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
                            transactionManager.setAppInvokeEnabled(true);
                            Logger.e("Trans" + transactionManager.toString());
                            System.out.println("===Trancational Response==22222=");
                            transactionManager.startTransaction(WarrantyPurchaseActivity.this, ActivityRequestCode);
                            System.out.println("===Trancational Response==333333=");
                        }
                        else {

                            final Checkout co = new Checkout();
                            co.setKeyID(createOrderResponse.data.mid);
                            try {
                                JSONObject options = new JSONObject();
                                options.put("name", "Exicom Power Systems pvt. Ltd.");
                                options.put("description", "Demoing Charges");
                                options.put("order_id", createOrderResponse.data.orderid);
                                options.put("send_sms_hash", true);
                                options.put("allow_rotation", true);
                                //You can omit the image option to fetch the image from dashboard
//                                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//                                options.put("currency", createOrderResponse.data.currency);
                                options.put("amount", createOrderResponse.data.amount);

                                JSONObject preFill = new JSONObject();
                                preFill.put("email", Pref.getValue(Pref.TYPE.EMAIL.toString(), null));
                                preFill.put("contact", Pref.getValue(Pref.TYPE.MOBILE.toString(), null));
                                options.put("prefill", preFill);
                                co.open(WarrantyPurchaseActivity.this, options);
                            } catch (Exception e) {
                                Toast.makeText(WarrantyPurchaseActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                                e.printStackTrace();
                            }
                        }




                    } catch (Exception e) {
                        callMailService(
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                                , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                                Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                                Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                                Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                                WarrantyPurchaseActivity.class.getSimpleName(),
                                "SPIN_ANDROID",
                                AppUtils.project_id,
                                AppUtils.bodyToString(call.request().body()),
                                "ANDROID",
                                call.request().url().toString(),
                                "Y",
                                AppUtils.SUCCESS_CODE,
                                e.getMessage(),
                                Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                                Pref.getValue(Pref.TYPE.MOBILE.toString(), null));


                        Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                    }
                } else {

                    callMailService(
                            Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                            Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                            , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                            Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                            Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                            Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                            WarrantyPurchaseActivity.class.getSimpleName(),
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


            }

            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                Toast.makeText(WarrantyPurchaseActivity.this, getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
                callMailService(
                        Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0),
                        Pref.getValue(Pref.TYPE.USERNAME.toString(), null)
                        , Pref.getValue(Pref.TYPE.EMAIL.toString(), null),
                        Pref.getValue(Pref.TYPE.DEVICE_ID.toString(), null),
                        Pref.getValue(Pref.TYPE.APP_VERSION.toString(), null),
                        Pref.getValue(Pref.TYPE.OS_VERSION.toString(), null),
                        WarrantyPurchaseActivity.class.getSimpleName(),
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

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            getOrderStatus(order_id);
//            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
//            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            
//            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
//            alertDialogBuilder.show();

            dialog_payment_cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dismissProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    private void dismissProgressDialogCancel() {
        if (dialog_payment_cancel != null && dialog_payment_cancel.isShowing()) {
            dialog_payment_cancel.dismiss();
        }
    }
    @Override
    protected void onStop() {
        dismissProgressDialog();
        dismissProgressDialogCancel();
        super.onStop();

    }



    void dialog_payment_cancel() {

        dialog_payment_cancel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_payment_cancel.setContentView(R.layout.dialog_bluetooth_on_off);
        dialog_payment_cancel.setCancelable(false);
        TextView btn_ok = (TextView) dialog_payment_cancel.findViewById(R.id.tv_confirm);
        TextView btn_cencel = (TextView) dialog_payment_cancel.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog_payment_cancel.findViewById(R.id.tv_message);
        tv_message.setText("Payment failed/canceled.                                    ");
        btn_ok.setText("Ok");
        btn_cencel.setText("Skip");
       btn_cencel.setVisibility(View.GONE);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_payment_cancel.dismiss();

            }
        });
        btn_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_payment_cancel.dismiss();

            }
        });
        dialog_payment_cancel.show();


    }
}