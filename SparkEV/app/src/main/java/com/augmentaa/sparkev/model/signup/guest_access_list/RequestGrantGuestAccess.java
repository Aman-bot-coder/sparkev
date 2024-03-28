package com.augmentaa.sparkev.model.signup.guest_access_list;

public class RequestGrantGuestAccess {

    public RequestGrantGuestAccess(String device_number, int request_id, int user_id) {
        this.device_number = device_number;
        this.request_id = request_id;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "RequestGrantGuestAccess{" +
                "device_number='" + device_number + '\'' +
                ", Request_id=" + request_id +
                ", user_id=" + user_id +
                '}';
    }

    public String device_number;
    public int  request_id;
    public int  user_id;
}
