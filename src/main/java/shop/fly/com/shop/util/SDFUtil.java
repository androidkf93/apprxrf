package shop.fly.com.shop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fly12 on 2017/6/3.
 */

public class SDFUtil {

    public static String getTime(){
        long lTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
        String format = sdf.format(lTime);
        return format;
    }

    public static String getHomeLeftTiem(){
        long lTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("M.d");
        String format = sdf.format(lTime);
        return format;
    }
    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str =formatter.format(curDate);
        return str;
    }

}
