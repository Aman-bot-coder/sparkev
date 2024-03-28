package com.augmentaa.sparkev.model.signup.mobile_validation;

public class RequestMobileNumber {

    public RequestMobileNumber(String email, String mobile, String key) {
        this.email = email;
        this.mobile = mobile;
        this.key = key;
    }


    public String email;
    public String mobile;
    public String key;

}
