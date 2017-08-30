package shop.fly.com.shop.interfaces;

/**
 * Created by Administrator on 2016/8/11.
 */
public interface UpLoadFileListener {
    /**
     * 上传进度
     * @param progress
     */
    public void upProgress(int progress);

    /**
     * 上传成功
     */
    public <T> void upDone(T t);

    /**
     * 上传失败
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
   public void error(int errorCode, String errorMsg);
}
