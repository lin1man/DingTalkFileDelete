package com.my.dingtalkdelete.dingtalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;

import com.my.dingtalkdelete.GlobalConfig;
import com.my.dingtalkdelete.dingtalk.models.ConversationInfo;
import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.util.RepeatBroadCastHelper;
import com.my.dingtalkdelete.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DentryInfoHookReceiver extends BroadcastReceiver {

    private void getDentryInfo(Context context) {
        List<ConversationInfo> conversationInfos = ConversationCache.getConversationInfo(context.getClassLoader());
        for (ConversationInfo info : conversationInfos) {
            if (!TextUtils.isEmpty(info.spaceId)) {
                SpaceRPC.listDentry(context.getClassLoader(), info);
                utils.sleep(30);//暂缓请求
            }
        }
        Intent intent = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_GET_DENTRY_INFO_RESPONSE);
        context.sendBroadcast(intent);
    }

    private void getCacheDentryInfo(Context context) {
        Set<DentryInfo> dentryInfos = DentryCache.getInstance().getDentryInfos();
        ArrayList<DentryInfo> infoArrayList = new ArrayList<>();
        Log.d(utils.TAG, "dentryInfos.size() = " + dentryInfos.size());
        infoArrayList.addAll(dentryInfos);
        Intent intent = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO_RESPONSE);
        intent.putParcelableArrayListExtra(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO_KEY, infoArrayList);
        context.sendBroadcast(intent);
    }

    private void clearDentryCache(Context context) {
        DentryCache.getInstance().clear();
        Intent intent = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO_RESPONSE);
        context.sendBroadcast(intent);
    }

    private void deleteDentryCache(Context context, Intent intent) {
        ArrayList<DentryInfo> deleteDentrys = intent.getParcelableArrayListExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY);
        Map<String, ArrayList<DentryInfo>> deleteDentrysMap = new HashMap<>();
        for (DentryInfo dentryInfo : deleteDentrys) {
            ArrayList<DentryInfo> infoArrayList = deleteDentrysMap.get(dentryInfo.spaceId);
            if (infoArrayList == null) {
                infoArrayList = new ArrayList<>();
                deleteDentrysMap.put(dentryInfo.spaceId, infoArrayList);
            }
            infoArrayList.add(dentryInfo);
        }
        ClassLoader classLoader = context.getClassLoader();
        for (Map.Entry<String, ArrayList<DentryInfo>> entry : deleteDentrysMap.entrySet()) {
            ArrayList<DentryInfo> dentryInfos = entry.getValue();
            if (dentryInfos != null && deleteDentrys.size() > 0) {
                Log.d(utils.TAG, "deleting spaceId:" + entry.getKey());
                List<Object> dentryInfosToDentryModelList = DentryCache.getInstance().dentryInfosToDentryModel(dentryInfos);
                SpaceRPC.deleteDentrys(classLoader, dentryInfosToDentryModelList);
                //notify caller
                Intent intentR = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_RESPONSE);
                intentR.putParcelableArrayListExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY, dentryInfos);
                context.sendBroadcast(intentR);

                DentryCache.getInstance().remove(dentryInfos);
                utils.sleep(100);
            }
        }
        Intent intentR = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_RESPONSE);
        intentR.putParcelableArrayListExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY, null);
        context.sendBroadcast(intentR);
        /*
        for (DentryInfo dentryInfo : deleteDentrys) {
            Log.d(utils.TAG, "deleting " + dentryInfo.path);
            SpaceRPC.deleteDentry(context.getClassLoader(), dentryInfo);
            //notify caller
            Intent intentR = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_RESPONSE);
            intentR.putExtra(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO_KEY, dentryInfo);
            context.sendBroadcast(intentR);

            DentryCache.getInstance().remove(dentryInfo);
            utils.sleep(1000);
        }
        */
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            Log.d(utils.TAG, DentryInfoHookReceiver.class.getSimpleName() + " receive:" + action);
            if (action.equals(GlobalConfig.DINGTALK_GET_DENTRY_INFO)) {
                getDentryInfo(context);
            } else if (action.equals(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO)) {
                getCacheDentryInfo(context);
            } else if (action.equals(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO)) {
                clearDentryCache(context);
            } else if (action.equals(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO)) {
                deleteDentryCache(context, intent);
            }
        }
    }
}
