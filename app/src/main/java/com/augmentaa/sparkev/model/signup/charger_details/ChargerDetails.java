package com.augmentaa.sparkev.model.signup.charger_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChargerDetails implements Parcelable
{

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("serial_no")
    @Expose
    public String serial_no;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("batch_id")
    @Expose
    public String batch_id;

    @SerializedName("station_id")
    @Expose
    public String station_id;

    @SerializedName("station_name")
    @Expose
    public String station_name;

    @SerializedName("current_version_id")
    @Expose
    public String current_version_id;

    @SerializedName("no_of_guns")
    @Expose
    public String no_of_guns;

    @SerializedName("Address")
    @Expose
    public String Address;

    @SerializedName("Lat")
    @Expose
    public String Lat;

    @SerializedName("Lng")
    @Expose
    public String Lng;

    @SerializedName("OTA_Config")
    @Expose
    public String OTA_Config;

    @SerializedName("Periodic_Check_Ref_Time")
    @Expose
    public String Periodic_Check_Ref_Time;

    @SerializedName("Periodicity_in_hours")
    @Expose
    public String Periodicity_in_hours;

    @SerializedName("When_to_Upgrade")
    @Expose
    public String When_to_Upgrade;

    @SerializedName("Upgrade_Specific_Time")
    @Expose
    public String Upgrade_Specific_Time;

    @SerializedName("is_available")
    @Expose
    public String is_available;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("created_date")
    @Expose
    public String created_date;

    @SerializedName("createdby")
    @Expose
    public String createdby;

    @SerializedName("modifyby")
    @Expose
    public String modifyby;

    @SerializedName("modify_date")
    @Expose
    public String modify_date;

    @SerializedName("power_capacity")
    @Expose
    public String power_capacity;

    @SerializedName("charger_type")
    @Expose
    public String charger_type;

    @SerializedName("connector_type")
    @Expose
    public String connector_type;

    @SerializedName("charging_station_name")
    @Expose
    public String charging_station_name;

    @SerializedName("charger_batch_name")
    @Expose
    public String charger_batch_name;

    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("charger_display_id")
    @Expose
    String charger_display_id;

    @SerializedName("map_id")
    @Expose
    public String map_id;

    @SerializedName("connector_data")
    @Expose
    public List <ConnectorData>connector_data;

    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("address2")
    @Expose
    public String address2;

    @SerializedName("PIN")
    @Expose
    public String PIN;
    @SerializedName("landmark")
    @Expose
    public String landmark;
    @SerializedName("city_id")
    @Expose
    public String city_id;
    @SerializedName("state_id")
    @Expose
    public String state_id;
    @SerializedName("country_id")
    @Expose
    public String country_id;

    @SerializedName("city_name")
    @Expose
    public String city_name;
    @SerializedName("state_name")
    @Expose
    public String state_name;
    @SerializedName("country_name")
    @Expose
    public String country_name;

    @SerializedName("electricity_line")
    @Expose
    public String electricity_line;
    @SerializedName("charger_status")
    @Expose
    public String charger_status;
    public String registration_no;
    public String vehicle_id;
    public String station_mobile;
    public String station_email;

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getConnector_no() {
        return connector_no;
    }

    public void setConnector_no(String connector_no) {
        this.connector_no = connector_no;
    }

    public String getConnector_type_name() {
        return connector_type_name;
    }

    public void setConnector_type_name(String connector_type_name) {
        this.connector_type_name = connector_type_name;
    }

    public String getConnector_id() {
        return connector_id;
    }

    public void setConnector_id(String connector_id) {
        this.connector_id = connector_id;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getCurrent_type_name() {
        return current_type_name;
    }

    public void setCurrent_type_name(String current_type_name) {
        this.current_type_name = current_type_name;
    }

    public String getConnector_type_id() {
        return connector_type_id;
    }

    public void setConnector_type_id(String connector_type_id) {
        this.connector_type_id = connector_type_id;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(String locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    public String getCpoName() {
        return cpoName;
    }

    public void setCpoName(String cpoName) {
        this.cpoName = cpoName;
    }

    public String getCpoID() {
        return cpoID;
    }

    public void setCpoID(String cpoID) {
        this.cpoID = cpoID;
    }

    public String connector_no;
    public String connector_type_name;
    public String connector_id;
    public String power;
    public String current_type_name;
    public String connector_type_id;
    public String o_time;
    public String c_time;
    public String address;
    public String locationType;
    public String locationTypeId;
    public String cpoName;
    public String cpoID;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getCurrent_version_id() {
        return current_version_id;
    }

    public void setCurrent_version_id(String current_version_id) {
        this.current_version_id = current_version_id;
    }

    public String getNo_of_guns() {
        return no_of_guns;
    }

    public void setNo_of_guns(String no_of_guns) {
        this.no_of_guns = no_of_guns;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOTA_Config() {
        return OTA_Config;
    }

    public void setOTA_Config(String OTA_Config) {
        this.OTA_Config = OTA_Config;
    }

    public String getPeriodic_Check_Ref_Time() {
        return Periodic_Check_Ref_Time;
    }

    public void setPeriodic_Check_Ref_Time(String periodic_Check_Ref_Time) {
        Periodic_Check_Ref_Time = periodic_Check_Ref_Time;
    }

    public String getPeriodicity_in_hours() {
        return Periodicity_in_hours;
    }

    public void setPeriodicity_in_hours(String periodicity_in_hours) {
        Periodicity_in_hours = periodicity_in_hours;
    }

    public String getWhen_to_Upgrade() {
        return When_to_Upgrade;
    }

    public void setWhen_to_Upgrade(String when_to_Upgrade) {
        When_to_Upgrade = when_to_Upgrade;
    }

    public String getUpgrade_Specific_Time() {
        return Upgrade_Specific_Time;
    }

    public void setUpgrade_Specific_Time(String upgrade_Specific_Time) {
        Upgrade_Specific_Time = upgrade_Specific_Time;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifyby() {
        return modifyby;
    }

    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }

    public String getPower_capacity() {
        return power_capacity;
    }

    public void setPower_capacity(String power_capacity) {
        this.power_capacity = power_capacity;
    }

    public String getCharger_type() {
        return charger_type;
    }

    public void setCharger_type(String charger_type) {
        this.charger_type = charger_type;
    }

    public String getConnector_type() {
        return connector_type;
    }

    public void setConnector_type(String connector_type) {
        this.connector_type = connector_type;
    }

    public String getCharging_station_name() {
        return charging_station_name;
    }

    public void setCharging_station_name(String charging_station_name) {
        this.charging_station_name = charging_station_name;
    }

    public String getCharger_batch_name() {
        return charger_batch_name;
    }

    public void setCharger_batch_name(String charger_batch_name) {
        this.charger_batch_name = charger_batch_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCharger_display_id() {
        return charger_display_id;
    }

    public void setCharger_display_id(String charger_display_id) {
        this.charger_display_id = charger_display_id;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public List<ConnectorData> getConnector_data() {
        return connector_data;
    }

    public void setConnector_data(List<ConnectorData> connector_data) {
        this.connector_data = connector_data;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getElectricity_line() {
        return electricity_line;
    }

    public void setElectricity_line(String electricity_line) {
        this.electricity_line = electricity_line;
    }

    public String getCharger_status() {
        return charger_status;
    }

    public void setCharger_status(String charger_status) {
        this.charger_status = charger_status;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public boolean isFav;


    public final static Creator<ChargerDetails> CREATOR = new Creator<ChargerDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChargerDetails createFromParcel(Parcel in) {
            return new ChargerDetails(in);
        }

        public ChargerDetails[] newArray(int size) {
            return (new ChargerDetails[size]);
        }

    };

    public ChargerDetails(String id, String name, String connector_type_name) {
        this.id = id;
        this.name = name;
        this.connector_type_name = connector_type_name;
    }

    protected ChargerDetails(Parcel in) {

        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.serial_no = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.station_name = ((String) in.readValue((String.class.getClassLoader())));
        this.batch_id = ((String) in.readValue((String.class.getClassLoader())));
        this.station_id = ((String) in.readValue((String.class.getClassLoader())));
        this.current_version_id = ((String) in.readValue((String.class.getClassLoader())));
        this.no_of_guns = ((String) in.readValue((String.class.getClassLoader())));
        this.Address = ((String) in.readValue((String.class.getClassLoader())));
        this.Lat = ((String) in.readValue((String.class.getClassLoader())));
        this.Lng = ((String) in.readValue((String.class.getClassLoader())));
        this.OTA_Config = ((String) in.readValue((String.class.getClassLoader())));
        this.Periodic_Check_Ref_Time = ((String) in.readValue((String.class.getClassLoader())));
        this.Periodicity_in_hours = ((String) in.readValue((String.class.getClassLoader())));
        this.When_to_Upgrade = ((String) in.readValue((String.class.getClassLoader())));
        this.Upgrade_Specific_Time = ((String) in.readValue((String.class.getClassLoader())));
        this.is_available = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.created_date = ((String) in.readValue((String.class.getClassLoader())));
        this.createdby = ((String) in.readValue((String.class.getClassLoader())));
        this.modifyby = ((String) in.readValue((String.class.getClassLoader())));
        this.modify_date = ((String) in.readValue((String.class.getClassLoader())));
        this.power_capacity = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type = ((String) in.readValue((String.class.getClassLoader())));
        this.charging_station_name = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_type = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_batch_name = ((String) in.readValue((String.class.getClassLoader())));
        this.mobile = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_display_id = ((String) in.readValue((String.class.getClassLoader())));
        this.map_id = ((String) in.readValue((String.class.getClassLoader())));
//        this.connector_data=(List<ConnectorData>)in.readValue((List.class.getClassLoader()));
        this.address1 = ((String) in.readValue((String.class.getClassLoader())));
        this.address2 = ((String) in.readValue((String.class.getClassLoader())));
        this.PIN = ((String) in.readValue((String.class.getClassLoader())));
        this.landmark = ((String) in.readValue((String.class.getClassLoader())));
        this.city_id = ((String) in.readValue((String.class.getClassLoader())));
        this.state_id = ((String) in.readValue((String.class.getClassLoader())));
        this.country_id = ((String) in.readValue((String.class.getClassLoader())));
        this.city_name = ((String) in.readValue((String.class.getClassLoader())));
        this.state_name = ((String) in.readValue((String.class.getClassLoader())));
        this.country_name = ((String) in.readValue((String.class.getClassLoader())));
        this.electricity_line = ((String) in.readValue((String.class.getClassLoader())));
        this.charger_status = ((String) in.readValue((String.class.getClassLoader())));

        this.registration_no = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicle_id = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_no = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type_name = ((String) in.readValue((String.class.getClassLoader())));
        this.power = ((String) in.readValue((String.class.getClassLoader())));
        this.current_type_name = ((String) in.readValue((String.class.getClassLoader())));
        this.o_time = ((String) in.readValue((String.class.getClassLoader())));
        this.c_time = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_id = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type_id= ((String) in.readValue((String.class.getClassLoader())));
        this.address= ((String) in.readValue((String.class.getClassLoader())));
        this.locationType= ((String) in.readValue((String.class.getClassLoader())));
        this.locationTypeId= ((String) in.readValue((String.class.getClassLoader())));
        this.cpoID= ((String) in.readValue((String.class.getClassLoader())));
        this.cpoName= ((String) in.readValue((String.class.getClassLoader())));





    }

    public String getO_time() {
        return o_time;
    }

    public void setO_time(String o_time) {
        this.o_time = o_time;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(serial_no);
        dest.writeValue(name);
        dest.writeValue(station_name);
        dest.writeValue(batch_id);
        dest.writeValue(station_id);
        dest.writeValue(current_version_id);
        dest.writeValue(no_of_guns);
        dest.writeValue(Address);
        dest.writeValue(Lat);
        dest.writeValue(Lng);
        dest.writeValue(OTA_Config);
        dest.writeValue(Periodic_Check_Ref_Time);
        dest.writeValue(Periodicity_in_hours);
        dest.writeValue(When_to_Upgrade);
        dest.writeValue(Upgrade_Specific_Time);
        dest.writeValue(is_available);
        dest.writeValue(status);
        dest.writeValue(created_date);
        dest.writeValue(createdby);
        dest.writeValue(modifyby);
        dest.writeValue(modify_date);
        dest.writeValue(power_capacity);
        dest.writeValue(connector_type);
        dest.writeValue(charging_station_name);
        dest.writeValue(charger_type);
        dest.writeValue(charger_batch_name);
        dest.writeValue(mobile);
        dest.writeValue(charger_display_id);
        dest.writeValue(map_id);
        dest.writeValue(address1);
        dest.writeValue(address2);
        dest.writeValue(PIN);
        dest.writeValue(landmark);
        dest.writeValue(city_id);
        dest.writeValue(state_id);
        dest.writeValue(country_id);
        dest.writeValue(city_name);
        dest.writeValue(state_name);
        dest.writeValue(country_name);
        dest.writeValue(electricity_line);
        dest.writeValue(charger_status);
        dest.writeValue(registration_no);
        dest.writeValue(vehicle_id);
        dest.writeValue(connector_no);
        dest.writeValue(connector_type_name);
        dest.writeValue(power);
        dest.writeValue(current_type_name);
        dest.writeValue(o_time);
        dest.writeValue(c_time);
        dest.writeValue(connector_id);
        dest.writeValue(connector_type_id);
        dest.writeValue(address);
        dest.writeValue(locationType);
        dest.writeValue(locationTypeId);
        dest.writeValue(cpoID);
        dest.writeValue(cpoName);

    }

    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "ChargerDetails{" +
                "id='" + id + '\'' +
                ", serial_no='" + serial_no + '\'' +
                ", name='" + name + '\'' +
                ", batch_id='" + batch_id + '\'' +
                ", station_id='" + station_id + '\'' +
                ", station_name='" + station_name + '\'' +
                ", current_version_id='" + current_version_id + '\'' +
                ", no_of_guns='" + no_of_guns + '\'' +
                ", Address='" + Address + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Lng='" + Lng + '\'' +
                ", OTA_Config='" + OTA_Config + '\'' +
                ", Periodic_Check_Ref_Time='" + Periodic_Check_Ref_Time + '\'' +
                ", Periodicity_in_hours='" + Periodicity_in_hours + '\'' +
                ", When_to_Upgrade='" + When_to_Upgrade + '\'' +
                ", Upgrade_Specific_Time='" + Upgrade_Specific_Time + '\'' +
                ", is_available='" + is_available + '\'' +
                ", status='" + status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", createdby='" + createdby + '\'' +
                ", modifyby='" + modifyby + '\'' +
                ", modify_date='" + modify_date + '\'' +
                ", power_capacity='" + power_capacity + '\'' +
                ", charger_type='" + charger_type + '\'' +
                ", connector_type='" + connector_type + '\'' +
                ", charging_station_name='" + charging_station_name + '\'' +
                ", charger_batch_name='" + charger_batch_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", charger_display_id='" + charger_display_id + '\'' +
                ", map_id='" + map_id + '\'' +
                ", connector_data=" + connector_data +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", PIN='" + PIN + '\'' +
                ", landmark='" + landmark + '\'' +
                ", city_id='" + city_id + '\'' +
                ", state_id='" + state_id + '\'' +
                ", country_id='" + country_id + '\'' +
                ", city_name='" + city_name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", electricity_line='" + electricity_line + '\'' +
                ", charger_status='" + charger_status + '\'' +
                ", registration_no='" + registration_no + '\'' +
                ", vehicle_id='" + vehicle_id + '\'' +
                ", connector_no='" + connector_no + '\'' +
                ", connector_type_name='" + connector_type_name + '\'' +
                ", connector_id='" + connector_id + '\'' +
                ", power='" + power + '\'' +
                ", current_type_name='" + current_type_name + '\'' +
                ", connector_type_id='" + connector_type_id + '\'' +
                ", o_time='" + o_time + '\'' +
                ", c_time='" + c_time + '\'' +
                ", address='" + address + '\'' +
                ", locationType='" + locationType + '\'' +
                ", locationTypeId='" + locationTypeId + '\'' +
                ", cpoName='" + cpoName + '\'' +
                ", cpoID='" + cpoID + '\'' +
                ", isFav=" + isFav +
                '}';
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }
}