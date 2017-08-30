package shop.fly.com.shop.custom;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shop.fly.com.shop.R;
import shop.fly.com.shop.service.MediaService;
import shop.fly.com.shop.ui.activity.parent.ParentActivity;
import shop.fly.com.shop.util.LogUtil;


public class GetProgressDialog {
	public static int longTime = 1000;

	public void setLongTime(int longTime) {
		this.longTime = longTime;
	}

	public static ProgressDialog getProgressDialog(Context context){
		ProgressDialog dialog = new ProgressDialog(context);
			dialog.setMessage("正在获取，请等待..");
			dialog.setIndeterminate(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
		return dialog;
	}
	public static  void cancel(ProgressDialog dialog){
		if(dialog != null && dialog.isShowing()){
			dialog.cancel();
			dialog.dismiss();
			dialog = null;
		}
	}
	public static ProgressDialog getUpDialog(Context context){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMax (100);
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		return dialog;
	}

	/**
	 * 正确提示框
	 * @param context
	 * @param msg 提示信息
	 * @param longtime 提示时长
     */
	public static void showSuccess(Context context, String msg, int longtime){

		try {
			final Dialog dialog = getDialog (context, R.drawable.success, msg);
			dialog.show ();
			Handler handler = new Handler ();
			longTime = longtime;
			handler.postDelayed (new Runnable () {
                @Override
                public void run() {
                    dialog.cancel ();
                    dialog.dismiss ();
                }
            }, longtime);
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	/**
	 * 错误提示框
	 * @param context
	 * @param msg 提示信息
	 * @param longtime 提示时长
     */
	public static void showError(Context context, String msg, int longtime){
		try {
			final Dialog dialog = getDialog (context, R.drawable.error, msg);
			longTime = longtime;
			dialog.show ();
			Handler handler = new Handler ();
			handler.postDelayed (new Runnable () {
                @Override
                public void run() {
                    dialog.cancel ();
                    dialog.dismiss ();
                }
            }, longtime);
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	public static void showTableCall(Context context, String msg){

	}


	/**
	 * 警告提示框
	 * @param context
	 * @param msg 提示信息
	 * @param longtime 提示时长
     */
	public static void showWarning(Context context, String msg, int longtime){
		try {
			final Dialog dialog = getDialog (context, R.drawable.warning, msg);
			longTime = longtime;
			dialog.show ();
			Handler handler = new Handler ();
			handler.postDelayed (new Runnable () {
                @Override
                public void run() {
                    dialog.cancel ();
                    dialog.dismiss ();
                }
            }, longtime);
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}



	public static Dialog getDialog(Context context, int resourcesId, String msg){
		Dialog dialog = new Dialog(context,R.style.ShareDialog);
		View view = View.inflate (context, R.layout.layout_popup_menu, null);
		ImageView img = (ImageView) view.findViewById (R.id.img_pop_memu);
		img.setImageResource (resourcesId);
//		img.setImageDrawable (drawable);
		TextView tv = (TextView) view.findViewById (R.id.tv_pop_memu);
		tv.setText (msg);
		dialog.setContentView (view);
		return dialog;
	}

	public static Dialog getCallFromSeatNum(Context context, String num) {
		Dialog dialog = new Dialog(context,R.style.ShareDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		View view = View.inflate (context, R.layout.layout_popup_call_seat_num, null);
		TextView tvPopTitle = (TextView) view.findViewById (R.id.tv_pop_title);
		tvPopTitle.setText(num + "号桌正在呼叫");
		TextView tv = (TextView) view.findViewById (R.id.tv_pop_memu);
		tv.setText (num + "号桌需要协助");

		view.findViewById(R.id.btn_yes).setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
			MediaService.stopService(context);
        });
		view.findViewById(R.id.btn_no).setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
			MediaService.stopService(context);
        });
		dialog.setContentView (view);
		return dialog;
	}
}
