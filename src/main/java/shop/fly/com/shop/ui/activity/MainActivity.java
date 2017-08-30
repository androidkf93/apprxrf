package shop.fly.com.shop.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.fly.com.shop.BluetoothOperation;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.FragmentTabAdapter;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.custom.DateTimePickDialogUtil;
import shop.fly.com.shop.interfaces.HomeSearchListener;
import shop.fly.com.shop.service.MediaService;
import shop.fly.com.shop.ui.activity.parent.*;

import shop.fly.com.shop.ui.fragment.OrderFragment;
import shop.fly.com.shop.ui.fragment.ShopFragment;
import shop.fly.com.shop.ui.fragment.ShopOperateFragment;
import shop.fly.com.shop.util.ApkUpdateUtils;
import shop.fly.com.shop.util.CodeUtil;
import shop.fly.com.shop.util.MSTCUtil;
import shop.fly.com.shop.util.MyRequestPermission;
import shop.fly.com.shop.util.PrintUtil;
import shop.fly.com.shop.util.SDFUtil;

public class MainActivity extends ParentFragmentActivity implements MyRequestPermission.OnCheckedRequestPermissionListener{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_title_right)
    ImageView imgTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_group)
    RelativeLayout rlGroup;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.ll_bottom_one)
    LinearLayout llBottomOne;
    @BindView(R.id.ll_bottom_two)
    LinearLayout llBottomTwo;
    @BindView(R.id.ll_bottom_four)
    LinearLayout llBottomFour;
    @BindView(R.id.ll_bottom_five)
    LinearLayout llBottomFive;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;

    private FragmentTabAdapter tabAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private Context context;
    private List<LinearLayout> bottomGroup = new ArrayList<>();

    private int selectPosition;
    private String date;
    private String lastKey = "";
    private String lastDate = "";
    private MyRequestPermission permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.e("bluetooth", "onCreate()");
        MediaService.stopService(this);
        context = this;
        permission = MyRequestPermission.getInstance(this);
        permission.setOnCheckedRequestPermissionListener(this);
        String[] str = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(permission.isCheckPermission(str)){
            ApkUpdateUtils.getInstance().setVisionData(this);
        }
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void init() {
        setTitle(getResources().getString(R.string.app_name));
        Intent intent = getIntent();
        int itemPosition = intent.getIntExtra("itemPosition", 0);
        String deskNO = intent.getStringExtra("deskNO");
        String orderType = intent.getStringExtra("orderType");
        if(!TextUtils.isEmpty(orderType) && orderType.equals("ORDER_RING")){
            forceShowCallSeatNum(deskNO);
            MediaService.stopService(context);
        }
        final  OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        bundle.putInt("itemPosition", itemPosition);
        orderFragment.setArguments(bundle);
        final OrderFragment orderFragmentPending = new OrderFragment();
        Bundle bundlePending = new Bundle();
        bundlePending.putInt("type", 1);
        orderFragmentPending.setArguments(bundlePending);

        fragments.add(orderFragment);
        fragments.add(orderFragmentPending);
        fragments.add(new ShopOperateFragment());
        fragments.add(new ShopFragment());
        tabAdapter = new FragmentTabAdapter(fragments, this, R.id.fl, 0);
        tabAdapter.getRadioGroup(0, rlTitle, true, null);
//        PrintUtil.getInstence(context).openConn();
        bottomGroup.add(llBottomOne);
        bottomGroup.add(llBottomTwo);
        bottomGroup.add(llBottomFour);
        bottomGroup.add(llBottomFive);
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                //完成自己的事件
                keyIsChange();
            }
            return false;
        });
        selectBottom(llBottomOne);
        setDate(SDFUtil.getHomeLeftTiem());
        PrintUtil.getInstence(context).openConn(true);
        if(TextUtils.isEmpty(Constant.getUuid()))
            MSTCUtil.userBind(context, Constant.getUuid(), null);
    }

    /**
     * 输入框是否发生变化
     * @return
     */
    public boolean keyIsChange(){
        if(!lastKey.equals(edtSearch.getText().toString()) || !lastDate.equals(date)){
            search();
            return true;
        }
        return false;
    }

    private void search() {
        lastKey = edtSearch.getText().toString();
        lastDate = date;
        ((OrderFragment) fragments.get(0)).search(edtSearch.getText().toString().trim(), date);
        ((OrderFragment) fragments.get(1)).search(edtSearch.getText().toString().trim(), date);
    }

    public String getLastKey(){
        lastKey = edtSearch.getText().toString();
        return lastKey;
    }


    public LinearLayout getLlTitleLeft() {
        return llTitleLeft;
    }

    public TextView getTvTitleLeft() {
        return tvTitleLeft;
    }

    @OnClick({R.id.ll_bottom_one, R.id.ll_bottom_two, R.id.ll_bottom_four, R.id.ll_bottom_five, /*R.id.img_search,*/ R.id.ll_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom_one:
                selectPosition = 0;
                selectBottom(llBottomOne);
                setDate(null);
                break;
            case R.id.ll_bottom_two:
                selectPosition = 1;
                setDate(null);
                selectBottom(llBottomTwo);
                break;
            case R.id.ll_bottom_four:
                selectPosition = 2;
                selectBottom(llBottomFour);
                setTitle("门店运营");
                break;
            case R.id.ll_bottom_five:
                selectPosition = 3;
                selectBottom(llBottomFive);
                setTitle("我的");
                break;
            case R.id.img_search:
                keyIsChange();
                return;
            case R.id.ll_title_left:
                if(TextUtils.isEmpty(date)){
                    date = SDFUtil.getTime();
                }
                new DateTimePickDialogUtil(this, date, time -> {
                    this.date = time;
                    keyIsChange();
                }).dateTimePicKDialog(tvTitleLeft);
                break;
        }
        if (tabAdapter != null)
            tabAdapter.getRadioGroup(selectPosition, rlTitle, true, null);
    }

    private void setDate(String date) {
        if(TextUtils.isEmpty(date)){
            date = tvTitleLeft.getText().toString();
            if (TextUtils.isEmpty(date))
                date = SDFUtil.getHomeLeftTiem();
        }
        llSearch.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitleLeft.setVisibility(View.VISIBLE);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleLeft.setText(date);
        Drawable drawableRight = getResources().getDrawable(R.drawable.icon_down_white);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getIntrinsicHeight());
        tvTitleLeft.setCompoundDrawables(null, null, drawableRight, null);
    }

    private void setTitle(@NonNull String title) {
        tvTitle.setText(title);
        tvTitle.setVisibility(View.VISIBLE);
        llTitleLeft.setVisibility(View.INVISIBLE);
        tvTitleLeft.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);
        llTitleRight.setVisibility(View.INVISIBLE);
    }

    private void selectBottom(LinearLayout selectLinearLayout){
        for (LinearLayout l:
                bottomGroup) {
            l.setEnabled(l != selectLinearLayout);
            for(int i = 0; i < l.getChildCount(); i ++){
                l.getChildAt(i).setEnabled(l != selectLinearLayout);
            }

        }
    }

    public Fragment getSelectPositionFragment() {
        return fragments.get(selectPosition);
    }




    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        Log.e("bluetooth", "onActivityResult()");
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case BluetoothOperation.CONNECT_DEVICE:
                    if(data != null){
                        PrintUtil.getInstence(context).openConn(false);
                        PrintUtil.getMyOpertion().open(data);
                    }

                    break;
                case BluetoothOperation.ENABLE_BT:
                    PrintUtil.getMyOpertion().chooseDevice(getClass().getSimpleName() + "BT");
            }
    }

    @Override
    public void isCheckedOK() {
        ApkUpdateUtils.getInstance().setVisionData(this);
    }

    @Override
    public void isCheckError() {

    }
}
