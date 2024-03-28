package com.augmentaa.sparkev.model.signup.mobile_static_text;


import java.util.List;

public class TextStaticData {
   public String message;
   public boolean status;
  public List<Data> data;

    @Override
    public String toString() {
        return "TextStaticData{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
