package shop.fly.com.shop.bean;

/**
 * Created by Administrator on 2016/8/12.
 */
public class UpLoadeBean {


    /**
     * State : 0
     * Message : OK
     * Upload : /upload/bbs/2016081203124855488.png
     * ImgUrl : http://api.zchengshi.com/upload/bbs/2016081203124855488.png
     */

    private int State;
    private String Message;
    private String Upload;
    private String ImgUrl;

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getUpload() {
        return Upload;
    }

    public void setUpload(String Upload) {
        this.Upload = Upload;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String ImgUrl) {
        this.ImgUrl = ImgUrl;
    }
}
