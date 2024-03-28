
package com.augmentaa.sparkev.model.signup.charger_summry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Month {

    @SerializedName("session")
    @Expose
    public Integer session;
    @SerializedName("energy_consumed")
    @Expose
    public Float energyConsumed;
    @SerializedName("duration")
    @Expose
    public String duration;

    @Override
    public String toString() {
        return "Month{" +
                "session=" + session +
                ", energyConsumed=" + energyConsumed +
                ", duration='" + duration + '\'' +
                '}';
    }
}