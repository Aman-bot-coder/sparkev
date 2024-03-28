package com.augmentaa.sparkev.model.signup.Vehicle;

public class ResponseAddVehicle {
   public String message;
   public boolean status;
   public Data data;

   @Override
   public String toString() {
      return "ResponseAddVehicle{" +
              "message='" + message + '\'' +
              ", status=" + status +
              ", data=" + data +
              '}';
   }
}
