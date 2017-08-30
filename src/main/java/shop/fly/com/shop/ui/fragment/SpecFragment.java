package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.SpecItem;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SpecFragment extends GroupItemFragment {
    @BindView(R.id.lv_content)
    ListView lvContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_add_spec)
    TextView tvAddSpec;
    private ArrayList<SpecBean> specBeen = new ArrayList<>();
    private SpecItem specItem = new SpecItem();
    private int productId;
    private boolean showPrice;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_spec, container, false);
    }

    @Override
    protected void initDate(Context context) {
        String textAdd = tvAddSpec.getText().toString();
        SpannableString spannableString = new SpannableString(textAdd);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAddSpec.setText(spannableString);
        Bundle bundle = getArguments();
        if(bundle != null){
            productId = bundle.getInt("productId", 0);
            showPrice = bundle.getBoolean("showPrice", false);
            if(productId == 0){
                specBeen = (ArrayList<SpecBean>) bundle.getSerializable("spec");
                if(specBeen == null){
                    specBeen = new ArrayList<>();
                }
                if(specBeen.size() == 0)
                    specBeen.add(new SpecBean());
                specItem.setItem(getContext(), llContent, specBeen, true, showPrice);
            }else{
                RetrofitFactory.getInstance().getProductProQuery(productId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<ArrayList<SpecBean>>(context) {
                            @Override
                            protected void onHandleSuccess(ArrayList<SpecBean> specBeen) {
                                specBeen.addAll(specBeen);
                                specItem.setItem(getContext(), llContent, specBeen, true, showPrice);
                            }
                        });
            }

        }

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
            getActivity().finish();
        });
        iTitle.setLeftText(null, "添加属性", null);
        iTitle.setRightText(null, "保存", v -> {
            if(llContent.getChildCount() > 0){
                for (int i = 0; i < specBeen.size(); i++) {
                    SpecBean specBean = specBeen.get(i);
                    if(productId != 0 && !TextUtils.isEmpty(specBean.getName())){
                        addOrUpdate(0, productId, true, i);
                    }else{
                        addOrUpdate(1, productId, true, i);
                    }
                }
            }else {
                specBeen.clear();
                Intent intent = new Intent();
                intent.putExtra("spec", specBeen);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }

        });
        iTitle.setTitleText("");
    }


    @OnClick({R.id.tv_add_spec} )
    public void onClick() {
        if(specBeen.size() == 0){
            change(false, 0);
            return;
        }
        SpecBean specBean = specBeen.get(specBeen.size() - 1);
        String specName = specBean.getName();
        if(productId != 0 && !TextUtils.isEmpty(specName)){
            addOrUpdate(0, productId, false, llContent.getChildCount() - 1);
        }else{
            addOrUpdate(1, productId, false, llContent.getChildCount() - 1);
        }
    }


    /**
     * 生成数据
     * @param product
     * @param isSave
     */
    private void addOrUpdate(int type, int product, boolean isSave, int position) {
        showLoading();
        View childAt;
        if(llContent.getChildCount() > position){
            childAt = llContent.getChildAt(position);
        }else{
            childAt = LayoutInflater.from(getContext()).inflate(R.layout.item_spec, llContent, false);
        }
        int stockNum;
        String name = ((EditText) childAt.findViewById(R.id.edt_spec_name)).getText().toString();
        TextView tvClose = (TextView) childAt.findViewById(R.id.tv_close);
        String price =  ((EditText) childAt.findViewById(R.id.edt_price)).getText().toString();
        String stock =  ((EditText) childAt.findViewById(R.id.edt_stock_num)).getText().toString();
        if(tvClose.getVisibility() == View.INVISIBLE || TextUtils.isEmpty(stock)){
            stockNum = -1;
        }else{
            stockNum = Integer.parseInt(stock);
        }
        SpecBean specBean = specBeen.get(position);
        if(specBean.equalsNull(name, price, stockNum)){
            specBeen.remove(specBean);
            change(isSave, position);
            return;
        }
        if(specBean.equalsAll(name, price, stockNum)){
            change(isSave, position);
            return;
        }
        specBean.setName(name);
        specBean.setProductId(product);
        specBean.setPrice(price);
        specBean.setStock(stockNum);
        String token = CodeUtil.MD5(specBean.getId() + "" + specBean.getName() +
                specBean.getPrice() + specBean.getProductId() + specBean.getStock());
        specBean.setToken(token);
        TaskExecutor.executeNetTask(() ->{
            Observable<BaseBean<String>> baseBeanObservable = null;
            switch (type){
                case 0:
                    baseBeanObservable = RetrofitFactory.getInstance().productProUpdate(specBean);
                    break;
                case 1:
                    baseBeanObservable = RetrofitFactory.getInstance().productProAdd(specBean);
                    break;
            }
            baseBeanObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        protected void onHandleSuccess(String s) {
                            change(isSave, position);
                        }
                    });
        });

    }
    private void change(boolean isSave, int position) {
        hideLoading();
        if(llContent.getChildCount() == 0 || position == llContent.getChildCount() - 1){
            if(isSave){
                Intent intent = new Intent();
                intent.putExtra("spec", specBeen);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }else{
                specBeen.add(new SpecBean());
                specItem.setItem(getContext(), llContent, specBeen, true, showPrice);
            }
        }
    }
}
