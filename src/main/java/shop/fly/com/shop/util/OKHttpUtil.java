package shop.fly.com.shop.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.NetWorkCallBack;

/**
 * Created by Administrator on 2017/5/18.
 */

public class OKHttpUtil {
    private static OKHttpUtil okHttpUtil;
    public static OKHttpUtil getInstance(){
        if(okHttpUtil == null)
            okHttpUtil = new OKHttpUtil();
        return okHttpUtil;
    }

    public void getUrl(String url, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        builder.addHeader("cookie", "user=" + Constant.user);
        LogUtil.e("cookie= " + "user=" + Constant.user, OKHttpUtil.class);
        Request request = null;
        request = builder.get().get().url(url).build();
        LogUtil.fromNetWorkUrl(url, OKHttpUtil.class);
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void getArrayUrl(String url, final Class cla, final NetWorkCallBack callBack){
        TaskExecutor.executeNetTask(() -> {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
            Request.Builder builder = new Request.Builder();
            builder.addHeader("cookie", "user=" + Constant.user);
            LogUtil.e("cookie= " + "user=" + Constant.user, OKHttpUtil.class);
            Request request = null;
            request = builder.get().get().url(url).build();
            LogUtil.fromNetWorkUrl(url, OKHttpUtil.class);
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String strJson = response.body().string();
                        LogUtil.e(strJson, this.getClass());
                        try {
                            JSONObject jsonObject = new JSONObject(strJson);
                            boolean result = jsonObject.getBoolean("Result");
                            String message = jsonObject.getString("Message");
                            if (result) {
                                JSONArray array = jsonObject.getJSONArray("Data");
                                Gson gson = new Gson();
                                List list = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++){
                                    list.add(gson.fromJson(array.get(i).toString(), cla));
                                }
                                TaskExecutor.runTaskOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onSuccess(list, cla);
                                    }
                                });
                            } else {
                                TaskExecutor.runTaskOnUiThread(() -> {
                                    callBack.onError(message);
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            TaskExecutor.runTaskOnUiThread(() -> {
                                callBack.onError("数据解析异常");
                            });
                        }
                    } else {
                        TaskExecutor.runTaskOnUiThread(() -> {
                            callBack.onError("数据解析异常");
                        });
                    }
                }
            });
        });
    }

    public <T> void getUrl(final String url, final Class<T> cla, final NetWorkCallBack callBack) {
        TaskExecutor.executeNetTask(() ->{
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
            Request.Builder builder = new Request.Builder();
            builder.addHeader("cookie", "user=" + Constant.user);
            LogUtil.e("cookie= " + "user=" + Constant.user, OKHttpUtil.class);
            Request request = null;
            request = builder.get().get().url(url).build();
            LogUtil.fromNetWorkUrl(url, OKHttpUtil.class);
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        String json = response.body().string();
                        try {
                            JSONObject jsonObj = new JSONObject(json);
                            boolean result = jsonObj.getBoolean("Result");
                            String message = jsonObj.getString("Message");
                            if(result && callBack != null){
                                if(cla == null){
                                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.onSuccess(null, null);
                                        }
                                    });
                                }else{
                                    final Object obj = gson.fromJson(json, cla);
                                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.onSuccess(obj, cla);
                                        }
                                    });
                                }
                            }else{
                                TaskExecutor.runTaskOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onError(message);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        callBack.onError("数据解析异常");
                    }
                }
            });});

    }



    public void postUrl(String url, String json, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        if(!url.equals(Url.MstchingUserBind)){
            builder.addHeader("cookie", "user=" + Constant.user);
            LogUtil.e("cookie= " + "user=" + Constant.user, OKHttpUtil.class);
        }
        Request request = null;
        RequestBody requestBodyPost = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request =  builder.url(url).post(requestBodyPost).build();
        LogUtil.fromNetWorkUrl("json_" + json, OKHttpUtil.class);
        LogUtil.fromNetWorkUrl(url, OKHttpUtil.class);

        client.newCall(request).enqueue(callback);
    }
    public void postUrl(String url, Map<String, String> map, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
//        Request.Builder builder = new Request.Builder();

        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }
        RequestBody requestBodyPost = builder.build();
        Request request = new Request.Builder().url(url).post(requestBodyPost).build();
        client.newCall(request).enqueue(callback);
    }
    public void postUrl(String url, String json, final Class cla, final NetWorkCallBack callBack){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        builder.addHeader("cookie", "user=" + Constant.user);
        LogUtil.e("cookie= " + "user=" + Constant.user, OKHttpUtil.class);
        Request request = null;
        RequestBody requestBodyPost = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request =  builder.url(url).post(requestBodyPost).build();
        LogUtil.fromNetWorkUrl("json_" + json, OKHttpUtil.class);
        LogUtil.fromNetWorkUrl(url, OKHttpUtil.class);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String strJson = response.body().string();
                    LogUtil.e("onResponse: " + strJson, this.getClass());
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        boolean result = jsonObject.getBoolean("Result");
                        String message = jsonObject.getString("Message");
                        if(result){
                            if(cla == null){
                                TaskExecutor.runTaskOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.onSuccess(result, cla);
                                    }
                                });

                            }else{
                                String data = jsonObject.getJSONObject("Data").toString();
                                if(data != null){
                                    Gson gson = new Gson();
                                    Object obj = gson.fromJson(data, cla);
                                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.onSuccess(obj, cla);
                                        }
                                    });

                                }
                            }
                        }else{
                            TaskExecutor.runTaskOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onError(message);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        TaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError("数据解析异常");
                            }
                        });

                    }

                }
            }
        });
    }

}
