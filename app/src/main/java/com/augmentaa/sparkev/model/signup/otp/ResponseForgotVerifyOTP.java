package com.augmentaa.sparkev.model.signup.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseForgotVerifyOTP {

   @SerializedName("status")
   @Expose
   public boolean status;
   @SerializedName("err_code")
   @Expose
   public String errCode;
   @SerializedName("message")
   @Expose
   public String message;

   public List<Data> data;


   @Override
   public String toString() {
      return "ResponseForVerifyOTP{" +
              "status=" + status +
              ", errCode='" + errCode + '\'' +
              ", message='" + message + '\'' +
              ", data=" + data +
              '}';
   }




}
