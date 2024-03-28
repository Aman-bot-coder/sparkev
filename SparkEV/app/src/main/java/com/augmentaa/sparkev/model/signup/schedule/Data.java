package com.augmentaa.sparkev.model.signup.schedule;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data implements Parcelable {

    @SerializedName("schedule_id")
    @Expose
    public int scheduleId;

    @SerializedName("schedule_name")
    @Expose
    public String scheduleName;

    @SerializedName("schedule_status")
    @Expose
    public String scheduleStatus;

    @SerializedName("schedule_type")
    @Expose
    public String scheduleType;

    @SerializedName("start_schedule_time")
    @Expose
    public String startScheduleTime;

    @SerializedName("stop_schedule_time")
    @Expose
    public String stopScheduleTime;

    @SerializedName("duration")
    @Expose
    public int duration;

    protected Data(Parcel in) {
        scheduleId = in.readInt();
        scheduleName = in.readString();
        scheduleStatus = in.readString();
        scheduleType = in.readString();
        startScheduleTime = in.readString();
        stopScheduleTime = in.readString();
        duration = in.readInt();
        days = in.createTypedArrayList(Day.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(scheduleId);
        dest.writeString(scheduleName);
        dest.writeString(scheduleStatus);
        dest.writeString(scheduleType);
        dest.writeString(startScheduleTime);
        dest.writeString(stopScheduleTime);
        dest.writeInt(duration);
        dest.writeTypedList(days);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public String toString() {
        return "Data{" +
                "scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", scheduleStatus='" + scheduleStatus + '\'' +
                ", scheduleType='" + scheduleType + '\'' +
                ", startScheduleTime='" + startScheduleTime + '\'' +
                ", stopScheduleTime='" + stopScheduleTime + '\'' +
                ", duration=" + duration +
                ", days=" + days +
                '}';
    }

    @SerializedName("days")
    @Expose
    public List<Day> days = null;



}