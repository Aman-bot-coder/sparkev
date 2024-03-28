package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectorTypeDetails implements Parcelable
{

@SerializedName("ct_id")
@Expose
public int ct_id;

    @Override
    public String toString() {
        return "{" +
                "ct_id=" + ct_id +
                ", name='" + name + '\'' +
                '}';
    }

    @SerializedName("name")
@Expose
public String name;
public final static Creator<ConnectorTypeDetails> CREATOR = new Creator<ConnectorTypeDetails>() {


@SuppressWarnings({
"unchecked"
})
public ConnectorTypeDetails createFromParcel(Parcel in) {
return new ConnectorTypeDetails(in);
}

public ConnectorTypeDetails[] newArray(int size) {
return (new ConnectorTypeDetails[size]);
}

}
;

protected ConnectorTypeDetails(Parcel in) {
this.ct_id = ((int) in.readValue((int.class.getClassLoader())));
this.name = ((String) in.readValue((String.class.getClassLoader())));
}

    public ConnectorTypeDetails(int id, String name) {
        this.ct_id = id;
        this.name = name;
    }

    public ConnectorTypeDetails() {

}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(ct_id);
dest.writeValue(name);
}

public int describeContents() {
return 0;
}

}