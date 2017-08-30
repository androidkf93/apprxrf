package shop.fly.com.shop.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.custom.GetProgressDialog;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.mvp.view.IParentView;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;

/**
 * Created by 陈亚飞 on 2016/6/20.
 */
public abstract class GroupItemFragment extends Fragment implements IParentView {

    private Context context;
    private ProgressDialog dialog ;
    private String TAG= "TAG_NAME";
    private ITitle iTitle;
    private GroupItemFragment mFragment;


    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        this.context = context;
         mFragment=this;
        iTitle = (ITitle) context;
        dialog = GetProgressDialog.getProgressDialog (context);

    }
    public void logname(Fragment fr){
        Log.d(TAG,fr.getClass().getName());
    };
    //滑动到 A 页 回调用 A-1 和 A+1 页面的onCreadeView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView (inflater, container);
        if (view == null)
            return super.onCreateView (inflater, container, savedInstanceState);
        ButterKnife.bind (this, view);
        initDate(context);
        if(iTitle != null){
            iTitle.cleanTitle ();
            setiTitle(iTitle);
        }
        setDate ();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getDate (context);

        super.onCreate (savedInstanceState);
    }
    /**
     * 获取视图
     * @param inflater
     * @param container
     * @return 视图
     */
    protected abstract View getView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化数据在setDate()之前调用
     * @param context 当前页
     */
    protected  abstract void initDate(Context context);
    /**
     * 获取数据(只会调用一次)
     * @param context 当前页
     */
    protected abstract void getDate(Context context);

    /**
     * 设置数据
     */
    protected abstract void setDate();

    /**
     * 设置顶部title显示控件
     */
    protected abstract void setiTitle(ITitle iTitle);

    public void showLoading() {
        // TODO Auto-generated method stub
        if(getActivity() instanceof GroupFragmentActivity){
            ((GroupFragmentActivity) getActivity()).showLoading();
            return;
        }
        if(dialog != null && !dialog.isShowing())
            dialog.show();
    }

    @Override
    public void hideLoading() {
        // TODO Auto-generated method stub
        if(getActivity() instanceof GroupFragmentActivity){
            ((GroupFragmentActivity) getActivity()).hideLoading();
            return;
        }
        if(dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    @Override
    public void noNetWork() {
        // TODO Auto-generated method stub
       hideLoading();
        showError (context.getResources ().getString (R.string.nw_error_10004));
    }


    @Override
    public void showError( String str) {
        // TODO Auto-generated method stub
        hideLoading();
        if(getActivity() instanceof GroupFragmentActivity){
            ((GroupFragmentActivity) getActivity()).showError(str);
            return;
        }

//        showError (status, str);

        if(!str.isEmpty ()){
            if(str.contains ("失败")){
                GetProgressDialog.showError (context, str, 1000);
            }else
            if(str.contains ("成功")){
                GetProgressDialog.showSuccess (context, str, 1000);
            }else{
                GetProgressDialog.showError (context, str, 1000);
            }

        }
    }
    @Override
    public void showNoNetWorkdate() {
        // TODO Auto-generated method stub
        FinalToast.ErrorStrId(context, R.string.nw_error_10002);
    }


}
