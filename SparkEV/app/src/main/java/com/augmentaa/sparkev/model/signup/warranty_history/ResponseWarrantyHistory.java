package com.augmentaa.sparkev.model.signup.warranty_history;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWarrantyHistory implements Parcelable {

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("err_code")
    @Expose
    public String errCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public int count;

    @Override
    public String toString() {
        return "ResponseWarrantyHistory{" +
                "status=" + status +
                ", errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }

    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    protected ResponseWarrantyHistory(Parcel in) {
        status = in.readByte() != 0;
        errCode = in.readString();
        message = in.readString();
        count = in.readInt();
    }

    public static final Creator<ResponseWarrantyHistory> CREATOR = new Creator<ResponseWarrantyHistory>() {
        @Override
        public ResponseWarrantyHistory createFromParcel(Parcel in) {
            return new ResponseWarrantyHistory(in);
        }

        @Override
        public ResponseWarrantyHistory[] newArray(int size) {
            return new ResponseWarrantyHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (status ? 1 : 0));
        parcel.writeString(errCode);
        parcel.writeString(message);
        parcel.writeInt(count);
    }
}