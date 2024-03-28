package com.augmentaa.sparkev.model.signup.schedule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUpdateSchedule {
   @SerializedName("message")
   @Expose
   public String message;
   @SerializedName("status")
   @Expose
   public boolean status;
}
