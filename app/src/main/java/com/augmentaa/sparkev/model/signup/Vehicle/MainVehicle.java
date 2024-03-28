package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainVehicle implements Parcelable {

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("data")
    @Expose
    public List<AddVehicleDetails> data = null;
    public final static Creator<MainVehicle> CREATOR = new Creator<MainVehicle>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MainVehicle createFromParcel(Parcel in) {
            return new MainVehicle(in);
        }

        public MainVehicle[] newArray(int size) {
            return (new MainVehicle[size]);
        }

    };

    protected MainVehicle(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (AddVehicleDetails.class.getClassLoader()));
    }

    public MainVehicle() {
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