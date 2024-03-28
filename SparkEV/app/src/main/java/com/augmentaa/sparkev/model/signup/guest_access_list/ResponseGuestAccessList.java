package com.augmentaa.sparkev.model.signup.guest_access_list;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGuestAccessList {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public ResponseGuestAccessList(int user_id) {
        this.user_id = user_id;
    }

    public  int user_id;

    @Override
    public String toString() {
        return "ResponseGuestAccessList{" +
                "status=" + status +
                ", msg=" + msg +
                ", data=" + data +
                ", user_id=" + user_id +
                '}';
    }
}