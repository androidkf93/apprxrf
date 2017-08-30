package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.ProductSortAdapter;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

public class ProductSortFragment extends GroupItemFragment {

    @BindView(R.id.list_content)
    ListView listContent;
    @BindView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @BindView(R.id.ll_selectAll)
    LinearLayout llSelectAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_up)
    TextView tvUp;
    @BindView(R.id.tv_down)
    TextView tvDown;
    private ArrayList<ProductBean> productBeen;
    private ProductSortAdapter sortAdapter;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected void initDate(Context context) {
        productBeen = (ArrayList<ProductBean>) getArguments().getSerializable("data");
        sortAdapter = new ProductSortAdapter(getContext(), productBeen, checkboxAll);
        listContent.setAdapter(sortAdapter);
    }

    @Override
    protected void getDate(Context context) {

    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
        iTitle.setTitleText("");
        String cagegoryName = getArguments().getString("categoryName", "");
        iTitle.setLeftText(null, cagegoryName, v -> {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });

    }

    private void operation(String operationName){
        int i = 0;
        final List<ProductBean> productBeen = sortAdapter.getSelectProduct();
        if(productBeen.size() > i){
            showLoading();
            final ProductBean productBean = productBeen.get(i);
            int productId = productBean.getId();
            TaskExecutor.executeNetTask(() -> {
                Observable<BaseBean<String>> baseBeanObservable = null;
                switch (operationName){
                    case "上架":
                        productBeen.remove(0);
                        if(productBeen.size() == 0){
                            sortAdapter.setDefaultCheck(false);
                        }else{
                            operation(operationName);
                        }
                        baseBeanObservable = RetrofitFactory.getInstance().productUpShelf(productId);
                        break;
                    case "下架":
                        productBeen.remove(0);
                        if(productBeen.size() == 0){
                            sortAdapter.setDefaultCheck(false);
                        }else{
                            operation(operationName);
                        }
                        baseBeanObservable = RetrofitFactory.getInstance().productDownShelf(productId);
                        break;
                    case "删除":
                        baseBeanObservable = RetrofitFactory.getInstance().productDelete(productId);
                        break;
                }

                baseBeanObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<String>(getContext()) {
                            @Override
                            protected void onHandleSuccess(String s) {
                                switch (operationName){
                                    case "上架":
                                        productBean.setIsUpShelf(true);
                                        break;
                                    case "下架":
                                        productBean.setIsUpShelf(false);
                                        break;
                                    case "删除":
                                        ProductSortFragment.this.productBeen.remove(productBean);
                                        break;
                                }

                                productBeen.remove(productBean);
                                if(productBeen.size() == 0){
                                    hideLoading();
                                    sortAdapter.setDefaultCheck(false);
                                }else{
                                    operation(operationName);
                                }
                            }
                        });
            });
        }
    }

    @OnClick({R.id.ll_selectAll, R.id.tv_delete, R.id.tv_up, R.id.tv_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_selectAll:
                boolean c = !checkboxAll.isChecked();
                sortAdapter.setDefaultCheck(c);
                break;
            case R.id.tv_delete:
                operation( ((TextView) view).getText().toString());
                break;
            case R.id.tv_up:
                operation( ((TextView) view).getText().toString());
                break;
            case R.id.tv_down:
                operation( ((TextView) view).getText().toString());
                break;
        }
    }



}
