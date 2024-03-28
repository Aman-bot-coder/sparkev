package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleModel implements Parcelable {

    public VehicleModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public VehicleModel(int id, String name,String chargerType) {
        this.id = id;
        this.name = name;
        this.chargerType = chargerType;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("current_type_id")
    @Expose
    public String current_type_id;
    @SerializedName("current_type_name")
    @Expose
    public String current_type_name;


    public String chargerType;
    public final static Creator<VehicleModel> CREATOR = new Creator<VehicleModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        public VehicleModel[] newArray(int size) {
            return (new VehicleModel[size]);
        }

    };

    protected VehicleModel(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.chargerType = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.current_type_id = ((String) in.readValue((String.class.getClassLoader())));
        this.current_type_name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VehicleModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(chargerType);
        dest.writeValue(description);
        dest.writeValue(current_type_id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}