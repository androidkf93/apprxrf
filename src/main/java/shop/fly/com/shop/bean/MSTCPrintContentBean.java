package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/7/23.
 */

public class MSTCPrintContentBean {
    private String Uuid;
    private String PrintContent;
    private int OpenUserId;

    public MSTCPrintContentBean(String uuid, String printContent, int openUserId) {
        Uuid = uuid;
        PrintContent = printContent;
        OpenUserId = openUserId;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getPrintContent() {
        return PrintContent;
    }

    public void setPrintContent(String printContent) {
        PrintContent = printContent;
    }

    public int getOpenUserId() {
        return OpenUserId;
    }

    public void setOpenUserId(int openUserId) {
        OpenUserId = openUserId;
    }

}
