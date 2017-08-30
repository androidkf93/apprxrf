package shop.fly.com.shop.ui.activity.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.manager.ActivityManager;
import shop.fly.com.shop.util.LogUtil;

/**
 * 跳转Fragment,传递Intent
 * key = fg_name, value = 要加载的Fragment名字
 * key = bundle, value = Bundle;
 */
public class GroupFragmentActivity extends ParentFragmentActivity implements ITitle {

    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.img_title_left)
    ImageView imgTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title_center)
    LinearLayout llTitleCenter;
    @BindView(R.id.img_title_right)
    ImageView imgTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private Fragment fragment;
    public final static String FRAGMENT_NAME = "fg_name";
    public final static String BUNDLE_NAME = "budle";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_fragment);
        ButterKnife.bind(this);
        ActivityManager.add(this);
        context = this;
        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            String strFgName = intent.getStringExtra("fg_name");
            try {
                Class cla = Class.forName(strFgName);
                fragment = (Fragment) cla.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            bundle = intent.getBundleExtra("budle");
        }

        if (fragment == null)
            return;
        FragmentManager sfm = getSupportFragmentManager();
        FragmentTransaction ft = sfm.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        ft.replace(R.id.fl, fragment);
        ft.commit();
    }



    @Override
    public TextView setTitleText(String msgTitle) {
        if(msgTitle != null)
            tvTitle.setText(msgTitle);
        return tvTitle;
    }

    @Override
    public RelativeLayout getTitleLayout() {
        return rlTitle;
    }

    @Override
    public void addLeftChildView(View view) {
        llTitleLeft.addView(view);
    }

    @Override
    public void addRightChildView(View view) {
        llTitleRight.addView(view);
    }

    @Override
    public ImageView setLeftImage(LinearLayout.LayoutParams params, Drawable drawable, View.OnClickListener onClickListener) {
        llTitleLeft.setVisibility(View.VISIBLE);
        imgTitleLeft.setVisibility(View.VISIBLE);
        if (drawable != null)
            imgTitleLeft.setImageDrawable(drawable);
        if (params != null)
            imgTitleLeft.setLayoutParams(params);
        if (onClickListener != null)
            imgTitleLeft.setOnClickListener(onClickListener);
        return imgTitleLeft;
    }

    @Override
    public TextView setLeftText(LinearLayout.LayoutParams params, String msgLeft, View.OnClickListener onClickListener) {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitleLeft.setVisibility(View.VISIBLE);
        if (params != null)
            tvTitleLeft.setLayoutParams(params);
        if (msgLeft != null)
            tvTitleLeft.setText(msgLeft);
        if (onClickListener != null)
            tvTitleLeft.setOnClickListener(onClickListener);
        return tvTitleLeft;
    }

    @Override
    public TextView setRightText(LinearLayout.LayoutParams params, String msgRight, View.OnClickListener onClickListener) {
        llTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setVisibility(View.VISIBLE);
        if (params != null)
            tvTitleRight.setLayoutParams(params);
        if (!msgRight.isEmpty())
            tvTitleRight.setText(msgRight);
        if (onClickListener != null)
            tvTitleRight.setOnClickListener(onClickListener);
        return tvTitleRight;
    }

    @Override
    public ImageView setRightImage(LinearLayout.LayoutParams params, Drawable drawable, View.OnClickListener onClickListener) {
        llTitleRight.setVisibility(View.VISIBLE);
        imgTitleRight.setVisibility(View.VISIBLE);
        if (params != null)
            imgTitleRight.setLayoutParams(params);
        if (drawable != null)
            imgTitleRight.setImageDrawable(drawable);
        if (onClickListener != null)
            imgTitleRight.setOnClickListener(onClickListener);
        return imgTitleRight;
    }

    @Override
    public void cleanTitle() {
        llTitleLeft.setVisibility(View.INVISIBLE);
        imgTitleLeft.setVisibility(View.GONE);
        tvTitleLeft.setVisibility(View.GONE);
        llTitleRight.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.img_title_left, R.id.tv_title_left, R.id.ll_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
            case R.id.tv_title_left:
            case R.id.ll_title_left:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.finish(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("onActivityResult", this.getClass());
        if(resultCode == Activity.RESULT_OK)
            fragment.onActivityResult(requestCode, resultCode, data);
    }
}
