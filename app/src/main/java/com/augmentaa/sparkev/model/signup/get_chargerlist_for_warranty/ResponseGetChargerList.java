package com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty;

import java.util.List;

public class ResponseGetChargerList {
   public boolean status;
   public String message;
   public int user_id;
   public int vehicle_id;
   public String registration_number;
   public String serial_number;
   public String mobile_number;
   public String is_OCPP_enabled;
   @Override
   public String toString() {
      return "ResponseGetChargerList{" +
              "status=" + status +
              ", message='" + message + '\'' +
              ", data=" + data +
              '}';
   }

   public List<Data> data;


}
