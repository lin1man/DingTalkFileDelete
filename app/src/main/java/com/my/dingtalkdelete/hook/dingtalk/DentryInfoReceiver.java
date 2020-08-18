package com.my.dingtalkdelete.hook.dingtalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.my.dingtalkdelete.GlobalConfig;
import com.my.dingtalkdelete.MainActivity;
import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.util.RepeatBroadCastHelper;
import com.my.dingtalkdelete.utils;

import java.util.ArrayList;


public class DentryInfoReceiver extends BroadcastReceiver {
    private static RepeatBroadCastHelper repeatBroadCastHelper = new RepeatBroadCastHelper();

    private void getCacheDentryHandler(Intent intent) {
        ArrayList<DentryInfo> infoArrayList = intent.getParcelableArrayListExtra(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO_KEY);
        MainActivity ref = MainActivity.getRef();
        if (ref != null && infoArrayList != null) {
            ref.setDentryInfoData(infoArrayList);
        } else {
            Log.d(utils.TAG, "getCacheDentryHandler ref or infoArrayList is null");
        }
    }

    private void deleteDentryHandler(Intent intent, MainActivity ref) {
        ArrayList<DentryInfo> infoArrayList = intent.getParcelableArrayListExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY);
        ref.deleteDentryInfo(infoArrayList);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && !repeatBroadCastHelper.repeat(intent)) {
            MainActivity ref = MainActivity.getRef();
            Log.d(utils.TAG, DentryInfoReceiver.class.getSimpleName() + " receive:" + action);
            if (action.equals(GlobalConfig.DINGTALK_GET_DENTRY_INFO_RESPONSE)) {
                if (ref != null) {
                    ref.Toast("get dentry info end");
                    ref.setDentryInfoCacheStatus(true);
                }
            } else if (action.equals(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO_RESPONSE)) {
                getCacheDentryHandler(intent);
            } else if (action.equals(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO_RESPONSE)) {
                if (ref != null) {
                    ref.Toast("clear dentry info cache end");
                    ref.setDentryInfoCacheStatus(false);
                }
            } else if (action.equals(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_RESPONSE)) {
                if (ref != null) {
                    deleteDentryHandler(intent, ref);
                }
            }
        }
    }
}
