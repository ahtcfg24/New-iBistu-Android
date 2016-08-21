package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.CheckUpdateUtil;

/**
 * 程序第一个Activity，使用的是singleTask mode
 */
public class WelcomeActivity extends Activity {
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
        getAlertDialog();
        if (AndroidUtils.isWifi(MyApplication.getAppContext())) {
            CheckUpdateUtil.checkUpdate(parentView, getAlertDialog());
        } else {
            handler.postDelayed(runnable, 1000);
        }
    }


    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                handler.post(runnable);
            }
        }).setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(WelcomeActivity.this, "正在后台下载安装包，请查看通知栏下载进度", Toast.LENGTH_LONG)
                     .show();
                CheckUpdateUtil.downloadInNotificationBar(MyApplication.UPDATE_DOWNLOAD_URL);
                handler.post(runnable);
            }
        });
        return builder.create();
    }

}
