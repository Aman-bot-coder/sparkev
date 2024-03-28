package com.augmentaa.sparkev.model.signup.one_time_schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("charger_serial_no")
    @Expose
    public String chargerSerialNo;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("schedule_id")
    @Expose
    public int scheduleId;
    @SerializedName("start_schedule_time")
    @Expose
    public String startScheduleTime;
    @SerializedName("stop_schedule_time")
    @Expose
    public String stopScheduleTime;
    @SerializedName("duration")
    @Expose
    public int duration;
    @SerializedName("schedule_type")
    @Expose
    public String scheduleType;
    @SerializedName("schedule_name")
    @Expose
    public String scheduleName;
    @SerializedName("day_name")
    @Expose
    public String dayName;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("createdby")
    @Expose
    public int createdby;
    @SerializedName("modify_date")
    @Expose
    public String modifyDate;
    @SerializedName("modifyby")
    @Expose
    public String modifyby;

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", chargerSerialNo='" + chargerSerialNo + '\'' +
                ", userId=" + userId +
                ", scheduleId=" + scheduleId +
                ", startScheduleTime='" + startScheduleTime + '\'' +
                ", stopScheduleTime='" + stopScheduleTime + '\'' +
                ", duration=" + duration +
                ", scheduleType='" + scheduleType + '\'' +
                ", scheduleName='" + scheduleName + '\'' +
                ", dayName='" + dayName + '\'' +
                ", status='" + status + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdby=" + createdby +
                ", modifyDate='" + modifyDate + '\'' +
                ", modifyby='" + modifyby + '\'' +
                '}';
    }
}