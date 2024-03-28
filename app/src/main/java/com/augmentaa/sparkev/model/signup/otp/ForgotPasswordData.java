package com.augmentaa.sparkev.model.signup.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordData {


    @Override
    public String toString() {
        return "ForgotPasswordData{" +
                "user_id=" + user_id +
                '}';
    }

    @SerializedName("user_id")
    @Expose
    public int user_id;


}