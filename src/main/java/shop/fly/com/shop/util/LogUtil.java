package shop.fly.com.shop.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


/**
 *	打印异常信息类(包含访问网)， 默认开启访问网络数据状态码捕捉并提示错误码对应的错误信息，可以手动关闭。
 *
 * @author 陈亚飞
 *
 */
public class LogUtil {

	private static boolean isLog = true;
	//	private static boolean isNetLog = true;
	private static boolean isUrlLog = true;
	private static boolean isUrlDateLog = false;
	private String TAG = getClass().getSimpleName();
	static LogUtil logUtil;
	public static LogUtil facotry(){
		if(logUtil == null){
			logUtil = new LogUtil();
			return logUtil;
		}
		return logUtil;
	}

	/**
	 * 设置全部开关状态。
	 * @param b true 全部打开  false 全部关闭。
	 */
	public static void setAllStatus(boolean b){
		if (isLog)
			LogUtil.isLog = b;
		if (isUrlLog)
			LogUtil.isUrlLog = b;
		if(isUrlDateLog)
			LogUtil.isUrlDateLog = b;
	}
	/**
	 * 是否开启其它信息提示
	 * @param isLog true or false
	 * @return
	 */
	public static LogUtil setLog(boolean isLog) {
		if(LogUtil.isLog)
			LogUtil.isLog = isLog;
		return logUtil;
	}



	/**
	 * 设置是否Url打印  当isLog 为 true 时开启异常和Url打印
	 * @param isUrlLog true or false
	 */
	public static LogUtil setUrlLog(boolean isUrlLog) {
		if(LogUtil.isUrlLog)
			LogUtil.isUrlLog = isUrlLog;
		return logUtil;
	}

	/**
	 * 设置是否开启获取网络数据打印
	 * @param isUrlDateLog true or false
	 */
	public static LogUtil setUrlDateLog(boolean isUrlDateLog) {
		if(LogUtil.isUrlDateLog)
			LogUtil.isUrlDateLog = isUrlDateLog;
		return logUtil;
	}

	public static void i(String logmsg, Class<?> cla){
		if(isLog)
			if(cla == null){
				Log.i ("tag_"  + "=", logmsg);
			}else{
				Log.i ("tag_"  + cla.getSimpleName() + "=", logmsg);
			}
	}

	public static void e(String logmsg, Class<?> cla){
		if(isLog)
			if(cla == null){
				Log.e ("tag_"  + "=", logmsg);
			}else{
				Log.e ("tag_"  + cla.getSimpleName() + "=", logmsg);
			}
	}

	public static void d(String logmsg, Class<?> cla){
		if(isLog)
			if(cla == null){
				Log.d ("tag_"  + "=", logmsg);
			}else{
				Log.d ("tag_"  + cla.getSimpleName() + "=", logmsg);
			}

	}

	/**
	 * 打印访问的接口地址
	 * @param url 需要打印的url地址
	 * @param cla 类名
	 */
	public static void fromNetWorkUrl(String url, Class<?> cla){
		if(isUrlLog)
			if(cla == null){
				Log.i("tag_url_" + "=", url);
			}else{
				Log.i("tag_url_" + cla.getSimpleName() + "=", url);
			}
	}

	/**
	 * 打印访问接口地址获得的数据
	 * @param urlDate 需要打印的数据
	 * @param cla 类名
	 */
	public static void fromNetWorkUrlDate(String urlDate, Class<?> cla){
		if(isUrlDateLog)
			if(cla == null){
				Log.i("tag_url_date_" + "=", urlDate);
			}else{
				Log.i("tag_url_date_" + cla.getSimpleName() + "=", urlDate);
			}
	}



	/***
	 * 未知错误接口
	 */
	public interface AnUnknownError{
		public void toAnUnknownError(String json);
	}

	public static AnUnknownError mAnUnknownError ;


}
