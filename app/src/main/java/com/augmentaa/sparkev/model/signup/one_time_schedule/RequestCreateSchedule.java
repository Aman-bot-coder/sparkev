package com.augmentaa.sparkev.model.signup.one_time_schedule;

public class RequestCreateSchedule {
    public String charger_serial_no;


    public int user_id;

    public RequestCreateSchedule(String charger_serial_no, int user_id, String username, String start_schedule_time, String stop_schedule_time, String duration, String status, String schedule_type, int created_by) {
        this.charger_serial_no = charger_serial_no;
        this.user_id = user_id;
        this.username = username;
        this.start_schedule_time = start_schedule_time;
        this.stop_schedule_time = stop_schedule_time;
        this.duration = duration;
        this.status = status;
        this.schedule_type = schedule_type;
        this.created_by = created_by;
    }

    public String username;
    public String start_schedule_time;
    public String stop_schedule_time;
    public String duration;
    public String status;
    public String schedule_type;
    public int created_by;

}
