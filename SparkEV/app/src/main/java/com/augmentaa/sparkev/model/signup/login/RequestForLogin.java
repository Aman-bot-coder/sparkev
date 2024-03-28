package com.augmentaa.sparkev.model.signup.login;

public class RequestForLogin {
    public String registration_origin;
    public String email;
    public String password;
    public String app_version;
    public String mobile_deviceid;
    public int ble_user_id;
    public String fcm_id;
    String device_id;

    @Override
    public String toString() {
        return "RequestForLogin{" +
                "registration_origin='" + registration_origin + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", app_version='" + app_version + '\'' +
                ", mobile_deviceid='" + mobile_deviceid + '\'' +
                ", ble_user_id=" + ble_user_id +
                ", fcm_id='" + fcm_id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", update_status='" + update_status + '\'' +
                ", project_id=" + project_id +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public RequestForLogin(String email, String password, String app_version, String mobile_deviceid, String update_status, String fcm_id, int project_id, double lat, double lng, String registration_origin) {
        this.email = email;
        this.password = password;
        this.app_version = app_version;
        this.mobile_deviceid = mobile_deviceid;
        this.update_status = update_status;
        this.fcm_id = fcm_id;
        this.project_id = project_id;
        this.lat = lat;
        this.lng = lng;
        this.registration_origin=registration_origin;
    }


    public RequestForLogin(int ble_user_id) {
        this.ble_user_id = ble_user_id;

    }




    public RequestForLogin(int ble_user_id, String app_version, String device_id, String fcm_id, String registration_origin) {
        this.app_version = app_version;
        this.device_id = device_id;
        this.fcm_id = fcm_id;
        this.ble_user_id=ble_user_id;
        this.registration_origin=registration_origin;
    }
    public String update_status;
    public int project_id;
    public double lat;
    public double lng;

}
