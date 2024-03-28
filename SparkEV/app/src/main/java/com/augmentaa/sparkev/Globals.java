package com.augmentaa.sparkev;

import java.util.UUID;

public class Globals {

    public static final String DEFAULT_PASS = "12D687";
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455");     //microchip traperent profile
    public static final UUID TX_CHAR_UUID  = UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616");       //microchip traperent profile
    public static final UUID RX_CHAR_UUID = UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3");
    //microchip traperent profile
    public static final String SIGNAL_AC_VOLTAGE = "0A";
    public static final String SIGNAL_CURRENT = "14";
    public static final String SIGNAL_SESSION_TIME = "59";
    public static final String SIGNAL_SESSION_UNIT = "35";
    public static final String SIGNAL_KWH = "65";
    public static final String SIGNAL_TOTAL_TIME = "6A";
    public static final String SIGNAL_NE_VOLTAGE = "43";
    public static final String SIGNAL_EARTH_LEAKAGE_CURRENT = "16";
    public static final String SIGNAL_CHARGER_STATUS = "67";
    public static final String SIGNAL_CO2_STATUS = "38";
    public static final String SIGNAL_GET_PASSWORD = "32";
    public static final String SIGNAL_ALARM = "39";
    public static final String SIGNAL_ALARM_LOG_COUNT = "6F";
    public static final String SIGNAL_SESSION_LOG_COUNT = "69";

    public static final String SIGNAL_LOW_VOLT_CUTOFF = "11";
    public static final String SIGNAL_LOW_VOLT_CUTIN_HYS = "55";
    public static final String SIGNAL_HIGH_VOLT_CUTOFF = "10";
    public static final String SIGNAL_HIGH_VOLT_CUTIN_HYS = "54";
    public static final String SIGNAL_RATED_CURRENT = "4F";
    public static final String SIGNAL_MAX_OUT_CURR_PERCENT = "17";
    public static final String SIGNAL_MIN_OUT_CURRENT = "18";

    public static final String SIGNAL_CARD_SERIAL_NUMBER = "4A";
    public static final String SIGNAL_CARD_PART_NUMBER = "4C";
    public static final String SIGNAL_SYSTEM_SERIAL_NUMBER = "4B";
    public static final String SIGNAL_SYSTEM_PART_NUMBER = "4D";
    public static final String SIGNAL_COMPLETE_SYSTEM_VERSION = "52";
    public static final String SIGNAL_BOOT_MODE = "41";

    public static final String SIGNAL_WIFI_SSID = "63";
    public static final String SIGNAL_WIFI_PASSWORD = "61";

    public static final String SIGNAL_SERVER_IP = "64";
    public static final String SIGNAL_SERVER_PORT = "5E";
    public static final String SIGNAL_SERVER_PATH = "60";
    public static final String SIGNAL_CHARGER_ID = "62";
    public static final String SIGNAL_CHARGE_KWH = "4E";
    public static final String SIGNAL_CHARGE_TIME = "58";
    public static final String SIGNAL_APPOINTMENT_CHARGE_TIME = "6C";
    public static final String SIGNAL_APPOINTMENT_CHARGE_DATE = "6D";
    public static final String SIGNAL_ALARM_SETTING = "50";
    public static final String SIGNAL_NE_CUTOFF = "46";
    public static final String SIGNAL_RFID_AUTH = "34";
    public static final String SIGNAL_FACTORY_RESET = "53";
    public static final String SIGNAL_RTC_TIME = "3F";
    public static final String SIGNAL_RTC_DATE = "40";
    public static final String SIGNAL_PUBLIC_PRIVATE = "5D";
    public static final String SET_PASSWORD_PREFIX = "10AC320100";
    //public static final String faqUrl="https://www.exicom-ps.com";
   public static final String faqUrl="https://helpac2.exicom.in";
    public static final String contactUs="https://www.exicom-ps.com/contact-us.html";
    public static final String START_CHARGING = "10AC3C0101";
    public static final String STOP_CHARGING = "10AC3C0110";
    public static final String GET_AC_VOLTAGE = "10AC0A0000000000";
    public static final String GET_CURRENT = "10AC140000000000";
    public static final String GET_SESSION_TIME = "10AC590000000000";
    public static final String GET_SESSION_UNIT = "10AC350000000000";
    public static final String GET_TOTAL_TIME = "10AC6A0000000000";
    public static final String GET_NE_VOLTAGE = "10AC430000000000";
    public static final String GET_EARTH_LEAKAGE_CURRENT = "10AC160000000000";
    public static final String GET_CHARGING_STATUS = "10AC670000000000";
    public static final String GET_KWH = "10AC650000000000";
    public static final String GET_CO2_SAVED = "10AC380000000000";
    public static final String GET_PASSWORD_COMMAND = "10AC320000000000";
    public static final String GET_ALARMS = "10AC390000000000";
    public static final String GET_ALARM_LOG_COUNT = "10AC6F0000000000";
    public static final String GET_ALARM_LOGS_PREFIX = "10AC7000";
    public static final String GET_SESSION_LOGS_COUNT = "10AC690000000000";
    public static final String GET_SESSION_LOGS_PREFIX = "10AC680000";

    public static final String GET_LOW_VOLTAGE_CUTOFF = "10AC110000000000";
    public static final String SET_LOW_VOLTAGE_CUTOFF = "10AC1101";
    public static final String GET_LOW_VOLTAGE_CUTIN_HYS = "10AC550000000000";
    public static final String GET_HIGH_VOLTAGE_CUTOFF = "10AC100000000000";
    public static final String SET_HIGH_VOLTAGE_CUTOFF = "10AC1001";
    public static final String GET_HIGH_VOLTAGE_CUTIN_HYS = "10AC540000000000";
    public static final String GET_RATED_CURRENT = "10AC4F0000000000";
    public static final String SET_RATED_CURRENT = "10AC4F01";
    public static final String GET_MAX_OUTPUT_CURRENT_PERCENT = "10AC170000000000";
    public static final String SET_MAX_OUTPUT_CURRENT = "10AC1701";
    public static final String GET_MIN_OUTPUT_CURRENT = "10AC180000000000";
    public static final String SET_MIN_OUTPUT_CURRENT = "10AC1801";

    //public static final String GET_CARD_SERIAL_NUMBER_01 = "10AC4A0000000000";
    public static final String GET_CARD_SERIAL_NUMBER_01 = "10AC4A0000000000";
    public static final String GET_CARD_SERIAL_NUMBER_02 = "10AC4A1000000000";
    public static final String GET_CARD_SERIAL_NUMBER_03 = "10AC4A2000000000";
    public static final String GET_CARD_SERIAL_NUMBER_04 = "10AC4A3000000000";
    public static final String GET_CARD_PART_NUMBER_01 = "10AC4C0000000000";
    public static final String GET_CARD_PART_NUMBER_02 = "10AC4C1000000000";
    public static final String GET_CARD_PART_NUMBER_03 = "10AC4C2000000000";
    public static final String GET_CARD_PART_NUMBER_04 = "10AC4C3000000000";
    public static final String GET_SYSTEM_SERIAL_NUMBER_01 = "10AC4B0000000000";
    public static final String GET_SYSTEM_SERIAL_NUMBER_02 = "10AC4B1000000000";
    public static final String GET_SYSTEM_SERIAL_NUMBER_03 = "10AC4B2000000000";
    public static final String GET_SYSTEM_SERIAL_NUMBER_04 = "10AC4B3000000000";
    public static final String GET_SYSTEM_PART_NUMBER_01 = "10AC4D0000000000";
    public static final String GET_SYSTEM_PART_NUMBER_02 = "10AC4D1000000000";
    public static final String GET_SYSTEM_PART_NUMBER_03 = "10AC4D2000000000";
    public static final String GET_SYSTEM_PART_NUMBER_04 = "10AC4D3000000000";
    public static final String GET_COMPLETE_SYSTEM_VERSION = "10AC520000000000";

    public static final String GET_WIFI_SSID = "10AC630000000000";
    public static final String GET_WIFI_PASSWORD = "10AC610000000000";
    public static final String SET_WIFI_SSID_PREFIX = "10AC630";
    public static final String SET_WIFI_PASS_PREFIX = "10AC610";
    public static final String SET_CHARGE_TIME_PREFIX = "10AC58010000";
    public static final String SET_CHARGE_KWH_PREFIX = "10AC4E01";
    public static final String SET_APPOINTMENT_CHARGE_TIME_PREFIX = "10AC6C010000";
    public static final String SET_APPOINTMENT_CHARGE_DATE_PREFIX = "10AC6D0100";
    public static final String ENABLE_APPOINTMENT = "10AC6E0100000001";
    public static final String DISABLE_APPOINTMENT = "10AC6E0100000000";
    public static final String GET_APPOINTMENT_STATUS = "10AC6E0000000000";

    public static final String GET_SERVER_IP = "10AC640000000000";
    public static final String GET_SERVER_PORT = "10AC5E0000000000";
    public static final String GET_SERVER_PATH = "10AC600000000000";
    public static final String SET_SERVER_IP_PREFIX = "10AC64";
    public static final String SET_SERVER_PORT_PREFIX = "10AC5E";
    public static final String SET_SERVER_PATH_PREFIX = "10AC60";
    public static final String GET_CHARGER_ID = "10AC620000000000";
    public static final String SET_CHARGER_ID_PREFIX = "10AC62";
    public static final String GET_CHARGE_KWH = "10AC4E0000000000";
    public static final String GET_CHARGE_TIME = "10AC580000000000";
    public static final String GET_APPOINTMENT_CHARGE_TIME = "10AC6C0000000000";
    public static final String GET_APPOINTMENT_CHARGE_DATE = "10AC6D0000000000";
    public static final String GET_PUBLIC_PRIVATE = "10AC5D0000000000";
    public static final String SET_PUBLIC_MODE_PREFIX = "10AC5D01000000";
    public static final String set_public_Charger = "10AC5D0000000002";
    public static final String set_private_Charger = "10AC5D0000000001";
    public static final String set_private_Charger1 = "10AC5D0000000000";

    public static final String GET_ALARM_SETTINGS = "10AC500000000000";
    public static final String GET_NE_VOLTAGE_CUTOFF = "10AC460000000000";
    public static final String SET_ALARM_SETTINGS_PREFIX = "10AC5001";
    public static final String SET_NE_VOLT_CUTOFF_PREFIX = "10AC4601";

    public static final String GET_RFID_AUTH = "10AC340000000000";
    public static final String SET_RFID_AUTH_PREFIX = "10AC3401";
    public static final String FACTORY_RESET_CMD = "10AC530100000000";

    public static final String SET_RTC_TIME_PREFIX = "10AC3F0100";
    public static final String SET_RTC_DATE_PREFIX = "10AC400100";

    public static final String ENTER_CARD_TO_BOOT_MODE = "10AC410100000000";
    public static final String PING_COMMAND = "20";
    public static final String DOWNLOAD_COMMAND = "21";
    public static final String RUN_COMMAND = "22";
    public static final String GET_STATUS_COMMAND = "23";
    public static final String SEND_DATA_COMMAND = "24";
    public static final String RESET_COMMAND = "25";
    public static final String ACK_COMMAND = "00CC";
    public static final String NACK_COMMAND = "0033";
    public static final String START_ADDRESS = "00002800";

    public static final Integer MIN_LOW_VOLT_CUTOFF_VAL = 90;
    public static final Integer MAX_LOW_VOLT_CUTOFF_VAL = 210;
    public static final Integer MIN_HIGH_VOLT_CUTOFF = 240;
    public static final Integer MAX_HIGH_VOLT_CUTOFF = 300;
    public static final Integer MIN_RATED_CURRENT = 10;
    public static final Integer MAX_RATED_CURRENT = 63;
    public static final Integer MIN_OUTPUT_CURRENT_PERCENT = 100;
    public static final Integer MAX_OUTPUT_CURRENT_PERCENT = 150;
    public static final Integer MAX_MIN_OUTPUT_CURRENT = 0;
    public static final Integer MIN_MIN_OUTPUT_CURRENT = 10;
    public final static char[] hexArray = "0123456789ABCDEF".toCharArray();





}
