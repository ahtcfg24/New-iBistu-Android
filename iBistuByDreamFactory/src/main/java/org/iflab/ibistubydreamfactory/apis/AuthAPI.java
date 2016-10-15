package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.ChangePasswordRequestBody;
import org.iflab.ibistubydreamfactory.models.LoginRequestBody;
import org.iflab.ibistubydreamfactory.models.RefreshTokenRequestBody;
import org.iflab.ibistubydreamfactory.models.RegisterRequestBody;
import org.iflab.ibistubydreamfactory.models.ResetPasswordRequestBody;
import org.iflab.ibistubydreamfactory.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * 认证接口
 */
public interface AuthAPI {
    /**
     * 登录接口
     *
     * @param loginRequestBody 登录请求体
     */
    @POST("user/session?remember_m=true")
    Call<User> login(@Body LoginRequestBody loginRequestBody);

    /**
     * 刷新token
     */
    @PUT("user/session")
    Call<User> refreshToken(@Body RefreshTokenRequestBody refreshTokenBody);

    /**
     * 注册接口
     *
     * @param registerRequestBody 注册请求体
     */
    @POST("user/register")
    Call<ResponseBody> register(@Body RegisterRequestBody registerRequestBody);

    /**
     * 退出登录
     */
    @DELETE("user/session")
    Call<ResponseBody> logout();

    /**
     * 修改密码接口
     *
     * @param changePasswordRequestBody 修改密码请求体
     */
    @POST("user/password")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequestBody changePasswordRequestBody);

    /**
     * 请求发送重置密码邮件接口
     *
     * @param resetPasswordRequestBody 重置密码请求体
     */
    @POST("user/password?reset=true")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequestBody resetPasswordRequestBody);
}
