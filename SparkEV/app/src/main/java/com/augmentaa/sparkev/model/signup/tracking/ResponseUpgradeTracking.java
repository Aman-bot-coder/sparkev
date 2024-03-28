package com.augmentaa.sparkev.model.signup.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUpgradeTracking {

   @SerializedName("status")
   @Expose
   public boolean status;
   @SerializedName("message")
   @Expose
   public String message;
   @SerializedName("count")
   @Expose
   public int count;
   @SerializedName("data")
   @Expose
   public List<Data> data = null;
}
