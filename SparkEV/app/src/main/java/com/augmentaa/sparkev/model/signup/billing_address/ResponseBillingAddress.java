package com.augmentaa.sparkev.model.signup.billing_address;

public class ResponseBillingAddress {
    public boolean status;
    public String message;

    @Override
    public String toString() {
        return "ResponseBillingAddress{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
