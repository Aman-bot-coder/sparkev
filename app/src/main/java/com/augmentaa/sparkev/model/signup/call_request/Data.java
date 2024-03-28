package com.augmentaa.sparkev.model.signup.call_request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("question")
    @Expose
    public String question;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("created_date")
    @Expose
    public String created_date;

    @SerializedName("createdby")
    @Expose
    public String createdby;

    @SerializedName("closed_by")
    @Expose
    public String closed_by;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("closed_date")
    @Expose
    public String closed_date;

    @SerializedName("date")
    @Expose
    public String date;


    @SerializedName("remarks")
    @Expose
    public String remarks;

    @SerializedName("name")
    @Expose
    public String name;


    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.question = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.created_date = ((String) in.readValue((String.class.getClassLoader())));
        this.createdby = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));


    }

    public Data() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(question);
        dest.writeValue(description);
        dest.writeValue(status);
        dest.writeValue(created_date);
        dest.writeValue(createdby);
        dest.writeValue(name);

    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", createdby='" + createdby + '\'' +
                ", closed_by='" + closed_by + '\'' +
                ", closed_date='" + closed_date + '\'' +
                ", date='" + date + '\'' +
                ", remarks='" + remarks + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}