package com.augmentaa.sparkev.model.signup.question_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question implements Parcelable {

    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SerializedName("name")
    @Expose
    public String name;

    public final static Creator<Question> CREATOR = new Creator<Question>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return (new Question[size]);
        }

    };

    protected Question(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.count = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.data, (Data.class.getClassLoader()));
        this.name = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Question() {
    }

    @Override
    public String toString() {
        return "Question{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                ", name='" + name + '\'' +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(count);
        dest.writeList(data);
        dest.writeValue(name);


    }

    public int describeContents() {
        return 0;
    }

}