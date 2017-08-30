package shop.fly.com.shop.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class MyRequestPermission {
	private static final int REQUEST_CODE = 0;
	private static final int SIX_SYSTEM_PHONE = 23;
	private static MyRequestPermission requestPermission;
	static Activity mActivity;

	public static MyRequestPermission getInstance(Activity activity) {
		mActivity = activity;
		if (requestPermission == null) {

			requestPermission = new MyRequestPermission();
		}
		return requestPermission;
	}

	private String[] permissions = new String[] { 
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.CAMERA,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.CALL_PHONE
//			Manifest.permission.WRITE_APN_SETTINGS,

	};
	private boolean isChecked = false;
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isCheckPermission(String[] permission) {
		if (Build.VERSION.SDK_INT < SIX_SYSTEM_PHONE) {
			return true;
		}

		this.permissions= permissions;
		for (int i = 0; i < permission.length; i++) {
			if (mActivity.checkSelfPermission(permission[i])!= PackageManager.PERMISSION_GRANTED) {
				mActivity.requestPermissions(permission, REQUEST_CODE);
				return isChecked=false;
			}else {
				isChecked=true;
			}
		}
		return isChecked;
	}
	public boolean isCheckPermission() {
		if (Build.VERSION.SDK_INT < SIX_SYSTEM_PHONE) {
			return true;
		}
		
		for (int i = 0; i < permissions.length; i++) {

			if (mActivity.checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED) {
				mActivity.requestPermissions(permissions, REQUEST_CODE);
				return isChecked=false;
			}else {
				isChecked=true;
			}
		}
		return isChecked;
	}
	
	
	@TargetApi(Build.VERSION_CODES.M)
	private boolean isOK(){
		for (int i = 0; i < permissions.length; i++) {
			if (mActivity.checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED) {

			}
		}
		return false;
	}


	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		Log.w("回调", "+++++++++++++");
		if (requestCode == REQUEST_CODE) {
			if (grantResults!=null && grantResults.length > 0) {
				// Permission Granted
				
				for (int i = 0; i < grantResults.length; i++) {
					isChecked = false;
					if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
						isChecked=true;
					}else {
						isChecked=false;
						if (onCheckedRequestPermissionListener != null) {
							
							onCheckedRequestPermissionListener.isCheckError();
							onCheckedRequestPermissionListener=null;
						}
						return;
					}
				}
				if (isChecked) {
					if (onCheckedRequestPermissionListener != null) {
						Log.d("isCheckedOK", "onCheckedRequestPermissionListener-isCheckedOK");
						onCheckedRequestPermissionListener.isCheckedOK();	
						onCheckedRequestPermissionListener =null;
						}
				}else {
					if (onCheckedRequestPermissionListener != null) {
						
						onCheckedRequestPermissionListener.isCheckError();
						onCheckedRequestPermissionListener=null;
					}
				}
				
			} else {
				// Permission Denied
//				Toast.makeText(mActivity, "授权失败", 1).show();
			}
		}
	}
	OnCheckedRequestPermissionListener onCheckedRequestPermissionListener;
	public void setOnCheckedRequestPermissionListener(OnCheckedRequestPermissionListener listener){
		this.onCheckedRequestPermissionListener=listener;
	}
	public static interface OnCheckedRequestPermissionListener{
		public void isCheckedOK();
		public void isCheckError();
	}
}
