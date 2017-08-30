package shop.fly.com.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.ui.activity.parent.ParentFragmentActivity;
import shop.fly.com.shop.ui.fragment.ProductAddFragment;
import shop.fly.com.shop.ui.fragment.ProductListFragment;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ProductAdapter extends BaseAdapter {
    private List<ProductBean> productBeen;
    private ProductListFragment productListFragment;
    private Context context;
    private LayoutInflater inflater;
    private CategoryBean categoryBean;
    public ProductAdapter(ProductListFragment productListFragment, List<ProductBean> productBeen, CategoryBean categoryBean) {
        this.productListFragment = productListFragment;
        this.productBeen = productBeen;
        this.categoryBean = categoryBean;
        context = productListFragment.getContext();
        inflater = LayoutInflater.from(productListFragment.getContext());
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
            convertView = inflater.inflate(R.layout.item_good, parent, false);
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
        holder.tvGoodPrice.setText(bean.getPrice());
        String stock = bean.getStock() == -1 ? "库存无限" : "库存：" + bean.getStock();
        holder.tvStock.setText(stock);
        holder.tvSalesVolumes.setText("月销：" + bean.getSoldCount());
        holder.tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupFragmentActivity.class);
            intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, ProductAddFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putBoolean("type", true);
            bundle.putSerializable("category", categoryBean);
            bundle.putSerializable("data", bean);
            intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
            ((Activity)context).startActivityForResult(intent, 5);
        });
        if(bean.isIsUpShelf()){
            holder.tvUp.setText("下架");
        }else{
            holder.tvUp.setText("上架");
        }

        holder.tvUp.setOnClickListener(v ->{
            productListFragment.showLoading();
            TaskExecutor.executeNetTask(() ->{
                RetrofitFactory.getInstance().productUpShelf(bean.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<String>(context) {
                            @Override
                            protected void onHandleSuccess(String s) {
                                productListFragment.hideLoading();
                                bean.setIsUpShelf(!bean.isIsUpShelf());
                                if(bean.isIsUpShelf()){
                                    holder.tvUp.setText("下架");
                                }else{
                                    holder.tvUp.setText("上架");
                                }
                            }
                        });
            });

        });

        return convertView;
    }

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    static class ViewHolder {
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
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_up)
        TextView tvUp;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
