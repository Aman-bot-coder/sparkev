package com.augmentaa.sparkev.model.signup.get_payment_mode;

public class Data {
    public int id;
    public String code;

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String name;


}
