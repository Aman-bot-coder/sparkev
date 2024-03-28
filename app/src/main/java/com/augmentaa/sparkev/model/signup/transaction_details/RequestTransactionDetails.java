package com.augmentaa.sparkev.model.signup.transaction_details;

public class RequestTransactionDetails {
   public int userid;

   public RequestTransactionDetails(int userid, String orderid) {
      this.userid = userid;
      this.orderid = orderid;
   }

   public String orderid;
}
