package com.augmentaa.sparkev.model.signup.guest_access_list;

public class RequestDenyGuestAccess {

    public String device_number;
    public int  request_id;

    @Override
    public String toString() {
        return "RequestDenyGuestAccess{" +
                "device_number='" + device_number + '\'' +
                ", request_id=" + request_id +
                ", user_id=" + user_id +
                '}';
    }

    public RequestDenyGuestAccess(String device_number, int request_id, int user_id) {
        this.device_number = device_number;
        this.request_id = request_id;
        this.user_id = user_id;
    }

    public int  user_id;
}
