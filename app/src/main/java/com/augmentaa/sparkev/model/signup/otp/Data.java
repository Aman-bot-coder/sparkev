package com.augmentaa.sparkev.model.signup.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("app_version")
    @Expose
    public String appVersion;
    @SerializedName("release")
    @Expose
    public String release;
    @SerializedName("user")
    @Expose
    public User user;

    @SerializedName("user_id")
    @Expose
    public int user_id;

    public int id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Data{" +
                "token='" + token + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", release='" + release + '\'' +
                ", user=" + user +
                ", id=" + id +
                '}';
    }
}