package com.augmentaa.sparkev.model.signup.warranty_status_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
@SerializedName("id")
@Expose
public int id;
@SerializedName("user_id")
@Expose
public int userId;
@SerializedName("activity_type")
@Expose
public String activityType;
@SerializedName("charger_id")
@Expose
public String chargerId;
@SerializedName("activity_id")
@Expose
public int activityId;
@SerializedName("start_date")
@Expose
public String startDate;
@SerializedName("end_date")
@Expose
public String endDate;
@SerializedName("amount_paid")
@Expose
public float amountPaid;
@SerializedName("transaction_id")
@Expose
public String transactionId;
@SerializedName("transaction_status")
@Expose
public String transactionStatus;
@SerializedName("status")
@Expose
public String status;
@SerializedName("remarks")
@Expose
public String remarks;
@SerializedName("created_date")
@Expose
public String createdDate;
@SerializedName("createdby")
@Expose
public String createdby;
@SerializedName("modify_date")
@Expose
public String modifyDate;
@SerializedName("modifyby")
@Expose
public String modifyby;
@SerializedName("invoice_path")
@Expose
public String invoicePath;
@SerializedName("charger_model_id")
@Expose
public int chargerModelId;
@SerializedName("charger_model_name")
@Expose
public String chargerModelName;
@SerializedName("name")
@Expose
public String name;
@SerializedName("code")
@Expose
public String code;
@SerializedName("plan_validity")
@Expose
public String planValidity;
@SerializedName("mrp")
@Expose
public float mrp;
@SerializedName("cgst")
@Expose
public float cgst;
@SerializedName("igst")
@Expose
public String igst;
@SerializedName("sgst")
@Expose
public float sgst;
@SerializedName("mrp_inctax")
@Expose
public float mrpInctax;
@SerializedName("description")
@Expose
public String description;
@SerializedName("auto_renew")
@Expose
public String autoRenew;
@SerializedName("validity_in_months")
@Expose
public int validityInMonths;
@SerializedName("grace_period")
@Expose
public int gracePeriod;

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityType='" + activityType + '\'' +
                ", chargerId='" + chargerId + '\'' +
                ", activityId=" + activityId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", amountPaid=" + amountPaid +
                ", transactionId='" + transactionId + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdby='" + createdby + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", modifyby='" + modifyby + '\'' +
                ", invoicePath='" + invoicePath + '\'' +
                ", chargerModelId=" + chargerModelId +
                ", chargerModelName='" + chargerModelName + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", planValidity='" + planValidity + '\'' +
                ", mrp=" + mrp +
                ", cgst=" + cgst +
                ", igst='" + igst + '\'' +
                ", sgst=" + sgst +
                ", mrpInctax=" + mrpInctax +
                ", description='" + description + '\'' +
                ", autoRenew='" + autoRenew + '\'' +
                ", validityInMonths=" + validityInMonths +
                ", gracePeriod=" + gracePeriod +
                '}';
    }
}