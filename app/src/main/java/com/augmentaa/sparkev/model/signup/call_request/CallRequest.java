package com.augmentaa.sparkev.model.signup.call_request;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallRequest implements Parcelable {


    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("question_id")
    @Expose
    public String question_id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("remarks")
    @Expose
    public String remarks;


    @SerializedName("createdby")
    @Expose
    public int createdby;
    @SerializedName("question")
    @Expose
    public String question;

    @SerializedName("closed_by")
    @Expose
    public String closed_by;

    @SerializedName("closed_date")
    @Expose
    public String closed_date;

    @SerializedName("source_app")
    @Expose
    public String source_app;



    public CallRequest(String status, int userId, String question_id, String name, String email, String mobile, String date, String remarks, int createdby, String source_app) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.question_id = question_id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.date = date;
        this.remarks = remarks;
        this.createdby = createdby;
        this.source_app = source_app;
    }

    public CallRequest(String status, int userId, String mobile) {
        this.status = status;
        this.userId = userId;
        this.mobile = mobile;

    }

    public CallRequest(String question_id, String question, String date, String status) {
        this.question = question;
        this.status = status;
        this.question_id = question_id;
        this.date = date;

    }


    public final static Creator<CallRequest> CREATOR = new Creator<CallRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CallRequest createFromParcel(Parcel in) {
            return new CallRequest(in);
        }

        public CallRequest[] newArray(int size) {
            return (new CallRequest[size]);
        }

    };

    protected CallRequest(Parcel in) {
        this.success = ((String) in.readValue((boolean.class.getClassLoader())));
        this.status = ((String) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((int) in.readValue((String.class.getClassLoader())));
        this.question_id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.mobile = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.remarks = ((String) in.readValue((String.class.getClassLoader())));
        this.createdby = ((int) in.readValue((String.class.getClassLoader())));
        this.question = ((String) in.readValue((String.class.getClassLoader())));
        this.closed_by = ((String) in.readValue((String.class.getClassLoader())));
        this.closed_date = ((String) in.readValue((String.class.getClassLoader())));



    }

    public CallRequest() {
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "success=" + success +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(userId);
        dest.writeValue(question_id);
        dest.writeValue(name);
        dest.writeValue(email);
        dest.writeValue(mobile);
        dest.writeValue(date);
        dest.writeValue(remarks);
        dest.writeValue(createdby);
        dest.writeValue(question);
        dest.writeValue(closed_by);
        dest.writeValue(closed_date);

    }

    public int describeContents() {
        return 0;
    }

}