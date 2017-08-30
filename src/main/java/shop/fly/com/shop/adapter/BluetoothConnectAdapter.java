package shop.fly.com.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.BluetoothBean;
import shop.fly.com.shop.ui.fragment.BluetoothDeviceListFragment;

/**
 * Created by Administrator on 2017/7/10.
 */

public class BluetoothConnectAdapter extends BaseAdapter {
    private List<BluetoothBean> bluetoothBeen;
    private BluetoothDeviceListFragment fragment;
    private LayoutInflater inflater;

    public BluetoothConnectAdapter(BluetoothDeviceListFragment fragment, List<BluetoothBean> bluetoothBeen) {
        this.bluetoothBeen = bluetoothBeen;
        this.fragment = fragment;
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @Override
    public int getCount() {
        return bluetoothBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_nearby_connect, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothBean bean = bluetoothBeen.get(position);
        holder.tvBluetoothName.setText(bean.getBluetoothName() + "(" + (bean.bluetoothtype == 0 ? "未连接" : "已连接") + ")");
        holder.tvBluetoothAddress.setText(bean.getBluetoothAddress());
        if(TextUtils.isEmpty(bean.getBluetoothAddress())){
            holder.tvConnect.setVisibility(View.GONE);
        }
//        holder.tvConnect.setText(bean.bluetoothtype == 0 ? "连接" : "断开连接");
        holder.tvConnect.setOnClickListener(v -> {
            String address = bean.getBluetoothAddress();
//            if(address.length())
//            address = address.substring(info.length() - 17);
            switch (bean.getBluetoothtype()) {
                case 0://repair and connect
                   fragment.returnToPreviousActivity(address, false);
                    break;
                case 1://connect
                case 2://pair and connect
                    fragment.returnToPreviousActivity(address, true);
                    break;
            }
        });
        return convertView;
    }
    public void cleanItem(){
        bluetoothBeen.clear();
        notifyDataSetChanged();
    }

    public void addItem(BluetoothBean bluetoothBean){
//        bluetoothBeen.remove(bluetoothBean);
        for (BluetoothBean b:
             bluetoothBeen) {
            if(b.getBluetoothAddress().equals(bluetoothBean.getBluetoothAddress()))
            return;
        }
        bluetoothBeen.add(bluetoothBean);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_bluetooth_name)
        TextView tvBluetoothName;
        @BindView(R.id.tv_bluetooth_address)
        TextView tvBluetoothAddress;
        @BindView(R.id.tv_connect)
        TextView tvConnect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
