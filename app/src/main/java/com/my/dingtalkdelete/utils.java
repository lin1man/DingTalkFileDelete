package com.my.dingtalkdelete;

import com.my.dingtalkdelete.util.Log;


public class utils {
    public static String TAG = "DingTalkDelete";

    public static String getStackTrace() {
        return getStackTrace(null);
    }

    public static String getStackTrace(Throwable th) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTraces;
        if (th == null) {
            stackTraces = Thread.currentThread().getStackTrace();
        } else {
            stackTraces = th.getStackTrace();
        }
        for (StackTraceElement stackTrace : stackTraces) {
            sb.append(stackTrace.toString()).append("\n");
        }
        return sb.toString();
    }

    public static String getStackTraceMsg(Throwable th) {
        if (th == null) {
            th = new Throwable();
        }
        return th.getMessage() + "\n" + getStackTrace(th);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable th) {
            Log.d(TAG, "sleep error???" + getStackTraceMsg(th));
        }
    }
}
