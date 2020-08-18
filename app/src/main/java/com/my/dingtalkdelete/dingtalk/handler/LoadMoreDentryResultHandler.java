package com.my.dingtalkdelete.dingtalk.handler;

import com.my.dingtalkdelete.dingtalk.DentryCache;
import com.my.dingtalkdelete.dingtalk.models.ConversationInfo;
import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;


public class LoadMoreDentryResultHandler implements InvocationHandler {
    private ConversationInfo conversationInfo;
    public LoadMoreDentryResultHandler(ConversationInfo conversationInfo) {
        this.conversationInfo = conversationInfo;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("onDataReceived")) {
            try {
                Object result = args[0];
                if (result != null) {
                    String loadMoreId = (String)XposedHelpers.getObjectField(result, "b");
                    List dentryList = (List) XposedHelpers.getObjectField(result, "d");
                    if (dentryList != null) {
                        for (int i = 0; i < dentryList.size(); i++) {
                            DentryInfo dentryInfo = new DentryInfo();
                            Object dentryModel = dentryList.get(i);
                            dentryInfo.title = conversationInfo.title;
                            dentryInfo.ownerId = "" + conversationInfo.ownerId;
                            dentryInfo.loadMoreId = loadMoreId;
                            dentryInfo.spaceId = (String) XposedHelpers.callMethod(dentryModel, "getSpaceId");
                            dentryInfo.name = (String) XposedHelpers.callMethod(dentryModel, "getName");
                            dentryInfo.creatorEmail = (String) XposedHelpers.callMethod(dentryModel, "getCreatorEmail");
                            dentryInfo.serverId = (String) XposedHelpers.callMethod(dentryModel, "getServerId");
                            dentryInfo.path = (String) XposedHelpers.callMethod(dentryModel, "getPath");
                            DentryCache.getInstance().put(dentryInfo, dentryModel);
                        }
                    }
                }
            } catch (Throwable th) {
                Log.d(utils.TAG, "LoadMoreDentryResultHandler ex:" + th.getMessage());
            }
        } else {
            Log.i(utils.TAG, "LoadMoreDentryResultHandler:" + method.getName());
        }
        return null;
    }
}
