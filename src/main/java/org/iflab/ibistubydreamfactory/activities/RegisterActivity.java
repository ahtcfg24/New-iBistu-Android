package org.iflab.ibistubydreamfactory.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.fragment.LoginFragment;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.RegisterRequestBody;
import org.iflab.ibistubydreamfactory.models.SuccessModel;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.MyCountTimer;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;
import org.iflab.ibistubydreamfactory.utils.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class RegisterActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private EditText phoneInput, confirmCodeInput, emailInput, passwordInput, confirmPasswordInput;
    private Button sendCodeButton;
    private ProgressBar progressBar;
    private View parentView;
    private AuthAPI authAPI;
    private Bundle bundle;
    private String phone;
    private String password;
    private String email;
    private String confirmPhone;//进行短信验证的手机号
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
        setContentView(parentView);
        initToolbar();
        initView();
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        bundle = new Bundle();
        fragmentManager = getFragmentManager();
        initSMSConfirmClient();


    }

    /**
     * 初始化工具栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        toolbar.setLogo(R.drawable.ic_action_logo_bistu);//设置静态logo
        setSupportActionBar(toolbar);//把ToolBar设置为ActionBar
    }

    /**
     * 初始化短信校验联网的Client
     */
    private void initSMSConfirmClient() {
        client = new AsyncHttpClient();
        client.addHeader("AppKey", MyApplication.APPKEY);
        String nonce = StringUtil.getRandomString(20);
        String curTime = StringUtil.getUTCSecond();
        client.addHeader("Nonce", nonce);
        client.addHeader("CurTime", curTime);
        client.addHeader("CheckSum", StringUtil.getCheckSum(MyApplication.APPSECRET, nonce, curTime));
    }

    private void initView() {
        sendCodeButton = (Button) findViewById(R.id.sendCodeButton);
        phoneInput = (EditText) findViewById(R.id.phone_input);
        confirmCodeInput = (EditText) findViewById(R.id.confirmCode_input);
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPassword_input);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    public void onButtonRegisterClick(View view) {
        phone = phoneInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        String confirmCode = confirmCodeInput.getText().toString();

        if (!RegexConfirmUtils.isMobile(phone)) {
            Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_SHORT).show();
        } else if (!RegexConfirmUtils.isNotNull(confirmCode)) {
            Snackbar.make(parentView, "请输入收到的验证码！", Snackbar.LENGTH_SHORT).show();
        } else if (!RegexConfirmUtils.isEmail(email)) {
            Snackbar.make(parentView, "请输入正确的邮箱！", Snackbar.LENGTH_SHORT).show();
        } else if (!RegexConfirmUtils.isNotContainsSpecial(password)) {
            Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Snackbar.make(parentView, "两次输入的密码不一样！", Snackbar.LENGTH_SHORT).show();
        } else if (!RegexConfirmUtils.isLengthRight(password, 5, 17)) {
            Snackbar.make(parentView, "密码长度在6-16之间！", Snackbar.LENGTH_SHORT).show();
        } else if (!phone.equals(confirmPhone)) {
            Snackbar.make(parentView, "该手机号还没有验证！", Snackbar.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.add("mobile", phone);
            params.add("code", confirmCode);
            client.post(MyApplication.SMSCONFIRMURL + "verifycode.action", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.getString("code").equals("200")) {
                            Toast.makeText(RegisterActivity.this, "验证成功!", Toast.LENGTH_SHORT)
                                 .show();
                            register(email, password, phone);
                        } else if (jsonObject.getString("code").equals("413")) {//验证码错误
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "验证码错误，请重新获取！", Snackbar.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "校验失败，错误码：" + jsonObject.getString("code"), Snackbar.LENGTH_SHORT)
                                    .show();
                            Log.i("短信错误码", jsonObject.getString("code"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Snackbar.make(parentView, "发送验证码失败：" + error.getMessage(), Snackbar.LENGTH_LONG)
                            .show();

                }
            });
        }
    }

    /**
     * 发起注册请求
     */
    private void register(final String email, final String password, final String phone) {
        final RegisterRequestBody request = new RegisterRequestBody();
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);

        Call<SuccessModel> call = authAPI.register(request);
        call.enqueue(new Callback<SuccessModel>() {

            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {//如果成功
                    Snackbar.make(parentView, "注册成功", Snackbar.LENGTH_LONG).show();
                    bundle.putString("email", email);
                    bundle.putString("password", password);
                    switchToLogin(bundle);
                } else {//失败
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(parentView, "注册失败：" + e.getError()
                                                         .getContext()
                                                         .getEmail()[0], Snackbar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {

            }
        });
    }

    public void onButtonGoLoginClick(View view) {//跳转到登录页面
        switchToLogin(bundle);
    }

    /**
     * 点击获取短信验证码
     */
    public void onGetConfirmCodeButtonClick(View view) {
        AndroidUtils.hideSoftInput(RegisterActivity.this);
        confirmPhone = phoneInput.getText().toString();
        if (!RegexConfirmUtils.isMobile(confirmPhone)) {
            Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_SHORT).show();
        } else {
            new MyCountTimer(60000, 1000, sendCodeButton, RegisterActivity.this).start();//发送后即显示重新获取验证码倒计时
            client.post(MyApplication.SMSCONFIRMURL + "sendcode.action", new RequestParams("mobile", confirmPhone), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.getString("code").equals("200")) {
                            Snackbar.make(parentView, "验证码发送成功，请在十分钟内验证，60秒后可重新发送！", Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "未知错误，错误码：" + jsonObject.getString("code"), Snackbar.LENGTH_SHORT)
                                    .show();
                            Log.i("短信错误码", jsonObject.getString("code"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println(new String(responseBody));
                    Snackbar.make(parentView, "发送验证码失败：" + error.getMessage(), Snackbar.LENGTH_LONG)
                            .show();

                }
            });
        }
    }

    /**
     * 跳转到登录页面
     */
    private void switchToLogin(Bundle args) {
        // 开启Fragment管理事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment loginFragment = new LoginFragment();
        loginFragment.setArguments(args);
        transaction.add(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);//把fragment添加到回退栈中
        // 提交事务
        transaction.commit();
    }

}
