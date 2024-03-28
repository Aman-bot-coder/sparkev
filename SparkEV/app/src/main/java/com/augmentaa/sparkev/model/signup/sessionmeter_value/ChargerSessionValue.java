package com.augmentaa.sparkev.model.signup.sessionmeter_value;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChargerSessionValue implements Parcelable {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;
    public final static Creator<ChargerSessionValue> CREATOR = new Creator<ChargerSessionValue>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChargerSessionValue createFromParcel(Parcel in) {
            return new ChargerSessionValue(in);
        }

        public ChargerSessionValue[] newArray(int size) {
            return (new ChargerSessionValue[size]);
        }

    };

    protected ChargerSessionValue(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.data, (Data.class.getClassLoader()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(count);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}