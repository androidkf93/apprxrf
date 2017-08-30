package shop.fly.com.shop.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Yd on 2017/3/31.
 */

public class Md5Utils {
    public static String generateCode(String url){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(url.getBytes());
            final byte[] cipher = digest.digest();
            for (byte b : cipher) {
                final String hexString = Integer.toHexString(b & 0xff);
                buffer.append(hexString.length() == 1 ? "0" + hexString : hexString);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
