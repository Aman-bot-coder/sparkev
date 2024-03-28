package com.augmentaa.sparkev.model.signup.warranty_history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History implements Parcelable {

    @SerializedName("activity_type")
    @Expose
    public String activityType;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("serial_no")
    @Expose
    public String serialNo;
    @SerializedName("charger_id")
    @Expose
    public int chargerId;
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
    @SerializedName("transaction_status")
    @Expose
    public String transactionStatus;
    @SerializedName("charger_model_name")
    @Expose
    public String chargerModelName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("plan_validity")
    @Expose
    public String planValidity;
    @SerializedName("auto_renew")
    @Expose
    public String autoRenew;
    @SerializedName("can_renew_plan")
    @Expose
    public String canRenewPlan;
    @SerializedName("can_renew_warranty")
    @Expose
    public String canRenewWarranty;
    public History(){

    }

    protected History(Parcel in) {
        activityType = in.readString();
        userId = in.readInt();
        serialNo = in.readString();
        chargerId = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        amountPaid = in.readFloat();
        transactionId = in.readString();
        transactionStatus = in.readString();
        chargerModelName = in.readString();
        name = in.readString();
        planValidity = in.readString();
        autoRenew = in.readString();
        canRenewPlan = in.readString();
        canRenewWarranty = in.readString();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activityType);
        parcel.writeInt(userId);
        parcel.writeString(serialNo);
        parcel.writeInt(chargerId);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeFloat(amountPaid);
        parcel.writeString(transactionId);
        parcel.writeString(transactionStatus);
        parcel.writeString(chargerModelName);
        parcel.writeString(name);
        parcel.writeString(planValidity);
        parcel.writeString(autoRenew);
        parcel.writeString(canRenewPlan);
        parcel.writeString(canRenewWarranty);
    }

    @Override
    public String toString() {
        return "History{" +
                "activityType='" + activityType + '\'' +
                ", userId=" + userId +
                ", serialNo='" + serialNo + '\'' +
                ", chargerId=" + chargerId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", amountPaid=" + amountPaid +
                ", transactionId='" + transactionId + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", chargerModelName='" + chargerModelName + '\'' +
                ", name='" + name + '\'' +
                ", planValidity='" + planValidity + '\'' +
                ", autoRenew='" + autoRenew + '\'' +
                ", canRenewPlan='" + canRenewPlan + '\'' +
                ", canRenewWarranty='" + canRenewWarranty + '\'' +
                '}';
    }
}

