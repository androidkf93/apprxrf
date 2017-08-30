package shop.fly.com.shop.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;

/**
 * Created by Burns on 2016/09/28
 */
public class ApkUpdateUtils {
    public static final String TAG = ApkUpdateUtils.class.getSimpleName();

    private static Activity activity;
    private static ApkUpdateUtils apkUpdateUtils;
    public static ApkUpdateUtils getInstance(){
        if(apkUpdateUtils == null){
            apkUpdateUtils = new ApkUpdateUtils();
        }
        return apkUpdateUtils;
    }
    public void setVisionData(Activity activity) {
        this.activity = activity;
        OKHttpUtil.getInstance().getUrl(Url.GetVersion, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response){
                if(response.isSuccessful()){
                    String jsonStr = null;
                    try {
                        jsonStr = response.body().string();
                        LogUtil.e(jsonStr, ApkUpdateUtils.class);
                        JSONObject object = new JSONObject(jsonStr);
                        boolean result = object.getBoolean("Result");
                        if(result){
                            String data = object.getString("Data");
                            String[] split = data.split(",");
                            if(split.length == 2){
                                Constant.versionCode = split[0];
                                Constant.DOWN_LODE_APP = split[1];
                                TaskExecutor.runTaskOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isUpdateVersion(activity);
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public boolean isUpdateVersion(Activity activity) {
        boolean update = false;
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            int version = info.versionCode;
            if (Integer.parseInt(Constant.versionCode) > version) {
                showPopuwindow();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 1f;
            activity.getWindow().setAttributes(lp);
        }
        return update;
    }


    private View v;
    private PopupWindow popup;

    private void showPopuwindow() {
        v = activity.getLayoutInflater().inflate(
                R.layout.update_version, null);
        setPopopuWindow(v);
        initIssusUI();
    }

    public void setPopopuWindow(View popopuWindow) {
        popup = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        popup.setAnimationStyle(R.style.mypopwindow_anim_alpha_style);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popup.setBackgroundDrawable(cd);//设置该属性后点击外部消失才会起作用
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);
        popup.setFocusable(true);
        popup.setTouchable(true);
        popup.setOutsideTouchable(false);
        popup.showAtLocation(v, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = activity.getWindow().getAttributes();
            lp1.alpha = 1f;
            activity.getWindow().setAttributes(lp1);
        });
    }

    private void initIssusUI() {
        TextView title = (TextView) v.findViewById(R.id.tv_title);
        title.setText("更新：" + Constant.versionCode);
        TextView next = (TextView) v.findViewById(R.id.btn_yes);
        TextView no = (TextView) v.findViewById(R.id.btn_no);
        next.setOnClickListener(v1 -> {
            FinalToast.ErrorContext(activity, "正在下载安装包，通知栏可查看进度");
            String mApkAddress = Constant.DOWN_LODE_APP;
            if (!canDownloadState()) {
                FinalToast.ErrorContext(activity, "下载服务不可用,请您启用");
                showDownloadSetting();
                return;
            }
            ApkUpdateUtils.download(activity, mApkAddress, activity.getResources().getString(R.string.app_name));
            popup.dismiss();
        });
        no.setOnClickListener(v12 -> popup.dismiss());
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            activity.startActivity(intent);
        }
    }
    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean canDownloadState() {
        try {
            int state = activity.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void download(Context context, String url, String title) {
        long downloadId = SpUtils.getInstance(context).getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (downloadId != -1L) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(context);
            int status = fdm.getDownloadStatus(downloadId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                start(context, url, title);
            } else if (status == DownloadManager.STATUS_FAILED) {
                start(context, url, title);
            } else {
                Log.d(TAG, "apk is already downloading");
            }
        } else {
            start(context, url, title);
        }
    }

    private static void start(Context context, String url, String title) {
        long id = FileDownloadManager.getInstance(context).startDownload(url,
                title, "下载完成后点击安装");
        SpUtils.getInstance(context).putLong(DownloadManager.EXTRA_DOWNLOAD_ID, id);
        Log.d(TAG, "apk start download " + id);
    }

}


