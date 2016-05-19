package com.ileja.calendar;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by chentao on 16/5/16.
 */
public class MainApp extends Application {

    private static MainApp _instance;
    private RefWatcher _refWatcher;

    public static MainApp get() {
        return _instance;
    }

    public static RefWatcher getRefWatcher() {
        return MainApp.get()._refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = (MainApp) getApplicationContext();
        _refWatcher = LeakCanary.install(this);

        Timber.plant(new Timber.DebugTree());
    }
}
