package com.augmentaa.sparkev.model.signup.add_charger;

public class ResponseAddChargerSyncBLE {
    public boolean status;

    @Override
    public String toString() {
        return "ResponseAddChargerSyncBLE{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public String message;

}
