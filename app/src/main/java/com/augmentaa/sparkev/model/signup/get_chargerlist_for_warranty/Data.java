package com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

   @Override
   public String toString() {
      return "Data{" +
              "serialNo='" + serialNo + '\'' +
              ", chargerId=" + chargerId +
              ", chargerModelName='" + chargerModelName + '\'' +
              ", renewalPending='" + renewalPending + '\'' +
              ", canRenewWarranty='" + canRenewWarranty + '\'' +
              ", canRenewPlan='" + canRenewPlan + '\'' +
              ", startDate='" + startDate + '\'' +
              ", exipry='" + exipry + '\'' +
              ", chargingStatus='" + chargingStatus + '\'' +
              ", mapAsChild=" + mapAsChild +
              ", planValidity='" + planValidity + '\'' +
              ", partNo='" + partNo + '\'' +
              ", nickName='" + nickName + '\'' +
              ", address='" + address + '\'' +
              ", countryId=" + countryId +
              ", pin='" + pin + '\'' +
              ", state_id=" + state_id +
              ", stateName='" + stateName + '\'' +
              ", countryName='" + countryName + '\'' +
              ", address1='" + address1 + '\'' +
              ", address2='" + address2 + '\'' +
              ", charger_mode='" + charger_mode + '\'' +
              ", current_fw_version_main_board='" + current_fw_version_main_board + '\'' +
              ", current_fw_version_id_main_board='" + current_fw_version_id_main_board + '\'' +
              ", current_fw_version_ocpp_board='" + current_fw_version_ocpp_board + '\'' +
              ", current_fw_version_id_ocpp_board='" + current_fw_version_id_ocpp_board + '\'' +
              ", current_ampere_value='" + current_ampere_value + '\'' +
              ", station_id=" + station_id +
              '}';
   }

   @SerializedName("serial_no")
   @Expose
   public String serialNo;
   @SerializedName("charger_id")
   @Expose
   public Integer chargerId;
   @SerializedName("charger_model_name")
   @Expose
   public String chargerModelName;
   @SerializedName("renewal_pending")
   @Expose
   public String renewalPending;
   @SerializedName("can_renew_warranty")
   @Expose
   public String canRenewWarranty;
   @SerializedName("can_renew_plan")
   @Expose
   public String canRenewPlan;
   @SerializedName("start_date")
   @Expose
   public String startDate;
   @SerializedName("exipry")
   @Expose
   public String exipry;
   @SerializedName("charging_status")
   @Expose
   public String chargingStatus;
   @SerializedName("map_as_child")
   @Expose
   public int mapAsChild;
   @SerializedName("plan_validity")
   @Expose
   public String planValidity;
   @SerializedName("part_no")
   @Expose
   public String partNo;
   @SerializedName("nick_name")
   @Expose
   public String nickName;

   @SerializedName("address")
   @Expose
   public String address;

   @SerializedName("country_id")
   @Expose
   public int countryId;

   @SerializedName("pin")
   @Expose
   public String pin;

   @SerializedName("state_id")
   @Expose
   public int state_id;


   @SerializedName("state_name")
   @Expose
   public String stateName;

   @SerializedName("country_name")
   @Expose
   public String countryName;

   @SerializedName("address1")
   @Expose
   public String address1;

   @SerializedName("address2")
   @Expose
   public String address2;


   @SerializedName("charger_mode")
   @Expose
   public String charger_mode;

   @SerializedName("current_fw_version_main_board")
   @Expose
   public String current_fw_version_main_board;

   @SerializedName("current_fw_version_id_main_board")
   @Expose
   public String current_fw_version_id_main_board;

   @SerializedName("current_fw_version_ocpp_board")
   @Expose
   public String current_fw_version_ocpp_board;

   @SerializedName("current_fw_version_id_ocpp_board")
   @Expose
   public String current_fw_version_id_ocpp_board;

   @SerializedName("current_ampere_value")
   @Expose
   public String current_ampere_value;

   @SerializedName("station_id")
   @Expose
   public int station_id;

   @SerializedName("client_certificate")
   @Expose
   public int client_certificate;

   @SerializedName("is_OCPP_enabled")
   @Expose
   public String is_OCPP_enabled;

   @SerializedName("name")
   @Expose
   public String name;

   @SerializedName("status")
   @Expose
   public String status;

   @SerializedName("ocpp_upgrade_status")
   @Expose
   public String ocpp_upgrade_status;



  public Data(){

  }
   protected Data(Parcel in) {
      serialNo = in.readString();
      if (in.readByte() == 0) {
         chargerId = null;
      } else {
         chargerId = in.readInt();
      }
      chargerModelName = in.readString();
      renewalPending = in.readString();
      canRenewWarranty = in.readString();
      canRenewPlan = in.readString();
      startDate = in.readString();
      exipry = in.readString();
      chargingStatus = in.readString();
      mapAsChild = in.readInt();
      planValidity = in.readString();
      partNo = in.readString();
      nickName = in.readString();
      address = in.readString();
      countryId = in.readInt();
      pin = in.readString();
      state_id = in.readInt();
      stateName = in.readString();
      countryName = in.readString();
      address1 = in.readString();
      address2 = in.readString();
      charger_mode = in.readString();
      current_fw_version_main_board = in.readString();
      current_fw_version_id_main_board = in.readString();
      current_fw_version_ocpp_board = in.readString();
      current_fw_version_id_ocpp_board = in.readString();
      current_ampere_value = in.readString();
      station_id = in.readInt();
      client_certificate= in.readInt();
      is_OCPP_enabled= in.readString();
      status= in.readString();
      ocpp_upgrade_status= in.readString();
      name= in.readString();
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
      parcel.writeString(serialNo);
      if (chargerId == null) {
         parcel.writeByte((byte) 0);
      } else {
         parcel.writeByte((byte) 1);
         parcel.writeInt(chargerId);
      }
      parcel.writeString(chargerModelName);
      parcel.writeString(renewalPending);
      parcel.writeString(canRenewWarranty);
      parcel.writeString(canRenewPlan);
      parcel.writeString(startDate);
      parcel.writeString(exipry);
      parcel.writeString(chargingStatus);
      parcel.writeInt(mapAsChild);
      parcel.writeString(planValidity);
      parcel.writeString(partNo);
      parcel.writeString(nickName);
      parcel.writeString(address);
      parcel.writeInt(countryId);
      parcel.writeString(pin);
      parcel.writeInt(state_id);
      parcel.writeString(stateName);
      parcel.writeString(countryName);
      parcel.writeString(address1);
      parcel.writeString(address2);
      parcel.writeString(charger_mode);
      parcel.writeString(current_fw_version_main_board);
      parcel.writeString(current_fw_version_id_main_board);
      parcel.writeString(current_fw_version_ocpp_board);
      parcel.writeString(current_fw_version_id_ocpp_board);
      parcel.writeString(current_ampere_value);
      parcel.writeInt(station_id);
      parcel.writeInt(client_certificate);
      parcel.writeString(is_OCPP_enabled);
      parcel.writeString(status);
      parcel.writeString(ocpp_upgrade_status);
      parcel.writeString(name);
   }
}