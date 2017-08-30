package shop.fly.com.shop.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.OperateAdapter;
import shop.fly.com.shop.bean.OperateBean;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.bean.OrderSumBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.MyWebActivity;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by fly12 on 2017/6/17.
 */

public class ShopOperateFragment extends Fragment {
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.gd_operate)
    GridView gdOperate;
    Unbinder unbinder;

    private OrderSumBean orderSumBean;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_operate, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
        init();
        gdOperate.setOnItemClickListener((parent, view1, position, id) -> {
            OperateAdapter adapter = (OperateAdapter) parent.getAdapter();
            Intent intent = new Intent(context, GroupFragmentActivity.class);
            String type = ((OperateBean)adapter.getItem(position)).getName();
            switch (type){
                case "分类":
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategoryFragment.class.getName());
                    break;
                case "商品":
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, ProductListFragment.class.getName());
                    break;
                case "通知公告":
                    setWebUrl(intent, type, "http://api.mubai168.com/Frame/Notic");
                    break;
                case "经营数据":
                    setWebUrl(intent, type, "http://api.mubai168.com/Frame/Report");
                    break;
                case "财务对帐":
                    setWebUrl(intent, type, "http://api.mubai168.com/Frame/Order");
                    break;
            }
            startActivity(intent);
        });
        return view;
    }

    private void setWebUrl(Intent intent, String type, String url) {
        url += "?u=" + Constant.user;
        intent.setClass(context, MyWebActivity.class);
        intent.putExtra(MyWebActivity.WEB_TITLE, type);
        intent.putExtra(MyWebActivity.WEB_URL, url);
    }

    private void init() {
        ArrayList<OperateBean> operateBeen = new ArrayList<>();
        operateBeen.add(new OperateBean("分类", getResources().getDrawable(R.drawable.classified_management)));
        operateBeen.add(new OperateBean("商品", getResources().getDrawable(R.drawable.good_manager)));
        operateBeen.add(new OperateBean("通知公告", getResources().getDrawable(R.drawable.notice)));
        operateBeen.add(new OperateBean("经营数据", getResources().getDrawable(R.drawable.operating_data)));
        operateBeen.add(new OperateBean("财务对帐", getResources().getDrawable(R.drawable.financial_reconciliation)));
        gdOperate.setAdapter(new OperateAdapter(getContext(), operateBeen));
        TaskExecutor.executeNetTask(() ->{
            RetrofitFactory.getInstance().getOrderSum()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ArrayList<OrderSumBean>>(getContext()) {
                        @Override
                        protected void onHandleSuccess(ArrayList<OrderSumBean> orderSumBeen) {
                            if (orderSumBeen.size() > 0){
                                orderSumBean = orderSumBeen.get(0);
                                tvOrderNum.setText(orderSumBean.getOrderNumber() + "单");
                                tvOrderPrice.setText(orderSumBean.getTotalAmount() + "元");
                            }
                        }
                    });
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
