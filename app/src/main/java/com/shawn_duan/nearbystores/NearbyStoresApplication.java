package com.shawn_duan.nearbystores;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by sduan on 12/5/16.
 */

public class NearbyStoresApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
