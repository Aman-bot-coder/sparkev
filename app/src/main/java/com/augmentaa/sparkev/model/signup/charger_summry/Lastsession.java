package com.augmentaa.sparkev.model.signup.charger_summry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lastsession {

    @Override
    public String toString() {
        return "Lastsession{" +
                "createdDate='" + createdDate + '\'' +
                ", energyConsumed=" + energyConsumed +
                ", duration='" + duration + '\'' +
                '}';
    }

    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("energy_consumed")
    @Expose
    public Float energyConsumed;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("created_date_char")
    @Expose
    public String created_date_char;


}