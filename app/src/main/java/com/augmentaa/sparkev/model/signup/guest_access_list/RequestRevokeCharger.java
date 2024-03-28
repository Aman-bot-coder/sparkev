package com.augmentaa.sparkev.model.signup.guest_access_list;

public class RequestRevokeCharger {


    public RequestRevokeCharger(int request_id, String client_dev_no, int user_id, int modifyby,String serial_no) {
        this.request_id = request_id;
        this.client_dev_no = client_dev_no;
        this.user_id = user_id;
        this.modifyby = modifyby;
       this.serial_no=serial_no;
    }

    public int request_id;
    public String client_dev_no;
    public int user_id;
    public int modifyby;
    public String serial_no;


}
