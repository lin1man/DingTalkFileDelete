package com.my.dingtalkdelete.util;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.my.dingtalkdelete.util.IdHelper;


public final class RepeatBroadCastHelper {
    private static final String BroadCastId = "BroadCastId";
    private String lastId;

    public static Intent createIntent(@Nullable String action) {
        Intent intent = new Intent(action);
        intent.putExtra(BroadCastId, IdHelper.generateId());
        return intent;
    }

    public final boolean repeat(Intent intent) {
        String stringExtra = intent.getStringExtra(BroadCastId);
        if (TextUtils.isEmpty(stringExtra)) {
            throw new IllegalArgumentException("Broadcast must have " + BroadCastId + ", use RepeatBroadCastHelper.createIntent() to create Intent");
        } else if (stringExtra.equals(lastId)) {
            return true;
        } else {
            lastId = stringExtra;
            return false;
        }
    }
}
