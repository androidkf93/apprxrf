package shop.fly.com.shop.retrofit;



import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import shop.fly.com.shop.BuildConfig;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;

/**
 * Created by Administrator on 2017/8/7.
 */

public class RetrofitFactory {
    private static final String BASE_URL = Url.URL;

    private static final long TIMEOUT = 30;

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置

    private static OkHttpClient httpClient = new OkHttpClient.Builder()

            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder().removeHeader("COOKIE");
                    builder.addHeader("COOKIE", "user=" + Constant.user);
                    return chain.proceed(builder.build());
                }
            })
            /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
            .addInterceptor(logInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create())
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitService.class);

    public static RetrofitService getInstance() {
        return retrofitService;
    }

    private static HttpLoggingInterceptor logInterceptor(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("OkHttp", "log = " + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }


}
