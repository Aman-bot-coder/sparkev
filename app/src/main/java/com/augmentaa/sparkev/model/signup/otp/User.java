package com.augmentaa.sparkev.model.signup.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("currentProvider")
    @Expose
    public Object currentProvider;
    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("jwt_token")
    @Expose
    public String jwtToken;
    @SerializedName("s3url")
    @Expose
    public String s3url;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(Object currentProvider) {
        this.currentProvider = currentProvider;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getS3url() {
        return s3url;
    }

    public void setS3url(String s3url) {
        this.s3url = s3url;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", currentProvider=" + currentProvider +
                ", mobile='" + mobile + '\'' +
                ", token='" + token + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                ", s3url='" + s3url + '\'' +
                '}';
    }
}
