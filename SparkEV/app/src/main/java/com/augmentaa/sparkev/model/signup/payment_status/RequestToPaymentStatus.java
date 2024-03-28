package com.augmentaa.sparkev.model.signup.payment_status;

public class RequestToPaymentStatus {
   public String orderid;
    public String serial_no;

    public RequestToPaymentStatus(String orderid, String serial_no, String source) {
        this.orderid = orderid;
        this.serial_no = serial_no;
        this.source = source;
    }

    public String source;


    public RequestToPaymentStatus(String orderid) {
        this.orderid = orderid;
    }
}
