package com.augmentaa.sparkev.model.signup.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseForVerifyOTP {

   @SerializedName("status")
   @Expose
   public boolean status;
   @SerializedName("err_code")
   @Expose
   public String errCode;
   @SerializedName("message")
   @Expose
   public String message;

   public Data data;

   public boolean getStatus() {
      return status;
   }

   public void setStatus(Boolean status) {
      this.status = status;
   }

   public String getErrCode() {
      return errCode;
   }

   public void setErrCode(String errCode) {
      this.errCode = errCode;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public Data getData() {
      return data;
   }

   public void setData(Data data) {
      this.data = data;
   }

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
