package shop.fly.com.shop.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;

import android.os.IBinder;

import java.io.IOException;
import shop.fly.com.shop.R;
import shop.fly.com.shop.util.LogUtil;

public class MediaService extends Service {
    private Uri uri;
    private static MediaPlayer mediaPlay;
    private static int defaultCountDown = 60;
    private static int countDown = defaultCountDown;
    private static CountDownTimer countDownTimer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("onCreate()", getClass());
        i= 0;
    }
    public static void stopService(Context context){
        Intent intent = new Intent(context, MediaService.class);
        intent.setAction("shop.fly.com.shop.android.intent.MEDIA");
        context.stopService(intent);
    }
int i = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        i++;
        LogUtil.e("onStartCommand(" + i + ")", getClass());
        if (intent.hasExtra("orderType")) {
            countDown = defaultCountDown;
            if(countDownTimer == null){
                initCountDownTimer();
            }else {
                countDownTimer.cancel();
                initCountDownTimer();
            }
            if(mediaPlay == null){
                mediaPlay = new MediaPlayer();
            }
            String orderType = intent.getStringExtra("orderType");
            switch (orderType){
                case "NEW_ORDER":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.new_order);
                    break;
                case "NEW_ORDER_ADDRESS":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.order_take_out);
                    break;
                case "REFUND_ORDER":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.refund_order);
                    break;
                case "ORDER_RING":
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ok);
                    break;
            }
            mediaPlay(uri);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initCountDownTimer() {
        countDownTimer = new CountDownTimer(countDown * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtil.e("countDown= " + millisUntilFinished, MediaService.class);
            }

            @Override
            public void onFinish() {
                LogUtil.e("countDown= onFinish()", MediaService.class);
                stopSelf();
            }
        };
        countDownTimer.start();
    }

    private void
    mediaPlay(Uri uri){
        if(mediaPlay.isPlaying()){
            mediaPlay.stop();
        }
        try {
            mediaPlay.reset();
            mediaPlay.setDataSource(this, uri);
            mediaPlay.setOnErrorListener((mp, what, extra) -> {
                LogUtil.e("onError(what= " + what + "extra= " + extra + ")", MediaService.class);
                return false;
            });
            mediaPlay.setOnCompletionListener(mp -> {
                LogUtil.e("播放完", MediaService.class);
                mediaPlay(uri);
            });
            mediaPlay.prepare();
            mediaPlay.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlay.isPlaying()){
            mediaPlay.stop();
            mediaPlay.release();
            mediaPlay = null;
            uri = null;
        }
        countDownTimer.cancel();
        countDownTimer = null;
        countDown = defaultCountDown;
        LogUtil.e("onDestroy()", getClass());
        i = 0;
    }

}