package org.iflab.ibistubydreamfactory.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.fragment.PostLostFoundFragment;

public class LostFoundActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        initToolbar();
        initView();


    }

    /**
     * 初始化工具栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("失物招领");
        toolbar.setLogo(R.drawable.ic_action_logo_bistu);//设置静态logo
        setSupportActionBar(toolbar);//把ToolBar设置为ActionBar
    }

    private void initView() {
        fragmentManager = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToPost();
            }
        });
    }

    /**
     * 跳转到发布页面
     */
    private void switchToPost() {
        // 开启Fragment管理事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment postFragment = new PostLostFoundFragment();
        transaction.add(R.id.fragment_container, postFragment);
        transaction.addToBackStack(null);//把fragment添加到回退栈中
        // 提交事务
        transaction.commit();
    }

}
