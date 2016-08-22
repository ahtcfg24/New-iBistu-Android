package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.UpdateAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.UpdateInfo;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.CheckUpdateUtil;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 程序第一个Activity
 */
public class WelcomeActivity extends Activity {
    private static String TAG = "WelcomeActivity";
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private View parentView;
    private Handler handler = new Handler();
    private Intent intent;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            intent = new Intent();
            User user = (User) aCache.getAsObject("user");
            if (user == null) {
                intent.setClass(WelcomeActivity.this, RegisterActivity.class);
            } else {
                intent.setClass(WelcomeActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
        setContentView(parentView);
        if (AndroidUtils.isWifi(MyApplication.getAppContext())) {
            checkUpdateOnStart();
        } else {
            handler.postDelayed(runnable, 1000);
        }
    }

    /**
     * 启动时检查更新
     */
    private void checkUpdateOnStart() {
        final UpdateAPI updateAPI = APISource.getInstance().getAPIObject(UpdateAPI.class);
        Call<UpdateInfo> call = updateAPI.getUpdateInfo();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.isSuccessful()) {
                    UpdateInfo updateInfo = response.body();
                    if (updateInfo.getVersionCode() > AndroidUtils.getVersionCode(MyApplication.getAppContext())) {
                        showUpdateDialog(updateInfo);
                    } else {
                        Log.i(TAG, updateInfo.toString());
                        handler.post(runnable);
                    }
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                Snackbar.make(parentView, "检查更新失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                handler.post(runnable);
            }
        });
    }

    /**
     * 弹出更新提示框
     */
    private void showUpdateDialog(final UpdateInfo updateInfo) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setPositiveButton("更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "正在后台下载安装包，请查看通知栏下载进度", Toast.LENGTH_LONG)
                     .show();
                MyApplication.UPDATE_DOWNLOAD_URL = MyApplication.INSTANCE_URL + updateInfo.getPath() + updateInfo
                        .getName() + "?api_key=" + MyApplication.API_KEY;//构造下载链接
                CheckUpdateUtil.downloadInNotificationBar(MyApplication.UPDATE_DOWNLOAD_URL);
                materialDialog.dismiss();
                handler.post(runnable);

            }
        }).setNegativeButton("以后再说", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                handler.post(runnable);
            }
        });
        materialDialog.setTitle("下载更新");
        materialDialog.setMessage(updateInfo.toString());
        materialDialog.show();

    }

}
