package com.augmentaa.sparkev.model.signup.alarm_logs_ble;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
   public String date;
   public String flag;
   public String alarm_name;

   public Data(String date, String flag, String alarm_name) {
      this.date = date;
      this.flag = flag;
      this.alarm_name = alarm_name;
   }

   protected Data(Parcel in) {
      date = in.readString();
      flag = in.readString();
      alarm_name = in.readString();
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
      parcel.writeString(date);
      parcel.writeString(flag);
      parcel.writeString(alarm_name);
   }
}
