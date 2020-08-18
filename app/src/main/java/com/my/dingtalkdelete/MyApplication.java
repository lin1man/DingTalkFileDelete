package com.my.dingtalkdelete;

import android.app.Application;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.currentThread().setUncaughtExceptionHandler(new UncaughtException());
    }
}
