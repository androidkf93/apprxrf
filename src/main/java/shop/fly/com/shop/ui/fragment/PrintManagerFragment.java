package shop.fly.com.shop.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import shop.fly.com.shop.BluetoothOperation;
import shop.fly.com.shop.R;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.RequestCode;
import shop.fly.com.shop.interfaces.IConnState;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.MSTCUtil;
import shop.fly.com.shop.util.PrintUtil;


/**
 * Created by Administrator on 2017/7/10.
 */

public class PrintManagerFragment extends GroupItemFragment implements IConnState{
    @BindView(R.id.tv_bluetooth_print_type)
    TextView tvBluetoothPrintType;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_offer)
    TextView tvOffer;
    @BindView(R.id.ll_wifi_print_connect_finish)
    LinearLayout llPrintWifi;
    @BindView(R.id.ll_bluetooth_print_connect)
    LinearLayout llBluetoothPrintConnect;
    @BindView(R.id.tv_print_name)
    TextView tvPrintName;
    @BindView(R.id.tv_print_type)
    TextView tvPrintType;
    private static boolean isConnected;
    private int currIndex = 0;//  0--bluetooth,1--wifi,2--usb

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_print_manager, container, false);
    }

    @Override
    protected void initDate(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }
        if(!TextUtils.isEmpty(Constant.getUuid())){
            tvPrintName.setText(Constant.getUuid());
        }
        if(Constant.mPrinter == null || !Constant.mPrinter.isConnected())
            tvBluetoothPrintType.setText("未连接");
        getGPRSstate();
    }

    private void getGPRSstate() {
        if(TextUtils.isEmpty(Constant.getUuid())){
            tvPrintName.setText("未连接");
            tvPrintType.setText("");
            return;
        }
        MSTCUtil.getDevicestate(Constant.getUuid(), data -> {
            tvPrintName.setText(Constant.getUuid());
            switch (Integer.parseInt(data)){
                case 0:
                    tvPrintType.setText("");
                    break;
                case 1:
                    tvPrintType.setText("缺纸");
                    break;
                case 2:
                    break;
                case 4:
                    tvPrintType.setText("离线");
                    break;
            }
        });
    }

    @Override
    protected void getDate(Context context) {
        LogUtil.e("getDate()", this.getClass());
    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftText(null, "打印设置", v -> getActivity().finish());
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        iTitle.setTitleText("");
    }


    @OnClick({R.id.ll_print_wifi_add, R.id.ll_help, R.id.ll_switch, R.id.ll_bluetooth_print_connect,
            R.id.ll_wifi_print_connect_finish})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_print_wifi_add:
                intent = new Intent(getContext(), GroupFragmentActivity.class);
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, AddGPRSPrintFragment.class.getName());
                startActivityForResult(intent, RequestCode.GPRS_ADD);
             /*   if(TextUtils.isEmpty(Constant.getUuid()) && permission.isCheckPermission(new String[]{Manifest.permission.CAMERA})){
                    startScan();
                }*/
                break;
            case R.id.ll_help:
                break;
            case R.id.ll_bluetooth_print_connect:
                intent = new Intent(getContext(), GroupFragmentActivity.class);
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, BluetoothDeviceListFragment.class.getName());
                startActivityForResult(intent, BluetoothOperation.CONNECT_DEVICE);
                break;
            case R.id.ll_switch:
                if(tvClose.getVisibility() == View.INVISIBLE){
                    tvClose.setVisibility(View.VISIBLE);
                    tvOffer.setVisibility(View.INVISIBLE);
                    Constant.defaultPrinter = false;
                }else{
                    tvClose.setVisibility(View.INVISIBLE);
                    tvOffer.setVisibility(View.VISIBLE);
                    Constant.defaultPrinter = true;
                }
                break;
            case R.id.tv_close:
                tvOffer.setVisibility(View.INVISIBLE);
                tvOffer.setEnabled(true);
                tvClose.setVisibility(View.VISIBLE);
                tvClose.setEnabled(false);
                break;
            case R.id.tv_offer:
                tvClose.setVisibility(View.INVISIBLE);
                tvClose.setEnabled(true);
                tvOffer.setVisibility(View.VISIBLE);
                tvOffer.setEnabled(false);
                break;
            case R.id.ll_wifi_print_connect_finish:
                if(!TextUtils.isEmpty(Constant.getUuid())){
                    intent = new Intent(getContext(), GroupFragmentActivity.class);
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, GPRSPrintMenagerFragment.class.getName());
                    startActivityForResult(intent, RequestCode.GPRS_P_M_F);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case BluetoothOperation.CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    PrintUtil.getInstence(getContext()).openConn(false);
                    PrintUtil.getInstence(getContext()).setIConnState(this);
                    PrintUtil.getInstence(getContext()).getMyOpertion().open(data);
                }
                break;
            case BluetoothOperation.ENABLE_BT:
                if (resultCode == Activity.RESULT_OK){
                    PrintUtil.getInstence(getContext()).getMyOpertion().chooseDevice(this.getClass().getSimpleName());
                }else{
                }
                break;
            case RequestCode.GPRS_ADD:
            case RequestCode.GPRS_P_M_F:
                getGPRSstate();
                break;
        }
    }



    @Override
    public void connState(int state) {
        switch (state){
            case 0:
            case 1:
            case 2:
                if(Constant.mPrinter == null || !Constant.mPrinter.isConnected())
                    tvBluetoothPrintType.setText("未连接");
                else
                    tvBluetoothPrintType.setText("正常");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PrintUtil.getInstence(getContext()).setIConnState(null);
    }
}
