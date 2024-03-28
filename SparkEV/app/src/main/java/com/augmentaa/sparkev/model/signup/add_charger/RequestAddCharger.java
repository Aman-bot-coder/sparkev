package com.augmentaa.sparkev.model.signup.add_charger;

public class RequestAddCharger {


    public int created_by;

    @Override
    public String toString() {
        return "RequestAddCharger{" +
                "created_by=" + created_by +
                ", client_dev_no='" + client_dev_no + '\'' +
                ", client_sw_no='" + client_sw_no + '\'' +
                ", nickname='" + nickname + '\'' +
                ", user=" + user +
                ", client_certificate='" + client_certificate + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public RequestAddCharger(int created_by, String client_dev_no, String client_sw_no, String nickname, int user, String client_certificate, double lat, double lng) {
        this.created_by = created_by;
        this.client_dev_no = client_dev_no;
        this.client_sw_no = client_sw_no;
        this.nickname = nickname;
        this.user = user;
        this.client_certificate = client_certificate;
        this.lat = lat;
        this.lng = lng;
    }

    public String client_dev_no;
    public String client_sw_no;
    public String nickname;
    public int user;
    public String client_certificate;
    public double lat;
    public double lng;

}
