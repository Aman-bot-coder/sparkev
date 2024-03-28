package com.augmentaa.sparkev.model.signup.session_logs_ble;

import java.util.List;

public class InsertSessionLogsBLE {

    public String charger_serial_no;
    public int user_id;
    public int vehicle_id;
    public int created_by;
    public String status;
    public List<Data> session_history;


    public InsertSessionLogsBLE(String charger_serial_no, int user_id, int vehicle_id, int created_by, String status, List<Data> session_history) {
        this.charger_serial_no = charger_serial_no;
        this.user_id = user_id;
        this.vehicle_id = vehicle_id;
        this.created_by = created_by;
        this.status = status;
        this.session_history = session_history;
    }
}
