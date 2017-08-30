package shop.fly.com.shop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/7.
 */

public class CategoryBean implements Serializable {

    /**
     * Id : 1
     * Sort : 1
     * Name : name1
     * Remark : ""
     * ProductCount : 0
     */

    private int Id;
    private int Sort;
    private String Name;
    private String Remark;
    private int ProductCount;
    private int ProductId;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int ProductCount) {
        this.ProductCount = ProductCount;
    }
}
