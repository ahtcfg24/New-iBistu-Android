package org.iflab.ibistubydreamfactory.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        setSupportActionBar(toolbar);//把ToolBar设置为ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
    }

    private void initView() {
        fragmentManager = getFragmentManager();
        findViewById(R.id.floatingButton).setOnClickListener(new View.OnClickListener() {
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
        Fragment postFragment = PostLostFoundFragment.newInstance();
        transaction.add(R.id.fragment_container, postFragment);
        transaction.addToBackStack(null);//把fragment添加到回退栈中
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lost_found, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
