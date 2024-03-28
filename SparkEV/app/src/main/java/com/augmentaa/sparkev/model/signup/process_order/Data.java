package com.augmentaa.sparkev.model.signup.process_order;

public class Data {

    public int os_id;
    public String charger_transaction_id;
    public int station_id;
    public String charger_id;
    public int user_id;
    public String user_mobile;
    public float energy_consumped;
    public float price_per_unit;
    public float bill_amount;
    public float tax_cgst;
    public float tax_sgst;
    public float total_amount;
    public float penalty_amount;
    public float reward_amount;
    public float payable_amount;
    public float actual_amount_paid;
    public String payment_status;
    public String payment_mode;
    public String invoice_path;
    public String os_status;
    public String created_date;
    public int created_by;
    public String modified_date;
    public int modified_by;
    public String meter_start_value;
    public String meter_stop_value;
    public String meter_start_time;

    @Override
    public String toString() {
        return "Data{" +
                "os_id=" + os_id +
                ", charger_transaction_id='" + charger_transaction_id + '\'' +
                ", station_id=" + station_id +
                ", charger_id='" + charger_id + '\'' +
                ", user_id=" + user_id +
                ", user_mobile='" + user_mobile + '\'' +
                ", energy_consumped=" + energy_consumped +
                ", price_per_unit=" + price_per_unit +
                ", bill_amount=" + bill_amount +
                ", tax_cgst=" + tax_cgst +
                ", tax_sgst=" + tax_sgst +
                ", total_amount=" + total_amount +
                ", penalty_amount=" + penalty_amount +
                ", reward_amount=" + reward_amount +
                ", payable_amount=" + payable_amount +
                ", actual_amount_paid=" + actual_amount_paid +
                ", payment_status='" + payment_status + '\'' +
                ", payment_mode='" + payment_mode + '\'' +
                ", invoice_path='" + invoice_path + '\'' +
                ", os_status='" + os_status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", created_by=" + created_by +
                ", modified_date='" + modified_date + '\'' +
                ", modified_by=" + modified_by +
                ", meter_start_value='" + meter_start_value + '\'' +
                ", meter_stop_value='" + meter_stop_value + '\'' +
                ", meter_start_time='" + meter_start_time + '\'' +
                ", meter_stop_time='" + meter_stop_time + '\'' +
                ", duration=" + duration +
                ", vehicle_number='" + vehicle_number + '\'' +
                ", mobile='" + mobile + '\'' +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", statio_name='" + statio_name + '\'' +
                ", cpo_name='" + cpo_name + '\'' +
                ", connector_type='" + connector_type + '\'' +
                ", conn_no=" + connector_no +
                '}';
    }

    public String meter_stop_time;
    public float duration;
    public String vehicle_number;
    public String mobile;
    public String user_name;
    public String email;
    public String statio_name;
    public String cpo_name;
    public String connector_type;
    public int connector_no;

}
