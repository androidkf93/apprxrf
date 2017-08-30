package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.CategoryUpdate;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategoryAddFragment extends GroupItemFragment {

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_remark)
    EditText edtRemark;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;

    private Context context;

    private Bundle bundle;
    private boolean isUp = false;
    private int id;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    protected void initDate(Context context) {
        this.context = context;
        tvBottom.setOnClickListener(view -> addCategory());
        bundle = getArguments();
        if(bundle != null){
            edtName.setText(bundle.getString("name", ""));
            edtRemark.setText(bundle.getString("remark", ""));
            id = bundle.getInt("id", 0);
            isUp = true;
        }

    }

    private void addCategory(){
        showLoading();
        CategoryUpdate categoryUpdate = new CategoryUpdate();
        categoryUpdate.Name = edtName.getText().toString();
        categoryUpdate.Remark = edtRemark.getText().toString();
        categoryUpdate.Id = id;
        categoryUpdate.Sort = 1;
        String token =(categoryUpdate.Id + categoryUpdate.Name + categoryUpdate.Name + categoryUpdate.Sort + categoryUpdate.getTime());
        categoryUpdate.setToken(token);
        TaskExecutor.executeNetTask(() -> {
            Observable<BaseBean<String>> baseBeanObservable = null;

            if(isUp){
                baseBeanObservable = RetrofitFactory.getInstance().categoryUpdate(categoryUpdate);
            }else{
                baseBeanObservable = RetrofitFactory.getInstance().categoryAdd(categoryUpdate);
            }
            baseBeanObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(context) {
                        @Override
                        protected void onHandleSuccess(String message) {
                            if (TextUtils.isEmpty(message)) {
                                message = !isUp ? "创建成功" : "更新成功";
                            }
                            final String sm = message;
                            FinalToast.ErrorContext(context, sm);
                            edtName.setText("");
                            edtRemark.setText("");
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        }
                    });
        });

    }
    @Override
    protected void getDate(Context context) {

    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        String title = "新建分类";
        if(bundle != null){
            title = "编辑分类";
        }
        iTitle.setLeftImage(null, ContextCompat.getDrawable(context, R.drawable.return_white), view -> getActivity().finish());
        iTitle.setLeftText(null, title, view ->  getActivity().finish());
        iTitle.setRightText(null, "取消", view -> getActivity().finish());
        iTitle.setTitleText("");
    }
}
