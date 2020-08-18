package com.my.dingtalkdelete;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.my.dingtalkdelete.dingtalk.DentryInfoHookReceiver;
import com.my.dingtalkdelete.dingtalk.UserInfoHookReceiver;
import com.my.dingtalkdelete.hook.common.ViewOnclick;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.content.pm.ApplicationInfo.FLAG_SYSTEM;
import static android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;


public class HookMain implements IXposedHookLoadPackage {
    private int appInfoFlagFilter = FLAG_SYSTEM | FLAG_UPDATED_SYSTEM_APP;
    private boolean isDingTalkHook = false;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) throws Throwable {
        if (lpp.appInfo != null && (lpp.appInfo.flags & appInfoFlagFilter) == 0) {
            String packageName = lpp.packageName;
            final String processName = lpp.processName;
            final XC_LoadPackage.LoadPackageParam lppf = lpp;
            if ("com.alibaba.android.rimet".equals(packageName)) {
                try {
                    XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Context context = (Context)param.args[0];
                            Application application = (Application)param.thisObject;
                            PluginHelper.getInstance().setContext(context);
                            if ("com.alibaba.android.rimet".equals(processName) && !isDingTalkHook) {
                                isDingTalkHook = true;

                                registerUserInfoReceiver(context);
                                registerDentryInfoReceiver(context);

                                //PluginHelper.getInstance().load(GetDingTalkConversion.class, lppf);
                                //PluginHelper.getInstance().load(ViewOnclick.class, lppf);
                            }
                        }
                    });
                } catch (Throwable th) {
                    Log.d(utils.TAG, "[" + packageName + ":" + processName + "]" + utils.getStackTraceMsg(th));
                }
            }
        }
    }

    private void registerUserInfoReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalConfig.DINGTALK_GET_USER_INFO);
        context.registerReceiver(new UserInfoHookReceiver(), intentFilter);
    }

    private void registerDentryInfoReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalConfig.DINGTALK_GET_DENTRY_INFO);
        intentFilter.addAction(GlobalConfig.DINGTALK_GET_CACHE_DENTRY_INFO);
        intentFilter.addAction(GlobalConfig.DINGTALK_CLEAR_CACHE_DENTRY_INFO);
        intentFilter.addAction(GlobalConfig.DINGTALK_DELETE_DENTRY_INFO);
        context.registerReceiver(new DentryInfoHookReceiver(), intentFilter);
    }
}
