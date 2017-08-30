package shop.fly.com.shop.bean;

/**
 * Created by Administrator on 2017/6/8.
 */

public class CategoryUpdate extends ParameBase {
    public int Id;
    public int Sort;
    public String Name;
    public String Remark;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
