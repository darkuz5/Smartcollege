package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.firewallsol.smartcollege.Funciones.GPSClass;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created on 07/10/15.
 */
public class Aplicacion extends Application implements Application.ActivityLifecycleCallbacks {

    public static boolean isApplicationActivityVisible = false;
    public static boolean isConversacionActivityVisible = false;

    public static Context activity;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(this);
        activity = getApplicationContext();

        Parse.initialize(this, "qeFFjE9KHEiZHsKzSfQDBZqTD3YcskrXtLRO58er", "qJYctl22fgfEECzJLWQjfm8R9CQwUmxNqEqL065O");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("");ParsePush.subscribeInBackground("");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        new GPSClass(activity);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isApplicationActivityVisible = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isApplicationActivityVisible = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
