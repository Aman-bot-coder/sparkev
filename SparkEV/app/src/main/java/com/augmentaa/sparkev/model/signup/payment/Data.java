package com.augmentaa.sparkev.model.signup.payment;

public class Data {

    public String token;

    @Override
    public String toString() {
        return "Data{" +
                "token='" + token + '\'' +
                ", orderid='" + orderid + '\'' +
                ", mid='" + mid + '\'' +
                ", amount='" + amount + '\'' +
                ", callbackurl='" + callbackurl + '\'' +
                '}';
    }

    public String orderid;
    public String mid;
    public  String amount;
    public String callbackurl;
    public String order_id;
    public String paymentType;
    public String paymentmode;

}
