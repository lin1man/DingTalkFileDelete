package com.my.dingtalkdelete;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class PluginHelper {
    private static PluginHelper helper;

    private Context context;
    private Map<String, IPlugin> pluginMap = new HashMap<>();

    private PluginHelper() {
    }
    public static PluginHelper getInstance() {
        if (helper == null) {
            synchronized (PluginHelper.class) {
                if (helper == null) {
                    helper = new PluginHelper();
                }
            }
        }
        return helper;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public boolean load(Class<? extends IPlugin> cls, XC_LoadPackage.LoadPackageParam lpp) {
        String simpleName = cls.getSimpleName();
        if (pluginMap.get(simpleName) == null) {
            try {
                IPlugin iPlugin = cls.newInstance();
                iPlugin.load(lpp);
                pluginMap.put(simpleName, iPlugin);
            } catch (Throwable th) {
                Log.d(utils.TAG, this.getClass().getSimpleName() + " load plugin " + simpleName + " error:" + utils.getStackTraceMsg(th));
            }
            return true;
        }
        return false;
    }
}
