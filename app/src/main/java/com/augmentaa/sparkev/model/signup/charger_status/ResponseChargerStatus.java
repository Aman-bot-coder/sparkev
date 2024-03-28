package com.augmentaa.sparkev.model.signup.charger_status;

public class ResponseChargerStatus {
   public String message;
   public boolean status;

   @Override
   public String toString() {
      return "ResponseChargerStatus{" +
              "message='" + message + '\'' +
              ", status=" + status +
              '}';
   }
}
