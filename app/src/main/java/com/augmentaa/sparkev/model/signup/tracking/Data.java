package com.augmentaa.sparkev.model.signup.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
   @SerializedName("request_no")
   @Expose
   public String requestNo;
   @SerializedName("charger_nick_name")
   @Expose
   public String chargerNickName;
   @SerializedName("charger_serial_no")
   @Expose
   public String chargerSerialNo;
   @SerializedName("request_dt")
   @Expose
   public String requestDt;
   @SerializedName("details")
   @Expose
   public List<Details> details = null;

}
