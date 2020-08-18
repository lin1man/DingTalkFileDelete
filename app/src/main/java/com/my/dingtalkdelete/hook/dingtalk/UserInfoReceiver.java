package com.my.dingtalkdelete.hook.dingtalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.my.dingtalkdelete.GlobalConfig;
import com.my.dingtalkdelete.MainActivity;
import com.my.dingtalkdelete.dingtalk.models.UserInfo;
import com.my.dingtalkdelete.util.RepeatBroadCastHelper;


public class UserInfoReceiver extends BroadcastReceiver {
    private static RepeatBroadCastHelper repeatBroadCastHelper = new RepeatBroadCastHelper();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && !repeatBroadCastHelper.repeat(intent)) {
            if (action.equals(GlobalConfig.DINGTALK_GET_USER_INFO_RESPONSE)) {
                UserInfo userInfo = (UserInfo) intent.getSerializableExtra("userinfo");
                MainActivity ref = MainActivity.getRef();
                if (ref != null) {
                    ref.SetUserInfo(userInfo);
                }
            }
        }
    }
}
