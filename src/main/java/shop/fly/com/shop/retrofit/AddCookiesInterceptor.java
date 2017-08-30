package shop.fly.com.shop.retrofit;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import shop.fly.com.shop.constant.Constant;

/**
 * Created by Administrator on 2017/8/7.
 */

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        Observable.just(Constant.user)
                .subscribe(cookie -> {
                    builder.addHeader("Cookie", cookie);
                });
        return chain.proceed(builder.build());
    }
}
