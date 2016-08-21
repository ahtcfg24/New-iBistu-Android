package org.iflab.ibistubydreamfactory.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.iflab.ibistubydreamfactory.MyApplication;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;

/**
 *
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
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Intent updateApk = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDownLoadId);//获取下载的文件的路径
                Log.i("安装路径", downloadFileUri.toString());

                updateApk.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                updateApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(updateApk);
            }
        }


    }
}