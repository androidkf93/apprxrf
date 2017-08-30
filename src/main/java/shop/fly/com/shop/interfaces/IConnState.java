package shop.fly.com.shop.interfaces;

/**
 * Created by fly12 on 2017/7/23.
 */

public interface IConnState {
    /**
     * 连接状态
     * @param state 0 连接成功 1断开连接 2 打印机链接关闭
     */
    void connState(int state);
}
