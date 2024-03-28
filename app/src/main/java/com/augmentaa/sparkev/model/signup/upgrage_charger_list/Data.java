package com.augmentaa.sparkev.model.signup.upgrage_charger_list;

public class Data {

    public String serial_no;
    public int city_id;
    public int country_id;
    public int state_id;
    public String city_name;

    @Override
    public String toString() {
        return "Data{" +
                "serial_no='" + serial_no + '\'' +
                ", city_id=" + city_id +
                ", country_id=" + country_id +
                ", state_id=" + state_id +
                ", city_name='" + city_name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", address='" + address + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", charger_id=" + charger_id +
                ", user_id=" + user_id +
                '}';
    }

    public String state_name;
    public String country_name;
    public String address;
    public String nick_name;
    public int charger_id;
    public int user_id;

}
