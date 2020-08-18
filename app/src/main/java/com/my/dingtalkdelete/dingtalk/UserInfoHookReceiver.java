package com.my.dingtalkdelete.dingtalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.my.dingtalkdelete.GlobalConfig;
import com.my.dingtalkdelete.dingtalk.handler.UserInfoHandler;
import com.my.dingtalkdelete.dingtalk.models.UserInfo;
import com.my.dingtalkdelete.util.RepeatBroadCastHelper;


public class UserInfoHookReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(GlobalConfig.DINGTALK_GET_USER_INFO)) {
                UserInfo userInfo = UserInfoHandler.getUserInfo(context.getClassLoader());
                if (userInfo == null) {
                    userInfo = new UserInfo();
                    userInfo.userName = "null";
                    userInfo.openId = 0;
                }
                Intent userInfoRespIntent = RepeatBroadCastHelper.createIntent(GlobalConfig.DINGTALK_GET_USER_INFO_RESPONSE);
                userInfoRespIntent.putExtra("userinfo", userInfo);
                context.sendBroadcast(userInfoRespIntent);
            }
        }
    }
}
