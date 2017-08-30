package shop.fly.com.shop.custom;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Message;
import android.widget.Toast;

public  class FinalToast {
	static Toast toast;
	public static void netTimeOutMakeText(Context context){
		Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
	}
	
	public static void ErrorMessage(Context context, Message msg){
		Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
	}
	public static void ErrorContext(Context context, String msg){
		if (toast == null)
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		toast.setText(msg);
		toast.show();
	}

	public static void ErrorStrId(Context context, int strId){
		try {
			String string = context.getResources().getString(strId);
			Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
