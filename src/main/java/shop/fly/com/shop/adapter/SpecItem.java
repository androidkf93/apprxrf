package shop.fly.com.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SpecItem {

    public void setItem(Context context, LinearLayout llContent, ArrayList<SpecBean> specBeen, boolean setFocusable, boolean isEdit) {
        llContent.removeAllViews();
        for (int i = 0; i < specBeen.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_spec, llContent, false);
            int j = i + 1;
            SpecBean specBean = specBeen.get(i);
            ((TextView) view.findViewById(R.id.tv_spec_name)).setText("规格" + j);
            EditText edtName = ((EditText) view.findViewById(R.id.edt_spec_name));
            EditText edtPrice = ((EditText) view.findViewById(R.id.edt_price));
            EditText edtStockNum = ((EditText) view.findViewById(R.id.edt_stock_num));
            LinearLayout llEdit = (LinearLayout) view.findViewById(R.id.ll_edit);
            LinearLayout llStoke = (LinearLayout) view.findViewById(R.id.ll_stoke);
            if(isEdit){
                llEdit.setVisibility(View.VISIBLE);
                TextView tvOffer = (TextView) view.findViewById(R.id.tv_offer);
                TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
                view.findViewById(R.id.ll_switch).setOnClickListener(v -> {
                    if(tvOffer.getVisibility() == View.VISIBLE){
                        tvClose.setVisibility(View.VISIBLE);
                        tvOffer.setVisibility(View.INVISIBLE);
                        llStoke.setVisibility(View.VISIBLE);
                    }
                    else{
                        tvClose.setVisibility(View.INVISIBLE);
                        tvOffer.setVisibility(View.VISIBLE);
                        llStoke.setVisibility(View.GONE);
                    }
                });

                if(specBean.getStock() == -1){
                    tvClose.setVisibility(View.INVISIBLE);
                    tvOffer.setVisibility(View.VISIBLE);
                    llStoke.setVisibility(View.GONE);
                }
                edtStockNum.setText(specBean.getStock() + "");
            }else{
                llEdit.setVisibility(View.GONE);
            }
            edtName.setText(specBean.getName());
            edtPrice.setText(specBean.getPrice());

            edtName.setFocusable(setFocusable);
            edtPrice.setFocusable(setFocusable);
            edtStockNum.setFocusable(setFocusable);
            view.findViewById(R.id.tv_delete).setOnClickListener(v ->{
                if(TextUtils.isEmpty(specBean.getName())){
                    specBeen.remove(specBean);
                    setItem(context, llContent, specBeen, setFocusable, isEdit);
                    return;
                }
                TaskExecutor.executeNetTask(() ->
                        RetrofitFactory.getInstance().getProductProDelete(specBean.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseObserver<String>(context) {
                                    @Override
                                    protected void onHandleSuccess(String s) {
                                        specBeen.remove(specBean);
                                        setItem(context, llContent, specBeen, setFocusable, isEdit);
                                    }
                                }));
            });

            llContent.addView(view);
        }
    }

}


