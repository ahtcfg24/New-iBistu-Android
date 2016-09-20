package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AuthAPI;
import org.iflab.ibistubydreamfactory.models.ChangePasswordRequestBody;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.SuccessModel;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.ClearLocalDataUtils;
import org.iflab.ibistubydreamfactory.utils.RegexConfirmUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCenterActivity extends AppCompatActivity {
    private EditText oldPasswordInput, newPasswordInput, repeatNewPasswordInput;
    private AuthAPI authAPI;
    private View parentView;
    private TextView emailTextView;
    private ProgressBar progressBar;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_user_center, null);
        setContentView(parentView);
        initView();
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        user = (User) aCache.getAsObject("user");
        emailTextView.setText(user.getEmail());

    }

    private void initView() {
        oldPasswordInput = (EditText) findViewById(R.id.editText_oldPassword);
        newPasswordInput = (EditText) findViewById(R.id.editText_newPassword);
        repeatNewPasswordInput = (EditText) findViewById(R.id.editText_repeatNewPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emailTextView = (TextView) findViewById(R.id.textView_email);
    }

    /**
     * 点击退出登录
     */
    public void onButtonLogoutClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Call<SuccessModel> call = authAPI.logout();
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {//如果成功
                    ClearLocalDataUtils.clearLocalData();//清空所有本地存储
                    startActivity(new Intent(UserCenterActivity.this, RegisterActivity.class));
                    finish();
                    HomeActivity.HOME_ACTIVITY_INSTANCE.finish();
                } else {//失败
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(parentView, "退出失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    /**
     * 点击绑定学号
     */
    public void onButtonBindClick(View view) {
        Snackbar.make(parentView, "敬请期待！", Snackbar.LENGTH_SHORT).show();
        // TODO: 2016/8/21 绑定学号
        Intent intent = new Intent();
        intent.setClass(this, CasActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && requestCode == RESULT_OK) {
            // TODO: 2016/9/20 接收返回信息
        }
    }

    /**
     * 点击修改密码
     */
    public void onButtonChangePasswordClick(View view) {
        String oldPassword = oldPasswordInput.getText().toString();
        String newPassword = newPasswordInput.getText().toString();
        String repeatNewPassword = repeatNewPasswordInput.getText().toString();
        if (!RegexConfirmUtils.isNotContainsSpecial(oldPassword)) {
            Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
        } else if (!RegexConfirmUtils.isNotContainsSpecial(newPassword)) {
            Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
        } else if (!newPassword.equals(repeatNewPassword)) {
            Snackbar.make(parentView, "两次输入的新密码不一致！", Snackbar.LENGTH_SHORT).show();
        } else if (newPassword.equals(oldPassword)) {
            Snackbar.make(parentView, "新密码不能和旧密码一样！", Snackbar.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            final ChangePasswordRequestBody request = new ChangePasswordRequestBody();
            request.setEmail(user.getEmail());
            request.setOld_password(oldPassword);
            request.setNew_password(newPassword);
            Call<SuccessModel> call = authAPI.changePassword(request);
            call.enqueue(new Callback<SuccessModel>() {

                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {//如果成功
                        Toast.makeText(UserCenterActivity.this, "密码修改成功,请重新登录", Toast.LENGTH_SHORT)
                             .show();
                        ClearLocalDataUtils.clearLocalData();//清空所有本地存储
                        startActivity(new Intent(UserCenterActivity.this, RegisterActivity.class));
                        finish();
                        HomeActivity.HOME_ACTIVITY_INSTANCE.finish();
                    } else {//失败
                        ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                        onFailure(call, e.toException());
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(parentView, "密码修改失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                            .show();

                }
            });


        }
    }
}
