package com.augmentaa.sparkev.model.signup.process_order;

public class ResponseToProcessOrder {
    public boolean status;
    public String message;

    @Override
    public String toString() {
        return "ResponseToProcessOrder{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Data data;

}
