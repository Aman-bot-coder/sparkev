package com.augmentaa.sparkev.model.signup.schedule;

public class RequestGetSchedule {

    public int user_id;

    public RequestGetSchedule(int user_id, String charger_serial_no, String schedule_type) {
        this.user_id = user_id;
        this.charger_serial_no = charger_serial_no;
        this.schedule_type = schedule_type;
    }

    public String charger_serial_no;
    public String schedule_type;

}
