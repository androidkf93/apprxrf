package shop.fly.com.shop.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import shop.fly.com.shop.BluetoothOperation;
import shop.fly.com.shop.IPrinterOpertion;
import shop.fly.com.shop.application.MyApplication;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.interfaces.IConnState;
import shop.fly.com.shop.interfaces.MSTCOperationListener;
import shop.fly.com.shop.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/7/13.
 */

public class PrintUtil {

    private static IPrinterOpertion myOpertion;
    private static boolean isConnected;
    private int currIndex = 0;//  0--bluetooth,1--wifi,2--usb
    private IConnState connState;
    private static Context context;
    private static PrintUtil p;

    public static PrintUtil getInstence(Context c){
        context = c;
        if(p == null )
            p = new PrintUtil();
        return p;
    }

    public void setIConnState(IConnState connState){
        this.connState = connState;
    }
    public static IPrinterOpertion getMyOpertion() {
        return myOpertion;
    }

    /**
     * tpye 0 蓝牙， 1 WIFI, 2 全部
     * @param orderBean
     * @param type
     */
    public void print(OrderBean orderBean, int type, MSTCOperationListener listener) {
        String address = orderBean.getAddress();
        String deskNo = orderBean.getDeskNO();
        StringBuffer sbf = new StringBuffer();
        sbf.append(Constant.logUser.getName());

        sbf.append("\n********************************");
        switch (orderBean.getOrderType()){ //订单类型 店内用餐 = 1,外卖派送 = 2,预约订餐 = 3,店内自提 = 4 [Int32]
            case 1:
                sbf.append("\n店内用餐");
                break;
            case 2:
                sbf.append("\n外卖派送");
                break;
            case 3://预订
                sbf.append("\n预约订餐");
                break;
            case 4:
                sbf.append("\n店内自提");
                break;
        }
        sbf.append("\n订单序号：" + orderBean.getOrderIndex());
        sbf.append("\n下单时间：" + orderBean.getAddTime());

        String goodDetails = null;
        try {
            goodDetails = getGoodDetails(orderBean.getDetails());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sbf.append("\n" + goodDetails);
        sbf.append("\n" + "--------------其他--------------");
        switch (orderBean.getOrderType()){ //订单类型 店内用餐 = 1,外卖派送 = 2,预约订餐 = 3,店内自提 = 4 [Int32]
            case 1:
                if(!TextUtils.isEmpty(deskNo)){//座号
                    sbf.append("\n座号：" + orderBean.getDeskNO());
                }
                sbf.append("\n餐具数量：" + orderBean.getTablewareNum());
                break;
            case 2:
                if(orderBean.isTakeOut() && !TextUtils.isEmpty(address) && !address.equals("null")){
                    sbf.append("\n外送地址：" +  orderBean.getAddress());
                }
                sbf.append("\n餐盒数量：" + orderBean.getPackageBoxNum());
                sbf.append("\n打包盒价格：" + orderBean.getPackageBoxTotalPrice());
                sbf.append("\n预计送达：" + orderBean.getBookingTime());
                break;
            case 3://预订
                sbf.append("\n预计到店：" + orderBean.getBookingTime());
                sbf.append("\n餐具数量：" + orderBean.getTablewareNum());
                break;
            case 4:
                sbf.append("\n餐盒数量：" + orderBean.getPackageBoxNum());
                sbf.append("\n打包盒价格：" + orderBean.getPackageBoxTotalPrice());
                break;
        }

        if(orderBean.getDouble(orderBean.getCouponAmount()) > 0){
            sbf.append("\n[优惠券减"+ orderBean.getCouponAmount() + "元]");
        }
        if(orderBean.getDouble(orderBean.getActivityLessAmount()) > 0){
            sbf.append("\n[活动减"+ orderBean.getActivityLessAmount() + "元]");
        }
        if(!TextUtils.isEmpty(orderBean.getRemarks())){
            sbf.append("\n备注：" + orderBean.getRemarks());
        }
        sbf.append("\n********************************");
        sbf.append("\n(原价：" + orderBean.getTotalAmount() + "元)");
        sbf.append("\n(用户在线支付" + orderBean.getPayAmount() + "元)\n\n\n\n\n");
        switch (type){
            case 0:
                if(Constant.mPrinter != null)
                    Constant.mPrinter.printText(sbf.toString());
                break;
            case 1:
                MSTCUtil.printContent(Constant.getUuid(), sbf.toString(), Constant.getOpenUserId(), listener);
                break;
            case 2:
                if(Constant.mPrinter != null)
                    Constant.mPrinter.printText(sbf.toString());
                MSTCUtil.printContent(Constant.getUuid(), sbf.toString(), Constant.getOpenUserId(), listener);
                break;
        }
    }

    private int width = 32;//纸张宽度
    private int defaultRightSpacing = 16; //数量开始位置最后的宽度
    private int rightSpacing = defaultRightSpacing; //数量据左边的距离
    private int leftSpacing = width - defaultRightSpacing; //数量据右边的距离
    private int defaultLeftSpacing = width - defaultRightSpacing;
    private String getGoodDetails(List<OrderBean.DetailsBean> detailsBeen) throws UnsupportedEncodingException {

        StringBuffer sbf = new StringBuffer("");
        for (int i = 0; i < detailsBeen.size(); i++) {
            OrderBean.DetailsBean d = detailsBeen.get(i);
            String name = d.getProductName() + "[" + d.getProductProName() + "]";
            if(name.length() > 14){
                name = name.substring(0, 14);
            }
            String number = "x" + d.getNumber();
            String price = "￥" + d.getPayAmount();
            leftSpacing = defaultLeftSpacing - name.getBytes("gbk").length;
            rightSpacing = defaultRightSpacing - price.getBytes("gbk").length - number.getBytes("gbk").length;
            sbf.append(name);
            for (int j = 0; j < leftSpacing; j++) {//添加距左边的位置
                sbf.append(" ");
            }
            sbf.append(number);
//            oneLeftSpacing = sbf.toString().indexOf("x");
            for (int j = 0; j < rightSpacing; j++) {//添加距左边的位置
                sbf.append(" ");
            }
            sbf.append(price);
            sbf.append("\n");
        }
        return sbf.toString();
    }

    /**
     * 连接蓝牙打印机
     * @param isCacheDivceAddress 是否连接上次连接的蓝牙打印机
     */
    public void openConn(boolean isCacheDivceAddress) {
//        TaskExecutor.runTaskOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        if (context == null)
            context = MyApplication.getContext();
        if(isCacheDivceAddress){
            BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            String deviceAddress = MySharedData.sharedata_ReadString(context, Constant.EXTRA_DEVICE_ADDRESS);
            LogUtil.e("deviceAddress=" + deviceAddress, PrintUtil.class);
            if(!TextUtils.isEmpty(deviceAddress)){
                try {
                    BluetoothDevice remoteDevice = mBtAdapter.getRemoteDevice(deviceAddress);
                    connBluetooth(remoteDevice.getAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                    Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals("printer001")) {
                            connBluetooth(device.getAddress());
                            return;
                        }
                    }
                }
                return;
            }else {
                Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("printer001")) {
                        connBluetooth(device.getAddress());
                        return;
                    }
                }
            }
        }else{
            switch (currIndex) {
                case 0: // bluetooth
                    myOpertion = new BluetoothOperation(context, mHandler);
                    break;
                case 1: // wifi
//                    myOpertion = new WifiOperation(PrintManagerFragment.this, mHandler);
                    break;
                case 2: // usb
//                    myOpertion = new UsbOperation(PrintManagerFragment.this, mHandler);
                    break;
                default:
                    break;
            }
//                myOpertion.chooseDevice(context.getClass().getSimpleName());
        }

    }

    private void connBluetooth(String address) {
        myOpertion = new BluetoothOperation(context, mHandler);
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_DEVICE_ADDRESS, address);
        intent.putExtra(Constant.EXTRA_RE_PAIR, false);
        myOpertion.open(intent);
    }

/*    public Map<String, String> getMstchingMap(){
        Map<String,String> maps = new HashMap<>();
        maps.put("appid", Url.appid);
        maps.put("nonce", CodeUtil.nonce());
        maps.put("timestamp", System.currentTimeMillis() / 1000 + "");
        String shar1 = Sha1Util.SHA1(maps);
        maps.put("signature", shar1);
        return maps;
    }*/


    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    Constant.mPrinter = myOpertion.getPrinter();
                    if(!(context instanceof MainActivity)){
                        Toast.makeText(context,"蓝牙打印机链接成功", Toast.LENGTH_SHORT).show();
                    }else{
                        LogUtil.e("蓝牙打印机链接成功", PrintUtil.class);
                    }
                    if(connState != null)
                        connState.connState(0);
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    Constant.mPrinter = null;
                    if(context instanceof MainActivity){
                        LogUtil.e("蓝牙打印机链接失败", PrintUtil.class);
                    }else{
                        Toast.makeText(context,"蓝牙打印机链接失败", Toast.LENGTH_SHORT).show();
                    }
                    if(connState != null)
                        connState.connState(1);
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    Constant.mPrinter = null;
                    if(context instanceof MainActivity){
                        LogUtil.e("蓝牙打印机链接关闭", PrintUtil.class);
                    }else{
                        Toast.makeText(context,"蓝牙打印机链接关闭", Toast.LENGTH_SHORT).show();
                    }
                    if(connState != null)
                        connState.connState(2);
//                    Toast.makeText(context, "蓝牙打印机链接关闭", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
//            updateButtonState();
        }

    };


/*    private void updateButtonState(){
        if(!isConnected){

            switch (currIndex) {
                case 0:
//                    connStr = String.format(connStr, btnBluetooth.getText());
//                    tvBluetoothPrintType.setText(connStr);
                    break;
                case 1:
//                    connStr = String.format(connStr, btnWifi.getText());
                    break;
                case 2:
//                    connStr = String.format(connStr, btnUsb.getText());
                    break;
                default:
                    break;
            }

        }else{

        }

    }*/

}
