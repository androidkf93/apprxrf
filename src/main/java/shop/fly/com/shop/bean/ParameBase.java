package shop.fly.com.shop.bean;

import java.io.Serializable;

import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.SDFUtil;

/**
 * Created by fly12 on 2017/6/3.
 */

public class ParameBase implements Serializable{

    /**
     *时间 格式 yyyy-MM-dd hh:mm:ss
     */
    private String time = SDFUtil.getTime();
    public String getTime() {
        return time;
    }

    public void setTime(String val) {
        time = val;
    }

    /**
     *来源网站 = 1, IOS = 2,Android = 3, 微信 = 4
     */
    private Integer sourceFrom;
    public Integer getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(Integer val) {
        sourceFrom = val;
    }

    /**
     *签名
     */
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String val) {
        token = val;
    }

}
