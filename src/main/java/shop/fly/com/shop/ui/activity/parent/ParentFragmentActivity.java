package shop.fly.com.shop.ui.activity.parent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.Login;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.custom.GetProgressDialog;
import shop.fly.com.shop.manager.ActivityManager;
import shop.fly.com.shop.mvp.view.IParentView;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.util.LogUtil;


public class ParentFragmentActivity extends FragmentActivity implements IParentView {
	private Context context;
	private ProgressDialog dialog ;
    private Dialog dialogSeatNum;
    private boolean isShow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		dialog = GetProgressDialog.getProgressDialog(context);
		if(context instanceof MainActivity){
			ActivityManager.finishAllContainMain(true);
		}
		ActivityManager.add(this);
	}


	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		if(dialog != null && !dialog.isShowing())
			dialog.show();
	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		if(dialog != null && dialog.isShowing())
			dialog.cancel();
	}

	@Override
	public void noNetWork() {
		// TODO Auto-generated method stub
		hideLoading ();
		showError (context.getResources ().getString (R.string.nw_error_10004));
	}

	@Override
	public void showError( String str) {
		// TODO Auto-generated method stub
		hideLoading();
		if(!str.isEmpty ()){
			if(str.indexOf ("失败") != -1){
				GetProgressDialog.showError (context, str, 1000);
			}else
			if(str.indexOf ("成功") != -1){
				GetProgressDialog.showSuccess (context, str, 1000);
			}else{
				GetProgressDialog.showError (context, str, 1000);
			}
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }

    public boolean isShow() {
        return isShow;
    }

    public ProgressDialog getParentDialog(){
		return dialog;
	}
	public  void forceShowCallSeatNum(String num){
		isShow = true;
		showCallSeatNum(num);
	}

	/**
	 * 显示呼叫功能
	 * @param num
     */
	public void showCallSeatNum(String num){
		if(isShow){
			LogUtil.e("showCallSeatNum( " + num + " )", getClass());
			if(dialogSeatNum != null){
				dialogSeatNum.dismiss();
				dialogSeatNum.cancel();
				dialogSeatNum = null;
			}
			dialogSeatNum = GetProgressDialog.getCallFromSeatNum(this, num);
			dialogSeatNum.show();
		}
    }


	@Override
	public void showNoNetWorkdate() {
		// TODO Auto-generated method stub
		FinalToast.ErrorStrId(context, R.string.nw_error_10002);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.finish(this);
        if(dialogSeatNum != null){
            if(dialogSeatNum.isShowing()){
                dialogSeatNum.dismiss();
                dialogSeatNum.cancel();
            }
            dialogSeatNum = null;
        }
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog.cancel();
            }
            dialog = null;
        }
	}
}
