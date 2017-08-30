package shop.fly.com.shop.constant;

import android.text.TextUtils;

import com.android.print.sdk.PrinterInstance;

import java.util.ArrayList;
import java.util.List;

import shop.fly.com.shop.application.MyApplication;
import shop.fly.com.shop.bean.Login;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.bean.UserBean;
import shop.fly.com.shop.util.MySharedData;
import shop.fly.com.shop.util.NotifyMenagerUtil;

/**
 * Created by Administrator on 2017/6/5.
 */

public class Constant {
    private static String uuid = "";
    private static int openUserId = 0;

    public static Login login;
    public static String user = "B8BDEF99-ECEA-4A69-A23C-521641F66E39";
    public static UserBean logUser = new UserBean();
    public static final int NEW_ORDER = 1008;
    public static final int NEW_ORDER_ADDRESS = 1009;
    public static final int NEW_ORDER_SEAT = 1012;
    public static final int REFUND_ORDER = 1010;
    public static final int REQUEST_CODE_SCAN =1011;
    public static final ArrayList<Integer> notifyIds = new ArrayList<>();

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_RE_PAIR = "re_pair";
    public static final String DECODED_CONTENT_KEY = "codedContent";
    public static final String DECODED_BITMAP_KEY = "codedBitmap";

    public static final String IS_CHECK_BS = "isCheckBs";
    public static PrinterInstance mPrinter;
    public static boolean defaultPrinter = true;

    public static String versionCode;
    public static String DOWN_LODE_APP;
    public static OrderBean orderBean;

    public static String getUuid() {
        if(TextUtils.isEmpty(uuid)){
            uuid = MySharedData.sharedata_ReadString(MyApplication.getContext(), "MSTCUuid");
        }
        return uuid;
    }

    public static void setUuid(String uuid) {
        Constant.uuid = uuid;
       MySharedData.sharedata_WriteString(MyApplication.getContext(), "MSTCUuid", uuid);
    }

    public static int getOpenUserId() {
        if(openUserId == 0){
            openUserId = MySharedData.sharedata_ReadInt(MyApplication.getContext(),"MSTCOpenUserId");
        }
        return openUserId;
    }

    public static void setOpenUserId(int openUserId) {
        Constant.openUserId = openUserId;
        MySharedData.sharedata_WriteInt(MyApplication.getContext(),"MSTCOpenUserId", openUserId);
    }

    public static void unBoundGPRS(){
        openUserId = 0;
        uuid = "";
        MySharedData.sharedata_WriteInt(MyApplication.getContext(), IS_CHECK_BS, 0);
        MySharedData.sharedata_WriteInt(MyApplication.getContext(), "MSTCOpenUserId", 0);
        MySharedData.sharedata_WriteString(MyApplication.getContext(), "MSTCUuid", "");
    }

    public static Login getLogin() {
        if (login == null || TextUtils.isEmpty(login.getName())){
            login = new Login();
            login = MySharedData.getAllDate(MyApplication.getContext(), login);
        }
        return login;
    }

    public static void setLogin(Login l) {
        MySharedData.putAllDate(MyApplication.getContext(), l);
        login = l;
    }
    public static void cleanLogin() {
        Login l = new Login();
        MySharedData.cleanDate(MyApplication.getContext(), l);
        login = l;
    }
    /**
     * 是否多打印一份给商家
     */
    public static boolean isCheckBs() {
        return MySharedData.sharedata_ReadInt(MyApplication.getContext(), IS_CHECK_BS) == 1;
    }

    /*(String orderNo, String orderIndex, String deskNO, String totalAmount,
    String payAmount, String lessAmount, String status, String couponAmount,
    String activityId, String activityLessAmount, String memberName, boolean takeOut,
    boolean isBooking, boolean isTakeSelf, String mobile, String address,
    String addTime, List<DetailsBean> details*/
//    "yyyy-MM-dd'T'HH:mm:ss.SSS"

    public static OrderBean getDefaultPrint(){
        if(orderBean == null){
            List<OrderBean.DetailsBean> details = new ArrayList<>();
//            String orderNo, int productId, String productName, int price, int number, int payAmount, int productProId, String productProName
            details.add(new OrderBean.DetailsBean("20170803888", 11, "大盘鸡", "68", 1, "68", 1, "大份"));
            details.add(new OrderBean.DetailsBean("20170803999", 12, "凉拌牛肉", "32", 1, "32", 2, "四两"));
            orderBean = new OrderBean("20170803888",//orderNo
                    "888",//orderIndex
                    "16",//deskNO
                    "100",//totalAmount
                    "95",//payAmount
                    "5",//lessAmount
                    "0",//status
                    "5",//couponAmount
                    "166",//activityId
                    "10",//activityLessAmount
                    "周年庆",//memberName
                    true,//takeOut
                    false,
                    false,
                    "13283718662",
                    "金水区曼哈顿广场宝视达",
                    "2017-08-03 10:20:30",
                    "2017-08-03 11:30:30",
                    "多放牛肉，多放鸡腿，少放鸡头，不要鸡毛",
                    1,
                    details);
        }
        return orderBean;
    }
}
