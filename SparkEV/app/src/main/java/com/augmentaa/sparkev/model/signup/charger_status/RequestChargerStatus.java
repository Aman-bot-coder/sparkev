package com.augmentaa.sparkev.model.signup.charger_status;

public class RequestChargerStatus {
   public String command;
   public String charger_id;

   public RequestChargerStatus(String command, String charger_id, String connector) {
      this.command = command;
      this.charger_id = charger_id;
      this.connector = connector;
   }

   public String connector;
}
