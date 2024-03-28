package com.augmentaa.sparkev.model.signup.payment_status;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    @Override
    public String toString() {
        return "Data{" +
                "user_id=" + user_id +
                ", activity_type='" + activity_type + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", amount_paid=" + amount_paid +
                ", transaction_id='" + transaction_id + '\'' +
                ", receipt_no='" + receipt_no + '\'' +
                ", status='" + status + '\'' +
                ", charger_model_name='" + charger_model_name + '\'' +
                ", plan_validity='" + plan_validity + '\'' +
                ", mrp=" + mrp +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", mrp_inctax=" + mrp_inctax +
                ", auto_renew='" + auto_renew + '\'' +
                ", pg_bank_name='" + pg_bank_name + '\'' +
                ", billing_address='" + billing_address + '\'' +
                ", invoice_path='" + invoice_path + '\'' +
                '}';
    }

    public int user_id;
    public String activity_type;
    public String start_date;
    public String end_date;
    public float amount_paid;
    public String transaction_id;
    public String receipt_no;
    public String status;
    public String charger_model_name;
    public String plan_validity;
    public float mrp;
    public float cgst;
    public float sgst;
    public float mrp_inctax;
    public String auto_renew;
    public String pg_bank_name;
    public String billing_address;
    public String invoice_path;


    protected Data(Parcel in) {
        user_id = in.readInt();
        activity_type = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        amount_paid = in.readFloat();
        transaction_id = in.readString();
        receipt_no = in.readString();
        status = in.readString();
        charger_model_name = in.readString();
        plan_validity = in.readString();
        mrp = in.readFloat();
        cgst = in.readFloat();
        sgst = in.readFloat();
        mrp_inctax = in.readFloat();
        auto_renew = in.readString();
        pg_bank_name = in.readString();
        billing_address = in.readString();
        invoice_path = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(user_id);
        parcel.writeString(activity_type);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeFloat(amount_paid);
        parcel.writeString(transaction_id);
        parcel.writeString(receipt_no);
        parcel.writeString(status);
        parcel.writeString(charger_model_name);
        parcel.writeString(plan_validity);
        parcel.writeFloat(mrp);
        parcel.writeFloat(cgst);
        parcel.writeFloat(sgst);
        parcel.writeFloat(mrp_inctax);
        parcel.writeString(auto_renew);
        parcel.writeString(pg_bank_name);
        parcel.writeString(billing_address);
        parcel.writeString(invoice_path);
    }
}
