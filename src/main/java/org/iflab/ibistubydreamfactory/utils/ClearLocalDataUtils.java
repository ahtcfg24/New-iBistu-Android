package org.iflab.ibistubydreamfactory.utils;

import org.iflab.ibistubydreamfactory.MyApplication;

/**
 * 清除本地存储的数据
 */
public class ClearLocalDataUtils {
    public static void clearLocalData() {
        ACache.get(MyApplication.getAppContext()).clear();//清空缓存
        SharedPreferenceUtil.putString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN", "");//清空preference
    }


}
