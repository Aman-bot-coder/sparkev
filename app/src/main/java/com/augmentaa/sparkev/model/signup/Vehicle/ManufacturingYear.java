package com.augmentaa.sparkev.model.signup.Vehicle;

public class ManufacturingYear {


    public ManufacturingYear(int id, String year) {
        this.id = id;
        this.year = year;
    }

    public int id;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + year + '\'' +
                '}';
    }

    public String year;

}