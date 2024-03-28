package com.augmentaa.sparkev.model.signup.Vehicle;

import android.os.Parcel;
import android.os.Parcelable;

public class AddVehicleDetails implements Parcelable {

    public String message;
    public String status;
    public int is_default;
    String brand_id;
    String model_id;
    String connector_type_id;
    String origin;
    String registration_no;
    String year_of_manufacture;
    String engine_no;
    String brand_name;
    String model_name;
    String connector_type_name;
    String created_date;
//    public Data data;
    String chassis_no;
    String vin_no;
    String created_by;
    public String id;
    public String modify_by;
    public String user_id;



    public static final Creator<AddVehicleDetails> CREATOR = new Creator<AddVehicleDetails>() {
        @Override
        public AddVehicleDetails createFromParcel(Parcel in) {
            return new AddVehicleDetails(in);
        }

        @Override
        public AddVehicleDetails[] newArray(int size) {
            return new AddVehicleDetails[size];
        }
    };

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getConnector_type_id() {
        return connector_type_id;
    }

    public void setConnector_type_id(String connector_type_id) {
        this.connector_type_id = connector_type_id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getYear_of_manufacture() {
        return year_of_manufacture;
    }

    public void setYear_of_manufacture(String year_of_manufacture) {
        this.year_of_manufacture = year_of_manufacture;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getConnector_type_name() {
        return connector_type_name;
    }

    public void setConnector_type_name(String connector_type_name) {
        this.connector_type_name = connector_type_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getChassis_no() {
        return chassis_no;
    }

    public void setChassis_no(String chassis_no) {
        this.chassis_no = chassis_no;
    }

    public String getVin_no() {
        return vin_no;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getModify_by() {
        return modify_by;
    }

    public void setModify_by(String modify_by) {
        this.modify_by = modify_by;
    }


    @Override
    public String toString() {
        return "{" +
                "brand_id='" + brand_id + '\'' +
                ", model_id='" + model_id + '\'' +
                ", connector_type_id='" + connector_type_id + '\'' +
                ", origin='" + origin + '\'' +
                ", registration_no='" + registration_no + '\'' +
                ", year_of_manufacture='" + year_of_manufacture + '\'' +
                ", engine_no='" + engine_no + '\'' +
                ", brand_name='" + brand_name + '\'' +
                ", model_name='" + model_name + '\'' +
                ", connector_type_name='" + connector_type_name + '\'' +
                ", created_date='" + created_date + '\'' +
                ", id='" + id + '\'' +
                ", chassis_no='" + chassis_no + '\'' +
                ", vin_no='" + vin_no + '\'' +
                ", created_by='" + created_by + '\'' +
                '}';
    }

    public AddVehicleDetails(String brand_id, String model_id, String connector_type_id, String registration_no, String year_of_manufacture, String engine_no, String chassis_no, String vin_no, String status, String created_by, int is_default,String user_id) {
        this.brand_id = brand_id;
        this.model_id = model_id;
        this.connector_type_id = connector_type_id;
        this.registration_no = registration_no;
        this.year_of_manufacture = year_of_manufacture;
        this.engine_no = engine_no;
        this.chassis_no = chassis_no;
        this.vin_no = vin_no;
        this.status = status;
        this.created_by = created_by;
        this.is_default = is_default;
        this.user_id=user_id;
    }

    public AddVehicleDetails(String brand_id, String model_id, String connector_type_id, String registration_no, String year_of_manufacture, String engine_no, String chassis_no, String vin_no, String status, String modify_by, int is_default, String id,String user_id) {
        this.brand_id = brand_id;
        this.model_id = model_id;
        this.connector_type_id = connector_type_id;
        this.registration_no = registration_no;
        this.year_of_manufacture = year_of_manufacture;
        this.engine_no = engine_no;
        this.chassis_no = chassis_no;
        this.vin_no = vin_no;
        this.status = status;
        this.modify_by = modify_by;
        this.is_default = is_default;
        this.id = id;
        this.user_id=user_id;
    }

    public AddVehicleDetails() {

    }

    protected AddVehicleDetails(Parcel in) {
        this.is_default = ((int) in.readValue((int.class.getClassLoader())));
        this.brand_id = ((String) in.readValue((String.class.getClassLoader())));
        this.model_id = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type_id = ((String) in.readValue((String.class.getClassLoader())));
        this.origin = ((String) in.readValue((String.class.getClassLoader())));
        this.registration_no = ((String) in.readValue((String.class.getClassLoader())));
        this.year_of_manufacture = ((String) in.readValue((String.class.getClassLoader())));
        this.engine_no = ((String) in.readValue((String.class.getClassLoader())));
        this.brand_name = ((String) in.readValue((String.class.getClassLoader())));
        this.model_name = ((String) in.readValue((String.class.getClassLoader())));
        this.connector_type_name = ((String) in.readValue((String.class.getClassLoader())));
        this.created_date = ((String) in.readValue((String.class.getClassLoader())));
        this.chassis_no = ((String) in.readValue((String.class.getClassLoader())));
        this.vin_no = ((String) in.readValue((String.class.getClassLoader())));
        this.created_by = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.modify_by = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.created_date = ((String) in.readValue((String.class.getClassLoader())));
//        this.data = ((Data) in.readValue((Data.class.getClassLoader())));


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(is_default);
        dest.writeValue(brand_id);
        dest.writeValue(model_id);
        dest.writeValue(connector_type_id);
        dest.writeValue(origin);
        dest.writeValue(registration_no);
        dest.writeValue(year_of_manufacture);
        dest.writeValue(engine_no);
        dest.writeValue(brand_name);
        dest.writeValue(model_name);
        dest.writeValue(connector_type_name);
        dest.writeValue(created_date);
        dest.writeValue(chassis_no);
        dest.writeValue(vin_no);
        dest.writeValue(created_by);
        dest.writeValue(id);
        dest.writeValue(modify_by);
        dest.writeValue(status);
        dest.writeValue(created_date);
//        dest.writeValue(data);

    }
}
