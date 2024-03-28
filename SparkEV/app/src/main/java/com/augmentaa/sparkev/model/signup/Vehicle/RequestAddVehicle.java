package com.augmentaa.sparkev.model.signup.Vehicle;

public class RequestAddVehicle {

    public int user_id;
    public int brand_id;
    public String chassis_no;
    public int connector_type_id;
    public int created_by;
    public String engine_no;
    public int is_default;
    public int model_id;
    public String registration_no;
    public String status;
    public String vin_no;
    public String year_of_manufacture;
    public int modify_by;
    public int id;

    public RequestAddVehicle(int brand_id, int model_id, int connector_type_id, String registration_no, String year_of_manufacture, String engine_no, String chassis_no, String vin_no, String status, int created_by, int is_default, int user_id) {
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
        this.user_id = user_id;
    }

    public RequestAddVehicle(int brand_id, int model_id, int connector_type_id, String registration_no, String year_of_manufacture, String engine_no, String chassis_no, String vin_no, String status, int modify_by, int is_default, int id, int user_id) {
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
        this.user_id = user_id;
    }


}
