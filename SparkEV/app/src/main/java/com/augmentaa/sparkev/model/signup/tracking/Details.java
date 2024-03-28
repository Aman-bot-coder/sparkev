package com.augmentaa.sparkev.model.signup.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

      @SerializedName("request_dt")
      @Expose
      public String requestDt;
      @SerializedName("status")
      @Expose
      public String status;
      @SerializedName("isChecked")
      @Expose
      public boolean isChecked;
}

