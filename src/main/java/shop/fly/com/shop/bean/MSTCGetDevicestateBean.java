package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/7/23.
 */

public class MSTCGetDevicestateBean{
    private String Uuid;
    private String UserId;

    public MSTCGetDevicestateBean(String uuid, String UserId) {
        Uuid = uuid;
        this.UserId = UserId;
    }

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
}
