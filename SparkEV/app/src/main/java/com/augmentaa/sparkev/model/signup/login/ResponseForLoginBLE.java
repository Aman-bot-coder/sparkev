package com.augmentaa.sparkev.model.signup.login;

import com.augmentaa.sparkev.model.signup.Vehicle.Vehicle;

import java.util.List;

public class ResponseForLoginBLE {


    public boolean status;

    public String message;
    public int id;
    public String f_Name;
    public String l_Name;
    public String email;
    public String mobile;
    public String role_id;
    public String role_code;
    public String role_name;
    public String token;
    public List <Vehicle> vehicles;

    @Override
    public String toString() {
        return "ResponseForLoginBLE{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", id=" + id +
                ", f_Name='" + f_Name + '\'' +
                ", l_Name='" + l_Name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", role_id='" + role_id + '\'' +
                ", role_code='" + role_code + '\'' +
                ", role_name='" + role_name + '\'' +
                ", token='" + token + '\'' +
                ", vehicles=" + vehicles +
                '}';
    }
}
