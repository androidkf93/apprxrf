package shop.fly.com.shop.manager;

import android.app.Activity;
import android.content.Intent;


import java.util.ArrayList;

import shop.fly.com.shop.ui.activity.MainActivity;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ActivityManager {
    public static ArrayList<Activity> activityManager;

    public static ArrayList<Activity> FactoryActivityManager(){
        if(activityManager == null)
            activityManager = new ArrayList<Activity> ();
        return activityManager;
    }

    public static void add(Activity activity){
        if(activityManager == null){
            FactoryActivityManager ();
        }
        activityManager.add (activity);
    }


    public static Activity getLastActivity(){
        if(activityManager == null){
            FactoryActivityManager ();
            return null;
        }
        final Activity activity = activityManager.get(activityManager.size() - 1);
        return activity;
    }

    public static void finish(Activity activity){
        if(activityManager == null){
            FactoryActivityManager ();
        }
        if(activityManager.size () > 0 ){
            if(activityManager.remove (activity)){
                activity.finish ();
            }
        }
    }


    public static void finishAllContainMain(boolean isContain){
        if(activityManager == null){
            activityManager = FactoryActivityManager();
        }
        while (activityManager.size () > 0){
            Activity activity = activityManager.remove(0);
            try {
                if(isContain){
                    activity.finish();
                }else
                if(activity.getClass() != MainActivity.class){
                    activity.finish ();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void finishSetResult(Activity activity, int resultCode, Intent intent){
        if(activityManager != null && activityManager.size () > 0 ){
            if(activityManager.remove (activity)){
                if(intent != null){
                    activity.setResult (resultCode, intent);
                }else{
                    activity.setResult (resultCode);
                }
                activity.finish ();
            }
        }
    }
}
