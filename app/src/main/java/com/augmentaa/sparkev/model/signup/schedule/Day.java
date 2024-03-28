package com.augmentaa.sparkev.model.signup.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day implements Parcelable {

@SerializedName("id")
@Expose
public int id;
@SerializedName("day_name")
@Expose
public String dayName;
@SerializedName("status")
@Expose
public String status;

    protected Day(Parcel in) {
        id = in.readInt();
        dayName = in.readString();
        status = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", dayName='" + dayName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(dayName);
        parcel.writeString(status);
    }
}