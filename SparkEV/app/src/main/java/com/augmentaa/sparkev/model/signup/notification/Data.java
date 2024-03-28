package com.augmentaa.sparkev.model.signup.notification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("event_name")
    @Expose
    public String eventName;
    @SerializedName("notification_message")
    @Expose
    public String notificationMessage;
    @SerializedName("created_date")
    @Expose
    public String createdDate;

    @SerializedName("status")
    @Expose
    public String status;



    protected Data(Parcel in) {
        id = in.readInt();
        eventName = in.readString();
        notificationMessage = in.readString();
        createdDate = in.readString();
        status = in.readString();
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
        parcel.writeInt(id);
        parcel.writeString(eventName);
        parcel.writeString(notificationMessage);
        parcel.writeString(createdDate);
        parcel.writeString(status);
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}