package shop.fly.com.shop.bean;

/**
 * Created by Administrator on 2017/7/10.
 */

public class BluetoothBean {
    public String bluetoothName;
    public String bluetoothAddress;
    public int bluetoothtype;

    public BluetoothBean(String bluetoothName, String bluetoothAddress, int bluetoothtype) {
        this.bluetoothName = bluetoothName;
        this.bluetoothAddress = bluetoothAddress;
        this.bluetoothtype = bluetoothtype;
    }

    public BluetoothBean() {
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public BluetoothBean setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
        return this;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public BluetoothBean setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        return this;
    }

    public int getBluetoothtype() {
        return bluetoothtype;
    }

    public BluetoothBean setBluetoothtype(int bluetoothtype) {
        this.bluetoothtype = bluetoothtype;
        return this;
    }
}
