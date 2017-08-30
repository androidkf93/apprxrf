package shop.fly.com.shop;

import android.content.Intent;

import com.android.print.sdk.PrinterInstance;

public interface IPrinterOpertion {
	public void open(Intent data);
	public void close();
	public void chooseDevice(String from);
	public PrinterInstance getPrinter();
}
