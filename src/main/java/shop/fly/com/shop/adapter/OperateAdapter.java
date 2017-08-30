package shop.fly.com.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.OperateBean;

/**
 * Created by fly12 on 2017/6/17.
 */

public class OperateAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OperateBean> been;
    private LayoutInflater inflater;

    public OperateAdapter(Context context, ArrayList<OperateBean> been) {
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
            convertView = inflater.inflate(R.layout.item_home_operate, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgIcon.setImageDrawable(been.get(position).getDrawable());
        holder.tvItemName.setText(been.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
