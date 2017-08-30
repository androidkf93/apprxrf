package shop.fly.com.shop.ui.activity.parent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

import shop.fly.com.shop.R;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.custom.GetProgressDialog;
import shop.fly.com.shop.interfaces.UpLoadFileListener;
import shop.fly.com.shop.manager.ActivityManager;
import shop.fly.com.shop.mvp.view.IParentView;
import shop.fly.com.shop.service.MediaService;
import shop.fly.com.shop.util.CaptureImageUtil;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.PhotoUtil;


public class ParentActivity extends Activity implements IParentView {

	private Context context;
	private ProgressDialog dialog ;
	private UpLoadFileListener upLoadFileListener;
	private  Uri imgUri;
	protected int scalSize = 10 * 1024;
	private Dialog dialogSeatNum;
	private boolean isShow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = this;
		dialog = GetProgressDialog.getProgressDialog(context);
		super.onCreate(savedInstanceState);
		ActivityManager.add(this);
	}


	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		if(dialog != null && !dialog.isShowing())
			dialog.show();
	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		if(dialog != null && dialog.isShowing())
			dialog.cancel();
	}

	@Override
	public void noNetWork() {
		// TODO Auto-generated method stub
		hideLoading ();
		showError ( context.getResources ().getString (R.string.nw_error_10004));
	}

	@Override
	public void showError(String str) {
		// TODO Auto-generated method stub
		hideLoading();
		if(!str.isEmpty ()){
			if(str.indexOf ("失败") != -1){
				GetProgressDialog.showError (context, str, 1000);
			}else
			if(str.indexOf ("成功") != -1){
				GetProgressDialog.showSuccess (context, str, 1000);
			}else{
				GetProgressDialog.showError (context, str, 1000);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isShow = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isShow = false;
	}

	@Override
	public void showNoNetWorkdate() {
		// TODO Auto-generated method stub
		FinalToast.ErrorStrId(context, R.string.nw_error_10002);
	}

	public void showCallSeatNum(String num){
		if(isShow){
			if(dialogSeatNum != null){
				dialogSeatNum.dismiss();
				dialogSeatNum.cancel();
				dialogSeatNum = null;
			}


			dialogSeatNum = GetProgressDialog.getCallFromSeatNum(this, num);
			dialogSeatNum.show();
		}
	}

	private static Toast mToast = null;
	public void makeToast(String str){
		if (mToast == null) {
			mToast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		} else {
			mToast.setText(str);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
			// 拍照获取图片
			case CaptureImageUtil.GET_IMAGE_BY_CAMERA:
				// uri传入与否影响图片获取方式,以下二选一
				// 方式一,自定义Uri(ImageUtil.imageUriFromCamera),用于保存拍照后图片地址
				if (CaptureImageUtil.imageUriFromCamera != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//					iv.setImageURI(ImageUtil.imageUriFromCamera);
					// 对图片进行裁剪
					CaptureImageUtil.cropImage(this, CaptureImageUtil.imageUriFromCamera);
					break;
				}
				break;

			// 手机相册获取图片
			case CaptureImageUtil.GET_IMAGE_FROM_PHONE:
				if (data != null && data.getData() != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
					// iv.setImageURI(data.getData());
					// 对图片进行裁剪
					CaptureImageUtil.cropImage(this, data.getData());
				}
				break;

			// 裁剪图片后结果
			case CaptureImageUtil.CROP_IMAGE:
				if (CaptureImageUtil.cropImageUri != null) {
					// 可以直接显示图片,或者进行其他处理(如压缩等)
//					img_icon.setImageURI(CaptureImageUtil.cropImageUri);

					//外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
					ContentResolver resolver = getContentResolver();
					//此处的用于判断接收的Activity是不是你想要的那个
					try {
						imgUri = /*data.getData()*/CaptureImageUtil.cropImageUri; //获得图片的uri
						Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, imgUri);
						//这里开始的第二部分，获取图片的路径：
						String[] proj = {MediaStore.Images.Media.DATA};
						//好像是android多媒体数据库的封装接口，具体的看Android文档
						Cursor cursor = managedQuery(imgUri, proj, null, null, null);
						//按我个人理解 这个是获得用户选择的图片的索引值
						int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						//将光标移至开头 ，这个很重要，不小心很容易引起越界
						cursor.moveToFirst();
						//最后根据索引值获取图片路径
						String path = cursor.getString(column_index);
						File file = PhotoUtil.scal(path, scalSize);
						path = file.getPath();
//						OkhttpUtil.uploadFile(context, UpLoadeBean.class, Urls.UrlStreamfilePostPng, path, upLoadFileListener);
					} catch (IOException e) {
						Log.e("TAG-->Error", e.toString());
					}
				}
				break;
			default:
				break;
		}
	}

	public Uri getImgUri() {
		return imgUri;
	}

	public void setUpLoadFileListener(UpLoadFileListener upLoadFileListener) {
		this.upLoadFileListener = upLoadFileListener;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.finish(this);
		if(dialog != null){
			if(dialog.isShowing()){
				dialog.dismiss();
				dialog.cancel();
			}
			dialog = null;
		}
		if(dialogSeatNum != null){
			if(dialogSeatNum.isShowing()){
				dialogSeatNum.dismiss();
				dialogSeatNum.cancel();
			}
			dialogSeatNum = null;
		}
	}


}
