package shop.fly.com.shop.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.util.Md5Utils;
import shop.fly.com.shop.util.YdPermissionUtils;

/**
 * Created by Administrator on 2017/7/17.
 */

public class QRFragment extends GroupItemFragment {
    @BindView(R.id.img_qr)
    ImageView imgQr;
    @BindView(R.id.btn_save)
    Button btnSave;

    File saveimageFile;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    protected void initDate(Context context) {

    }

    @Override
    protected void getDate(Context context) {

    }

    @Override
    protected void setDate() {
//        String url = Url.URL + "/home/StoreQrc/" + Constant.logUser.getId();
//        Glide.with(getContext()).load(Constant.logUser.getQrcCodeImg()).into(imgQr);
        DrawableTypeRequest<String> load = Glide.with(getContext()).load(Constant.logUser.getQrcCodeImg());
        load.asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    saveimageFile = getFileByName(Constant.logUser.getQrcCodeImg());
                    FileOutputStream fileout = new FileOutputStream(saveimageFile);
                    fileout.write(bytes);
                    final Bitmap bitmap = BitmapFactory.decodeFile(saveimageFile.getAbsolutePath());
                    getActivity().runOnUiThread(() -> imgQr.setImageBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftText(null, "门店二维码", v -> getActivity().finish());
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        iTitle.setTitleText("");
    }
    @OnClick(R.id.btn_save)
    void onClick(){

        YdPermissionUtils.requestPermissions(getActivity(), 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new YdPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                new AlertDialog.Builder(getActivity()).setMessage("保存二维码到本地相册？").setPositiveButton("确定", (dialog, which) -> saveimage2Gallery()).show();
            }

            @Override
            public void onPermissionDenied() {
                new AlertDialog.Builder(getActivity()).setMessage("拒绝该权限将导致某些功能无法使用哦,请至设置界面手动开启").setPositiveButton("去设置", (dialogInterface, i) -> getAppDetailSettingIntent()).show();
            }
        });
    }


    private void saveimage2Gallery(){
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    saveimageFile.getAbsolutePath(), fileName, null);
            Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //通知图库刷新
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + saveimageFile.getAbsolutePath())));
    }

    String fileName;
    private File getFileByName(String url){
        File parent;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            parent = getActivity().getExternalCacheDir();
        }else{
            parent = getActivity().getCacheDir();
        }
        fileName = Md5Utils.generateCode(url);
        File file = new File(parent, fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
 /**
    * 用户拒绝权限之后跳转到应用设置界面打开权限
    */
    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getContext().getPackageName());
        }
        startActivity(localIntent);
    }
/*
    //往SD卡写入文件的方法
    public void savaFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/budejie";
            File dir1 = new File(filePath);
            if (!dir1.exists()){
                dir1.mkdirs();
            }
            filename = filePath+ "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
            Toast.makeText(getContext(), "图片已成功保存到"+ filePath, Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getContext(), "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }*/
}
