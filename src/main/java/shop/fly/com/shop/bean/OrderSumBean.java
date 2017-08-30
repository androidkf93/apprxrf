package shop.fly.com.shop.bean;

/**
 * Created by Administrator on 2017/6/20.
 */

public class OrderSumBean {

    /**
     * OrderNumber : 0
     * TotalAmount : 0.0
     */

    private int OrderNumber;
    private double TotalAmount;

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }
}
