package com.my.dingtalkdelete;

import android.util.Log;


public class UncaughtException implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.d(utils.TAG, "UncaughtException:" +  utils.getStackTraceMsg(e));
    }
}
