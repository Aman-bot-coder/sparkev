package com.augmentaa.sparkev.utils;

import com.google.gson.Gson;

public class GsonUtils {
    private static GsonUtils sInstance;

    public static GsonUtils getInstance() {
        if (sInstance == null) {
            sInstance = new GsonUtils();
        }
        return sInstance;
    }

    private Gson mGson;

    private GsonUtils() {
        mGson = new Gson();
    }

    public String toJson(Object object) {
        return mGson.toJson(object);
    }

    public Gson getGson() {
        return mGson;
    }
}