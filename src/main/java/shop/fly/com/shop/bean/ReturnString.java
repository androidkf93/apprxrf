package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/8/23.
 */

public class ReturnString {
    private boolean Result;
    private String Message;
    private String Data;

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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
