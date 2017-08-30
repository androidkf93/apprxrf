package shop.fly.com.shop.bean;

import com.xuke.zxing.zxing.android.Intents;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by fly12 on 2017/7/30.
 */

public class PrintTextBean {
    private int Alignment = 0;
    private String BaseText;
    private int Bold = 0;
    private int FontSize = 0;
    private int PrintType = 0;

    public PrintTextBean(String baseTex) {

        BaseText = baseTex;
      /*  try {
            BaseText = URLEncoder.encode(baseTex, "gbk");
        } catch (UnsupportedEncodingException e) {
            BaseText = "";
            e.printStackTrace();
        }*/
    }

    public int getAlignment() {
        return Alignment;
    }

    public void setAlignment(int alignment) {
        Alignment = alignment;
    }

    public String getBaseTex() {
        return BaseText;
    }

    public void setBaseTex(String baseTex) {
        BaseText = baseTex;
     /*   try {
            BaseText = URLEncoder.encode(baseTex, "gbk");
        } catch (UnsupportedEncodingException e) {
            BaseText = "";
            e.printStackTrace();
        }*/
    }

    public int getBold() {
        return Bold;
    }

    public void setBold(int bold) {
        Bold = bold;
    }

    public int getFontSize() {
        return FontSize;
    }

    public void setFontSize(int fontSize) {
        FontSize = fontSize;
    }

    public int getPrintType() {
        return PrintType;
    }

    public void setPrintType(int printType) {
        PrintType = printType;
    }
}
