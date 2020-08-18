package com.my.dingtalkdelete.dingtalk.handler;

import android.util.Log;

import com.my.dingtalkdelete.dingtalk.ConversationCache;
import com.my.dingtalkdelete.dingtalk.models.UserInfo;
import com.my.dingtalkdelete.utils;

import de.robv.android.xposed.XposedHelpers;


public class UserInfoHandler {
    private static Object getAuthServiceInstance(ClassLoader classLoader) {
        Class<?> authClass = XposedHelpers.findClass("com.alibaba.wukong.auth.AuthService", classLoader);
        return XposedHelpers.callStaticMethod(authClass, "getInstance");
    }

    public static UserInfo getUserInfo(ClassLoader classLoader) {//todo IMContext.q() getNickname p() getOpenId
        try {
            Object authServiceInstance = getAuthServiceInstance(classLoader);
            Object latestAuthInfo = XposedHelpers.callMethod(authServiceInstance, "latestAuthInfo");
            if (latestAuthInfo == null) {
                return null;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.openId = (long)XposedHelpers.callMethod(latestAuthInfo, "getOpenId");
            userInfo.userName = (String)XposedHelpers.callMethod(latestAuthInfo, "getNickname");
            return userInfo;
        } catch (Throwable th) {
            Log.d(utils.TAG, UserInfoHandler.class.getSimpleName() + " ex:" + utils.getStackTraceMsg(th));
            return null;
        }
    }

    private static boolean isLogin(ClassLoader classLoader) {
        try {
            Object authServiceInstance = getAuthServiceInstance(classLoader);
            return (boolean)XposedHelpers.callMethod(authServiceInstance, "isLogin");
        } catch (Throwable th) {
            Log.d(utils.TAG, UserInfoHandler.class.getSimpleName() + " ex:" + utils.getStackTraceMsg(th));
            return false;
        }
    }
}
