package com.augmentaa.sparkev.model.signup.get_payment_mode;

import java.util.List;

public class PaymentMode {
   public String message;
   public boolean status;

   @Override
   public String toString() {
      return "PaymentMode{" +
              "message='" + message + '\'' +
              ", status=" + status +
              ", data=" + data +
              '}';
   }

   public List <Data> data;

}
