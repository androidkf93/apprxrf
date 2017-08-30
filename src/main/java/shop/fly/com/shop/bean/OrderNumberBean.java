package shop.fly.com.shop.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/14.
 */

public class OrderNumberBean {
    /**
     * 1 : 1
     */

    @SerializedName("1")
    private int value1;

    @SerializedName("2")
    private int value2;

    @SerializedName("3")
    private int value3;

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

}
