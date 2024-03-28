package com.augmentaa.sparkev.model.signup.one_time_schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("start_schedule_time")
    @Expose
    public String startScheduleTime;
    @SerializedName("stop_schedule_time")
    @Expose
    public String stopScheduleTime;
    @SerializedName("duration")
    @Expose
    public Integer duration;
    @SerializedName("day_name")
    @Expose
    public String dayName;

    @SerializedName("id")
    @Expose
    public int id;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Schedule(String startScheduleTime, String stopScheduleTime, Integer duration, String dayName, boolean isChecked) {
        this.startScheduleTime = startScheduleTime;
        this.stopScheduleTime = stopScheduleTime;
        this.duration = duration;
        this.dayName = dayName;
        this.isChecked = isChecked;
    }

    public Schedule( String dayName, boolean isChecked) {
        this.startScheduleTime = startScheduleTime;
        this.stopScheduleTime = stopScheduleTime;
        this.duration = duration;
        this.dayName = dayName;
        this.isChecked = isChecked;
    }


    public boolean isChecked;

}