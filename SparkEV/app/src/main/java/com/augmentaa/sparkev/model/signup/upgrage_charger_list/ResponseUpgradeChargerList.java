package com.augmentaa.sparkev.model.signup.upgrage_charger_list;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUpgradeChargerList {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("err_code")
    @Expose
    public String errCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;


    @Override
    public String toString() {
        return "ResponseUpgradeChargerList{" +
                "status=" + status +
                ", errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
