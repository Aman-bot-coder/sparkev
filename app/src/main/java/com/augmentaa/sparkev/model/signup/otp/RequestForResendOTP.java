package com.augmentaa.sparkev.model.signup.otp;

public class RequestForResendOTP {

    public String username;
    public String mobile;
    public String email;
    public String id;
    public String otp_purpose;


    public RequestForResendOTP(String username, String mobile, String email, String id,String otp_purpose) {
        this.username = username;
        this.id = id;
        this.mobile = mobile;
        this.email = email;
        this.otp_purpose=otp_purpose;

    }

}
