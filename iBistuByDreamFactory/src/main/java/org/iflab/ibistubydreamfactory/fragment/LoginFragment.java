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
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;
import org.iflab.ibistubydreamfactory.utils.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.login_email_input)
    EditText loginEmailInput;
    @BindView(R.id.login_password_input)
    EditText loginPasswordInput;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.button_resetPassword)
    Button buttonResetPassword;
    private AuthAPI authAPI;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, parentView);
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            loginEmailInput.setText(bundle.getString("email"));
            loginPasswordInput.setText(bundle.getString("password"));
        }
        return parentView;
    }


    /**
     * 监听按钮点击事件
     */
    @OnClick({R.id.forgotPassword, R.id.button_login, R.id.button_resetPassword})
    public void onClick(View view) {
        AndroidUtils.hideSoftInput(LoginFragment.this.getActivity());
        String email = loginEmailInput.getText().toString();
        switch (view.getId()) {
            case R.id.forgotPassword:
                loginPasswordInput.setVisibility(View.GONE);
                buttonLogin.setVisibility(View.GONE);
                forgotPassword.setVisibility(View.GONE);
                buttonResetPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.button_login:
                String password = loginPasswordInput.getText().toString();

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
                break;
            case R.id.button_resetPassword:
                if (!RegexConfirmUtils.isEmail(email)) {
                    Snackbar.make(parentView, "请输入正确的邮箱！", Snackbar.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    final ResetPasswordRequestBody request = new ResetPasswordRequestBody();
                    request.setEmail(email);

                    Call<ResponseBody> call = authAPI.resetPassword(request);
                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "重置密码失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                    .show();
                            System.out.println("Throwable是{" + t.getMessage() + "}");

                        }
                    });


                }
                break;
        }
    }
}
