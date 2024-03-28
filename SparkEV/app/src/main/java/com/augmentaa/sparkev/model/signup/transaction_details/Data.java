package com.augmentaa.sparkev.model.signup.transaction_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("user_id")
@Expose
public Integer userId;
@SerializedName("activity_type")
@Expose
public String activityType;
@SerializedName("charger_serial_no")
@Expose
public String chargerSerialNo;
@SerializedName("start_date")
@Expose
public String startDate;
@SerializedName("end_date")
@Expose
public String endDate;
@SerializedName("amount_paid")
@Expose
public Integer amountPaid;
@SerializedName("transaction_id")
@Expose
public String transactionId;
@SerializedName("receipt_no")
@Expose
public String receiptNo;
@SerializedName("status")
@Expose
public String status;
@SerializedName("charger_model_name")
@Expose
public String chargerModelName;
@SerializedName("plan_validity")
@Expose
public String planValidity;
@SerializedName("mrp")
@Expose
public Integer mrp;
@SerializedName("cgst")
@Expose
public Integer cgst;
@SerializedName("sgst")
@Expose
public Integer sgst;
@SerializedName("mrp_inctax")
@Expose
public Integer mrpInctax;
@SerializedName("auto_renew")
@Expose
public String autoRenew;
@SerializedName("pg_bank_name")
@Expose
public String pgBankName;
@SerializedName("billing_address")
@Expose
public String billingAddress;
@SerializedName("invoice_path")
@Expose
public String invoicePath;

}