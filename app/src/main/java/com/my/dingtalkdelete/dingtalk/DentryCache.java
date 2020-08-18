package com.my.dingtalkdelete.dingtalk;

import com.my.dingtalkdelete.dingtalk.models.DentryInfo;
import com.my.dingtalkdelete.util.Log;
import com.my.dingtalkdelete.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DentryCache {
    private static DentryCache dentryCache;
    private static Map<DentryInfo, Object> dentryInfoMap = new HashMap<>();

    private DentryCache() {

    }

    public static DentryCache getInstance() {
        if (dentryCache == null) {
            synchronized (DentryCache.class) {
                if (dentryCache == null) {
                    dentryCache = new DentryCache();
                }
            }
        }
        return dentryCache;
    }

    public void clear() {
        dentryInfoMap.clear();
    }

    public void put(DentryInfo info, Object object) {
        dentryInfoMap.put(info, object);
    }

    public Object get(DentryInfo info) {
        return dentryInfoMap.get(info);
    }

    public void remove(DentryInfo info) {
        try {
            dentryInfoMap.remove(info);
        } catch (Throwable th) {
            Log.d(utils.TAG, "remove dentryInfo ex:" + th.getMessage());
        }
    }

    public void remove(List<DentryInfo> infos) {
        for (DentryInfo info : infos) {
            remove(info);
        }
    }

    public Set<DentryInfo> getDentryInfos() {
        return dentryInfoMap.keySet();
    }

    public List<Object> dentryInfosToDentryModel(List<DentryInfo> infos) {
        List<Object> dentryModelList = new ArrayList<>();
        for (DentryInfo info : infos) {
            Object objDentryModel = get(info);
            if (objDentryModel != null) {
                dentryModelList.add(objDentryModel);
            }
        }
        return dentryModelList;
    }
}
