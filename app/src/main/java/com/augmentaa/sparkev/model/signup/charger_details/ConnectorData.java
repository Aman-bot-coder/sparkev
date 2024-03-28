package com.augmentaa.sparkev.model.signup.charger_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectorData implements Parcelable {
    @SerializedName("map_id")
    @Expose
    public String map_id;
    @SerializedName("charger_id")
    @Expose
    public String charger_id;
    @SerializedName("connector_no")
    @Expose
    public String connector_no;

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    @SerializedName("connector_type_id")
    @Expose
    public String connectorTypeId;

    @SerializedName("current_status")
    @Expose
    public String current_status;

    @SerializedName("current_status_date")
    @Expose
    public String current_status_date;

    @SerializedName("charger_display_id")
    @Expose
    public String charger_display_id;



    @SerializedName("connector_type_name")
    @Expose
    public String connector_type_name;
    @SerializedName("model_id")
    @Expose
    public String modelId;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("io_type_id")
    @Expose
    public String io_type_id;
    @SerializedName("io_type_name")
    @Expose
    public String ioTypeName;
    @SerializedName("current_type_id")
    @Expose
    public String currentTypeId;
    @SerializedName("current_type_name")
    @Expose
    public String currentTypeName;
    @SerializedName("voltage")
    @Expose
    public String voltage;
    @SerializedName("phase")
    @Expose
    public String phase;
    @SerializedName("max_amp")
    @Expose
    public String max_amp;
    @SerializedName("power")
    @Expose
    public String power;
    @SerializedName("frequency")
    @Expose
    public String frequency;
    @SerializedName("status")
    @Expose
    public String status;
    public final static Creator<ConnectorData> CREATOR = new Creator<ConnectorData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ConnectorData createFromParcel(Parcel in) {
            return new ConnectorData(in);
        }

        public ConnectorData[] newArray(int size) {
            return (new ConnectorData[size]);
        }

    }
            ;

    protected ConnectorData(Parcel in) {
        this.map_id = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_id = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_no = ((String) in.readValue((String.class.getClassLoader())));
        this.connectorTypeId = ((String) in.readValue((String.class.getClassLoader())));
        this.current_status = ((String) in.readValue((String.class.getClassLoader())));
        this.current_status_date = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_display_id = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type_name = ((String) in.readValue((String.class.getClassLoader())));
        this.modelId = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.io_type_id = ((String) in.readValue((String.class.getClassLoader())));
        this.ioTypeName = ((String) in.readValue((String.class.getClassLoader())));
        this.currentTypeId = ((String) in.readValue((String.class.getClassLoader())));
        this.currentTypeName = ((String) in.readValue((String.class.getClassLoader())));
        this.voltage = ((String) in.readValue((String.class.getClassLoader())));
        this.phase = ((String) in.readValue((String.class.getClassLoader())));
        this.max_amp = ((String) in.readValue((int.class.getClassLoader())));
        this.power = ((String) in.readValue((String.class.getClassLoader())));
        this.frequency = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ConnectorData() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(map_id);
        dest.writeValue(charger_id);
        dest.writeValue(connector_no);
        dest.writeValue(connectorTypeId);
        dest.writeValue(current_status);
        dest.writeValue(current_status_date);
        dest.writeValue(charger_display_id);
        dest.writeValue(connector_type_name);
        dest.writeValue(modelId);
        dest.writeValue(id);
        dest.writeValue(io_type_id);
        dest.writeValue(ioTypeName);
        dest.writeValue(currentTypeId);
        dest.writeValue(currentTypeName);
        dest.writeValue(voltage);
        dest.writeValue(phase);
        dest.writeValue(max_amp);
        dest.writeValue(power);
        dest.writeValue(frequency);
        dest.writeValue(status);
    }

    @Override
    public String toString() {
        return "ConnectorData{" +
                "map_id='" + map_id + '\'' +
                ", charger_id='" + charger_id + '\'' +
                ", connector_no='" + connector_no + '\'' +
                ", connectorTypeId='" + connectorTypeId + '\'' +
                ", current_status='" + current_status + '\'' +
                ", current_status_date='" + current_status_date + '\'' +
                ", charger_display_id='" + charger_display_id + '\'' +
                ", connector_type_name='" + connector_type_name + '\'' +
                ", modelId='" + modelId + '\'' +
                ", id='" + id + '\'' +
                ", io_type_id='" + io_type_id + '\'' +
                ", ioTypeName='" + ioTypeName + '\'' +
                ", currentTypeId='" + currentTypeId + '\'' +
                ", currentTypeName='" + currentTypeName + '\'' +
                ", voltage='" + voltage + '\'' +
                ", phase='" + phase + '\'' +
                ", max_amp='" + max_amp + '\'' +
                ", power='" + power + '\'' +
                ", frequency='" + frequency + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public int describeContents() {
        return 0;
    }
}
