package com.augmentaa.sparkev.model.signup.profile;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileData implements Parcelable {
    protected ProfileData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        f_Name = in.readString();
        m_Name = in.readString();
        l_Name = in.readString();
        gender = in.readString();
        birth_date = in.readString();
        email = in.readString();
        mobile = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        PIN = in.readInt();
        landmark = in.readString();
        country_id = in.readInt();
        city_id = in.readInt();
        state_id = in.readInt();
        image_url = in.readString();
        state_name = in.readString();
        country_name = in.readString();
    }

    public static final Creator<ProfileData> CREATOR = new Creator<ProfileData>() {
        @Override
        public ProfileData createFromParcel(Parcel in) {
            return new ProfileData(in);
        }

        @Override
        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };

    public ProfileData() {
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", f_Name='" + f_Name + '\'' +
                ", m_Name='" + m_Name + '\'' +
                ", l_Name='" + l_Name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", PIN=" + PIN +
                ", landmark='" + landmark + '\'' +
                ", country_id=" + country_id +
                ", city_id=" + city_id +
                ", state_id=" + state_id +
                ", image_url='" + image_url + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_name='" + country_name + '\'' +
                '}';
    }

    public int id;
    public String name;
    public String f_Name;
    public String m_Name;
    public String l_Name;
    public String gender;
    public String birth_date;
    public String email;
    public String mobile;
    public String address1;
    public String address2;
    public int PIN;
    public String landmark;
    public int country_id;
    public int city_id;
    public int state_id;
    public String image_url;
    public String state_name;
    public String country_name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(f_Name);
        parcel.writeString(m_Name);
        parcel.writeString(l_Name);
        parcel.writeString(gender);
        parcel.writeString(birth_date);
        parcel.writeString(email);
        parcel.writeString(mobile);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeInt(PIN);
        parcel.writeString(landmark);
        parcel.writeInt(country_id);
        parcel.writeInt(city_id);
        parcel.writeInt(state_id);
        parcel.writeString(image_url);
        parcel.writeString(state_name);
        parcel.writeString(country_name);
    }
}
