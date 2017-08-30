package shop.fly.com.shop.retrofit;

import android.content.Context;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.parent.ParentFragmentActivity;
import shop.fly.com.shop.util.LogUtil;

/**
 * Created by Administrator on 2017/8/7.
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private static final String TAG = "BaseObserver";
    private Context mContext;
    private boolean isHideShowWithSuccess;

    public BaseObserver(Context mContext) {
        this.mContext = mContext;
        isHideShowWithSuccess = true;
    }
    public BaseObserver(Context mContext, boolean isHideShowWithSuccess) {
        this.mContext = mContext;
        this.isHideShowWithSuccess = isHideShowWithSuccess;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull BaseBean<T> tBaseBean) {
        if(tBaseBean.isResult()){
            onHandleSuccess(tBaseBean.getData());
        }else {
            onHandleError(tBaseBean.getMessage());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e(e.toString(), BaseObserver.class);
    }

    @Override
    public void onComplete() {
        LogUtil.d("onComplete()", BaseObserver.class);
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        FinalToast.ErrorContext(mContext, msg);
        if(mContext instanceof MainActivity)
            ((MainActivity) mContext).showError(msg);
        else if(mContext instanceof ParentFragmentActivity)
            ((ParentFragmentActivity) mContext).showError(msg);
    }

}
