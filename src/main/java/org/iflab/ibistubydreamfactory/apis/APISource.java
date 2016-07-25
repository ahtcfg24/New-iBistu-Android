package org.iflab.ibistubydreamfactory.apis;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
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

    public static String testToken;

    public static Boolean isHaveLogin = false;

    private APISource() {
        //构建httpclient
        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder ongoing = chain.request().newBuilder();

                if (MyApplication.API_KEY == null) {
                    Log.w(APISource.class.getSimpleName(), "ApiKey为空");
                } else {
                    //将ApiKey添加到请求头信息里
                    ongoing.addHeader("X-DreamFactory-Api-Key", MyApplication.API_KEY);
                }
//不使用token，只使用apikey
//                if (!isHaveLogin) {
//                    String token = SharedPreferenceUtil.getString(MyApplication.getAppContext(), MyApplication.SESSION_TOKEN);
//                    if (token != null && !token.isEmpty()) {
//                        //将token添加到请求头信息里
//                        ongoing.addHeader("X-DreamFactory-Session-Token", token);
//                    }
//                } else if (testToken != null) {
//                    //将token添加到请求头信息里
//                    ongoing.addHeader("X-DreamFactory-Session-Token", testToken);
//                }

                return chain.proceed(ongoing.build());
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
     * 处理错误信息
     * @param response
     * @return
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
     * 根据传入的Service类，动态返回所需的Service
     */
    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
