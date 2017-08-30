package shop.fly.com.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.util.CodeUtil;

/**
 * Created by Administrator on 2017/6/14.
 */

public class OrderItemGoodAdapter extends BaseAdapter {

    private Context context;
    private List<OrderBean.DetailsBean> been;
    private LayoutInflater inflater;

    public OrderItemGoodAdapter(Context context, List<OrderBean.DetailsBean> been) {
        this.context = context;
        this.been = been;
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
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_order_good, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        OrderBean.DetailsBean bean = been.get(position);
        String pName =  bean.getProductName();
        if(TextUtils.isEmpty(bean.getProductName())){
            pName = "";
        }
        if(!TextUtils.isEmpty(bean.getProductProName())){
            pName +=  " (" + bean.getProductProName() + ")";
        }
        holder.tvGoodName.setText(pName);
        CodeUtil.setText(holder.tvGoodPrice, "￥" + bean.getPrice());
        CodeUtil.setText(holder.tvNum, "x" + bean.getNumber());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_good_price)
        TextView tvGoodPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
