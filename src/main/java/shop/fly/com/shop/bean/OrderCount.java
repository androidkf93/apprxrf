package shop.fly.com.shop.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/29.
 */

public class OrderCount {

    /**
     * 1 : 1
     * 2 : 2
     * 3 : 3
     * 4 : 4
     */

    @SerializedName("1")
    private int value1;
    @SerializedName("2")
    private int value2;
    @SerializedName("3")
    private int value3;
    @SerializedName("4")
    private int value4;

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

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

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }
}
