package com.example.sashatinkoff.fbshare;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by sashatinkoff on 25.10.16.
 */

public class App extends Application {

    private static App self;

    @Override
    public void onCreate() {
        super.onCreate();

        self = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Context getContext() {
        return self;
    }
}
