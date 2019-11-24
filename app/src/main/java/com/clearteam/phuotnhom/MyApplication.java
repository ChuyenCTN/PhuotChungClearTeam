package com.clearteam.phuotnhom;

import android.app.Application;

import com.clearteam.phuotnhom.utils.HSSPreference;

/**
 * Created by Tuấn Sơn on 19/7/2017.
 */

public class MyApplication extends Application {

    private static MyApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        HSSPreference.initialize(getApplicationContext());
    }

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }
}
