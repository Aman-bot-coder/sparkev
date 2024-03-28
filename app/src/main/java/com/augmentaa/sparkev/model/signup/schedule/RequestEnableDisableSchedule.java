package com.augmentaa.sparkev.model.signup.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestEnableDisableSchedule {
   @SerializedName("schedule")
   @Expose
   public List<ResponseUpdateSchedule> schedule = null;
   @SerializedName("modify_by")
   @Expose
   public int modifyBy;
}
