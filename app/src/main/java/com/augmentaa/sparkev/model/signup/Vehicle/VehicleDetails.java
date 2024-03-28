package com.augmentaa.sparkev.model.signup.Vehicle;

public class VehicleDetails {
    String vehicleID;

    public VehicleDetails(String vehicleResNo, String ownerType, String date_time) {
        this.vehicleResNo = vehicleResNo;
        this.ownerType = ownerType;
        this.date_time=date_time;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleManufactureName() {
        return vehicleManufactureName;
    }

    public void setVehicleManufactureName(String vehicleManufactureName) {
        this.vehicleManufactureName = vehicleManufactureName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleVariant() {
        return vehicleVariant;
    }

    public void setVehicleVariant(String vehicleVariant) {
        this.vehicleVariant = vehicleVariant;
    }

    public String getVehicleResNo() {
        return vehicleResNo;
    }

    public void setVehicleResNo(String vehicleResNo) {
        this.vehicleResNo = vehicleResNo;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    String vehicleName;
    String vehicleManufactureName;
    String vehicleType;
    String vehicleModel;
    String vehicleVariant;
    String vehicleResNo;
    String ownerType;

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    String date_time;
}
