package com.augmentaa.sparkev.model.signup.charger_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @Override
    public String toString() {
        return "Data{" +
                "mapId=" + mapId +
                ", userId=" + userId +
                ", userFName='" + userFName + '\'' +
                ", userLName='" + userLName + '\'' +
                ", chargerId=" + chargerId +
                ", chargerDisplayId='" + chargerDisplayId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mapAsChild=" + mapAsChild +
                ", status='" + status + '\'' +
                ", createdby=" + createdby +
                ", createdDate='" + createdDate + '\'' +
                ", clientCertificate='" + clientCertificate + '\'' +
                ", warrantyStatus='" + warrantyStatus + '\'' +
                ", planStatus='" + planStatus + '\'' +
                ", canRenewPlan='" + canRenewPlan + '\'' +
                ", canRenewWarranty='" + canRenewWarranty + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", countryId=" + countryId +
                ", pin=" + pin +
                ", lastPingDatetime='" + lastPingDatetime + '\'' +
                ", cityId=" + cityId +
                ", stateId=" + stateId +
                ", currentFwVersionMainBoard='" + currentFwVersionMainBoard + '\'' +
                ", currentFwVersionIdMainBoard='" + currentFwVersionIdMainBoard + '\'' +
                ", currentFwVersionOcppBoard='" + currentFwVersionOcppBoard + '\'' +
                ", currentFwVersionIdOcppBoard='" + currentFwVersionIdOcppBoard + '\'' +
                ", currentAmpereValue='" + currentAmpereValue + '\'' +
                '}';
    }

    @SerializedName("map_id")
    @Expose
    public int mapId;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("user_f_name")
    @Expose
    public String userFName;
    @SerializedName("user_l_name")
    @Expose
    public String userLName;
    @SerializedName("charger_id")
    @Expose
    public int chargerId;
    @SerializedName("charger_display_id")
    @Expose
    public String chargerDisplayId;
    @SerializedName("serial_no")
    @Expose
    public String serialNo;
    @SerializedName("nick_name")
    @Expose
    public String nickName;
    @SerializedName("map_as_child")
    @Expose
    public int mapAsChild;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("createdby")
    @Expose
    public int createdby;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("client_certificate")
    @Expose
    public String clientCertificate;
    @SerializedName("warranty_status")
    @Expose
    public String warrantyStatus;
    @SerializedName("plan_status")
    @Expose
    public String planStatus;
    @SerializedName("can_renew_plan")
    @Expose
    public String canRenewPlan;
    @SerializedName("can_renew_warranty")
    @Expose
    public String canRenewWarranty;
    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("address2")
    @Expose
    public String address2;
    @SerializedName("lat")
    @Expose
    public double lat;
    @SerializedName("lng")
    @Expose
    public double lng;
    @SerializedName("country_id")
    @Expose
    public int countryId;
    @SerializedName("PIN")
    @Expose
    public int pin;
    @SerializedName("last_ping_datetime")
    @Expose
    public String lastPingDatetime;
    @SerializedName("city_id")
    @Expose
    public int cityId;
    @SerializedName("state_id")
    @Expose
    public int stateId;
    @SerializedName("current_fw_version_main_board")
    @Expose
    public String currentFwVersionMainBoard;
    @SerializedName("current_fw_version_id_main_board")
    @Expose
    public String currentFwVersionIdMainBoard;
    @SerializedName("current_fw_version_ocpp_board")
    @Expose
    public String currentFwVersionOcppBoard;
    @SerializedName("current_fw_version_id_ocpp_board")
    @Expose
    public String currentFwVersionIdOcppBoard;
    @SerializedName("current_ampere_value")
    @Expose
    public String currentAmpereValue;


}