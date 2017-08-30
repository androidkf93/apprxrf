package shop.fly.com.shop.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.adapter.OrderAdapter;
import shop.fly.com.shop.bean.OrderCount;
import shop.fly.com.shop.interfaces.HomeSearchListener;
import shop.fly.com.shop.interfaces.Refresh;
import shop.fly.com.shop.menus.OrderMode;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.NotifyMenagerUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.SDFUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Fly on 2017/6/5.
 */

public class OrderFragment extends Fragment implements Refresh, HomeSearchListener{
    @BindView(R.id.rbt_left)
    RadioButton rbtLeft;
    @BindView(R.id.rbt_right)
    RadioButton rbtRight;
    @BindView(R.id.rg_choose)
    RadioGroup rgChoose;
    @BindView(R.id.plv_content)
    PullToRefreshListView plvContent;
    @BindView(R.id.tv_no_payment)
    TextView tvNoPayment;
    @BindView(R.id.rl_no_payment)
    RelativeLayout rlNoPayment;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.rl_complete)
    RelativeLayout rlComplete;
    @BindView(R.id.tv_refunded)
    TextView tvRefunded;
    @BindView(R.id.rl_refunded)
    RelativeLayout rlRefunded;
    @BindView(R.id.tv_canceled)
    TextView tvCanceled;
    @BindView(R.id.rl_canceled)
    RelativeLayout rlCanceled;
    private ArrayList<RelativeLayout> arrayRl = new ArrayList<>();
    private List<OrderBean> orderBeenOne;
    private List<OrderBean> orderBeenTwo;

    private Context context;
    int v1 = 0, v2 = 0, v3 = 0, v4;
    int type = 0;
    int position = 0;
    private OrderAdapter adapterOne;
    private OrderAdapter adapterTwo;
    private OrderMode orderMode;
    private Map<Integer, Integer> mapPage = new HashMap<>();
    private String key = "";
    private String date = "";
    private MainActivity mainActivity;
    private Map<Integer, Boolean> mapRefresh = new HashMap<>();//是否需要强制刷新
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_oder, container, false);
        ButterKnife.bind(this, contentView);
        context = getContext();
        init();
        getDate();
        return contentView;
    }

    private void init() {
        initRefresh();
        plvContent.setMode(PullToRefreshBase.Mode.BOTH);
        if (getArguments() != null){
            type = getArguments().getInt("type", 0);
            position = getArguments().getInt("itemPosition", 0);
        }
        if (type == 0) {
            orderMode = position == 0 ? OrderMode.Piad : OrderMode.ApplyForRefund;
            arrayRl.add(rlNoPayment);
            arrayRl.add(rlRefunded);
            rlComplete.setVisibility(View.GONE);
            rlCanceled.setVisibility(View.GONE);
            selectItem(position == 0 ? rlNoPayment : rlRefunded);
        } else {
            orderMode = position == 0 ? OrderMode.Completed : OrderMode.Canceled;
            rlNoPayment.setVisibility(View.GONE);
            rlRefunded.setVisibility(View.GONE);
            rlComplete.setVisibility(View.VISIBLE);
            rlCanceled.setVisibility(View.VISIBLE);
            arrayRl.add(rlComplete);
            arrayRl.add(rlCanceled);
            selectItem(rlComplete);
        }
        mainActivity = (MainActivity) getActivity();
        plvContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                key = mainActivity.getLastKey();
                NotifyMenagerUtil.cancleOrder();
                getDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                NotifyMenagerUtil.cancleOrder();
                int page = mapPage.get(orderMode.getDefaultValues());
                getOder(++ page, key, date);
            }
        });

    }

    private void initRefresh() {
        mapRefresh.put(OrderMode.Piad.getDefaultValues(), true);
        mapRefresh.put(OrderMode.ApplyForRefund.getDefaultValues(), true);
        mapRefresh.put(OrderMode.Completed.getDefaultValues(), true);
        mapRefresh.put(OrderMode.Canceled.getDefaultValues(), true);
    }
    /**
     * 数据发生改变
     * @param key
     */
    private void chageData(String key) {
        initRefresh();
        getDate();
    }
    public void orderCount(int type){
        switch (type){
            case 1:
                setNum(tvNoPayment, -- v1,  getString(R.string.paid));
                break;
            case 3:
                setNum(tvRefunded, v3, getString(R.string.apply_for_refunded));
                break;
        }
    }

    public void setNum(TextView tv, int num, String content) {
        String snum = num > 99 ? num + "+" : num + "";
        content = String.format(content, snum);
        tv.setText(content);
    }

    private void getDate() {
        if (context instanceof MainActivity) {
            ((MainActivity) context).showLoading();
        }
        if(TextUtils.isEmpty(date)){
            date = SDFUtil.getTime();
        }

        TaskExecutor.executeTask(() ->{
            RetrofitFactory.getInstance().queryOrderCount(date)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<OrderCount>(getContext(), false) {
                        @Override
                        protected void onHandleSuccess(OrderCount orderCount) {
                            v1 = orderCount.getValue1();
                            v2 = orderCount.getValue2();
                            v3 = orderCount.getValue3();
                            v4 = orderCount.getValue4();
                            setNum(tvNoPayment, v1, getString(R.string.paid));
                            setNum(tvRefunded, v3, getString(R.string.apply_for_refunded));
                            setNum(tvComplete, v2, getString(R.string.completed));
                            setNum(tvCanceled, v4, getString(R.string.canceled));
                            getOder(1, key, date);
                        }
                    });
        });

    }
    private void getOder(int page, String key, final String date) {
        if(!plvContent.isRefreshing()){
            TaskExecutor.runTaskOnUiThread(() -> ((MainActivity)getActivity()).showLoading());
        }
        mapRefresh.put(orderMode.getDefaultValues(), false);
        this.key = key;
        mapPage.put(orderMode.getDefaultValues(), page);
        String token = CodeUtil.MD5(SDFUtil.getCurrentTime() + page + "10" + orderMode.getDefaultValues()).toUpperCase();
        TaskExecutor.executeTask(() ->{
            RetrofitFactory.getInstance().queryOrder(page, 10, orderMode.getDefaultValues(), SDFUtil.getTime(), date, key, token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ArrayList<OrderBean>>(getContext()) {
                        @Override
                        protected void onHandleSuccess(ArrayList<OrderBean> orderBeen) {
                            if (context instanceof MainActivity) {
                                ((MainActivity) context).hideLoading();
                            }
                            plvContent.onRefreshComplete();
                            switch(orderMode){
                                case Piad:
                                case Completed:
                                    if(orderBeenOne == null){
                                        orderBeenOne = orderBeen;
                                        adapterOne = new OrderAdapter(OrderFragment.this, orderBeenOne, orderMode);
                                        plvContent.setAdapter(adapterOne);
                                    }else {
                                        if(page == 1)
                                            orderBeenOne.clear();
                                        orderBeenOne.addAll(orderBeen);
                                        adapterOne.notifyDataSetChanged();
                                    }
                                    break;
                                case ApplyForRefund:
                                case Canceled:
                                    if(orderBeenTwo == null){
                                        orderBeenTwo = orderBeen;
                                        adapterTwo = new OrderAdapter(OrderFragment.this, orderBeenTwo, orderMode);
                                        plvContent.setAdapter(adapterTwo);
                                    }else {
                                        if(page == 1)
                                            orderBeenTwo.clear();
                                        orderBeenTwo.addAll(orderBeen);
                                        adapterTwo.notifyDataSetChanged();
                                    }
                                    break;
                            }
                        }

                        @Override
                        protected void onHandleError(String msg) {
                            super.onHandleError(msg);
                            if(plvContent.isRefreshing())
                                plvContent.onRefreshComplete();
                        }
                    });
        });

    }

    private void selectItem(RelativeLayout relativeLayout) {
        if (relativeLayout != null) {
            for (RelativeLayout rl :
                    arrayRl) {
                if (rl == relativeLayout) {
                    rl.setEnabled(false);
                    View childAt = rl.getChildAt(0);
                    if (childAt instanceof LinearLayout) {
                        for (int i = 0; i < ((LinearLayout) childAt).getChildCount(); i++) {
                            ((LinearLayout) childAt).getChildAt(i).setEnabled(false);
                        }
                    }
                    childAt.setEnabled(false);
                    rl.getChildAt(1).setVisibility(View.VISIBLE);
                } else {
                    rl.setEnabled(true);
                    rl.getChildAt(0).setEnabled(true);
                    View childAt = rl.getChildAt(0);
                    if (childAt instanceof LinearLayout) {
                        for (int i = 0; i < ((LinearLayout) childAt).getChildCount(); i++) {
                            ((LinearLayout) childAt).getChildAt(i).setEnabled(true);
                        }
                    }
                    rl.getChildAt(1).setVisibility(View.GONE);
                }
            }
        }

    }

    @OnClick({R.id.rl_no_payment, R.id.rl_complete, R.id.rl_refunded, R.id.rl_canceled})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_no_payment:
                orderMode = OrderMode.Piad;
                selectItem((RelativeLayout) view);
                if(adapterOne != null && !mapRefresh.get(orderMode.getDefaultValues())){
                    plvContent.setAdapter(adapterOne);
                }else {
                    getOder(1, key, date);
                }
                break;
            case R.id.rl_refunded:
                selectItem((RelativeLayout) view);
                orderMode = OrderMode.ApplyForRefund;
                if(adapterTwo != null && !mapRefresh.get(orderMode.getDefaultValues())){
                    plvContent.setAdapter(adapterTwo);
                }else {
                    getOder(1, key, date);
                }
                break;
            case R.id.rl_complete:
                selectItem((RelativeLayout) view);
                orderMode = OrderMode.Completed;
                if(adapterOne != null && !mapRefresh.get(orderMode.getDefaultValues())){
                    plvContent.setAdapter(adapterOne);
                }else {
                    getOder(1, key, date);
                }
                break;
            case R.id.rl_canceled:
                selectItem((RelativeLayout) view);
                orderMode = OrderMode.Canceled;
                if(adapterTwo != null && !mapRefresh.get(orderMode.getDefaultValues())){
                    plvContent.setAdapter(adapterTwo);
                }else {
                    getOder(1, key, date);
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean refresh(String type) {
        boolean isVisible = getUserVisibleHint();
        boolean notification = false;
        if(isVisible && this.type == 0){
            switch (type){
                case "NEW_ORDER":
                    mapRefresh.put(OrderMode.Piad.getDefaultValues(), true);
                    if(orderMode == OrderMode.Piad){
                        notification = true;
                        getDate();

                    }else{
                        notification = false;
                    }
                    break;
                case "REFUND_ORDER":
                    mapRefresh.put(OrderMode.ApplyForRefund.getDefaultValues(), true);
                    if(orderMode == OrderMode.ApplyForRefund){
                        notification = true;
                        getDate();
                    }else{
                        notification = false;
                    }
                    break;
            }
        }else {
            notification = false;
        }
        return notification;
    }

    @Override
    public void search(String key, String date) {
        this.key = key;
        this.date = date;
        if(plvContent != null)//页面没有加载时控件未绑定则为null
            chageData(key);

    }
}
