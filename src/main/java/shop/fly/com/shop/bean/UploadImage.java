package shop.fly.com.shop.bean;

/**
 * Created by Administrator on 2017/6/23.
 */

public class UploadImage extends ParameBase {
    private String Data;
    private String FileName;
    /**
     * 上传类型1 logo, 2 商品图片
     */
    private int type;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setTokent(){
        super.setToken(FileName + type);
    }
}
