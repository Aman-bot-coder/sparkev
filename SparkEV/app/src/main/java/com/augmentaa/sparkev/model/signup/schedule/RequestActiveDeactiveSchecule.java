package com.augmentaa.sparkev.model.signup.schedule;

public class RequestActiveDeactiveSchecule {
    public int schedule_id;

    public RequestActiveDeactiveSchecule(int schedule_id, String schedule_status, int modify_by) {
        this.schedule_id = schedule_id;
        this.schedule_status = schedule_status;
        this.modify_by = modify_by;
    }

    public String schedule_status;
    public int modify_by;

}
