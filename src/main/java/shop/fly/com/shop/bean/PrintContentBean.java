package shop.fly.com.shop.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fly12 on 2017/7/30.
 */

public class PrintContentBean {
    private String Uuid;
    private ArrayList<PrintTextBean> PrintContent;
    private int OpenUserId;

    public PrintContentBean(String uuid, ArrayList<PrintTextBean> printContent, int openUserId) {
        this.Uuid = uuid;
        PrintContent = printContent;
        OpenUserId = openUserId;
    }


    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        this.Uuid = uuid;
    }

    public List<PrintTextBean> getPrintContent() {
        return PrintContent;
    }

    public void setPrintContent(ArrayList<PrintTextBean> printContent) {
        PrintContent = printContent;
    }

    public int getOpenUserId() {
        return OpenUserId;
    }

    public void setOpenUserId(int openUserId) {
        OpenUserId = openUserId;
    }
}
