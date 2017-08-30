package shop.fly.com.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.constant.RequestCode;
import shop.fly.com.shop.menus.SpecMode;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.ui.fragment.CategoryAddFragment;
import shop.fly.com.shop.ui.fragment.ProductAddFragment;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> been;
    private LayoutInflater inflater;
    private String categoryName;
    private int type = 0;
    private int selectPosition = 0;

    public CategoryAdapter(Context context, List<CategoryBean> been) {
        this.context = context;
        this.been = been;
        inflater = LayoutInflater.from(context);
        type = 0;
    }

    public CategoryAdapter(Context context, List<CategoryBean> been, int type) {
        this.context = context;
        this.been = been;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectPosition(int selectPosition) {
        if(type == 1){
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }
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
        CategoryBean bean = been.get(position);
        if(type == 0){
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_category, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvCategoryName.setText(bean.getName());
            holder.tvGoodNum.setText(bean.getProductCount() + "个商品");
            holder.tvEdit.setOnClickListener(view -> {
                Intent intent = new Intent(context, GroupFragmentActivity.class);
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategoryAddFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putString("name", bean.getName());
                bundle.putString("remark", bean.getRemark());
                bundle.putInt("id", bean.getId());
                intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                ((Activity)context).startActivityForResult(intent, RequestCode.CATEGORY);
            });

            holder.tvAddGood.setOnClickListener(v -> {
                Intent intent = new Intent(context, GroupFragmentActivity.class);
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, ProductAddFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", bean);
                intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                ((Activity)context).startActivityForResult(intent, RequestCode.CATEGORY);
            });

        }else{
            convertView = inflater.inflate(R.layout.item_category_one_text, parent, false);
            TextView categoryName = (TextView) convertView.findViewById(R.id.tv_category_name);
            categoryName.setEnabled(selectPosition != position);
            categoryName.setText(bean.getName());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_good_num)
        TextView tvGoodNum;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_add_good)
        TextView tvAddGood;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
