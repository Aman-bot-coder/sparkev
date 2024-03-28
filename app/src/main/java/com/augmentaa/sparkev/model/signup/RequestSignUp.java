package com.augmentaa.sparkev.model.signup;

public class RequestSignUp {
    public String name;
    public String email;
    public String mobile;
    public String password;
    public int role;
    public int country_id;
    public int project_id;
    public String fcm_id;
    public String app_version;
    public int os_version;
    public String device_id;
    public String registration_origin;
    public String username;
//   public String username  ;


    public RequestSignUp(String name, String email,String username, String mobile, String password, int role, int country_id, int project_id, String fcm_id, String app_version, int os_version, String device_id,String registration_origin) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.mobile = mobile;
        this.password = password;
        this.role = role;
        this.country_id = country_id;
        this.project_id = project_id;
        this.fcm_id = fcm_id;
        this.app_version = app_version;
        this.os_version = os_version;
        this.device_id = device_id;
        this.registration_origin=registration_origin;
//
    }

    
}


