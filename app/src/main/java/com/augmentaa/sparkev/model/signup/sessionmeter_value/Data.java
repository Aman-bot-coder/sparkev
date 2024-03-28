package com.augmentaa.sparkev.model.signup.sessionmeter_value;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("action")
    @Expose
    public String action;
    @SerializedName("message_id")
    @Expose
    public String messageId;
    @SerializedName("message_code")
    @Expose
    public String messageCode;
    @SerializedName("charger_id")
    @Expose
    public String chargerId;
    @SerializedName("idtag")
    @Expose
    public String idtag;
    @SerializedName("connector_id")
    @Expose
    public String connectorId;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("errorcode")
    @Expose
    public String errorcode;
    @SerializedName("meter_value")
    @Expose
    public String value;
    @SerializedName("context")
    @Expose
    public String context;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("measurand")
    @Expose
    public String measurand;
    @SerializedName("phase")
    @Expose
    public String phase;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("unit")
    @Expose
    public String unit;
    @SerializedName("meter_start")
    @Expose
    public String meterStart;
    @SerializedName("meter_stop")
    @Expose
    public String meterStop;
    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("value_timestamp")
    @Expose
    public String valueTimestamp;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("start_value")
    @Expose
    public String startValue;
    @SerializedName("stop_time")
    @Expose
    public String stopTime;
    @SerializedName("stop_value")
    @Expose
    public String stopValue;
    @SerializedName("meter_reading")
    @Expose
    public String meterReading;
    @SerializedName("energy_consumed")
    @Expose
    public String energyConsumed;
    @SerializedName("initial_soc")
    @Expose
    public String initialSoc;
    @SerializedName("final_soc")
    @Expose
    public String finalSoc;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("auth_status")
    @Expose
    public String authStatus;
    @SerializedName("currenttime")
    @Expose
    public String currenttime;
    @SerializedName("created_on")
    @Expose
    public String createdOn;
    @SerializedName("active_transaction_status")
    @Expose
    public String activeTransactionStatus;
    @SerializedName("modify_date")
    @Expose
    public String modifyDate;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.action = ((String) in.readValue((String.class.getClassLoader())));
        this.messageId = ((String) in.readValue((String.class.getClassLoader())));
        this.messageCode = ((String) in.readValue((String.class.getClassLoader())));
        this.chargerId = ((String) in.readValue((String.class.getClassLoader())));
        this.idtag = ((String) in.readValue((String.class.getClassLoader())));
        this.connectorId = ((String) in.readValue((String.class.getClassLoader())));
        this.transactionId = ((String) in.readValue((String.class.getClassLoader())));
        this.errorcode = ((String) in.readValue((String.class.getClassLoader())));
        this.value = ((String) in.readValue((String.class.getClassLoader())));
        this.context = ((String) in.readValue((String.class.getClassLoader())));
        this.format = ((String) in.readValue((String.class.getClassLoader())));
        this.measurand = ((String) in.readValue((String.class.getClassLoader())));
        this.phase = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.unit = ((String) in.readValue((String.class.getClassLoader())));
        this.meterStart = ((String) in.readValue((String.class.getClassLoader())));
        this.meterStop = ((String) in.readValue((String.class.getClassLoader())));
        this.reason = ((String) in.readValue((String.class.getClassLoader())));
        this.valueTimestamp = ((String) in.readValue((String.class.getClassLoader())));
        this.startTime = ((String) in.readValue((String.class.getClassLoader())));
        this.startValue = ((String) in.readValue((String.class.getClassLoader())));
        this.stopTime = ((String) in.readValue((String.class.getClassLoader())));
        this.stopValue = ((String) in.readValue((String.class.getClassLoader())));
        this.meterReading = ((String) in.readValue((String.class.getClassLoader())));
        this.energyConsumed = ((String) in.readValue((String.class.getClassLoader())));
        this.initialSoc = ((String) in.readValue((String.class.getClassLoader())));
        this.finalSoc = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((Object.class.getClassLoader())));
        this.authStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.currenttime = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.activeTransactionStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.modifyDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(action);
        dest.writeValue(messageId);
        dest.writeValue(messageCode);
        dest.writeValue(chargerId);
        dest.writeValue(idtag);
        dest.writeValue(connectorId);
        dest.writeValue(transactionId);
        dest.writeValue(errorcode);
        dest.writeValue(value);
        dest.writeValue(context);
        dest.writeValue(format);
        dest.writeValue(measurand);
        dest.writeValue(phase);
        dest.writeValue(location);
        dest.writeValue(unit);
        dest.writeValue(meterStart);
        dest.writeValue(meterStop);
        dest.writeValue(reason);
        dest.writeValue(valueTimestamp);
        dest.writeValue(startTime);
        dest.writeValue(startValue);
        dest.writeValue(stopTime);
        dest.writeValue(stopValue);
        dest.writeValue(meterReading);
        dest.writeValue(energyConsumed);
        dest.writeValue(initialSoc);
        dest.writeValue(finalSoc);
        dest.writeValue(duration);
        dest.writeValue(authStatus);
        dest.writeValue(currenttime);
        dest.writeValue(createdOn);
        dest.writeValue(activeTransactionStatus);
        dest.writeValue(modifyDate);
    }

    public int describeContents() {
        return 0;
    }

}