package com.augmentaa.sparkev.model.signup.razorpay;

import com.augmentaa.sparkev.model.signup.payment.Data;

public class CreateOrderResponse {

    public String id;
    public String currency;
    public float amount;
    public String key;
    public Data data;

    @Override
    public String toString() {
        return "CreateOrderResponse{" +
                "id='" + id + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", key='" + key + '\'' +
                '}';
    }
}
