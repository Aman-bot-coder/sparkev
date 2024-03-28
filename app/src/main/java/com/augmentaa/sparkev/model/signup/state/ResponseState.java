package com.augmentaa.sparkev.model.signup.state;

public class ResponseState {
    public int id;
    public String name;
    public String status;
    public String created_date;
    public int createdby;

    @Override
    public String toString() {
        return "ResponseState{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", createdby=" + createdby +
                '}';
    }
}
