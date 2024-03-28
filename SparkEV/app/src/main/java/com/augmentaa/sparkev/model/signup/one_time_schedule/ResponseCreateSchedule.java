package com.augmentaa.sparkev.model.signup.one_time_schedule;

public class ResponseCreateSchedule {
    @Override
    public String toString() {
        return "ResponseCreateSchedule{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public String message;
    public boolean status;


}
