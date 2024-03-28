package com.augmentaa.sparkev.model.signup.remote_start_stop;

public class ResponseChargerStartStop {


    public boolean status;
    public String message;


    @Override
    public String toString() {
        return "ResponseChargerStartStop{" +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}