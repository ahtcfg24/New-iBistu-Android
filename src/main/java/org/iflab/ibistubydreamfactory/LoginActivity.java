package org.iflab.ibistubydreamfactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import junit.framework.Assert;

import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LoginRequest;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private ProgressBar progressBar;
    private APISource apiSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    /**
     * 监听登录按钮
     */
    public void onLoginClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            apiSource = APISource.getInstance();
            AuthAPI authAPI = apiSource.getAPIObject(AuthAPI.class);

            final LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setPassword(password);
            request.setRemember_me(true);//默认获得永不过期的token

            Call<User> call = authAPI.login(request);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {//如果登录成功
                        User user = response.body();
                        Assert.assertEquals(user.getEmail(), request.getEmail());
                        //记录token
                        APISource.testToken = user.getSessionToken();
                        SharedPreferenceUtil.putString(getApplicationContext(), MyApplication.SESSION_TOKEN, user
                                .getSessionToken());
                        APISource.isHaveLogin = true;
                        finish();
                    } else {//登录失败
                        ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                        onFailure(call, e.toException());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "登录失败：" + t.getMessage(), Toast.LENGTH_LONG)
                         .show();
                    System.out.println("Throwable是{" + t.getMessage() + "}");

                }
            });
        }

    }
}
