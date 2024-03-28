package com.augmentaa.sparkev.model.signup.warranty_status;

import java.util.List;

import com.augmentaa.sparkev.model.signup.warranty_status_details.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCheckWarrantyStatus {

   @SerializedName("status")
   @Expose
   public boolean status;
   @SerializedName("err_code")
   @Expose
   public String errCode;
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