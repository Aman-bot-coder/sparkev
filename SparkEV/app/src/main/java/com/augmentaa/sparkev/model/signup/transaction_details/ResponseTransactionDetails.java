package com.augmentaa.sparkev.model.signup.transaction_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTransactionDetails {
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

}
