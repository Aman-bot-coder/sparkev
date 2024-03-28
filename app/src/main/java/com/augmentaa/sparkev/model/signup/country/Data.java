package com.augmentaa.sparkev.model.signup.country;

import java.util.List;

public class Data {

    public int id;
    public String name;
    public String description;
    public String iso_code;
    public String country_code;
    public int min_mobile_length;
    public int max_mobile_length;
    public String status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    List<Data> data;

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", iso_code='" + iso_code + '\'' +
                ", country_code='" + country_code + '\'' +
                ", min_mobile_length=" + min_mobile_length +
                ", max_mobile_length=" + max_mobile_length +
                ", status='" + status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", modify_date='" + modify_date + '\'' +
                '}';
    }

    public String created_date;
    public String modify_date;
}
