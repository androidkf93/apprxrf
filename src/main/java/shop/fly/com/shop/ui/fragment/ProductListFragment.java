package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.CategoryAdapter;
import shop.fly.com.shop.adapter.ProductAdapter;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.constant.RequestCode;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ProductListFragment extends GroupItemFragment {

    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.lv_right)
    ListView lvRight;

    private Context context;
    List<CategoryBean> categoryBeen;

    private ArrayList<ProductBean> productBeen;
    private ProductAdapter productAdapter;
    private CategoryBean categoryBean;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_good, container, false);
    }

    @Override
    protected void initDate(Context context) {
        this.context = context;
        lvLeft.setOnItemClickListener((parent, view, position, id) -> {
            categoryBean = categoryBeen.get(position);
            CategoryAdapter adapter = (CategoryAdapter) parent.getAdapter();
            adapter.setSelectPosition(position);
            productQuery(categoryBean.getId(), false);
        });
    }

    @Override
    protected void getDate(final Context context) {
        LogUtil.e("getDate()", context.getClass());
        showLoading();

        TaskExecutor.executeNetTask(() ->
            RetrofitFactory.getInstance().getCategoryQuery()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<CategoryBean>>(context) {
                        @Override
                        protected void onHandleSuccess(List<CategoryBean> categoryBeen) {
                            ProductListFragment.this.hideLoading();
                            ProductListFragment.this.categoryBeen = categoryBeen;
                            lvLeft.setAdapter(new CategoryAdapter(context, categoryBeen, 1));
                            if (categoryBeen.size() > 0) {
                                categoryBean = categoryBeen.get(0);
                                productQuery(categoryBean.getId(), false);
                            }
                        }
                    }));
    }

    int lastId = -2;

    private void productQuery(int id, boolean isReFresh) {
        if (lastId == id && !isReFresh)
            return;
        lastId = id;
        showLoading();
        TaskExecutor.executeNetTask(() ->
            RetrofitFactory.getInstance().getProductQuery(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ArrayList<ProductBean>>(context) {
                        @Override
                        protected void onHandleSuccess(ArrayList<ProductBean> products) {
                            hideLoading();
                            if (productAdapter == null) {
                                productBeen = products;
                                productAdapter = new ProductAdapter(ProductListFragment.this, productBeen, categoryBean);
                                lvRight.setAdapter(productAdapter);
                            } else {
                                productBeen.clear();
                                productBeen.addAll(products);
                                productAdapter.setCategoryBean(categoryBean);
                                productAdapter.notifyDataSetChanged();
                            }
                        }
                    }));
    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftText(null, "商品管理", null);
        iTitle.setLeftImage(null, ContextCompat.getDrawable(context, R.drawable.return_white), view -> getActivity().finish());
        iTitle.setTitleText("");
    }

    @OnClick({R.id.tv_category_manager, R.id.fl_sort, R.id.ll_add_good})
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), GroupFragmentActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_category_manager:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategoryFragment.class.getName());
                startActivityForResult(intent, RequestCode.CATEGORY);
                break;
            case R.id.fl_sort:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, ProductSortFragment.class.getName());
                bundle.putSerializable("data", productBeen);
                bundle.putString("categoryName", categoryBean.getName());
                intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                startActivityForResult(intent, RequestCode.PRODUCT);
                break;
            case R.id.ll_add_good:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, ProductAddFragment.class.getName());
                startActivityForResult(intent, RequestCode.PRODUCT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            productQuery(categoryBean.getId(), true);
        }
    }
}
