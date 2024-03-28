package com.augmentaa.sparkev.model.signup.payment;

public class PaymentResponse {
    public boolean status;
    public String message;
   public Data data;

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

