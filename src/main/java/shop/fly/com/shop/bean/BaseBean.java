package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/8/21.
 */

public class BaseBean<T> {
    private boolean Result;
    private String Message;
    private T Data;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
