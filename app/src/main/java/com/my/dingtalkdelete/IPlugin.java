package com.my.dingtalkdelete;

import de.robv.android.xposed.callbacks.XC_LoadPackage;


public interface IPlugin {
    void load(XC_LoadPackage.LoadPackageParam lpp);
}
