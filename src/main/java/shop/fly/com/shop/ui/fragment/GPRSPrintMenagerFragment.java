package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.application.MyApplication;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.interfaces.MSTCOperationListener;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.MySharedData;
import shop.fly.com.shop.util.PrintUtil;

/**
 * Created by Administrator on 2017/8/2.
 */

public class GPRSPrintMenagerFragment extends GroupItemFragment {

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.checkbox_bs)
    CheckBox checkboxBs;
    @BindView(R.id.checkbox_customer)
    CheckBox checkboxCustomer;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_gprs_maneger, container, false);
    }

    @Override
    protected void initDate(Context context) {
        tvNumber.setText("编号：" + Constant.getUuid());
        checkboxBs.setChecked(Constant.isCheckBs());
        checkboxBs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MySharedData.sharedata_WriteInt(context, Constant.IS_CHECK_BS, isChecked ? 1 : 0);
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
        iTitle.setLeftText(null, "GPRS打印机设置", v -> getActivity().finish());
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        iTitle.setTitleText("");
    }


    @OnClick({R.id.tv_unbound, R.id.tv_print_test})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_unbound:
                Constant.unBoundGPRS();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
            case R.id.tv_print_test:
                PrintUtil.getInstence(MyApplication.getContext()).print(Constant.getDefaultPrint(), 2, data -> {
                    LogUtil.e(data, GPRSPrintMenagerFragment.class);
                    if(Constant.isCheckBs()){
                        PrintUtil.getInstence(MyApplication.getContext()).print(Constant.getDefaultPrint(), 2, data1 -> {
                            LogUtil.e(data1, GPRSPrintMenagerFragment.class);
                        });
                    }
                });
                break;
        }

    }
}
