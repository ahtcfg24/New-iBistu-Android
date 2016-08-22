package org.iflab.ibistubydreamfactory.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.iflab.ibistubydreamfactory.MyApplication;

import java.io.File;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;

/**
 * 接收下载完成的广播，并自动启动安装
 */
public class CompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_DOWNLOAD_COMPLETE)) {
            Log.i("Receiver", "安装包下载完成");
            long myDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);  //拿到下载的Id
            Log.i("myId", myDownLoadId + "");
            String savedDownLoadId = SharedPreferenceUtil.getString(MyApplication.getAppContext(), "DOWNLOAD_ID");
            Log.i("savedId", savedDownLoadId);
            if (Long.parseLong(savedDownLoadId) == (myDownLoadId)) {     //如果是保存在本地的一样,那么启动并安装
                Log.i("Receiver", "开始安装");
                openFile(context);
            }
        }


    }

    /**
     * apk自动安装
     */
    private void openFile(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File("/sdcard/Download/iBistu.apk")); //这里是APK路径
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}