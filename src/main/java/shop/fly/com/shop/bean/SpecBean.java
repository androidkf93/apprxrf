package shop.fly.com.shop.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SpecBean extends ParameBase {
    private int ProductId;
    private int Id;
    private String Price;
    private int Stock;
    private String Name;

    public SpecBean() {
        setPrice("0");
    }

    public SpecBean(int productId, int id, String name) {
        ProductId = productId;
        this.Id = id;
        Name = name;
    }


    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getPrice() {
        return TextUtils.isEmpty(Price) ? "0" : Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public boolean equalsAll(String name, String price, int stock){
        return  equalsNull(name, price, stock) && name.equals(getName()) &&
                stock == getStock() &&
                price.equals(getPrice());
    }

    /**
     * 所有信息都为初始状态
     * @param name
     * @param price
     * @param stock
     * @return
     */
    public boolean equalsNull(String name, String price, int stock){
        return  TextUtils.isEmpty(name) &&
                TextUtils.isEmpty(price) || price.equals("0") &&
                stock == 0;
    }
}
