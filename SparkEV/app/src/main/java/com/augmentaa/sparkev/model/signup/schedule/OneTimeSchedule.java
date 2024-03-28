package com.augmentaa.sparkev.model.signup.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OneTimeSchedule {

    @SerializedName("duration")
    @Expose
    public Integer duration;
    @SerializedName("day_name")
    @Expose
    public String dayName;

    @SerializedName("status")
    @Expose
    public String status;



    public OneTimeSchedule(int duration, String dayName, String status) {
        this.duration = duration;
        this.dayName = dayName;
        this.status = status;
    }


}