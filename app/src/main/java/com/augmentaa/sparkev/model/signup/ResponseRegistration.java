package com.augmentaa.sparkev.model.signup;

public class ResponseRegistration {

    @Override
    public String toString() {
        return "ResponseRegistration{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
//", username='" + username + '\'' + changed by prash
    public int status;
    public String msg;
    public int id;
    public String name;
    public int username;



}


