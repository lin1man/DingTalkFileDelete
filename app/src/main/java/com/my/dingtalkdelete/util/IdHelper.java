package com.my.dingtalkdelete.util;


public class IdHelper {
    public static String generateId() {
        return String.valueOf(System.currentTimeMillis()) + Math.random() * 10000.0d;
    }
}
