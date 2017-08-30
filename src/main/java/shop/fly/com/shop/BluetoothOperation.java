package shop.fly.com.shop;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;


import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;

import java.lang.reflect.Method;

import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.ui.fragment.BluetoothDeviceListFragment;
import shop.fly.com.shop.util.MySharedData;

public class BluetoothOperation implements IPrinterOpertion {
	public static final int CONNECT_DEVICE = 1;
	public static final int ENABLE_BT = 2;
	private static final String TAG = "BluetoothOpertion";
	private BluetoothAdapter adapter;
	private Context mContext;
	private boolean hasRegBoundReceiver;
	private boolean rePair;

	private BluetoothDevice mDevice;
	private String deviceAddress;
	private Handler mHandler;
	//	private PrinterInstance Constant.mPrinter;
	private boolean hasRegDisconnectReceiver;
	private IntentFilter filter;

	public BluetoothOperation(Context context, Handler handler) {
		adapter = BluetoothAdapter.getDefaultAdapter();
		mContext = context;
		mHandler = handler;
		hasRegDisconnectReceiver = false;

		filter = new IntentFilter();
		//filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		//filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
	}

	public void open(Intent data) {
		if(data == null)
			return;
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		deviceAddress = data.getExtras().getString(
				BluetoothDeviceListFragment.EXTRA_DEVICE_ADDRESS);
		mDevice = adapter.getRemoteDevice(deviceAddress);
		if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
			Log.i(TAG, "device.getBondState() is BluetoothDevice.BOND_NONE");
			PairOrRePairDevice(false, mDevice);
		} else if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
			rePair = data.getExtras().getBoolean(BluetoothDeviceListFragment.EXTRA_RE_PAIR);
			if (rePair) {
				PairOrRePairDevice(true, mDevice);
			} else {
				openPrinter();
			}
		}
	}

	// use device to init printer.
	private void openPrinter() {
		Log.e("Bluetooth", "openPrinter()");
		Constant.mPrinter = new PrinterInstance(mContext, mDevice, mHandler);
		// default is gbk...
		// Constant.mPrinter.setEncoding("gbk");
		MySharedData.sharedata_WriteString(mContext, BluetoothDeviceListFragment.EXTRA_DEVICE_ADDRESS, deviceAddress);
		Constant.mPrinter.openConnection();
	}

	private boolean PairOrRePairDevice(boolean re_pair, BluetoothDevice device) {
		boolean success = false;
		try {
			if (!hasRegBoundReceiver) {
				mDevice = device;
				IntentFilter boundFilter = new IntentFilter(
						BluetoothDevice.ACTION_BOND_STATE_CHANGED);
				mContext.registerReceiver(boundDeviceReceiver, boundFilter);
				hasRegBoundReceiver = true;
			}
			if (!(mContext instanceof MainActivity))
				if (re_pair) {
					// cancel bond
					Method removeBondMethod = BluetoothDevice.class
							.getMethod("removeBond");
					success = (Boolean) removeBondMethod.invoke(device);
					Log.i(TAG, "removeBond is success? : " + success);
				} else {
					// Input password
					// Method setPinMethod =
					// BluetoothDevice.class.getMethod("setPin");
					// setPinMethod.invoke(device, 1234);
					// create bond

					Method createBondMethod = BluetoothDevice.class
							.getMethod("createBond");
					success = (Boolean) createBondMethod.invoke(device);
					Log.i(TAG, "createBond is success? : " + success);
				}
		} catch (Exception e) {
			Log.i(TAG, "removeBond or createBond failed.");
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	// receive bound broadcast to open connect.
	private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (!mDevice.equals(device)) {
					return;
				}
				switch (device.getBondState()) {
					case BluetoothDevice.BOND_BONDING:
						Log.i(TAG, "bounding......");
						break;
					case BluetoothDevice.BOND_BONDED:
						Log.i(TAG, "bound success");
						// if bound success, auto init BluetoothPrinter. open
						// connect.
						if (hasRegBoundReceiver) {
							mContext.unregisterReceiver(boundDeviceReceiver);
							hasRegBoundReceiver = false;
						}
						openPrinter();
						break;
					case BluetoothDevice.BOND_NONE:
						if (rePair) {
							rePair = false;
							Log.i(TAG, "removeBond success, wait create bound.");
							PairOrRePairDevice(false, device);
						} else if (hasRegBoundReceiver) {
							mContext.unregisterReceiver(boundDeviceReceiver);
							hasRegBoundReceiver = false;
							// bond failed
							mHandler.obtainMessage(PrinterConstants.Connect.FAILED).sendToTarget();
							Log.i(TAG, "bound cancel");
						}
					default:
						break;
				}
			}
		}
	};

	public void close() {
		if (Constant.mPrinter != null) {
			Constant.mPrinter.closeConnection();
			Constant.mPrinter = null;
		}
		if(hasRegDisconnectReceiver){
			mContext.unregisterReceiver(myReceiver);
			hasRegDisconnectReceiver = false;
		}
	}

	public PrinterInstance getPrinter() {
		if (Constant.mPrinter != null && Constant.mPrinter.isConnected()) {
			if(!hasRegDisconnectReceiver){
				mContext.registerReceiver(myReceiver, filter);
				hasRegDisconnectReceiver = true;
			}
		}
		return Constant.mPrinter;
	}

	// receive the state change of the bluetooth.
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			Log.i(TAG, "receiver is: " + action);
			if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
				if (device != null && Constant.mPrinter != null && Constant.mPrinter.isConnected() && device.equals(mDevice)) {
					close();
				}
			}
		}
	};

	@Override
	public void chooseDevice(String from) {
		Log.e("Bluetooth_from", from);
		if (!adapter.isEnabled()) {
			Log.e("Bluetooth", !adapter.isEnabled() + "");
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			((Activity) mContext).startActivityForResult(enableIntent,
					ENABLE_BT);
		} else {
			Intent intent = new Intent(mContext, GroupFragmentActivity.class);
			intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, BluetoothDeviceListFragment.class.getName());
			((Activity) mContext).startActivityForResult(intent,
					CONNECT_DEVICE);
			Log.e("Bluetooth", !adapter.isEnabled() + "");
		}
	}
}
