package com.augmentaa.sparkev.model.signup.one_time_schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestCreateScheduleRecurring {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("username")
    @Expose
    public String username;
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
    public String createdBy;
    @SerializedName("schedule_type")
    @Expose
    public String scheduleType;
    @SerializedName("schedule_name")
    @Expose
    public String scheduleName;


    public RequestCreateScheduleRecurring(String userId, String username, String chargerSerialNo, List<Schedule> schedule, String status, String createdBy, String scheduleType, String scheduleName) {
        this.userId = userId;
        this.username = username;
        this.chargerSerialNo = chargerSerialNo;
        this.schedule = schedule;
        this.status = status;
        this.createdBy = createdBy;
        this.scheduleType = scheduleType;
        this.scheduleName = scheduleName;
    }
}
