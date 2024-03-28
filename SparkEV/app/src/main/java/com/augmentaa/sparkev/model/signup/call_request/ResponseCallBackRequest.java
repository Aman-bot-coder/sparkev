package com.augmentaa.sparkev.model.signup.call_request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCallBackRequest {

    @Override
    public String toString() {
        return "ResponseCallBackRequest{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public List<Data> data = null;
}
