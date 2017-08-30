package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.CategoryAdapter;
import shop.fly.com.shop.adapter.ProductAdapter;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.constant.RequestCode;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.menus.SpecMode;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategoryFragment extends GroupItemFragment {

    private Context context;
    @BindView(R.id.lv_data)
    ListView lvData;
    SpecMode mode = SpecMode.CATEGORY;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    protected void initDate(Context context) {

    }

    @Override
    protected void getDate(Context context) {
        this.context = context;
        Bundle bundle = getArguments();
        if(bundle != null){
            mode = (SpecMode) bundle.getSerializable("type");
        }
        showLoading();
        TaskExecutor.executeNetTask(() ->
                    RetrofitFactory.getInstance().getCategoryQuery()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<CategoryBean>>(context) {
                        @Override
                        protected void onHandleSuccess(List<CategoryBean> categoryBeen) {
                            CategoryFragment.this.hideLoading();
                            lvData.setAdapter(new CategoryAdapter(context, categoryBeen));
                        }
                    })
        );

    }

    @Override
    protected void setDate() {
        lvData.setOnItemClickListener((parent, view, position, id) -> {
            if(mode != null && mode == SpecMode.SELECT_CATEGORY){
                CategoryBean bean = (CategoryBean) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.putExtra("data", bean);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftText(null, "管理分类", null);
        iTitle.setLeftImage(null,getResources().getDrawable(R.drawable.return_white), view -> getActivity().finish());
        iTitle.setTitleText("");
    }


    @OnClick({R.id.tv_bottom_left, R.id.tv_bottom_right})
    public void onClick(View view) {
        Intent intent = new Intent(context, GroupFragmentActivity.class);
        switch (view.getId()) {
            case R.id.tv_bottom_left:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategorySortFragment.class.getName());
                getActivity().startActivityForResult(intent, RequestCode.CATEGORY);
                break;
            case R.id.tv_bottom_right:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategoryAddFragment.class.getName());
                getActivity().startActivityForResult(intent, RequestCode.CATEGORY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == RequestCode.CATEGORY){
            getDate(getContext());
        }
    }

}
