package com.augmentaa.sparkev.model.signup.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetSchedule {
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
      return "ResponseGetSchedule{" +
              "status=" + status +
              ", message='" + message + '\'' +
              ", count=" + count +
              ", data=" + data +
              '}';
   }
}
