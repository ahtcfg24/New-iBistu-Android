package org.iflab.ibistubydreamfactory.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.Assert;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.activities.HomeActivity;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LoginRequestBody;
import org.iflab.ibistubydreamfactory.models.ResetPasswordRequestBody;
import org.iflab.ibistubydreamfactory.models.SuccessModel;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;
import org.iflab.ibistubydreamfactory.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private AuthAPI authAPI;
    private EditText editEmail;
    private EditText editPassword;
    private ProgressBar progressBar;
    private Button loginButton, resetPasswordButton;
    private TextView forgotPasswordTextView;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        initView(parentView);
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            editEmail.setText(bundle.getString("email"));
            editPassword.setText(bundle.getString("password"));
        }
        return parentView;
    }


    /**
     * 初始化控件
     */
    private void initView(View rootView) {
        editEmail = (EditText) rootView.findViewById(R.id.login_email_input);
        editPassword = (EditText) rootView.findViewById(R.id.login_password_input);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loginButton = (Button) rootView.findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);
        forgotPasswordTextView = (TextView) rootView.findViewById(R.id.forgotPassword);
        forgotPasswordTextView.setOnClickListener(this);
        resetPasswordButton = (Button) rootView.findViewById(R.id.button_resetPassword);
        resetPasswordButton.setOnClickListener(this);

    }


    /**
     * 监听按钮点击事件
     */
    @Override
    public void onClick(View v) {
        AndroidUtils.hideSoftInput(LoginFragment.this.getActivity());
        String email = editEmail.getText().toString();

        if (v.equals(loginButton)) {//监听登录按钮
            String password = editPassword.getText().toString();

            if (!RegexConfirmUtils.isEmail(email)) {
                Snackbar.make(parentView, "请输入正确的邮箱！", Snackbar.LENGTH_SHORT).show();
            } else if (!RegexConfirmUtils.isNotContainsSpecial(password)) {
                Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
            } else if (!RegexConfirmUtils.isLengthRight(password, 5, 17)) {
                Snackbar.make(parentView, "密码长度在6-16之间！", Snackbar.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);

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
                            aCache.put("user", user, 30 * ACache.TIME_DAY);//保存user对象
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
                        Snackbar.make(parentView, "登录失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                .show();

                    }
                });
            }
        } else if (v.equals(forgotPasswordTextView)) {
            editPassword.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            forgotPasswordTextView.setVisibility(View.GONE);
            resetPasswordButton.setVisibility(View.VISIBLE);

        } else if (v.equals(resetPasswordButton)) {
            if (!RegexConfirmUtils.isEmail(email)) {
                Snackbar.make(parentView, "请输入正确的邮箱！", Snackbar.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                final ResetPasswordRequestBody request = new ResetPasswordRequestBody();
                request.setEmail(email);

                Call<SuccessModel> call = authAPI.resetPassword(request);
                call.enqueue(new Callback<SuccessModel>() {

                    @Override
                    public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {//如果成功
                            Snackbar.make(parentView, "我们已经向您的邮箱发送了密码重置邮件，请登录邮箱查看", Snackbar.LENGTH_LONG)
                                    .show();
                        } else {//失败
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(parentView, "重置密码失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                        System.out.println("Throwable是{" + t.getMessage() + "}");

                    }
                });


            }
        }

    }
}
