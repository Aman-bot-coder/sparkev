package com.augmentaa.sparkev.model.signup.add_charger;

public class ResponseAddCharger {

    public int status;
    public String msg;
    public String client_certificate;
    public int id;
  public String device_owner;
    public String nickname;

    @Override
    public String toString() {
        return "ResponseAddCharger{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", client_certificate='" + client_certificate + '\'' +
                ", id=" + id +
                ", device_owner='" + device_owner + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
