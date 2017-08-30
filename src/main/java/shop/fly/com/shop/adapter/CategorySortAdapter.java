package shop.fly.com.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.ui.fragment.CategorySortFragment;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategorySortAdapter extends BaseAdapter {
    private CategorySortFragment categorySortFragment;
    private List<CategoryBean> been;
    private LayoutInflater inflater;

    public CategorySortAdapter(CategorySortFragment categorySortFragment, List<CategoryBean> been) {
        this.been = been;
        this.categorySortFragment = categorySortFragment;
        inflater = LayoutInflater.from(categorySortFragment.getContext());
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_category_edit, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCategoryName.setText(bean.getName());
        holder.tvGoodNum.setText(bean.getProductCount() + "个商品");
        holder.imgDelete.setOnClickListener(view -> {
            categorySortFragment.delete(bean.getId());
        });
        holder.imgTop.setOnClickListener(v -> {
            categorySortFragment.sort(bean.getId(), true);
        });
        return convertView;
    }





    static class ViewHolder {
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_good_num)
        TextView tvGoodNum;
        @BindView(R.id.img_sort)
        ImageView imgSort;
        @BindView(R.id.img_top)
        ImageView imgTop;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
