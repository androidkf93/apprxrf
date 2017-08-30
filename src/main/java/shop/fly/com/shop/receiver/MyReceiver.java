package shop.fly.com.shop.receiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import shop.fly.com.shop.R;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.manager.ActivityManager;
import shop.fly.com.shop.service.MediaService;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.parent.ParentActivity;
import shop.fly.com.shop.ui.activity.parent.ParentFragmentActivity;
import shop.fly.com.shop.ui.fragment.OrderFragment;
import shop.fly.com.shop.util.LogUtil;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
    private String cancelReason;
    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        type 为REFUND_ORDER NEW_ORDER

        if(TextUtils.isEmpty(Constant.logUser.getName()) || Constant.logUser.getId() == 0)
            return;
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "message = " + message);
        Log.e(TAG, "extras = " + extras);
        if(TextUtils.isEmpty(message) || TextUtils.isEmpty(extras))
            return;
        String orderIndex = "";
        String address = "";
        String deskNO = "";
        try {
            JSONObject json = new JSONObject(message);
            orderIndex = json.getString("OrderIndex");
            address = json.getString("Address");
            deskNO = json.getString("DeskNO");
            cancelReason = json.getString("CancelReason");

            extras = new JSONObject(extras).getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(extras.equals("REFUND_ORDER") || extras.equals("NEW_ORDER") || extras.equals("ORDER_RING") && !TextUtils.isEmpty(orderIndex)){
            Activity lastActivity = ActivityManager.getLastActivity();
            if(lastActivity instanceof MainActivity){
                Fragment fragment = ((MainActivity) lastActivity).getSelectPositionFragment();
                if(fragment instanceof OrderFragment){
                    ((OrderFragment) fragment).refresh(extras);
                }
            }
            notification(context, orderIndex, address, deskNO, extras);
        }
    }

    private void notification(Context context, String orderIndex, String address, String deskNO, String extras) {
        String message = "";
        Log.e(TAG, deskNO);
        int notificationId = 1000;
        try {
            notificationId = Integer.parseInt(orderIndex);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String content = "订单序号：" + orderIndex;
        switch (extras){
            case "NEW_ORDER":
                message = "您有新的订单";
                if(TextUtils.isEmpty(address) || address.equals("null")){
                }else{
                    extras = "NEW_ORDER_ADDRESS";
                }
                break;
            case "REFUND_ORDER":
                content += "[" + cancelReason + "]";
                message = "您有新的退款订单";
                break;
            case "ORDER_RING":
                deskNO = TextUtils.isEmpty(deskNO) ? "" : deskNO;
                message = deskNO + "号桌客户正在呼叫";
                Activity lastActivity = ActivityManager.getLastActivity();
                if(lastActivity instanceof ParentFragmentActivity){
                    LogUtil.e("ParentFragmentActivity", getClass());
                    ((ParentFragmentActivity) lastActivity).showCallSeatNum(deskNO);
                }else if(lastActivity instanceof ParentActivity){
                    LogUtil.e("ParentActivity", getClass());
                    ((ParentActivity) lastActivity).showCallSeatNum(deskNO);
                }
                break;
        }
        Intent intentService = new Intent(context, MediaService.class);
        intentService.setAction("shop.fly.com.shop.android.intent.MEDIA");
        intentService.putExtra("orderType", extras);
        context.startService(intentService);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("itemPosition", extras.equals("REFUND_ORDER") ? 1 : 0);
        intent.putExtra("orderType", extras);

        if(extras.equals("ORDER_RING")){
            intent.putExtra("deskNO", deskNO);
        }

        NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).
                        setLargeIcon(bd.getBitmap())
                .setContentTitle(message)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS)
                .setContentText(content)
                .setAutoCancel(true);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        n.setContentIntent(pIntent);
        nManager.notify(notificationId, n.build());
    }
}
