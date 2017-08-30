package shop.fly.com.shop.util;

import android.app.NotificationManager;
import android.content.Context;

import shop.fly.com.shop.application.MyApplication;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.service.MediaService;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NotifyMenagerUtil {
    public static void cancleOrder(){
        NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        MediaService.stopService(MyApplication.getContext());
    }

    public static NotificationManager getNotificationManager(){
        return (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
