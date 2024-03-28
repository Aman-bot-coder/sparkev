package com.augmentaa.sparkev.model.signup.remote_start_stop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestChargerStartStop implements Parcelable {

    @SerializedName("command")
    @Expose
    public String command;
    @SerializedName("charger_id")

    @Expose
    public String charger_id;
    @SerializedName("charger_sr_no")

    @Expose
    public String charger_sr_no;
    @SerializedName("connector")

    @Expose
    public int connector;


    @SerializedName("id_tag")
    @Expose
    String id_tag;

    @SerializedName("transaction_id")
    @Expose
    String transaction_id;

    @SerializedName("user_id")
    @Expose
    int user_id;

    @SerializedName("os_version")
    @Expose
    String os_version;

    @SerializedName("command_source")
    @Expose
    String command_source;

    @SerializedName("device_id")
    @Expose
    String device_id;

    @SerializedName("app_version")
    @Expose
    String app_version;

    @SerializedName("station_id")
    @Expose
    String station_id;

    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    int vehicle_id;
    String vehicle_number;
    public String id_tag_type;
    public String booking_id;



    public final static Creator<RequestChargerStartStop> CREATOR = new Creator<RequestChargerStartStop>() {
        @SuppressWarnings({
                "unchecked"
        })
        public RequestChargerStartStop createFromParcel(Parcel in) {
            return new RequestChargerStartStop(in);
        }

        public RequestChargerStartStop[] newArray(int size) {
            return (new RequestChargerStartStop[size]);
        }

    };

    protected RequestChargerStartStop(Parcel in) {
        this.command = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_id = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_sr_no = ((String) in.readValue((String.class.getClassLoader())));
        this.connector = ((int) in.readValue((String.class.getClassLoader())));
        this.id_tag = ((String) in.readValue((String.class.getClassLoader())));
        this.transaction_id = ((String) in.readValue((String.class.getClassLoader())));

        this.user_id = ((int) in.readValue((String.class.getClassLoader())));
        this.os_version = ((String) in.readValue((String.class.getClassLoader())));
        this.command_source = ((String) in.readValue((String.class.getClassLoader())));
        this.device_id = ((String) in.readValue((String.class.getClassLoader())));
        this.app_version = ((String) in.readValue((String.class.getClassLoader())));
        this.station_id = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RequestChargerStartStop() {
    }



    public RequestChargerStartStop(String command, String charger_id, String charger_sr_no, int connector, String id_tag, String transaction_id, int user_id, String os_version, String command_source,
                                   String device_id, String app_version, String station_id, String mobile_no, int vehicle_id, String
            vehicle_number, String id_tag_type) {
        this.command = command;
        this.charger_id = charger_id;
        this.charger_sr_no = charger_sr_no;
        this.connector = connector;
        this.id_tag = id_tag;
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.os_version = os_version;
        this.command_source = command_source;
        this.device_id = device_id;
        this.app_version = app_version;
        this.station_id = station_id;
        this.mobile_no = mobile_no;
        this.vehicle_id = vehicle_id;
        this.vehicle_number = vehicle_number;
        this.id_tag_type=id_tag_type;
        this.booking_id=booking_id;
    }







    public RequestChargerStartStop(String command, String charger_id, String charger_sr_no, int connector, String id_tag, String transaction_id, int user_id, String os_version, String command_source,
                                   String device_id, String app_version, String station_id, String mobile_no, int vehicle_id, String
                                     vehicle_number, String id_tag_type, String booking_id) {
        this.command = command;
        this.charger_id = charger_id;
        this.charger_sr_no = charger_sr_no;
        this.connector = connector;
        this.id_tag = id_tag;
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.os_version = os_version;
        this.command_source = command_source;
        this.device_id = device_id;
        this.app_version = app_version;
        this.station_id = station_id;
        this.mobile_no = mobile_no;
        this.vehicle_id = vehicle_id;
        this.vehicle_number = vehicle_number;
        this.id_tag_type=id_tag_type;
        this.booking_id=booking_id;
    }

    public RequestChargerStartStop(String command, String charger_id, String charger_sr_no, int connector, String id_tag, String transaction_id) {
        this.command = command;
        this.charger_id = charger_id;
        this.charger_sr_no = charger_sr_no;
        this.connector = connector;
        this.id_tag = id_tag;
        this.transaction_id = transaction_id;

    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(command);
        dest.writeValue(charger_id);
        dest.writeValue(charger_sr_no);
        dest.writeValue(connector);
        dest.writeValue(id_tag);
        dest.writeValue(transaction_id);

        dest.writeValue(user_id);
        dest.writeValue(os_version);
        dest.writeValue(command_source);
        dest.writeValue(device_id);
        dest.writeValue(app_version);
        dest.writeValue(station_id);

    }

    public int describeContents() {
        return 0;
    }

}