package org.iflab.ibistubydreamfactory.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.fragment.LoginFragment;

public class RegisterActivity extends AppCompatActivity {
    private Fragment loginFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragmentManager = getFragmentManager();

    }

    public void onButtonRegisterClick(View view) {
        // TODO: 2016/8/19 注册请求
    }

    public void onButtonGoLoginClick(View view) {//跳转到登录页面
        // 开启Fragment管理事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        loginFragment = new LoginFragment();
        transaction.add(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);//把fragment添加到回退栈中
        // 提交事务
        transaction.commit();
    }


    public void onGetConfirmCodeButtonClick(View view) {
        // TODO: 2016/8/19 发送验证码请求
    }
}
