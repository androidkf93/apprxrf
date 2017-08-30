package shop.fly.com.shop.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/6/20.
 */

public class OperateBean {
    private String name;
    private Drawable drawable;

    public OperateBean(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
