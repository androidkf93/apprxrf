package shop.fly.com.shop.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.CategoryAdapter;
import shop.fly.com.shop.adapter.CategorySortAdapter;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategorySortFragment extends GroupItemFragment {

    @BindView(R.id.ll_category_bottom)
    LinearLayout llCategoryBottom;
    @BindView(R.id.lv_data)
    ListView lvData;
    private Context context;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    protected void initDate(Context context) {
        llCategoryBottom.setVisibility(View.GONE);
    }

    @Override
    protected void getDate(Context context) {
        this.context = context;
        showLoading();
        TaskExecutor.executeNetTask(() -> {
            RetrofitFactory.getInstance().getCategoryQuery()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<CategoryBean>>(context) {
                        @Override
                        protected void onHandleSuccess(List<CategoryBean> categoryBeen) {
                            CategorySortFragment.this.hideLoading();
                            lvData.setAdapter(new CategoryAdapter(context, categoryBeen));
                        }
                    });
        });

    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftText(null, "分类排序", null);
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), view -> getActivity().finish());
        iTitle.setTitleText("");

    }

    public void sort(int categoryId, boolean up){
        LogUtil.e("sort(" + categoryId + ")", CategorySortFragment.class);
        TaskExecutor.executeNetTask(() ->
            RetrofitFactory.getInstance().getCategorySort(categoryId, up)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(context) {
                        @Override
                        protected void onHandleSuccess(String s) {
                            getDate(getContext());
                        }
                    }));
    }

    public void delete(int id){
        LogUtil.e("delete(" + id + ")", CategorySortFragment.class);
        TaskExecutor.executeNetTask(() ->
        RetrofitFactory.getInstance().categoryDelete(id)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseObserver<String>(context) {
            @Override
            protected void onHandleSuccess(String s) {
                getDate(getContext());
            }
        }));
    }




}
