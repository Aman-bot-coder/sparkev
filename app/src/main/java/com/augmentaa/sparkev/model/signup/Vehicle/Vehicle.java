package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {

    public int id;
    public int brand_id;
    public String brand_name;
    public int model_id;
    public String model_name;
    public int connector_type_id;
    public String connector_type_name;
    public String registration_no;
    public String year_of_manufacture;
    public String model_image_url;
    public String engine_no;
    public String chassis_no;
    public String vin_no;
    public int is_default;
    public String status;
    public String created_date;
    public int created_by;

    protected Vehicle(Parcel in) {
        id = in.readInt();
        brand_id = in.readInt();
        brand_name = in.readString();
        model_id = in.readInt();
        model_name = in.readString();
        connector_type_id = in.readInt();
        connector_type_name = in.readString();
        registration_no = in.readString();
        year_of_manufacture = in.readString();
        model_image_url = in.readString();
        engine_no = in.readString();
        chassis_no = in.readString();
        vin_no = in.readString();
        is_default = in.readInt();
        status = in.readString();
        created_date = in.readString();
        created_by = in.readInt();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(brand_id);
        parcel.writeString(brand_name);
        parcel.writeInt(model_id);
        parcel.writeString(model_name);
        parcel.writeInt(connector_type_id);
        parcel.writeString(connector_type_name);
        parcel.writeString(registration_no);
        parcel.writeString(year_of_manufacture);
        parcel.writeString(model_image_url);
        parcel.writeString(engine_no);
        parcel.writeString(chassis_no);
        parcel.writeString(vin_no);
        parcel.writeInt(is_default);
        parcel.writeString(status);
        parcel.writeString(created_date);
        parcel.writeInt(created_by);
    }
}
