package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;

public class WelcomeActivity extends Activity {
    private Intent intent;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {//延迟三秒启动线程
            @Override
            public void run() {
                intent = new Intent();
                user = (User) aCache.getAsObject("user");
                if (user == null) {
                    intent.setClass(WelcomeActivity.this, RegisterActivity.class);
                } else {
                    intent.setClass(WelcomeActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
