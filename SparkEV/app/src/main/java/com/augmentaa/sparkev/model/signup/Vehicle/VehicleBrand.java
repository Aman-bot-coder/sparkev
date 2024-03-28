package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleBrand implements Parcelable
{

@SerializedName("id")
@Expose
public int id;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @SerializedName("name")
@Expose
public String name;
public final static Creator<VehicleBrand> CREATOR = new Creator<VehicleBrand>() {


@SuppressWarnings({
"unchecked"
})
public VehicleBrand createFromParcel(Parcel in) {
return new VehicleBrand(in);
}

public VehicleBrand[] newArray(int size) {
return (new VehicleBrand[size]);
}

}
;

protected VehicleBrand(Parcel in) {
this.id = ((int) in.readValue((int.class.getClassLoader())));
this.name = ((String) in.readValue((String.class.getClassLoader())));
}

    public VehicleBrand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public VehicleBrand() {

}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(id);
dest.writeValue(name);
}

public int describeContents() {
return 0;
}

}