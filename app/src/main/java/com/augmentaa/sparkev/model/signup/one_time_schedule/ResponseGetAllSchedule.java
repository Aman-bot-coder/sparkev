package com.augmentaa.sparkev.model.signup.one_time_schedule;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGetAllSchedule {

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
public List<Data> data = null;

    @Override
    public String toString() {
        return "ResponseGetAllSchedule{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}