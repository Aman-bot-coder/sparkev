package com.augmentaa.sparkev.model.signup.charger_summry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detailed {

    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("energy_consumed")
    @Expose
    public Float energyConsumed;
    @SerializedName("duration")
    @Expose
    public Float duration;

    @SerializedName("created_date_char")
    @Expose
    public String created_date_char;

    @Override
    public String toString() {
        return "Detailed{" +
                "createdDate='" + createdDate + '\'' +
                ", energyConsumed=" + energyConsumed +
                ", duration=" + duration +
                '}';
    }
}