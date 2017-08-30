package shop.fly.com.shop.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.Login;
import shop.fly.com.shop.bean.UserBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.CircleImageView;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.LoginActivity;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.MyWebActivity;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.ApkUpdateUtils;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.MySharedData;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ShopFragment extends Fragment {
    @BindView(R.id.img_user)
    CircleImageView imgUser;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.img_qr)
    ImageView imgQr;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.sc_shop)
    ScrollView scShop;
    @BindView(R.id.ll_qr)
    LinearLayout llQr;
    @BindView(R.id.ll_shop)
    LinearLayout llShop;
    @BindView(R.id.tv_shop_status)
    TextView tvShopStatus;
    @BindView(R.id.img_shop_status)
    ImageView imgShopStatus;
    @BindView(R.id.rl_qr)
    RelativeLayout rlQr;

    private PackageInfo packageInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        try {
            packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String versionName = packageInfo.versionName;
            tvVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvShopName.setText(Constant.logUser.getName());
        Glide.with(getContext()).load(Constant.logUser.getLogo()).into(imgUser);
        imgShopStatus.setEnabled(Constant.logUser.isEnableOrder());
        tvShopStatus.setEnabled(Constant.logUser.isEnableOrder());
        return view;
    }

    @OnClick({R.id.ll_qr, R.id.ll_shop,  R.id.ll_address, R.id.ll_shop_address_top, R.id.ll_cue_tone, R.id.ll_print,
            R.id.ll_business_service, R.id.ll_feed_back, R.id.ll_contact, R.id.ll_version, R.id.btn_exit_account})
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), GroupFragmentActivity.class);
        switch (view.getId()) {
            case R.id.ll_qr:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, QRFragment.class.getName());
                startActivity(intent);
                break;
            case R.id.ll_shop:
                shopState(!Constant.logUser.isEnableOrder());
                break;
            case R.id.ll_shop_address_top:
            case R.id.ll_address:
                setWebUrl(intent, "店铺信息", "http://api.mubai168.com/Frame/Store");
                break;
            case R.id.ll_cue_tone:
                break;
            case R.id.ll_print:
                intent = new Intent(getContext(), GroupFragmentActivity.class);
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, PrintManagerFragment.class.getName());
                startActivity(intent);
                break;
            case R.id.ll_business_service:
                setWebUrl(intent, "服务中心",  "http://api.mubai168.com/Frame/Service");
                break;
            case R.id.ll_feed_back:
                setWebUrl(intent, "意见反馈", "http://api.mubai168.com/Frame/FeedBack");
                break;
            case R.id.ll_contact:
                break;
            case R.id.ll_version:
                boolean updateVersion = ApkUpdateUtils.getInstance().isUpdateVersion(getActivity());
                if (!updateVersion){
                    FinalToast.ErrorContext(getContext(), "已是最新版本不需要更新");
                }
                break;
            case R.id.btn_exit_account:
                Constant.cleanLogin();
                Constant.user = "B8BDEF99-ECEA-4A69-A23C-521641F66E39";
                JPushInterface.setAlias(getContext(), Constant.logUser.getId() + "", (i, s, set) -> {
                    LogUtil.e("i= " + i + "\ns= " + s + "set= " + set + "\nalias= " + Constant.logUser.getId(), LoginActivity.class);
                });
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void shopState(boolean state) {
        ((MainActivity) getActivity()).showLoading();

        TaskExecutor.executeNetTask(() ->
                RetrofitFactory.getInstance().getStoreDownShelf(state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<Boolean>(getContext()) {
                            @Override
                            protected void onHandleSuccess(Boolean b) {
                                ((MainActivity) getActivity()).hideLoading();
                                Constant.logUser.setEnableOrder(!Constant.logUser.isEnableOrder());
                                tvShopStatus.setText(state ? "营业中" : "暂停营业");
                                tvShopStatus.setEnabled(state);
                                imgShopStatus.setEnabled(state);
                            }
                        }));

    }

    private void setWebUrl(Intent intent, String type, String url) {
        url += "?u=" + Constant.user;
        intent.setClass(getContext(), MyWebActivity.class);
        intent.putExtra(MyWebActivity.WEB_TITLE, type);
        intent.putExtra(MyWebActivity.WEB_URL, url);
        startActivity(intent);
    }
}
