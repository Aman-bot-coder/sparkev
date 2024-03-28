package com.augmentaa.sparkev.model.signup.add_charger;

public class RequestAddChargerSyncBLE {
    @Override
    public String toString() {
        return "RequestAddChargerSyncBLE{" +
                "serial_no='" + serial_no + '\'' +
                ", client_certificate='" + client_certificate + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", user_id=" + user_id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", status='" + status + '\'' +
                ", action='" + action + '\'' +
                ", map_as_child=" + map_as_child +
                '}';
    }

    public RequestAddChargerSyncBLE(String serial_no, String client_certificate, String nick_name, int user_id, double lat, double lng, String status, String action, int map_as_child) {
        this.serial_no = serial_no;
        this.client_certificate = client_certificate;
        this.nick_name = nick_name;
        this.user_id = user_id;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.action = action;
        this.map_as_child = map_as_child;
    }

    public String serial_no;
    public String client_certificate;
    public String nick_name;
    public int user_id;
    public double lat;
    public double lng;
    public String status;
    public String action;
    public int map_as_child;
}
