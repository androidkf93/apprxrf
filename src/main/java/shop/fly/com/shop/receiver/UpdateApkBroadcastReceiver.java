package shop.fly.com.shop.receiver;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

import shop.fly.com.shop.application.MyApplication;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.SpUtils;

/**
 * Created by Administrator on 2017/8/25.
 */

public class UpdateApkBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.e(action, getClass());
        long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long cacheDownLoadId = SpUtils.getInstance(context).getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1l);
        switch (action){
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                LogUtil.e("下载完成", getClass());

                LogUtil.e("downLoadId= " + downLoadId, getClass());
                LogUtil.e("cacheDownLoadId= " + cacheDownLoadId, getClass());
                if (cacheDownLoadId == downLoadId) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = queryDownloadedApk(context);
                    install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
                break;
            case DownloadManager.ACTION_NOTIFICATION_CLICKED:
            case DownloadManager.EXTRA_DOWNLOAD_ID:
                LogUtil.e("点击通知栏", getClass());
                LogUtil.e("downLoadId= " + downLoadId, getClass());
                LogUtil.e("cacheDownLoadId= " + cacheDownLoadId, getClass());
                if (cacheDownLoadId == downLoadId) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = queryDownloadedApk(context);
                    install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
                break;
        }

    }
    public static File queryDownloadedApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = SpUtils.getInstance(context).getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1l);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }

        return targetApkFile;
    }
}
