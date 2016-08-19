package org.iflab.ibistubydreamfactory.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import junit.framework.Assert;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.activities.HomeActivity;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LoginRequestBody;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private EditText editEmail;
    private EditText editPassword;
    private ProgressBar progressBar;
    private APISource apiSource;
    private Button loginButton, resetPasswordButton;
    private ACache aCache = ACache.get(MyApplication.getAppContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView(rootView);

        return rootView;
    }


    /**
     * 初始化控件
     */
    private void initView(View rootView) {
        editEmail = (EditText) rootView.findViewById(R.id.edit_email);
        editPassword = (EditText) rootView.findViewById(R.id.edit_password);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loginButton = (Button) rootView.findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);
        resetPasswordButton = (Button) rootView.findViewById(R.id.button_resetPassword);

    }


    @Override
    public void onClick(View v) {
        String email = editEmail.getText().toString();

        if (v.equals(loginButton)) {//监听登录按钮
            progressBar.setVisibility(View.VISIBLE);
            String password = editPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getActivity(), "邮箱不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                apiSource = APISource.getInstance();
                AuthAPI authAPI = apiSource.getAPIObject(AuthAPI.class);

                final LoginRequestBody request = new LoginRequestBody();
                request.setEmail(email);
                request.setPassword(password);

                Call<User> call = authAPI.login(request);
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {//如果登录成功
                            User user = response.body();
                            aCache.put("user", user, 24 * ACache.TIME_HOUR);//保存user对象
                            Assert.assertEquals(user.getEmail(), request.getEmail());
                            //记录token,保存到缓存是位了检测token是否过期，保存到preference是为了刷新token时读取旧token
                            SharedPreferenceUtil.putString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN", user
                                    .getSessionToken());
                            aCache.put("SESSION_TOKEN", user.getSessionToken(), 24 * ACache.TIME_HOUR);//token保存24小时
                            getActivity().finish();
                            startActivity(new Intent(LoginFragment.this.getActivity(), HomeActivity.class));
                        } else {//登录失败
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "登录失败：" + t.getMessage(), Toast.LENGTH_LONG)
                             .show();
                        System.out.println("Throwable是{" + t.getMessage() + "}");

                    }
                });
            }
        } else if (v.equals(resetPasswordButton)) {
            // TODO: 2016/8/19 重置密码
        }

    }
}
