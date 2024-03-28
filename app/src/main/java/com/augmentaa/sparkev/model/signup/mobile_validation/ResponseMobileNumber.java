package com.augmentaa.sparkev.model.signup.mobile_validation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMobileNumber {

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("err_code")
    @Expose
    public String errCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    @Override
    public String toString() {
        return "ResponseMobileNumber{" +
                "status=" + status +
                ", errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}