package com.augmentaa.sparkev.model.signup.data_transfer;

public class ResponseCurrent {
    public String message;

    @Override
    public String toString() {
        return "ResponseCurrent{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public boolean status;
}
