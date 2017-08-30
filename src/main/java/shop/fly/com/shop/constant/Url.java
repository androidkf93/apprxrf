package shop.fly.com.shop.constant;

/**
 * Created by fly12 on 2017/6/3.
 */

public class Url {

    public final static String appid = "c49b95114a45";
    public final static String appSecret = "4ba9b42696b0baa76619";

    public final static String URL = "http://api.mubai168.com/api/";
    private final static String URLMstching = "http://www.open.mstching.com/";

    public final static String UpdateImage = URL + "handler/UploadImage";
    public final static String GetVersion = URL + "handler/GetVersion";

    public final static String StoreDownShelf = URL + "Store/DownShelf";
    public final static String MstchingUserBind = URLMstching + "home/userbind" ;
    public final static String MstchingGetDeviceState = URLMstching + "home/getdevicestate" ;
    public final static String MstchingPrintContent2 = URLMstching + "home/printcontent2" ;
}
