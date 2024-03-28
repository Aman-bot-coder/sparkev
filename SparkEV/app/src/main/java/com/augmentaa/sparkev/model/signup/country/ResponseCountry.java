package com.augmentaa.sparkev.model.signup.country;

import java.util.List;

public class ResponseCountry {
    public String message;
    public boolean status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> data;

    public ResponseCountry() {
    }

    public List<Data> getSearch_data() {
        return search_data;
    }

    public void setSearch_data(List<Data> search_data) {
        this.search_data = search_data;
    }

    public List<Data> search_data;

}
