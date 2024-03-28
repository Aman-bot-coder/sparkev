package com.augmentaa.sparkev.model.signup.billing_address;

public class RequestBillingAddress {
    public String serial_no;
    public String address1;
    public String address2;
    public int PIN;
    public int city_id;
    public int state_id;
    public int country_id;
    public String landmark;
    public double Lat;
    public double Lng;
    public int modify_by;
    public String address;
    public int station_id;


    public RequestBillingAddress(String serial_no, String address, String address1, String address2, int PIN, int city_id, int state_id, int country_id,
                                 String landmark, double lat, double lng, int modify_by,int station_id) {
        this.serial_no = serial_no;
        this.address1 = address1;
        this.address2 = address2;
        this.PIN = PIN;
        this.city_id = city_id;
        this.state_id = state_id;
        this.country_id = country_id;
        this.landmark = landmark;
        this.Lat = lat;
        this.Lng = lng;
        this.modify_by = modify_by;
        this.address=address;
        this.station_id=station_id;
    }
}
