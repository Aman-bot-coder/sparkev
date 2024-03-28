package com.augmentaa.sparkev.model.signup.error_log;

import android.os.Parcel;
import android.os.Parcelable;

public class ErrorMessage_Email implements Parcelable {
    public int id;
    public String username;
    public String email;
    public String device_id;
    public String app_version;
    public String os_version;
    public String activity_name;
    public String application_name;
    public int project_id;
    public String api_parameters;
    public String application_plateform;
    public String url;
    public String status;
    public int error_code;
    public String error_discription;
    public int created_by;
    public String message;

    protected ErrorMessage_Email(Parcel in) {
        id = in.readInt();
        username = in.readString();
        email = in.readString();
        device_id = in.readString();
        app_version = in.readString();
        os_version = in.readString();
        activity_name = in.readString();
        application_name = in.readString();
        project_id = in.readInt();
        api_parameters = in.readString();
        application_plateform = in.readString();
        url = in.readString();
        status = in.readString();
        error_code = in.readInt();
        error_discription = in.readString();
        created_by = in.readInt();
        mobile = in.readString();
    }

    public static final Creator<ErrorMessage_Email> CREATOR = new Creator<ErrorMessage_Email>() {
        @Override
        public ErrorMessage_Email createFromParcel(Parcel in) {
            return new ErrorMessage_Email(in);
        }

        @Override
        public ErrorMessage_Email[] newArray(int size) {
            return new ErrorMessage_Email[size];
        }
    };

    public ErrorMessage_Email(int id, String username, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform, String url, String status, int error_code, String error_discription, int created_by) {
        this.id = id;
        this.username = username;
        this.device_id = device_id;
        this.app_version = app_version;
        this.os_version = os_version;
        this.activity_name = activity_name;
        this.application_name = application_name;
        this.project_id = project_id;
        this.api_parameters = api_parameters;
        this.application_plateform = application_plateform;
        this.url = url;
        this.status = status;
        this.error_code = error_code;
        this.error_discription = error_discription;
        this.created_by = created_by;
    }



    @Override
    public String toString() {
        return "ErrorMessage_Email{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", device_id='" + device_id + '\'' +
                ", app_version='" + app_version + '\'' +
                ", os_version='" + os_version + '\'' +
                ", activity_name='" + activity_name + '\'' +
                ", application_name='" + application_name + '\'' +
                ", project_id='" + project_id + '\'' +
                ", api_parameters='" + api_parameters + '\'' +
                ", application_plateform='" + application_plateform + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", error_code=" + error_code +
                ", error_discription='" + error_discription + '\'' +
                ", created_by='" + created_by + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_discription() {
        return error_discription;
    }

    public void setError_discription(String error_discription) {
        this.error_discription = error_discription;
    }



    public String mobile;

    public ErrorMessage_Email(int id, String username, String email, String device_id, String app_version, String os_version, String activity_name, String application_name, int project_id, String api_parameters, String application_plateform,
                              String url, String status, int error_code, String error_discription, int created_by, String mobile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.device_id = device_id;
        this.app_version = app_version;
        this.os_version = os_version;
        this.activity_name = activity_name;
        this.application_name = application_name;
        this.project_id = project_id;
        this.api_parameters = api_parameters;
        this.application_plateform = application_plateform;
        this.url = url;
        this.status = status;
        this.error_code = error_code;
        this.error_discription = error_discription;
        this.created_by = created_by;
        this.mobile = mobile;
    }

    public ErrorMessage_Email(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(device_id);
        dest.writeString(app_version);
        dest.writeString(os_version);
        dest.writeString(activity_name);
        dest.writeString(application_name);
        dest.writeInt(project_id);
        dest.writeString(api_parameters);
        dest.writeString(application_plateform);
        dest.writeString(url);
        dest.writeString(status);
        dest.writeInt(error_code);
        dest.writeString(error_discription);
        dest.writeInt(created_by);
        dest.writeString(mobile);
    }




}
