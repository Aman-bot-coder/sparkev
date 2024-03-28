package com.augmentaa.sparkev.model.signup.update_password;

public class RequestForUpdatePassword {

    int id;String password;int modify_by;String registration_origin;

    public RequestForUpdatePassword(int id, String password,int modify_by,String registration_origin) {
        this.id = id;
        this.modify_by = modify_by;
        this.password = password;
        this.registration_origin = registration_origin;
    }
}
