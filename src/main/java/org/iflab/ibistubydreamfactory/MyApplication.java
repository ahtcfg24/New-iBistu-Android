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
    public static String INSTANCE_URL;//DreamFactory的提供的接口地址
    public static String API_KEY;//DreamFactory接口的key，可在DreamFactory控制台的apps标签下找到
    public static String APPSECRET;//云信短信要求的AppSecret
    public static String APPKEY;//云信短信要求的AppKey
    public static String SMSCONFIRMURL;//云信短信验证URL
    public static String UPDATE_DOWNLOAD_URL = "";//软件更新下载URL
    public static String REFRESH_TOKEN_URL = "";//刷新Token的URL
    public static String newsListBaseURL="http://iamding.cn:8080/newsapi/newslist";
    private static Context context;//具有最高生存周期的Context
    public static String[] newsCategory = {
            "综合新闻", "图片新闻", "人才培养", "教学科研", "文化活动", "校园人物", "交流合作", "社会服务", "媒体关注"
    };

    /**
     * 提供最高生命周期的Context
     */
    public static Context getAppContext() {
        return MyApplication.context;
    }

    public void onCreate() {
        super.onCreate();

        MyApplication.context = getApplicationContext();

        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            //从AndroidManifest配置文件中获取这些值
            INSTANCE_URL = bundle.getString("instanceUrl");
            API_KEY = bundle.getString("apiKey");
            APPSECRET = bundle.getString("AppSecret");
            APPKEY = bundle.getString("AppKey");
            SMSCONFIRMURL = bundle.getString("smsUrl");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(MyApplication.class.getSimpleName(), "读取配置文件出错", e);
        }
    }
}
