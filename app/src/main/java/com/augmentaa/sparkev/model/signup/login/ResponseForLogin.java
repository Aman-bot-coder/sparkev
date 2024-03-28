package com.augmentaa.sparkev.model.signup.login;

import com.augmentaa.sparkev.model.signup.otp.User;

public class ResponseForLogin {
    public String token;
    public String app_version;
    public String release;
    public User user;
    public int status;
    public String data;

    @Override
    public String toString() {
        return "ResponseForLogin{" +
                "token='" + token + '\'' +
                ", app_version='" + app_version + '\'' +
                ", release='" + release + '\'' +
                ", user=" + user +
                ", status=" + status +
                ", data='" + data + '\'' +
                '}';
    }
}
