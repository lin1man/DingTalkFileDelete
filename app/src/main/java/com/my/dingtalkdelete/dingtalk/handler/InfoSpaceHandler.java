package com.my.dingtalkdelete.dingtalk.handler;

import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class InfoSpaceHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("onDataReceived")) {
            Log.d(utils.TAG, "InfoSpaceHandler:" + args[0].toString());
        } else {
            Log.i(utils.TAG, "InfoSpaceHandler:" + method.getName());
        }
        return null;
    }
}
