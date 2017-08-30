package shop.fly.com.shop.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/13.
 */

public class VisionCodeBean {

    @SerializedName("package")
    private String packageX;
    private String versionName;
    private String versionCode_N;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode_N() {
        return versionCode_N;
    }

    public void setVersionCode_N(String versionCode_N) {
        this.versionCode_N = versionCode_N;
    }
}
