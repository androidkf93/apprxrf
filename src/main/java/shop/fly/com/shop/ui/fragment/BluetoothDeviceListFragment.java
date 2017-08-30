package shop.fly.com.shop.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.BluetoothConnectAdapter;
import shop.fly.com.shop.bean.BluetoothBean;
import shop.fly.com.shop.interfaces.ITitle;

/**
 * Created by Administrator on 2017/7/10.
 */

public class BluetoothDeviceListFragment extends GroupItemFragment {

    @BindView(R.id.lv_bluetooth_nearby)
    ListView lvBluetoothNearby;
    @BindView(R.id.btn_scanning)
    Button btnScanning;

    private static final String TAG = "DeviceListActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_RE_PAIR = "re_pair";
    private static final int ACCESS_LOCATION = 1001;
    private BluetoothAdapter mBtAdapter;
    private BluetoothConnectAdapter adapter;
    private ArrayList<BluetoothBean> bluetoothBeen = new ArrayList<>();

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_print_connect, container, false);
    }

    @Override
    protected void initDate(Context context) {
        adapter = new BluetoothConnectAdapter(this, bluetoothBeen);
        lvBluetoothNearby.setAdapter(adapter);
        lvBluetoothNearby.setOnItemClickListener(mDeviceClickListener);
//        lvBluetoothNearby.setOnCreateContextMenuListener(mCreateContextMenuListener);
//        lvBluetoothNearby.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });
        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(requestPermission()){
            doDiscovery();
        }
    }

    @Override
    protected void getDate(Context context) {

    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        iTitle.setLeftText(null, "蓝牙打印机", v -> getActivity().finish());
        iTitle.setTitleText("");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_LOCATION:
                if (hasAllPermissionsGranted(grantResults)) {
                    // Permission Granted
                    doDiscovery();
                    btnScanning.setEnabled(false);
                } else {
                    // Permission Denied
                }
                break;
        }
    }


    @OnClick(R.id.btn_scanning)
    void onClick(View view){
        if(requestPermission()){
            doDiscovery();
            view.setEnabled(false);
        }
    }


    private boolean requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionCheck = 0;
            permissionCheck = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionCheck += getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                //注册权限
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_LOCATION); //Any number
                return false;
            }else{//已获得过权限
                //进行蓝牙设备搜索操作
                return true;
            }
        }
        return false;
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Start device discover with the BluetoothConnectAdapter
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
//        getActivity().setProgressBarIndeterminateVisibility(true);
        showLoading();
        btnScanning.setEnabled(false);
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        adapter.cleanItem();
        // Request discover from BluetoothConnectAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, which is the last 17 chars in the View
        /*    String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            returnToPreviousActivity(address, false);*/
        }
    };



    private View.OnCreateContextMenuListener mCreateContextMenuListener = new View.OnCreateContextMenuListener(){
        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo arg2) {
            menu.setHeaderTitle(R.string.select_options);

            String info = ((TextView)(((AdapterView.AdapterContextMenuInfo)arg2).targetView)).getText().toString();
            //if(((AdapterContextMenuInfo)arg2).position < pairedDeviceNum)
            if (info.contains(" ( " + getResources().getText(R.string.has_paired) +" )")) {
                menu.add(0, 0, 0, R.string.rePaire_connect).setOnMenuItemClickListener(mOnMenuItemClickListener);
                menu.add(0, 1, 1, R.string.connect_paired).setOnMenuItemClickListener(mOnMenuItemClickListener);
            }
            else
            {
                menu.add(0, 2, 2, R.string.paire_connect).setOnMenuItemClickListener(mOnMenuItemClickListener);
            }
        }
    };
    @Override
    public void onResume() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().registerReceiver(mReceiver, intent);
        super.onResume();
    }


    @Override
    public void onStop() {
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null && mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        getContext().unregisterReceiver(mReceiver);
        super.onStop();
    }
    private final MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = item -> {
        String info = ((TextView)((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).targetView).getText().toString();
        String address = info.substring(info.length() - 17);
        switch (item.getItemId()) {
            case 0://repair and connect
                returnToPreviousActivity(address, true);
                break;
            case 1://connect
            case 2://pair and connect
                returnToPreviousActivity(address, false);
                break;
        }
        return false;
    };
    public void returnToPreviousActivity(String address, boolean re_pair)
    {
        // Cancel discovery because it's costly and we're about to connect
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Create the result Intent and include the MAC address
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
        intent.putExtra(EXTRA_RE_PAIR, re_pair);

        // Set result and finish this Activity
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if(b != null){
                Object[] lstName = b.keySet().toArray();
                // 显示所有收到的消息及其细节
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
                }
            }
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                BluetoothBean bluetoothBean = new BluetoothBean(device.getName(),
                        device.getAddress(),
                        device.getBondState() == BluetoothDevice.BOND_BONDED ? 1 : 0 );
                /*String itemName = device.getName()
                        + " ( " + getResources().getText(device.getBondState() == BluetoothDevice.BOND_BONDED ? R.string.has_paired : R.string.not_paired) +" )"
                        + "\n" + device.getAddress();*/
//                mPairedDevicesArrayAdapter.remove(itemName);
                adapter.addItem(bluetoothBean);
                lvBluetoothNearby.setEnabled(true);
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                hideLoading();
                if (adapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    adapter.addItem(new BluetoothBean(noDevices, "", -1));
                    lvBluetoothNearby.setEnabled(false);
                }
                btnScanning.setEnabled(true);
            }
        }
    };

}
