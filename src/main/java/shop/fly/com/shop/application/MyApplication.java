package shop.fly.com.shop.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;
import shop.fly.com.shop.util.LogUtil;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MyApplication extends Application {
    private static  MyApplication application;
    public static MyApplication getInstance(){
        if (application == null)
            application = new MyApplication();
        return application;
    }

    public static Context getContext(){
        return getInstance().getApplicationContext();
    }
    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        LogUtil.setAllStatus(true);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
//        ZXingLibrary.initDisplayOpinion(this);
    }

    public static void startUpdateService() {
        // TODO Auto-generated method stub
        Intent mIntent = new Intent();
        mIntent.setAction("com.shop.UPDATE_SERVICE");//你定义的service的action
        mIntent.setPackage(getContext().getPackageName());//这里你需要设置你应用的包名
        getContext().startService(mIntent);
    }

    public static void stopUpdateService() {
        // TODO Auto-generated method stub
        Intent mIntent = new Intent();
        mIntent.setAction("com.shop.UPDATE_SERVICE");//你定义的service的action
        mIntent.setPackage(getContext().getPackageName());//这里你需要设置你应用的包名
        getContext().stopService(mIntent);
    }
}
