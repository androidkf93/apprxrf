package shop.fly.com.shop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.bean.ReturnString;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.custom.MyListView;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.menus.OrderMode;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.ui.fragment.OrderFragment;
import shop.fly.com.shop.ui.fragment.PrintManagerFragment;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.NotifyMenagerUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.PrintUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/6.
 */

public class OrderAdapter extends BaseAdapter {
    private OrderFragment orderFragment;
    private Context context;
    private List<OrderBean> been;
    private LayoutInflater inflater;
    private OrderMode mode;

    public OrderAdapter(OrderFragment orderFragment, List<OrderBean> been, OrderMode mode) {
        this.context = orderFragment.getContext();
        this.been = been;
        this.mode = mode;
        this.orderFragment = orderFragment;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return been.size();
    }

    @Override
    public Object getItem(int position) {
        return been.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderBean bean = been.get(position);
        holder.mlvGood.setAdapter(new OrderItemGoodAdapter(context, bean.getDetails()));
        holder.tvTotalPrice.setText("￥" + bean.getTotalAmount());
        holder.tvCreateData.setText(bean.getAddTime());
        if (!TextUtils.isEmpty(bean.getDeskNO())) {
            holder.tvSeatNum.setText("座号  "+ bean.getDeskNO());
        }else
            holder.tvSeatNum.setText("");

        if(bean.getDouble(bean.getCouponAmount()) > 0){
            holder.llCoupon.setVisibility(View.VISIBLE);
            holder.tvCouponDeduction.setText("￥"+ bean.getCouponAmount());
        }else {
            holder.llCoupon.setVisibility(View.GONE);
        }

        if(bean.getDouble(bean.getActivityLessAmount()) > 0){
            holder.llActivity.setVisibility(View.VISIBLE);
            holder.tvActivityDeduction.setText("￥"+ bean.getActivityLessAmount());
        }else{
            holder.llActivity.setVisibility(View.GONE);
        }
        String orderType = "店内用餐";
//      订单类型 店内用餐 = 1,外卖派送 = 2,预约订餐 = 3,店内自提 = 4
        switch (bean.getOrderType()){
            case 1:
                setTablewareNum(holder, bean);
                orderType = "店内用餐";
                holder.llDelivery.setVisibility(View.GONE);
                holder.llPaddingTime.setVisibility(View.GONE);
                holder.llAddress.setVisibility(View.GONE);
                break;
            case 2:
                orderType = "外卖派送";
                setPageNum(holder, bean);
                holder.llPaddingTime.setVisibility(View.GONE);
                holder.llAddress.setVisibility(View.VISIBLE);
                CodeUtil.setText(holder.tvAddress, bean.getAddress());
                CodeUtil.setText(holder.tvContact, bean.getMemberName());
                CodeUtil.setText(holder.tvPhone, bean.getMobile());
                break;
            case 3://预订
                orderType = "预约订餐";
                setTablewareNum(holder, bean);
                holder.llAddress.setVisibility(View.GONE);
                holder.llPaddingTime.setVisibility(View.VISIBLE);
                holder.tvPaddingType.setText("预计到店");
                holder.tvPaddingTime.setText(bean.getBookingTime());
                break;
            case 4:
                orderType = "店内自提";
                setPageNum(holder, bean);
                holder.llAddress.setVisibility(View.GONE);
                holder.llPaddingTime.setVisibility(View.VISIBLE);
                holder.tvPaddingType.setText("预计到店");
                holder.tvPaddingTime.setText(bean.getBookingTime());
                break;
        }

        CodeUtil.setText(holder.tvOrderNo, bean.getOrderIndex() + "[" + orderType + "]");
        if(TextUtils.isEmpty(bean.getRemarks())){
            holder.rlRemarks.setVisibility(View.GONE);
        }else {
            holder.rlRemarks.setVisibility(View.VISIBLE);
            holder.tvRemarks.setText(bean.getRemarks());
        }
        holder.tvPrint.setVisibility(View.GONE);
        holder.rlCancelReason.setVisibility(View.GONE);
        holder.tvOperation.setVisibility(View.GONE);
        holder.tvReject.setVisibility(View.GONE);
        switch (mode) {
            case Piad:
                holder.tvPrint.setVisibility(View.VISIBLE);
                holder.tvOperation.setVisibility(View.VISIBLE);
                holder.tvReject.setVisibility(View.GONE);
                holder.tvOperation.setText("确认订单");
                break;
            case ApplyForRefund:
                holder.tvOperation.setVisibility(View.VISIBLE);
                holder.tvOperation.setText("确认退款");
                holder.tvReject.setVisibility(View.VISIBLE);
                break;
            case CancelReason:
                holder.rlCancelReason.setVisibility(View.VISIBLE);
                holder.tvCancelReason.setText(bean.getCancelReason());
                break;
        }

        holder.tvReject.setOnClickListener((view) ->{
            NotifyMenagerUtil.cancleOrder();
            if(context instanceof MainActivity)
                ((MainActivity) context).showLoading();
            TaskExecutor.executeNetTask(() ->
                    RetrofitFactory.getInstance().getRejectRefunOrder()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseObserver<String>(context) {
                                @Override
                                protected void onHandleSuccess(String s) {
                                    if(context instanceof MainActivity){
                                        ((MainActivity) context).hideLoading();
                                    }
                                    FinalToast.ErrorContext(context, s);
                                    been.remove(position);
                                    notifyDataSetChanged();
                                }
                            }));

        });

        holder.tvPrint.setOnClickListener(v -> {
            if(Constant.mPrinter == null && TextUtils.isEmpty(Constant.getUuid())) {
                new AlertDialog.Builder(context).setMessage("打印机暂未链接，是否链接打印机").setPositiveButton("是", (dialog, which) -> {
                    Intent intent = new Intent(context, GroupFragmentActivity.class);
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, PrintManagerFragment.class.getName());
                    context.startActivity(intent);
                }).setNegativeButton("否", (dialog, which) -> {
                }).show();
            }else if(Constant.mPrinter != null && !TextUtils.isEmpty(Constant.getUuid())){
                PrintUtil.getInstence(context).print(bean, 2, data -> {
                    if(Constant.isCheckBs()){
                        PrintUtil.getInstence(context).print(bean, 2, null);
                    }
                });
            }else if(Constant.mPrinter != null){
                PrintUtil.getInstence(context).print(bean, 0, null);
            }else{
                PrintUtil.getInstence(context).print(bean, 1,  data -> {
                    if(Constant.isCheckBs()){
                        PrintUtil.getInstence(context).print(bean, 2, null);
                    }
                });
            }
        });

        holder.tvOperation.setOnClickListener((view) -> {
            NotifyMenagerUtil.cancleOrder();
            switch (mode) {
                case Piad:
                    if(Constant.mPrinter == null && TextUtils.isEmpty(Constant.getUuid())) {
                        new AlertDialog.Builder(context).setMessage("打印机暂未链接，是否链接打印机").setPositiveButton("是", (dialog, which) -> {
                            Intent intent = new Intent(context, GroupFragmentActivity.class);
                            intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, PrintManagerFragment.class.getName());
                            context.startActivity(intent);
                        }).setNegativeButton("否", (dialog, which) -> {
                            operationUrl(position, bean, 1);
                        }).show();
                    }else if(Constant.mPrinter != null && !TextUtils.isEmpty(Constant.getUuid())){
                        PrintUtil.getInstence(context).print(bean, 2, data -> {
                            if(Constant.isCheckBs()){
                                PrintUtil.getInstence(context).print(bean, 2, null);
                            }
                        });
                    }else if(Constant.mPrinter != null){
                        PrintUtil.getInstence(context).print(bean, 0, null);
                        operationUrl(position, bean, 1);
                    }else{
                        PrintUtil.getInstence(context).print(bean, 1,  data -> {
                            if(Constant.isCheckBs()){
                                PrintUtil.getInstence(context).print(bean, 2, null);
                            }
                        });
                        operationUrl(position, bean, 1);
                    }
                    break;
                case ApplyForRefund:
                    operationUrl(position, bean, 2);
                    break;
                case Canceled:
                    break;
            }
        });
        return convertView;
    }

    private void setTablewareNum(ViewHolder holder, OrderBean bean) {
        if(bean.getTablewareNum() == 0){
            holder.rlTablewareNum.setVisibility(View.GONE);
        }else{
            holder.rlTablewareNum.setVisibility(View.VISIBLE);
            holder.tvTablewareNum.setText(bean.getTablewareNum() + "");
        }
    }
    private void setPageNum(ViewHolder holder, OrderBean bean){
        if(!TextUtils.isEmpty(bean.getPackageBoxNum()) && !bean.getPackageBoxNum().equals("0")){
            holder.llDelivery.setVisibility(View.VISIBLE);
            holder.tvPackageBoxNum.setText(bean.getPackageBoxNum());
            holder.tvPackageBoxPrice.setText(bean.getPackageBoxTotalPrice());
        }else{
            holder.llDelivery.setVisibility(View.GONE);
        }
    }

    private void operationUrl(final int position, OrderBean bean, int type) {
        TaskExecutor.executeNetTask(() ->{
            Observable<BaseBean<String>> baseBeanObservable = null;
            switch (type){
                case 1:
                    baseBeanObservable = RetrofitFactory.getInstance().confirmOrder(bean.getOrderNo());
                    break;
                case 2:
                    baseBeanObservable = RetrofitFactory.getInstance().confirmRefund(bean.getOrderNo());
                    break;
            }
            baseBeanObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(context) {
                        @Override
                        protected void onHandleSuccess(String s) {

                            switch (mode){
                                case Piad:
                                    if(TextUtils.isEmpty(s))
                                        s = "订单已确定";
                                    orderFragment.orderCount(1);
                                    break;
                                case ApplyForRefund:
                                    if(TextUtils.isEmpty(s))
                                        s = "退款成功";
                                    orderFragment.orderCount(3);
                                    break;
                            }
                            been.remove(position);
                            notifyDataSetChanged();
                            FinalToast.ErrorContext(context, s);
                        }
                    });
        });

    }

    static class ViewHolder {
        @BindView(R.id.tv_order_no)
        TextView tvOrderNo;
        @BindView(R.id.tv_seat_num)
        TextView tvSeatNum;
        @BindView(R.id.ll_seat)
        LinearLayout llSeat;
        @BindView(R.id.tv_contact)
        TextView tvContact;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.tv_padding_time)
        TextView tvPaddingTime;
        @BindView(R.id.mlv_good)
        MyListView mlvGood;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        @BindView(R.id.tv_reject)
        TextView tvReject;
        @BindView(R.id.tv_operation)
        TextView tvOperation;
        @BindView(R.id.tv_create_date)
        TextView tvCreateData;
        @BindView(R.id.rl_remarks)
        RelativeLayout rlRemarks;
        @BindView(R.id.tv_remarks)
        TextView tvRemarks;
        @BindView(R.id.tv_tableware_num)
        TextView tvTablewareNum;
        @BindView(R.id.rl_tableware_num)
        RelativeLayout rlTablewareNum;
        @BindView(R.id.ll_delivery)
        LinearLayout llDelivery;
        @BindView(R.id.tv_package_box_num)
        TextView tvPackageBoxNum;
        @BindView(R.id.tv_package_box_price)
        TextView tvPackageBoxPrice;
        @BindView(R.id.ll_coupon)
        LinearLayout llCoupon;
        @BindView(R.id.tv_coupon_deduction)
        TextView tvCouponDeduction;
        @BindView(R.id.ll_activity)
        LinearLayout llActivity;
        @BindView(R.id.tv_activity_deduction)
        TextView tvActivityDeduction;
        @BindView(R.id.ll_padding_time)
        LinearLayout llPaddingTime;
        @BindView(R.id.tv_padding_type)
        TextView tvPaddingType;
        @BindView(R.id.tv_print)
        TextView tvPrint;
        @BindView(R.id.rl_cancel_reason)
        RelativeLayout rlCancelReason;
        @BindView(R.id.tv_cancel_reason)
        TextView tvCancelReason;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
