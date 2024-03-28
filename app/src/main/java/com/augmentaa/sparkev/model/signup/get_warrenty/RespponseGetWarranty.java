package com.augmentaa.sparkev.model.signup.get_warrenty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespponseGetWarranty implements Parcelable {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("err_code")
    @Expose
    public String errCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public int pos;

    public String address;
    public String state_name;
    public String country_name;
    public String pincode;
    public int state_id;
    public int country_id;
    public String address1;
    public String address2;
    public String charger_sr_no;

    public String renewalPending;
    public String canRenewWarranty;
    public String canRenewPlan;

    public int station_id;

    protected RespponseGetWarranty(Parcel in) {
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
        errCode = in.readString();
        message = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        data = in.createTypedArrayList(Data.CREATOR);
        address = in.readString();
        state_name = in.readString();
        country_name = in.readString();
        pincode = in.readString();
        state_id = in.readInt();
        country_id = in.readInt();
        address1 = in.readString();
        address2 = in.readString();
        pos = in.readInt();
        charger_sr_no = in.readString();
        renewalPending = in.readString();
        canRenewWarranty = in.readString();
        canRenewPlan = in.readString();
        station_id = in.readInt();

    }

    public static final Creator<RespponseGetWarranty> CREATOR = new Creator<RespponseGetWarranty>() {
        @Override
        public RespponseGetWarranty createFromParcel(Parcel in) {
            return new RespponseGetWarranty(in);
        }

        @Override
        public RespponseGetWarranty[] newArray(int size) {
            return new RespponseGetWarranty[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
        parcel.writeString(errCode);
        parcel.writeString(message);
        if (count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(count);
        }
        parcel.writeTypedList(data);
        parcel.writeString(address);
        parcel.writeString(state_name);
        parcel.writeString(country_name);
        parcel.writeString(pincode);
        parcel.writeInt(state_id);
        parcel.writeInt(country_id);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeInt(pos);
        parcel.writeString(charger_sr_no);
        parcel.writeString(renewalPending);
        parcel.writeString(canRenewWarranty);
        parcel.writeString(canRenewPlan);
        parcel.writeInt(station_id);
    }


    @Override
    public String toString() {
        return "RespponseGetWarranty{" +
                "status=" + status +
                ", errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                ", pos=" + pos +
                ", address='" + address + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", pincode='" + pincode + '\'' +
                ", state_id=" + state_id +
                ", country_id=" + country_id +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", charger_sr_no='" + charger_sr_no + '\'' +
                ", renewalPending='" + renewalPending + '\'' +
                ", canRenewWarranty='" + canRenewWarranty + '\'' +
                ", canRenewPlan='" + canRenewPlan + '\'' +
                ", station_id=" + station_id +
                '}';
    }
}