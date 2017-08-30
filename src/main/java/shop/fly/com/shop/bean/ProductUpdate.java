package shop.fly.com.shop.bean;

import java.util.ArrayList;

import shop.fly.com.shop.util.CodeUtil;

/**
 * Created by Administrator on 2017/6/23.
 */

public class ProductUpdate extends ParameBase {
    private int ProductId;
    private int CategoryId;
    private String Name;
    private String Image;
    private String Remark;
    private String Unit;
    private int Stock ;
    private String Price;
    private String PackageBoxNum;
    private String PackageBoxPrice;

    public ProductUpdate(int ProductId, int categoryId, String name, String image, String remark,
                         String unit, int stock, String price, String packageBoxNum, String packageBoxPrice) {
        this.ProductId = ProductId;
        CategoryId = categoryId;
        Name = name;
        Image = image;
        Remark = remark;
        Unit = unit;
        Stock = stock;
        Price = price;
        PackageBoxNum = packageBoxNum;
        PackageBoxPrice = packageBoxPrice;
    }

    public ProductUpdate(int productId) {
        ProductId = productId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }
    public void setToken(){
        super.setToken(CodeUtil.MD5(CategoryId + Image + Name + Remark + Stock + Unit));
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }
}
