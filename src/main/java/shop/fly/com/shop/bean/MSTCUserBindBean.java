package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/7/23.
 */

public class MSTCUserBindBean extends MstChingParameBaseBean {
    private String Uuid;
    private String UserId;

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public MSTCUserBindBean(String uuid, String userId) {
        super();
        Uuid = uuid;
        UserId = userId;
    }

}
