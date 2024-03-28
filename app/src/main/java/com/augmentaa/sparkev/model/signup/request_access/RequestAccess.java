package com.augmentaa.sparkev.model.signup.request_access;

public class RequestAccess {

   public String  device_number;
   public String user_name;
   public String user_email;
   public String device_owner;
   public int user_id;

   public RequestAccess(String device_number, String user_name, String user_email, String device_owner, int user_id) {
      this.device_number = device_number;
      this.user_name = user_name;
      this.user_email = user_email;
      this.device_owner = device_owner;
      this.user_id = user_id;
   }
}
