package org.iflab.ibistubydreamfactory;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 代表整个APP的Application类，程序中首先加载，需要在AndroidManifest中注册
 */
public class MyApplication extends Application {
    private static Context context;//具有最高生存周期的Context
    public static String SESSION_TOKEN;//用户每次登录时服务器随机生成的凭证
    public static String INSTANCE_URL;//DreamFactory的提供的接口地址
    public static String API_KEY;//DreamFactory接口的key，可在DreamFactory控制台的apps标签下找到

    public void onCreate() {
        super.onCreate();

        MyApplication.context = getApplicationContext();

        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            //从AndroidManifest配置文件中获取这些值
            SESSION_TOKEN = bundle.getString("sessionToken");
            INSTANCE_URL = bundle.getString("instanceUrl");
            API_KEY = bundle.getString("apiKey");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(MyApplication.class.getSimpleName(), "读取配置文件出错", e);
        }
    }

    /**
     * 提供最高生命周期的Context
     * @return
     */
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
