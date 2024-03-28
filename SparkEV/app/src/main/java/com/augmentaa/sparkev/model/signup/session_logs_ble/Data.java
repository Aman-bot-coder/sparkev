package com.augmentaa.sparkev.model.signup.session_logs_ble;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
   public String start_time;
   public String stop_time;
   public float session_kwh;

   public Data(String start_time, String stop_time, float session_kwh) {
      this.start_time = start_time;
      this.stop_time = stop_time;
      this.session_kwh = session_kwh;
   }

   protected Data(Parcel in) {
      start_time = in.readString();
      stop_time = in.readString();
      session_kwh = in.readFloat();
   }

   public static final Creator<Data> CREATOR = new Creator<Data>() {
      @Override
      public Data createFromParcel(Parcel in) {
         return new Data(in);
      }

      @Override
      public Data[] newArray(int size) {
         return new Data[size];
      }
   };

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(start_time);
      parcel.writeString(stop_time);
      parcel.writeFloat(session_kwh);
   }
}
