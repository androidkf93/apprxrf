package shop.fly.com.shop.bean;

import java.util.ArrayList;

import shop.fly.com.shop.util.CodeUtil;

/**
 * Created by Administrator on 2017/6/23.
 */

public class ProductUpdateList extends ProductUpdate {
    private ArrayList<SpecBean> Pro;


    public ProductUpdateList(int categoryId, String name, String image, String remark, String unit, int stock,
                             String price,String packageBoxNum, String packageBoxPrice, ArrayList<SpecBean> pro) {
        super(0, categoryId, name, image, remark, unit, stock, price, packageBoxNum, packageBoxPrice );
        Pro = pro;
    }

}
