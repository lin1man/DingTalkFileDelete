package com.my.dingtalkdelete.hook.common;

import android.util.Log;
import android.view.View;

import com.my.dingtalkdelete.IPlugin;
import com.my.dingtalkdelete.utils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ViewOnclick implements IPlugin {
    private class ViewOnClickListenerHooker extends XC_MethodHook {
        private String packageName;
        public ViewOnClickListenerHooker(String packageName) {
            this.packageName = packageName;
        }
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            final View view = (View) param.thisObject;
            final View.OnClickListener listener = (View.OnClickListener) param.args[0];
            final String stackTrace = utils.getStackTrace();
            View.OnClickListener newListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        Log.d(utils.TAG, packageName + ".ViewOnClickListener:[" + v.getId() + "][" + view.getId() + "]" + listener.toString() + "\n" + stackTrace);
                        listener.onClick(v);
                    }
                }
            };
            param.args[0] = newListener;
        }

    }

    @Override
    public void load(XC_LoadPackage.LoadPackageParam lpp) {
        try {
            XposedHelpers.findAndHookMethod(View.class, "setOnClickListener", View.OnClickListener.class, new ViewOnClickListenerHooker(lpp.packageName));
        } catch (Throwable th) {
            Log.e(utils.TAG, ViewOnclick.class.getSimpleName() + " ex:" + utils.getStackTraceMsg(th));
        }
    }
}
