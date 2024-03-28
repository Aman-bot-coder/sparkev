package com.augmentaa.sparkev.model.signup.initiate_upgrade_request;

public class RequestUpgradeInitiate {


    public RequestUpgradeInitiate(int user_id, String customer_mobile, String address1, int charger_id, String request_dt, String customer_remark, String status, int state_id, int country_id) {
        this.user_id = user_id;
        this.customer_mobile = customer_mobile;
        this.address1 = address1;
        this.charger_id = charger_id;
        this.request_dt = request_dt;
        this.customer_remark = customer_remark;
        this.status = status;
        this.state_id = state_id;
        this.country_id = country_id;
    }

    public int user_id;
    public String customer_mobile;
//    public String customer_address;
    public int charger_id;
    public String request_dt;
    public String customer_remark;
    public String status;
    public String address1;
//    public String address2;
//    public String landmark;
//    public int city_id;
    public int state_id;
    public int country_id;
//    public String pin;

}
