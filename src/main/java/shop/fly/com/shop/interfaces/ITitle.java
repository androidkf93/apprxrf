package shop.fly.com.shop.interfaces;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 使用公共Title的Activity
 * Created by Administrator on 2016/6/20.
 */

public interface ITitle {
    /**
     * 设置顶部title。
     * @param msgTitle
     * @return tcTitle
     */
    public TextView setTitleText(String msgTitle);

    /**
     * 获取顶部布局.
     * @return
     */
    public RelativeLayout getTitleLayout();

    /**
     * 左侧试图中添加ChildView
     * @param view item view
     */
    public void addLeftChildView(View view);
    /**
     * 右侧试图中添加ChildView
     * @param view item view
     */
    public void addRightChildView(View view);

    /**
     * 设置左侧imageView基本属性
     * @param params 空件宽高padding等属性 为null则使用默认值
     * @param drawable 设置imageView src drawable图片 为null则使用默认值
     * @param onClickListener 添加事件 为null则不添加点击事件
     * @return imageView方便使用者操作其他属性.
     */
    public ImageView setLeftImage(LinearLayout.LayoutParams params, Drawable drawable, View.OnClickListener onClickListener);
    /**
     * 设置左侧TextView基本属性
     * @param params 空件宽高padding等属性 为null则使用默认值
     * @param msgLeft 设置textVite text 文字 为null则使用默认值
     * @param onClickListener 添加事件
     * @return 左侧textView方便使用者操作其他属性.
     */
    public TextView setLeftText(LinearLayout.LayoutParams params, String msgLeft, View.OnClickListener onClickListener);

    /**
     * 设置左侧TextView基本属性
     * @param params 空件宽高padding等属性 为null则使用默认值
     * @param msgRight 设置textVite text 文字 为null则使用默认值
     * @param onClickListener 添加事件
     * @return 左侧textView方便使用者操作其他属性.
     */
    public TextView setRightText(LinearLayout.LayoutParams params, String msgRight, View.OnClickListener onClickListener);
    /**
     * 设置右侧imageView基本属性
     * @param params 空件宽高padding等属性 为null则使用默认值
     * @param drawable 设置imageView src drawable图片 为null则使用默认值
     * @param onClickListener 添加事件 为null则不添加点击事件
     * @return imageView方便使用者操作其他属性.
     */
    public ImageView setRightImage(LinearLayout.LayoutParams params, Drawable drawable, View.OnClickListener onClickListener);

    /**
     * 将顶部试图视图初始化
     */
    public void cleanTitle();
}
