package com.augmentaa.sparkev.model.signup.otp;

public class RequestForOTPVefifyUser {

   public String email;
   public String mobile;
   public String otp;
   public String username;
   public String app_version;
   public String mobile_deviceid;
   public String fcm_id;
   public String otp_purpose;

   public RequestForOTPVefifyUser(String email, String mobile, String otp, String username, String app_version, String mobile_deviceid, String fcm_id) {
      this.email = email;
      this.mobile = mobile;
      this.otp = otp;
      this.username = username;
      this.app_version = app_version;
      this.mobile_deviceid = mobile_deviceid;
      this.fcm_id = fcm_id;
   }

   public RequestForOTPVefifyUser(String username, String mobile, String email, String otp,String otp_purpose) {
      this.username = username;
      this.otp = otp;
      this.mobile = mobile;
      this.email = email;
      this.otp_purpose=otp_purpose;

   }

}
