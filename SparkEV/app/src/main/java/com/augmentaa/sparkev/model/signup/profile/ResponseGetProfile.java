package com.augmentaa.sparkev.model.signup.profile;

import java.util.List;

public class ResponseGetProfile {
    public boolean status;
    public String message;

    @Override
    public String toString() {
        return "ResponseGetProfile{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public List<ProfileData> data;
}
