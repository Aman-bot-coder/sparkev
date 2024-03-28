package com.augmentaa.sparkev.model.signup.call_request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCallBackRequest {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("user_id")
    @Expose
    public int userId;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    public RequestCallBackRequest(String status, int userId, String mobile) {
        this.status = status;
        this.userId = userId;
        this.mobile = mobile;
    }
}
