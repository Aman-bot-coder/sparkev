package com.augmentaa.sparkev;

import android.app.Application;

import com.augmentaa.sparkev.utils.Pref;
import com.google.firebase.FirebaseApp;


public class SPINApplication extends Application {

    private static SPINApplication instance;

    public static SPINApplication get() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Pref.openPref(this);
        FirebaseApp.initializeApp(this);
    }



}
