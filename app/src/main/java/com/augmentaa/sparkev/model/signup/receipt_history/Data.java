package com.augmentaa.sparkev.model.signup.receipt_history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data  implements Parcelable{
    @Override
    public String toString() {
        return "Data{" +
                "userId=" + userId +
                ", activityType='" + activityType + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", amountPaid=" + amountPaid +
                ", transactionId='" + transactionId + '\'' +
                ", receiptNo='" + receiptNo + '\'' +
                ", status='" + status + '\'' +
                ", chargerModelName='" + chargerModelName + '\'' +
                ", planValidity='" + planValidity + '\'' +
                ", mrp=" + mrp +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", mrpInctax=" + mrpInctax +
                ", autoRenew='" + autoRenew + '\'' +
                ", pgBankName='" + pgBankName + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", invoicePath='" + invoicePath + '\'' +
                ", nickName='" + nickName + '\'' +
                ", created_date='" + created_date + '\'' +
                ", up_desc='" + up_desc + '\'' +
                ", up_vas='" + up_vas + '\'' +
                ", plan_name='" + plan_name + '\'' +
                ", up_request_date='" + up_request_date + '\'' +
                ", up_closedate='" + up_closedate + '\'' +
                '}';
    }

    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("activity_type")
    @Expose
    public String activityType;
    @SerializedName("serial_no")
    @Expose
    public String serialNo;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("amount_paid")
    @Expose
    public float amountPaid;
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
    public float mrp;
    @SerializedName("cgst")
    @Expose
    public float cgst;
    @SerializedName("sgst")
    @Expose
    public float sgst;
    @SerializedName("mrp_inctax")
    @Expose
    public float mrpInctax;
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
    @SerializedName("nick_name")
    @Expose
    public String nickName;

    @SerializedName("created_date")
    @Expose
    public String created_date;



    @SerializedName("up_desc")
    @Expose
    public String  up_desc;
    @SerializedName("up_vas")
    @Expose
    public String  up_vas;
    @SerializedName("plan_name")
    @Expose
    public String   plan_name;
    @SerializedName("up_request_date")
    @Expose
    public String   up_request_date;
    @SerializedName("up_closedate")
    @Expose
    public String   up_closedate;


    protected Data(Parcel in) {
        userId = in.readInt();
        activityType = in.readString();
        serialNo = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        amountPaid = in.readFloat();
        transactionId = in.readString();
        receiptNo = in.readString();
        status = in.readString();
        chargerModelName = in.readString();
        planValidity = in.readString();
        mrp = in.readFloat();
        cgst = in.readFloat();
        sgst = in.readFloat();
        mrpInctax = in.readFloat();
        autoRenew = in.readString();
        pgBankName = in.readString();
        billingAddress = in.readString();
        invoicePath = in.readString();
        nickName = in.readString();
        created_date = in.readString();
        up_desc = in.readString();
        up_vas = in.readString();
        plan_name = in.readString();
        up_request_date = in.readString();
        up_closedate = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(activityType);
        parcel.writeString(serialNo);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeFloat(amountPaid);
        parcel.writeString(transactionId);
        parcel.writeString(receiptNo);
        parcel.writeString(status);
        parcel.writeString(chargerModelName);
        parcel.writeString(planValidity);
        parcel.writeFloat(mrp);
        parcel.writeFloat(cgst);
        parcel.writeFloat(sgst);
        parcel.writeFloat(mrpInctax);
        parcel.writeString(autoRenew);
        parcel.writeString(pgBankName);
        parcel.writeString(billingAddress);
        parcel.writeString(invoicePath);
        parcel.writeString(nickName);
        parcel.writeString(created_date);
        parcel.writeString(up_desc);
        parcel.writeString(up_vas);
        parcel.writeString(plan_name);
        parcel.writeString(up_request_date);
        parcel.writeString(up_closedate);
    }
}