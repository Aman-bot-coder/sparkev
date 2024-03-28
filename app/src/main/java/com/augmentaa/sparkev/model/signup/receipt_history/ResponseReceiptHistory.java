package com.augmentaa.sparkev.model.signup.receipt_history;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseReceiptHistory {

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
        return "ResponseReceiptHistory{" +
                "status=" + status +
                ", errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}