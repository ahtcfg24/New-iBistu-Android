package org.iflab.ibistubydreamfactory.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.fragment.LoginFragment;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.RegisterRequestBody;
import org.iflab.ibistubydreamfactory.utils.AndroidUtils;
import org.iflab.ibistubydreamfactory.utils.MyCountTimer;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册Activity
 */
public class RegisterActivity extends AppCompatActivity {

    //    private AsyncHttpClient client;
    private static final int CONFIRM_SUCCESS = 1;//代表验证码验证成功的消息，用于在验证短信的线程中传递消息给主线程
    private static final int CONFIRM_FAIL = 0;//代表验证码验证失败
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.phone_input)
    EditText phoneInput;
    @BindView(R.id.confirmCode_input)
    EditText confirmCodeInput;
    @BindView(R.id.sendCodeButton)
    Button sendCodeButton;
    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input)
    EditText passwordInput;
    @BindView(R.id.confirmPassword_input)
    EditText confirmPasswordInput;
    private FragmentManager fragmentManager;
    private View parentView;
    private AuthAPI authAPI;
    private Bundle bundle;
    private String phone;
    private String email;
    private String password;
    private String confirmPhone;
    /**
     * 用来处理验证码校验成功后的事情的线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            register(email, password, phone);
        }
    };
    /**
     * 用来操作主线程（UI线程）中的控件的handler，它属于本线程
     */
    private Handler handlerOfUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case CONFIRM_SUCCESS:
                    System.out.println("msg.arg1" + msg.arg1);
                    handlerOfUI.post(runnable);//验证码是对的
                    break;
                case CONFIRM_FAIL:
                    progressBar.setVisibility(View.GONE);//让进度加载框隐藏
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * mob短信校验sdk提供的事件回调
     */
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();//创建一个消息对象，用于放置消息
            if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交的验证码是正确的
                    msg.arg1 = CONFIRM_SUCCESS;//校验成功
                    Log.i("验证码正确：", data + "");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//成功发送验证码
                    Snackbar.make(parentView, "验证码发送成功，请在十分钟内验证，60秒后可重新发送！", Snackbar.LENGTH_LONG)
                            .show();
                    Log.i("验证码已经发送到手机", data + "");
                } else {
                    ((Throwable) data).printStackTrace();
                    Log.i("其他事件：", data + "");
                }
            } else {//回调失败
                msg.arg1 = CONFIRM_FAIL;//校验失败
                try {
                    JSONObject jsonObject = new JSONObject(((Throwable) data).getMessage());
                    Snackbar.make(parentView, jsonObject.getString("detail"), Snackbar.LENGTH_LONG)
                            .show();//输出失败原因
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("回调失败", ((Throwable) data).getMessage());
            }
            handlerOfUI.sendMessage(msg);//把短信验证的结果传递给主线程
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        initToolbar();
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        bundle = new Bundle();
        fragmentManager = getFragmentManager();
        initSMSConfirm();


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
     * 初始化网易云信短信校验联网的Client
     */
//    private void initSMSConfirm() {
//        client = new AsyncHttpClient();
//        client.addHeader("AppKey", MyApplication.APPKEY);
//        String nonce = StringUtil.getRandomString(20);
//        String curTime = StringUtil.getUTCSecond();
//        client.addHeader("Nonce", nonce);
//        client.addHeader("CurTime", curTime);
//        client.addHeader("CheckSum", StringUtil.getCheckSum(MyApplication.APPSECRET, nonce, curTime));
//    }

    /**
     * 初始化mob短信验证
     */
    private void initSMSConfirm() {
        SMSSDK.initSDK(this, MyApplication.SMS_APP_KEY, MyApplication.SMS_APP_SECRET);
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    /**
     * 注册按钮点击事件
     */
    public void onButtonRegisterClick(View view) {
        AndroidUtils.hideSoftInput(RegisterActivity.this);
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
            SMSSDK.submitVerificationCode("86", phone, confirmCode);//向mob短信后台提交输入的验证码，验证是否正确
//            sendConfirmCode(confirmCode);
        }
    }

    /**
     * 向服务器提交短信验证码
     */
//    private void sendConfirmCode(String confirmCode) {
//        RequestParams params = new RequestParams();
//        params.add("mobile", phone);
//        params.add("code", confirmCode);
//        client.post(MyApplication.SMSCONFIRMURL + "verifycode.action", params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    JSONObject jsonObject = new JSONObject(new String(responseBody));
//                    if (jsonObject.getString("code").equals("200")) {
//                        Toast.makeText(RegisterActivity.this, "验证成功!", Toast.LENGTH_SHORT).show();
//                        register(email, password, phone);
//                    } else if (jsonObject.getString("code").equals("413")) {//验证码错误
//                        progressBar.setVisibility(View.GONE);
//                        Snackbar.make(parentView, "验证码错误，请重新获取！", Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        Snackbar.make(parentView, "校验失败，错误码：" + jsonObject.getString("code"), Snackbar.LENGTH_SHORT)
//                                .show();
//                        Log.i("短信错误码", jsonObject.getString("code"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Snackbar.make(parentView, "发送验证码失败：" + error.getMessage(), Snackbar.LENGTH_LONG)
//                        .show();
//
//            }
//        });
//    }

    /**
     * 发起注册请求
     */
    private void register(final String email, final String password, final String phone) {
        final RegisterRequestBody request = new RegisterRequestBody();
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);

        Call<ResponseBody> call = authAPI.register(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(parentView, "注册失败：" + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void onButtonGoLoginClick(View view) {//跳转到登录页面
        AndroidUtils.hideSoftInput(RegisterActivity.this);
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
            SMSSDK.getVerificationCode("86", confirmPhone);//向填入的手机号发送验证码
//            getSMSConfirmCode();
        }
    }

//    private void getSMSConfirmCode(){
//        client.post(MyApplication.SMSCONFIRMURL + "sendcode.action", new RequestParams("mobile", confirmPhone), new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(new String(responseBody));
//                        if (jsonObject.getString("code").equals("200")) {
//                            Snackbar.make(parentView, "验证码发送成功，请在十分钟内验证，60秒后可重新发送！", Snackbar.LENGTH_LONG)
//                                    .show();
//                        } else {
//                            progressBar.setVisibility(View.GONE);
//                            Snackbar.make(parentView, "未知错误，错误码：" + jsonObject.getString("code"), Snackbar.LENGTH_SHORT)
//                                    .show();
//                            Log.i("短信错误码", jsonObject.getString("code"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    System.out.println(new String(responseBody));
//                    Snackbar.make(parentView, "发送验证码失败：" + error.getMessage(), Snackbar.LENGTH_LONG)
//                            .show();
//
//                }
//            });
//    }

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
