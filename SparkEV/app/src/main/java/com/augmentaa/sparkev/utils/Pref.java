package com.augmentaa.sparkev.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {
	
	public static SharedPreferences sharedPreferences = null;
	Context mContext;
	
	public synchronized static void openPref(Context context){
		if(sharedPreferences==null)
			context=context;
		sharedPreferences = context.getSharedPreferences("CHARGER_APP", Context.MODE_PRIVATE);
	}

	public static void remove(String key) {
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.remove(key);
		prefsPrivateEditor.apply();
	}

	public static void clear() {
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.clear();
		prefsPrivateEditor.commit();
	}

	public enum TYPE{
		USER_ID,USERNAME,MOBILE,CURR_LATI,CURR_LONGI,
		STATION_LATI,STATION_LONGI,EMAIL,IMAGE_URL,F_NAME,L_NAME,LOGIN,CLIENT_ID,
		PASSWORD,CHARGER_STATUS,CHARGER_ID,CONNECTOR_NO,CHARGER_SCREEN_ACTIVE,
		OS_VERSION,ORIGIN,DEVICE_ID,APP_VERSION,IDTAG,STATION_ID,
		CHARGER_SERIAL_NO,VEHICLE_DETAILS,VEHICLE_ID,VEHICLE_REG,CONN_TYPE,
		CHARGER_TYPE,STATION_DATA,CONN_TYPE_ID,PROJECT_ID,TOKEN_EXI,TOKEN,NICK_NAME,
		CHARGER_OTP_FLAG,USER_ROLE_ID,USER_ROLE_CODE,USER_ROLE_NAME,ACTIVE_LIST_SIZE,OTP_AUTH,CLIENT_NAME,STATION_CLOSE_TIME,
		STATION_OPEN_TIME,S_USER_ID,S_F_NAME,S_M_NAME,S_L_NAME,S_TOKEN,S_MOBILE,S_EMAIL,
		FCM_ID,B_PIN,B_ADDRESS,MYCHARGERLIST,CHARGER_LIST_DATA,CHARGER_PART_NUMBER,CHARGER_CRED,SYSTEM_NUMBER,
		IS_OCCP,IS_CHARGING,MAP_AS_CHILD,SET_CURRENT,REQUEST_SEND,IS_SCHEDULE_TIME,IS_SCHEDULE_KWH,APP_VERSION_CODE,IS_UPGRADE_CHARGER_STATUS;

	}
	public static String getValue(String key, String defaultValue)
	{

		String result = Pref.sharedPreferences.getString(key, defaultValue);
		return result;
	}
	public static boolean getBoolValue(String key, boolean defaultValue)
	{

		boolean result = Pref.sharedPreferences.getBoolean(key, defaultValue);

		return result;
	}
	public static void setValue(String key, String value)
	{
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putString(key, value);
		prefsPrivateEditor.commit();


	}


	public static void setIntValue(String key, int value)
	{

		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putInt(key, value);
		prefsPrivateEditor.commit();


	}
	public static int getIntValue(String key, int defaultValue)
	{

		int result = Pref.sharedPreferences.getInt(key, defaultValue);

		return result;
	}
	public static void setBoolValue(String key, boolean value)
	{
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putBoolean(key, value);
		prefsPrivateEditor.commit();


	}
	

}
