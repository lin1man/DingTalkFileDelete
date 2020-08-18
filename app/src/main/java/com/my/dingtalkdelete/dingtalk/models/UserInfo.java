package com.my.dingtalkdelete.dingtalk.models;

import java.io.Serializable;


public class UserInfo implements Serializable {
    public String userName;
    public long openId;

    @Override
    public String toString() {
        return "UserInfoReceiver{" +
                "userName='" + userName + '\'' +
                ", openId=" + openId +
                '}';
    }
}
