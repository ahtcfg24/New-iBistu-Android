package org.iflab.ibistubydreamfactory.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.UpdateAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.UpdateInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 检查更新并下载安装
 */
public class CheckUpdateUtil {


    public static void checkUpdate(final View parentView, final AlertDialog dialog) {
        final UpdateAPI updateAPI = APISource.getInstance().getAPIObject(UpdateAPI.class);
        Call<UpdateInfo> call = updateAPI.getUpdateInfo();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.isSuccessful()) {
                    UpdateInfo updateInfo = response.body();
                    MyApplication.UPDATE_DOWNLOAD_URL = MyApplication.INSTANCE_URL + updateInfo.getPath() + updateInfo
                            .getName();
                    if (updateInfo.getVersionCode() > AndroidUtils.getVersionCode(MyApplication.getAppContext())) {
                        dialog.setTitle("下载更新");
                        dialog.setMessage("发现新版本：" + updateInfo.getVersion() + "\n更新内容：" + updateInfo
                                .getVersionInfo() + "\n更新时间：" + updateInfo.getUpdateTime() + "\n安装包大小：" + updateInfo
                                .getUpdateSize() + "M");
                        dialog.show();
                    }
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                Snackbar.make(parentView, "检查更新失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 在通知栏下载文件
     */
    public static void downloadInNotificationBar(String url) {
        url = url + "?" + "api_key=" + MyApplication.API_KEY;//构造完整的url
        Log.i("url", url);
        DownloadManager downloadManager = (DownloadManager) MyApplication.getAppContext()
                                                                         .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(url));
        //设置存储路径
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "iBistu.apk");
        //设置下载名称
        downloadRequest.setTitle(MyApplication.getAppContext()
                                              .getResources()
                                              .getString(R.string.app_name));
        //设置下载进度条在下载过程中以及下载完毕后都可见
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置下载描述
        downloadRequest.setDescription("iBistu版本更新");
        //设置文件MIME类型为安装包
        downloadRequest.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        downloadRequest.allowScanningByMediaScanner();
        // 设置为可见和可管理
        downloadRequest.setVisibleInDownloadsUi(true);
        //将下载请求放入队列
        long downloadId = downloadManager.enqueue(downloadRequest);
        Log.i("Id", downloadId + "");
        //保存id
        SharedPreferenceUtil.putString(MyApplication.getAppContext(), "DOWNLOAD_ID", downloadId + "");


    }


}
