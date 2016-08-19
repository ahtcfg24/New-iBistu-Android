package org.iflab.ibistubydreamfactory.apis;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.SharedPreferenceUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 用于处理request
 */
public class APISource {

    private static APISource INSTANCE;//这个类的唯一对象

    private Retrofit retrofit;//retrofit2对象

    private OkHttpClient httpClient;//okHttp对象

    private static Converter<ResponseBody, ErrorMessage> errorConverter;

    public static String token;//当前登录的token
    private String toRefreshToken;//用于刷新token的旧token
    private ACache aCache = ACache.get(MyApplication.getAppContext());


    private APISource() {
        //构建httpclient
        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder request = chain.request().newBuilder();
                //将ApiKey添加到请求头信息里
                request.addHeader("X-DreamFactory-Api-Key", MyApplication.API_KEY);
                toRefreshToken = SharedPreferenceUtil.getString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN");
                token = aCache.getAsString("SESSION_TOKEN");
                if (toRefreshToken != null && !toRefreshToken.isEmpty()) {//如果旧token存在，说明已经登录了
                    if (token != null && !token.isEmpty()) {//token不为空时说明token没有过期
                        //将token添加到请求头信息里
                        request.addHeader("X-DreamFactory-Session-Token", token);
                    } else {//否则说明token已经过期，需要刷新token
                        AuthAPI authAPI = getInstance().getAPIObject(AuthAPI.class);
                        Call<User> call=authAPI.refreshToken();
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {//如果登录成功
                                    User user = response.body();
                                    aCache.put("user",user,24*ACache.TIME_HOUR);//保存user对象
                                    //记录token,保存到缓存是位了检测token是否过期，保存到preference是为了刷新token时读取旧token
                                    SharedPreferenceUtil.putString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN", user
                                            .getSessionToken());
                                    aCache.put("SESSION_TOKEN",user.getSessionToken(),24*ACache.TIME_HOUR);//token保存24小时
                                    System.out.println("刷新token成功");
                                } else {//刷新失败
                                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                                    onFailure(call, e.toException());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                System.out.println("刷新token失败：Throwable是{" + t.getMessage() + "}");
                            }
                        });
                    }
                }

                return chain.proceed(request.build());
            }
        }).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //构建retrofit对象
        retrofit = new Retrofit.Builder().baseUrl(MyApplication.INSTANCE_URL)
                                         .client(httpClient)
                                         .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                                         .build();

        errorConverter = retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);
    }

    /**
     * 处理接口访问错误时返回的信息
     */
    public static ErrorMessage getErrorMessage(Response response) {
        ErrorMessage error = null;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            error = new ErrorMessage("未知错误");
            Log.e("ERROR", "序列化错误信息时出现未知错误", e);
        }
        return error;
    }

    /**
     * 获得唯一的APISource实例来调用API
     *
     * @return 唯一的APISource
     */
    public synchronized static APISource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new APISource();
        }
        return INSTANCE;
    }

    /**
     * 根据传入的API类，动态返回所需的API对象
     */
    public <T> T getAPIObject(Class<T> apiClass) {
        return retrofit.create(apiClass);
    }
}
