package com.augmentaa.sparkev.model.signup.profile;

import java.util.List;

public class ResponseForUpdateProfile {
    public boolean status;
    public String message;
    public List<ProfileData> data;


    @Override
    public String toString() {
        return "ResponseForUpdateProfile{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
