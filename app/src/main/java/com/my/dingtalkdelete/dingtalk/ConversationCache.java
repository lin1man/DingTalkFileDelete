package com.my.dingtalkdelete.dingtalk;

import android.text.TextUtils;
import android.util.Log;

import com.my.dingtalkdelete.dingtalk.models.ConversationInfo;
import com.my.dingtalkdelete.utils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;


public class ConversationCache {
    private static ClassLoader mclassLoader;
    private static ConversationInfo conversationCacheToInfo(Object cache) {
        try {
            ConversationInfo info = new ConversationInfo();
            info.title = (String)XposedHelpers.callMethod(cache, "title");
            info.type = (int)XposedHelpers.callMethod(cache, "type");
            info.categoryId = (long)XposedHelpers.callMethod(cache, "getCategoryId");
            info.ownerId = (long)XposedHelpers.callMethod(cache, "getOwnerId");
            info.peerId = (long)XposedHelpers.callMethod(cache, "getPeerId");
            info.groupId = (long)XposedHelpers.callMethod(cache, "groupId");
            try {
                Object status = XposedHelpers.callMethod(cache, "status");
                Object typeValue = XposedHelpers.callMethod(status, "typeValue");
                info.status = ConversationInfo.ConversationStatus.fromValue((int)typeValue);
            } catch (Throwable th) {
                Log.e(utils.TAG, ConversationCache.class.getSimpleName() + " status:" + th.getMessage());
                info.status = ConversationInfo.ConversationStatus.DISBAND;
            }
            try {
                Class<?> mewCls = XposedHelpers.findClass("mew", mclassLoader);
                info.spaceId = (String)XposedHelpers.callStaticMethod(mewCls, "a", cache);//getSpaceIdFromConversation(Conversation)
                if (!TextUtils.isEmpty(info.spaceId)) {
                    Log.d(utils.TAG, ConversationInfo.class.getSimpleName() + " listDentry:spaceId:" + info.spaceId + " ownerId:" + info.ownerId + " title:" + info.title);
                }
            } catch (Throwable th) {
                Log.e(utils.TAG, ConversationCache.class.getSimpleName() + " spaceId:" + th.getMessage());
                info.spaceId = "";
            }
            return info;
        } catch (Throwable th) {
            Log.e(utils.TAG, ConversationCache.class.getSimpleName() + " conversationCacheToInfo:" + th.getMessage());
            return null;
        }
    }

    public static List<ConversationInfo> getConversationInfo(ClassLoader classLoader) {
        List<ConversationInfo> infoList = new ArrayList<>();
        mclassLoader = classLoader;
        try {
            Class<?> immoduleCls = XposedHelpers.findClass("com.alibaba.wukong.im.context.IMModule", classLoader);
            Object immoduleObj = XposedHelpers.callStaticMethod(immoduleCls, "getInstance");
            Object conversationCache = XposedHelpers.callMethod(immoduleObj, "getConversationCache");
            List cache = (List)XposedHelpers.callMethod(conversationCache, "a", 100000);
            for (int i = 0; i < cache.size(); i++) {
                Object o = cache.get(i);
                ConversationInfo conversationInfo = conversationCacheToInfo(o);
                if (conversationInfo != null) {
                    Log.d(utils.TAG, conversationInfo.toString());
                    infoList.add(conversationInfo);
                }
            }
        } catch (Throwable th) {
            Log.e(utils.TAG, ConversationCache.class.getSimpleName() + ":" + utils.getStackTraceMsg(th));
        }
        return infoList;
    }
}
