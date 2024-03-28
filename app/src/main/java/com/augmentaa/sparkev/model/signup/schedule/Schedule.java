package com.augmentaa.sparkev.model.signup.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("day_name")
    @Expose
    public String dayName;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("id")
    @Expose
    public int id;

    public boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public Schedule( String dayName,String status) {
        this.dayName = dayName;
        this.status = status;
    }

    public Schedule( String dayName,String status,int id) {
        this.dayName = dayName;
        this.status = status;
        this.id=id;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "dayName='" + dayName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Schedule(String dayName, boolean isChecked) {
        this.dayName = dayName;
        this.isChecked = isChecked;
    }
}