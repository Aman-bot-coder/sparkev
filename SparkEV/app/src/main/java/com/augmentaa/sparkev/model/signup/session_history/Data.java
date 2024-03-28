package com.augmentaa.sparkev.model.signup.session_history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {
    @Override
    public String toString() {
        return "Data{" +
                "nickName='" + nickName + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", meterStartTime='" + meterStartTime + '\'' +
                ", energyConsumed='" + energyConsumed + '\'' +
                ", duration=" + duration +
                '}';
    }

    @SerializedName("nick_name")
    @Expose
    public String nickName;
    @SerializedName("serial_no")
    @Expose
    public String serialNo;
    @SerializedName("meter_start_time")
    @Expose
    public String meterStartTime;

    @SerializedName("meter_start_time_char")
    @Expose
    public String meterStartTime_char;


    @SerializedName("energy_consumed")
    @Expose
    public String energyConsumed;
    @SerializedName("duration")
    @Expose
    public String duration;



    @SerializedName("energy_consumed_kw")
    @Expose
    public String energy_consumed_kw;

    protected Data(Parcel in) {
        nickName = in.readString();
        serialNo = in.readString();
        meterStartTime = in.readString();
        energyConsumed = in.readString();
        duration = in.readString();
        energy_consumed_kw=in.readString();
        meterStartTime_char=in.readString();
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
        parcel.writeString(nickName);
        parcel.writeString(serialNo);
        parcel.writeString(meterStartTime);
        parcel.writeString(energyConsumed);
        parcel.writeString(duration);
        parcel.writeString(energy_consumed_kw);
        parcel.writeString(meterStartTime_char);
    }
}
