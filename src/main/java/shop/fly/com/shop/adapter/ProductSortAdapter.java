package shop.fly.com.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.util.LogUtil;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ProductSortAdapter extends BaseAdapter {
    private Context context;
    private List<ProductBean> productBeen;
    private LayoutInflater inflater;
    private List<ProductBean> selectProduct = new ArrayList<>();
    private Map<Integer, Boolean> selectMap = new HashMap<>();
    private  CheckBox chechAll;

    public ProductSortAdapter(Context context, List<ProductBean> productBeen, CheckBox chechAll) {
        this.context = context;
        this.productBeen = productBeen;
        this.chechAll = chechAll;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < productBeen.size(); i++) {
            selectMap.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return productBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return productBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_good_sort, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ProductBean bean = productBeen.get(position);
        Glide.with(context)
                .load(bean.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgGood);
        holder.tvGoodName.setText(bean.getName());
        String stock = bean.getStock() == -1 ? "库存无限" : "库存：" + bean.getStock();
        holder.tvStock.setText(stock);
        holder.tvSalesVolumes.setText("月销：" + bean.getSoldCount());
        holder.checkbox.setChecked(selectMap.get(position));
        holder.tvGoodPrice.setText(bean.getPrice());
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked && selectProduct.size() == productBeen.size() ||
                    !isChecked && selectProduct.size() == 0){
                return;
            }
            selectMap.put(position, isChecked);
            if(isChecked){
                selectProduct.add(bean);
            }else{
                selectProduct.remove(bean);
            }
            if(selectProduct.size() == productBeen.size() || selectProduct.size() == 0){
                chechAll.setChecked(isChecked);
            }else {
                chechAll.setChecked(false);
            }
           /* if(isChecked){
                selectProduct.add(bean);
            }else{
                selectProduct.remove(bean);
            }
            if(selectProduct.size() == productBeen.size() || selectProduct.size() == 0){
                chechAll.setChecked(isChecked);
            }else{
                chechAll.setChecked(false);
            }*/
            LogUtil.e("selectProduct.size()= " + selectProduct.size(), ProductSortAdapter.class);
        });
        if(bean.isIsUpShelf()){
            holder.itemGroup.setBackgroundResource(R.color.white);
        }else{
            holder.itemGroup.setBackgroundResource(R.color.grey_f5);
        }
        return convertView;
    }

    public List<ProductBean> getSelectProduct() {
        return selectProduct;
    }

    public void setDefaultCheck(boolean defaultCheck) {
        selectProduct.clear();
        if(defaultCheck)
            selectProduct.addAll(productBeen);
        chechAll.setChecked(defaultCheck);
        for (int i = 0; i < productBeen.size(); i++) {
            selectMap.put(i, defaultCheck);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.img_good)
        ImageView imgGood;
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_stock)
        TextView tvStock;
        @BindView(R.id.tv_sales_volumes)
        TextView tvSalesVolumes;
        @BindView(R.id.tv_good_price)
        TextView tvGoodPrice;
        @BindView(R.id.item_group)
        RelativeLayout itemGroup;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
