package com.my.dingtalkdelete.hook.dingtalk;

import android.text.TextUtils;
import android.util.Log;

import com.my.dingtalkdelete.IPlugin;
import com.my.dingtalkdelete.utils;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class GetDingTalkConversion implements IPlugin {
    String getTitle(Object obj) {
        try {
            String title = (String)XposedHelpers.getObjectField(obj, "categoryTitle");
            if (TextUtils.isEmpty(title)) {
                title = "";
            }
            return title;
        } catch (Throwable th) {
            return "";
        }
    }
    @Override
    public void load(XC_LoadPackage.LoadPackageParam lpp) {
        ClassLoader classLoader = lpp.classLoader;
        Class<?> sessionFragment = XposedHelpers.findClass("com.alibaba.android.dingtalkim.session.SessionFragmentImplV2", classLoader);
        XposedHelpers.findAndHookMethod(sessionFragment, "f", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ArrayList result = (ArrayList)param.getResult();
                Log.d(utils.TAG, "DingtalkConversation.size()=" + result.size());
                int size = result.size();
                for (int i = 0; i < size; i++) {
                    Object obj = result.get(i);
                    Log.d(utils.TAG, "title[" + i + "]" + getTitle(obj));
                }
            }
        });
    }
}
