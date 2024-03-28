package com.augmentaa.sparkev.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
