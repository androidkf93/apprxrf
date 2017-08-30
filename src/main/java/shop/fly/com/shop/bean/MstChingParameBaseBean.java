package shop.fly.com.shop.bean;

import java.util.HashMap;
import java.util.Map;

import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.Sha1Util;

/**
 * Created by fly12 on 2017/7/23.
 */

public class MstChingParameBaseBean {
    private String appid;
    private String nonce;
    private String timestamp;
    private String signature;

    public MstChingParameBaseBean() {
        appid = Url.appid;
        nonce = CodeUtil.nonce();
        timestamp = System.currentTimeMillis() / 1000 + "";
        signature = initSignature();
    }

    private String initSignature(){
        Map<String,String> maps = new HashMap<>();
        maps.put("appsecret", Url.appSecret);
        maps.put("nonce", nonce);
        maps.put("timestamp", timestamp);
        String shar1 = Sha1Util.SHA1(maps);
        return shar1;
    }
    /*Map<String,String> maps = new HashMap<>();
    maps.put("appid", Url.appid);
    maps.put("nonce", CodeUtil.nonce());
    maps.put("timestamp", System.currentTimeMillis() / 1000 + "");
    String shar1 = Sha1Util.SHA1(maps);
    maps.put("signature", shar1);*/

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
