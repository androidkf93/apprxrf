package shop.fly.com.shop.interfaces;


/**
 * Created by Administrator on 2017/5/18.
 */

public interface NetWorkCallBack {
    <T extends Object> void onSuccess(T t, Class cla);
    void onError(String errorMeg);
}
