package com.augmentaa.sparkev.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    // Development API

//    public static final String BASE_URL_getBatteryData = "https://buzzjoblisttest.exicom.in/api/";
//    public static final String BASE_URL_Payment_Process = "https://buzzjoblisttest.exicom.in/apipp/";
//    public static final String BASE_URL_CHARGER = "https://betacmsassistant.exicom.in/";
//    public static final String BASE_URL_SPIN = "https://spinapistaging.exicom.in/v1/";


    // Production APIF
    public static final String BASE_URL_getBatteryData = "http://103.151.107.121:4205/";
   // public static final String BASE_URL_CHARGER = "https://Chargexmw.exicom.in/";
    public static final String BASE_URL_CHARGER = "http://103.151.107.121:4205/";
    public static final String BASE_URL_Payment_Process = "http://103.151.107.121:4205/";
//    public static final String BASE_URL_SPIN = "https://spinapi.exicom.in/v1/"; // COMMENTED BY PRASHANT
      public static final String BASE_URL_SPIN = "http://103.151.107.121:4205/";
    private static Retrofit retrofit = null;

    APIClient() {

    }

    public static Retrofit getClientNew() {
        retrofit = null;
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Builder().baseUrl(BASE_URL_getBatteryData).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getPaymentProcess() {
        retrofit = null;
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Builder().baseUrl(BASE_URL_Payment_Process).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


    public static Retrofit getChargerURL() {
        retrofit = null;
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Builder().baseUrl(BASE_URL_CHARGER).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getSpinURL() {
        retrofit = null;
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Builder().baseUrl(BASE_URL_SPIN).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


}
