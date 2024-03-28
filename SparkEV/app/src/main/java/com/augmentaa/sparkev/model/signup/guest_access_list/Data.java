package com.augmentaa.sparkev.model.signup.guest_access_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    public Data(int id, String deviceNumber, int userId, String userName, String userEmail, String deviceOwner, int status, String createdAt, String updatedAt) {
        this.id = id;
        this.deviceNumber = deviceNumber;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.deviceOwner = deviceOwner;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", deviceNumber='" + deviceNumber + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", deviceOwner='" + deviceOwner + '\'' +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", owner_email='" + owner_email + '\'' +
                ", owner_mobile='" + owner_mobile + '\'' +
                '}';
    }

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("device_number")
    @Expose
    public String deviceNumber;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_email")
    @Expose
    public String userEmail;
    @SerializedName("device_owner")
    @Expose
    public String deviceOwner;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    @SerializedName("owner_name")
    @Expose
    public String owner_name;

    @SerializedName("owner_email")
    @Expose
    public String owner_email;

    @SerializedName("owner_mobile")
    @Expose
    public String owner_mobile;





}

