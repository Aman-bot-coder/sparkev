package com.augmentaa.sparkev.model.signup.charger_summry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGetAllSummary {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("data")
    @Expose
    public Data data;

    @Override
    public String toString() {
        return "ResponseGetAllSummary{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
