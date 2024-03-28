package com.augmentaa.sparkev.model.signup.warranty_history;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("serial_no")
    @Expose
    public String serialNo;
    @SerializedName("charger_id")
    @Expose
    public int chargerId;
    @SerializedName("charger_model_name")
    @Expose
    public String chargerModelName;
    @SerializedName("renewal_pending")
    @Expose
    public String renewalPending;
    @SerializedName("can_renew_warranty")
    @Expose
    public String canRenewWarranty;
    @SerializedName("can_renew_plan")
    @Expose
    public String canRenewPlan;

    @Override
    public String toString() {
        return "Data{" +
                "serialNo='" + serialNo + '\'' +
                ", chargerId=" + chargerId +
                ", chargerModelName='" + chargerModelName + '\'' +
                ", renewalPending='" + renewalPending + '\'' +
                ", canRenewWarranty='" + canRenewWarranty + '\'' +
                ", canRenewPlan='" + canRenewPlan + '\'' +
                ", history=" + history +
                '}';
    }

    @SerializedName("history")
    @Expose
    public List<History> history = null;

    protected Data(Parcel in) {
        serialNo = in.readString();
        chargerId = in.readInt();
        chargerModelName = in.readString();
        renewalPending = in.readString();
        canRenewWarranty = in.readString();
        canRenewPlan = in.readString();
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
        parcel.writeString(serialNo);
        parcel.writeInt(chargerId);
        parcel.writeString(chargerModelName);
        parcel.writeString(renewalPending);
        parcel.writeString(canRenewWarranty);
        parcel.writeString(canRenewPlan);
    }
}