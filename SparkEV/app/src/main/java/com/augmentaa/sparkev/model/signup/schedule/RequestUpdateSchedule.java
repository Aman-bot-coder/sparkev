package com.augmentaa.sparkev.model.signup.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUpdateSchedule {

      @SerializedName("user_id")
      @Expose
      public int userId;
      @SerializedName("charger_serial_no")
      @Expose
      public String chargerSerialNo;
      @SerializedName("schedule")
      @Expose
      public List<Schedule> schedule = null;
      @SerializedName("status")
      @Expose
      public String status;
      @SerializedName("created_by")
      @Expose
      public int createdBy;
      @SerializedName("schedule_type")
      @Expose
      public String scheduleType;

      @SerializedName("schedule_status")
      @Expose
      public String scheduleStatus;

      @SerializedName("schedule_id")
      @Expose
      public int schedule_id;

      @SerializedName("start_schedule_time")
      @Expose
      public String startScheduleTime;

      @SerializedName("stop_schedule_time")
      @Expose
      public String stopScheduleTime;

      @SerializedName("duration")
      @Expose
      public int duration;

      @SerializedName("schedule_name")
      @Expose
      public String scheduleName;

      public RequestUpdateSchedule(int userId, String chargerSerialNo, List<Schedule> schedule, String status, int createdBy, String scheduleType, String scheduleName, String scheduleStatus,String startScheduleTime, String stopScheduleTime,int duration) {
            this.userId = userId;
            this.chargerSerialNo = chargerSerialNo;
            this.schedule = schedule;
            this.status = status;
            this.createdBy = createdBy;
            this.scheduleType = scheduleType;
            this.scheduleName = scheduleName;
            this.scheduleStatus=scheduleStatus;
            this.startScheduleTime = startScheduleTime;
            this.stopScheduleTime=stopScheduleTime;
            this.duration=duration;
      }


      public RequestUpdateSchedule(int userId, String chargerSerialNo, List<Schedule> schedule, String status, int createdBy, String scheduleType, String scheduleName, String scheduleStatus,int schedule_id,String startScheduleTime, String stopScheduleTime,int duration) {
            this.userId = userId;
            this.chargerSerialNo = chargerSerialNo;
            this.schedule = schedule;
            this.status = status;
            this.createdBy = createdBy;
            this.scheduleType = scheduleType;
            this.scheduleName = scheduleName;
            this.scheduleStatus=scheduleStatus;
            this.schedule_id=schedule_id;
            this.startScheduleTime = startScheduleTime;
            this.stopScheduleTime=stopScheduleTime;
            this.duration=duration;
      }



}
