package com.augmentaa.sparkev.model.signup.payment;

public class PaymentRequest {

    public PaymentRequest(int userid, float amount, String type, int activity_id, String mobileno, String source, String serial_no,String paymentmode) {
        this.userid = userid;
        this.amount = amount;
        this.type = type;
        this.activity_id = activity_id;
        this.mobileno = mobileno;
        this.source = source;
        this.serial_no = serial_no;
        this.paymentmode = paymentmode;
    }

    public PaymentRequest(int userid, float amount, String type, int activity_id, String mobileno, String source, String serial_no) {
        this.userid = userid;
        this.amount = amount;
        this.type = type;
        this.activity_id = activity_id;
        this.mobileno = mobileno;
        this.source = source;
        this.serial_no = serial_no;
    }

    public int userid;
    public float amount;
    public String type;
    public int activity_id;
    public String mobileno;
    public String source;
    public String serial_no;
    public String paymentmode;

}
