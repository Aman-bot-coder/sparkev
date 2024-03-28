package com.augmentaa.sparkev.model.signup.charger_summry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("detailed")
@Expose
public List<Detailed> detailed = null;
@SerializedName("month")
@Expose
public List<Month> month = null;

@SerializedName("lastsession")
@Expose
public Lastsession lastsession;


@SerializedName("totaltime")
@Expose
public float totaltime;

@SerializedName("total_energy")
@Expose
public Float totalEnergy;

    @Override
    public String toString() {
        return "Data{" +
                "detailed=" + detailed +
                ", month=" + month +
                ", lastsession=" + lastsession +
                ", totaltime=" + totaltime +
                ", totalEnergy=" + totalEnergy +
                '}';
    }
}