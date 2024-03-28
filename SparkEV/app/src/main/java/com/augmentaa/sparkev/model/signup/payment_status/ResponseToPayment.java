package com.augmentaa.sparkev.model.signup.payment_status;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseToPayment implements Parcelable {
    public boolean status;
    public String message;



    protected ResponseToPayment(Parcel in) {
        status = in.readByte() != 0;
        message = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(message);
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseToPayment> CREATOR = new Creator<ResponseToPayment>() {
        @Override
        public ResponseToPayment createFromParcel(Parcel in) {
            return new ResponseToPayment(in);
        }

        @Override
        public ResponseToPayment[] newArray(int size) {
            return new ResponseToPayment[size];
        }
    };

    @Override
    public String toString() {
        return "ResponseToProcessOrder{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Data data;


}
