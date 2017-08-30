package shop.fly.com.shop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ProductBean implements Serializable {

    /**
     * Id : 1
     * CategoryId : 1
     * Name : product1
     * Price : 0
     * Image :
     * Remark : test
     * Unit : 11
     * Stock : 110
     * SoldCount : 0
     * IsUpShelf : true
     */

    private int Id;
    private int CategoryId;
    private String Name;
    private String Price;
    private String Image;
    private String Remark;
    private String Unit;
    private int Stock;
    private String PackageBoxNum;
    private String PackageBoxPrice;
    private int SoldCount;
    private boolean IsUpShelf;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getPackageBoxNum() {
        return PackageBoxNum;
    }

    public void setPackageBoxNum(String packageBoxNum) {
        PackageBoxNum = packageBoxNum;
    }

    public String getPackageBoxPrice() {
        return PackageBoxPrice;
    }

    public void setPackageBoxPrice(String packageBoxPrice) {
        PackageBoxPrice = packageBoxPrice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    public int getSoldCount() {
        return SoldCount;
    }

    public void setSoldCount(int SoldCount) {
        this.SoldCount = SoldCount;
    }

    public boolean isIsUpShelf() {
        return IsUpShelf;
    }

    public void setIsUpShelf(boolean IsUpShelf) {
        this.IsUpShelf = IsUpShelf;
    }
}
