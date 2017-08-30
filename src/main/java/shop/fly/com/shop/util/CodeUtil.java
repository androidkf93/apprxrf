package shop.fly.com.shop.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Random;

public class CodeUtil {

	//获取屏幕的宽度
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
public static String timestamp(){
	int time = (int) System.currentTimeMillis() / 1000;
	return time + "";
}

public static String nonce(){
	Random random = new Random();
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < 9; i++) {
		sb.append(random.nextInt(9) + "");
	}
	return sb.toString();
}

	// MD5加密32位
	public static String MD5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	/**
	 * 解码（UTF-8）
	 * @param text
	 * @return
	 */
	public static String decoder(String text){
		try {
			return URLDecoder.decode(text, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
	/**
	 * 编码 （UTF-8）
	 * @param text
	 * @return
	 */
	public static String encode(String text){
		try {
			return URLEncoder.encode(text, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String priceDoubleNum(String price){
		if(price.indexOf(".") != -1){
			switch (price.substring(price.indexOf(".") + 1).length()){
				case 1:
					price = price + "0";
				break;
				case 2:
					if(price.indexOf(".") == 0)
						price = "0" + price;
				break;
				default:
					price = price.substring(0, price.indexOf(".") + 3);
					break;
			}
		}else{
			price = price + ".00";
		}
		return price;
	}


	private static final int[] SFZNum = new int[]{7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
	private static final int[] resultNum =new int[]{1,0,'X',9,8,7,6,5,4,3,2};

	/**
	 * 身份证号规则验证
	 * @param idNumber
	 * @return
     */
	public static boolean JudgeIDNumber(String idNumber){
		if(idNumber.length()==18){
			//			"7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 2"
			String s = idNumber.substring(0,17);
			System.out.println(s);
			String result = "";
			int sum = 0;
			int[] a = new int[17];
			for(int i=0;i<s.length();i++){
				a[i] = Integer.parseInt(s.substring(i,i+1));
			}
			for(int i=0;i<17;i++){
				sum = sum + SFZNum[i]*a[i];

			}
			sum = sum % 11;
			result = sum==2 ? "X":String.valueOf(resultNum[sum]);
			if(idNumber.substring(17, 18).equals(result)){
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void setText(TextView view, String data){
		if(TextUtils.isEmpty(data)){
			data = "";
		}
		view.setText(data);
	}
}
