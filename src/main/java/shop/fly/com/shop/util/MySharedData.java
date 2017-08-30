package shop.fly.com.shop.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

/**
 * 
 * @author Ivan
 * 
 * @todo 操作MySharedPreferences类
 * 
 */
public class MySharedData {

	// SharedPreferences 数据库名字
	private static String dateName = "zct_demo";

	// SharedPreferences 数据库名字
	public static void setDateName(String dateName) {
		MySharedData.dateName = dateName;
	}

	/**
	 * 存储数据到SharedPreferences中
	 * 
	 * @param key
	 * @param value
	 */
	public static void sharedata_WriteString(Context context, String key,
			String value) {
		SharedPreferences.Editor sharedataEditor = context
				.getSharedPreferences(dateName, 0).edit();
		sharedataEditor.putString(key, value);
		sharedataEditor.commit();
	}

	/**
	 * 读取SharedPreferences中需要的数据
	 * 
	 * @param key
	 * @return value
	 */
	public static String sharedata_ReadString(Context context, String key) {
		SharedPreferences sharedata = context.getSharedPreferences(dateName, 0);
		String info ="";
		try {

			info=sharedata.getString(key, "");
		}catch (ClassCastException e){
			info=sharedata.getInt(key,0)+"";
		}
		return info;
	}

	/**
	 * 存储数据到SharedPreferences中
	 * 
	 * @param key
	 * @param value
	 */
	public static void sharedata_WriteInt(Context context, String key, int value) {
		SharedPreferences.Editor sharedataEditor = context
				.getSharedPreferences(dateName, 0).edit();
		sharedataEditor.putInt(key, value);
		sharedataEditor.commit();
	}

	/**
	 * 读取SharedPreferences中需要的数据
	 * 
	 * @param key
	 * @return value
	 */
	public static int sharedata_ReadInt(Context context, String key) {
		SharedPreferences sharedata = context.getSharedPreferences(dateName, 0);
		int anInt = -1;
		try {
			anInt = sharedata.getInt (key, 0);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return anInt;
	}

	/**
	 * 存储数据到SharedPreferences中
	 * 
	 * @param key
	 * @param value
	 */
	public static void sharedata_Writefloat(Context context, String key,
			float value) {
		SharedPreferences.Editor sharedataEditor = context
				.getSharedPreferences(dateName, 0).edit();
		sharedataEditor.putFloat(key, value);
		sharedataEditor.commit();
	}

	/**
	 * 读取SharedPreferences中需要的数据
	 * 
	 * @param key
	 * @return value
	 */
	public static float sharedata_Readfloat(Context context, String key) {
		SharedPreferences sharedata = context.getSharedPreferences(dateName, 0);
		float aFloat = -1;
		try {
			aFloat = sharedata.getFloat (key, 0);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return aFloat;
	}

	/**
	 * 存储数据到SharedPreferences中
	 * 
	 * @param key
	 * @param value
	 */
	public static void sharedata_WriteLong(Context context, String key,
			long value) {
		SharedPreferences.Editor sharedataEditor = context
				.getSharedPreferences(dateName, 0).edit();
		sharedataEditor.putLong(key, value);
		sharedataEditor.commit();
	}

	/**
	 * 读取SharedPreferences中需要的数据
	 * 
	 * @param key
	 * @return value
	 */
	public static long sharedata_ReadLong(Context context, String key) {
		SharedPreferences sharedata = context.getSharedPreferences(dateName, 0);
		long aLong = -1;
		try {
			aLong = sharedata.getLong (key, 0);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return aLong;
	}

	/**
	 * 存储 T 所有(string, int, float, long)数据 key为 T 字段名
	 * @param context 当前页面或AppContext.
	 * @param t 需要存储的类
     * @param <T> 需要存储的类
     */
	public static <T> void putAllDate(Context context, T t){
		if(context == null || t == null){
			return;
		}
		Class userClass = t.getClass ();
		try {
			Field[] fields = userClass.getDeclaredFields();
			for (Field field:
					fields ) {
				field.setAccessible(true);
				String name = t.getClass().getName() + field.getName ();
				Object obj = field.get (t);
				if(obj == null)
					continue;
				String type = field.getType ().toString ();
				switch (type){
					case "class java.lang.String":
						MySharedData.sharedata_WriteString (context.getApplicationContext(), name, obj.toString ());
						break;
					case "int":
						MySharedData.sharedata_WriteInt (context.getApplicationContext(), name, (Integer)obj);
						break;
					case "float":
						MySharedData.sharedata_Writefloat (context.getApplicationContext(), name, (Float) obj);
						break;
					case "long":
						MySharedData.sharedata_WriteLong (context.getApplicationContext(), name, (Long)obj);
						break;
                    case "boolean":
                        int i = ((boolean) obj) == true ? 1 : 0;
                        MySharedData.sharedata_WriteInt(context.getApplicationContext(), name, i);
                        break;
				}

			}
		} catch (IllegalAccessException e) {
			e.printStackTrace ();
		}
	}

	/**
	 * 清除缓存在SharedPreferences的数据
	 * @param context 当前页面或者AppContext.
	 * @param t 需要清除的类
	 * @param <T> 需要清除的类
     */
	public static <T> void cleanDate(Context context, T t){
		if(context == null || t == null){
			LogUtil.e("设置信息失败，context 或 数据为 null", MySharedData.class);
			return;
		}
		Class userClass = t.getClass ();
		try {
			Field[] fields = userClass.getDeclaredFields();
			for (Field field:
					fields ) {
				field.setAccessible(true);
				String name = t.getClass().getName() + field.getName ();
				Object obj = field.get (t);
				String type = field.getType ().toString ();
				switch (type){
					case "class java.lang.String":
						MySharedData.sharedata_WriteString (context.getApplicationContext(), name, obj == null ? "" : obj.toString ());
						break;
					case "int":
						MySharedData.sharedata_WriteInt (context.getApplicationContext(),name, obj == null ? 0 : (Integer)obj);
						break;
					case "float":
						MySharedData.sharedata_Writefloat (context.getApplicationContext().getApplicationContext(), name, obj == null ? 0 : (Float) obj);
						break;
					case "long":
						MySharedData.sharedata_WriteLong (context.getApplicationContext(), name, obj == null ? 0 : (Long)obj);
						break;
					case "boolean":
                        MySharedData.sharedata_WriteInt(context.getApplicationContext(), name, 0);
						break;
				}

			}
		} catch (IllegalAccessException e) {
			e.printStackTrace ();
		}
	}


	/**
	 * 获取缓存SharedPreferences中的数据
	 * @param context 当前页面或者AppContext.
	 * @param t 需要获取的类
	 * @param <T> 需要获取的类
     * @return 获取查询到的数据并返回实体类
     */
	public static <T> T getAllDate(Context context, T t){
		if(context == null || t == null){
			LogUtil.e("获取信息失败", MySharedData.class);
			return t;
		}
		Class userClass = t.getClass ();
		try {
			Field[] fields = userClass.getDeclaredFields ();
			for (Field field:
					fields ) {
				field.setAccessible(true);
				String name = t.getClass().getName() + field.getName ();
				String type = field.getType ().toString ();
				switch (type){
					case "class java.lang.String":
						field.set (t, MySharedData.sharedata_ReadString (context.getApplicationContext(), name));
						break;
					case "int":
						field.set(t, MySharedData.sharedata_ReadInt (context.getApplicationContext(), name));
						break;
					case "float":
						field.set(t, MySharedData.sharedata_Readfloat (context.getApplicationContext(), name));
						break;
					case "long":
						field.set(t, MySharedData.sharedata_ReadLong (context.getApplicationContext(), name));
						break;
					case "boolean":
                        int i = MySharedData.sharedata_ReadInt(context.getApplicationContext(), name);

                        field.set(t,  i == 1);
						break;
				}

			}
		} catch (IllegalAccessException e) {
			e.printStackTrace ();
		}
		return t;
	}
}
