package com.my.dingtalkdelete.dingtalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.my.dingtalkdelete.GlobalConfig;
import com.my.dingtalkdelete.dingtalk.handler.InfoSpaceHandler;
import com.my.dingtalkdelete.dingtalk.handler.LoadMoreDentryResultHandler;
import com.my.dingtalkdelete.dingtalk.handler.UserInfoHandler;
import com.my.dingtalkdelete.dingtalk.models.ConversationInfo;
import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.dingtalk.models.UserInfo;
import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.util.RepeatBroadCastHelper;
import com.my.dingtalkdelete.utils;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class SpaceRPC {
    //deleteDentryToRecycle
    //{"lwp":"/r/Adaptor/CSpace/listDentry","headers":{"mid":"xxxxxxxx 0"},"body":[{"loadMoreId":"","length":50,"sortType":21,"spaceId":xxxx,"folderId":"0","topType":1}]}
    //{"lwp":"/r/Adaptor/CSpace/deleteDentry","headers":{"mid":"xxxxxxxx 0"},"body":[{"spaceId":xxxx,"ids":["nnnnn"],"physical":false}]}

    private static Object getSpaceRPC(ClassLoader classLoader) {
        Class<?> mdlCls = XposedHelpers.findClass("mdl", classLoader);
        return XposedHelpers.callStaticMethod(mdlCls, "a");
    }

    public static void infoSpace(ClassLoader classLoader, String spaceId) {
        try {
            Object spaceRPC = getSpaceRPC(classLoader);
            Class<?> ellCls = XposedHelpers.findClass("ell", classLoader);
            if (spaceRPC != null) {
                Object ellProxy = Proxy.newProxyInstance(classLoader, new Class[]{ellCls}, new InfoSpaceHandler());
                XposedHelpers.callMethod(spaceRPC, "a", spaceId, false, ellProxy);
            }
        } catch (Throwable th) {
            Log.d(utils.TAG, "infoSpace ex:" + utils.getStackTraceMsg(th));
        }
    }

    public static void listDentry(ClassLoader classLoader, ConversationInfo conversationInfo) {
        try {
            Object spaceRPC = getSpaceRPC(classLoader);
            Class<?> ellCls = XposedHelpers.findClass("ell", classLoader);
            Object ellProxy = Proxy.newProxyInstance(classLoader, new Class[]{ellCls}, new LoadMoreDentryResultHandler(conversationInfo));
            XposedHelpers.callMethod(spaceRPC, "a", conversationInfo.spaceId, "0", null, 1, null, ellProxy);//a(final java.lang.String spaceId, final java.lang.String folderId, final java.lang.String path, final int sortType, final java.lang.String loadMoreId, final ell<wx> listener)
        } catch (Throwable th) {
            Log.d(utils.TAG, "listDentry ex:" + utils.getStackTraceMsg(th));
        }
    }

    public static void deleteDentry(ClassLoader classLoader, DentryInfo dentryInfo) {
        List<Object> list = new ArrayList<>();
        Object objDentryModel = DentryCache.getInstance().get(dentryInfo);
        if (objDentryModel == null) {
            Log.e(utils.TAG, "DentryInfoCache no found path:" + dentryInfo.path + " id:" + dentryInfo.serverId);
        } else {
            list.add(objDentryModel);
            deleteDentrys(classLoader, list);
        }
    }

    public static void deleteDentrys(ClassLoader classLoader, List<Object> dentryInfos) {
        try {
            Object spaceRPC = getSpaceRPC(classLoader);
            XposedHelpers.callMethod(spaceRPC, "a", dentryInfos, null);
        } catch (Throwable th) {
            Log.d(utils.TAG, "deleteDentrys ex:" + utils.getStackTraceMsg(th));
        }
    }
}
