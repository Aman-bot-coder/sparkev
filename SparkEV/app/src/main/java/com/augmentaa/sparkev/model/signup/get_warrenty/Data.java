package com.augmentaa.sparkev.model.signup.get_warrenty;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("charger_model_id")
    @Expose
    public int chargerModelId;
    @SerializedName("charger_model_name")
    @Expose
    public String chargerModelName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("plan_validity")
    @Expose
    public String planValidity;
    @SerializedName("mrp")
    @Expose
    public float mrp;
    @SerializedName("cgst")
    @Expose
    public float cgst;
    @SerializedName("igst")
    @Expose
    public float igst;
    @SerializedName("sgst")
    @Expose
    public float sgst;
    @SerializedName("mrp_inctax")
    @Expose
    public float mrpInctax;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("auto_renew")
    @Expose
    public String autoRenew;

    @SerializedName("status")
    @Expose
    public String status;


    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", chargerModelId=" + chargerModelId +
                ", chargerModelName='" + chargerModelName + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", planValidity='" + planValidity + '\'' +
                ", mrp=" + mrp +
                ", cgst=" + cgst +
                ", igst=" + igst +
                ", sgst=" + sgst +
                ", mrpInctax=" + mrpInctax +
                ", description='" + description + '\'' +
                ", autoRenew='" + autoRenew + '\'' +
                ", status='" + status + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdby='" + createdby + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", modifyby='" + modifyby + '\'' +
                ", validityInMonths=" + validityInMonths +
                ", gracePeriod=" + gracePeriod +
                ", capacity='" + capacity + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", charger_sr_no='" + charger_sr_no + '\'' +
                ", serial_no='" + serial_no + '\'' +
                '}';
    }

    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("createdby")
    @Expose
    public String createdby;
    @SerializedName("modify_date")
    @Expose
    public String modifyDate;
    @SerializedName("modifyby")
    @Expose
    public String modifyby;
    @SerializedName("validity_in_months")
    @Expose
    public int validityInMonths;
    @SerializedName("grace_period")
    @Expose
    public int gracePeriod;
    @SerializedName("capacity")
    @Expose
    public String capacity;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;

    @SerializedName("charger_sr_no")
    @Expose
    public String charger_sr_no;

    @SerializedName("serial_no")
    @Expose
    public String serial_no;





    public Data() {
    }

    public String getCharger_sr_no() {
        return charger_sr_no;
    }

    public void setCharger_sr_no(String charger_sr_no) {
        this.charger_sr_no = charger_sr_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    protected Data(Parcel in) {
        id = in.readInt();
        chargerModelId = in.readInt();
        chargerModelName = in.readString();
        name = in.readString();
        code = in.readString();
        planValidity = in.readString();
        mrp = in.readFloat();
        cgst = in.readFloat();
        igst = in.readFloat();
        sgst = in.readFloat();
        mrpInctax = in.readFloat();
        description = in.readString();
        autoRenew = in.readString();
        status = in.readString();
        createdDate = in.readString();
        createdby = in.readString();
        modifyDate = in.readString();
        modifyby = in.readString();
        validityInMonths = in.readInt();
        gracePeriod = in.readInt();
        capacity = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        charger_sr_no = in.readString();
        serial_no = in.readString();

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
        parcel.writeInt(id);
        parcel.writeInt(chargerModelId);
        parcel.writeString(chargerModelName);
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(planValidity);
        parcel.writeFloat(mrp);
        parcel.writeFloat(cgst);
        parcel.writeFloat(igst);
        parcel.writeFloat(sgst);
        parcel.writeFloat(mrpInctax);
        parcel.writeString(description);
        parcel.writeString(autoRenew);
        parcel.writeString(status);
        parcel.writeString(createdDate);
        parcel.writeString(createdby);
        parcel.writeString(modifyDate);
        parcel.writeString(modifyby);
        parcel.writeInt(validityInMonths);
        parcel.writeInt(gracePeriod);
        parcel.writeString(capacity);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(charger_sr_no);
        parcel.writeString(serial_no);

    }


}