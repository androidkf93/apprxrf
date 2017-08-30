package shop.fly.com.shop.menus;

/**
 * Created by Administrator on 2017/6/13.
 */

public enum OrderMode {
    /**
     * 待付款
     */
    PendingPayment(0),
    /**
     * 已付款=>新订单
     */
    Piad(1),
    /**
     * 已完成=>已完成
     */
    Completed(2),
    /**
     * 申请退款=>退款
     */
    ApplyForRefund(3),

    /**
     * 已取消=>已退款
     */
    Canceled(4),

    /**
     * 拒绝退款=>未退款
     */
    CancelReason(6);
    private int defaultValues = 1;

    public int getDefaultValues() {
        return defaultValues;
    }

    OrderMode(int i) {
        defaultValues = i;
    }
}
