package com.augmentaa.sparkev.model.signup.data_transfer;

public class RequestCurrent {


    public RequestCurrent(String command, String child_command, String charger_id, String charger_sr_no, String vendor_id, int connector, String data) {
        this.command = command;
        this.child_command = child_command;
        this.charger_id = charger_id;
        this.charger_sr_no = charger_sr_no;
        this.vendor_id = vendor_id;
        this.connector = connector;
        this.data = data;
    }

    public String command;
    public String child_command;
    public String charger_id;
    public String charger_sr_no;
    public String vendor_id;
    public int connector;
    public String data;
}
