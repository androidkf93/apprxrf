package shop.fly.com.shop.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xuke.zxing.zxing.android.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.interfaces.IConnState;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.MSTCUtil;
import shop.fly.com.shop.util.MyRequestPermission;

import static shop.fly.com.shop.constant.Constant.DECODED_BITMAP_KEY;
import static shop.fly.com.shop.constant.Constant.DECODED_CONTENT_KEY;
import static shop.fly.com.shop.constant.Constant.REQUEST_CODE_SCAN;

/**
 * Created by Administrator on 2017/8/2.
 */

public class AddGPRSPrintFragment extends GroupItemFragment implements MyRequestPermission.OnCheckedRequestPermissionListener,
        IConnState {
    @BindView(R.id.btn_scanning)
    Button btnScanning;

    private MyRequestPermission permission;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_gprs_print, container, false);
    }

    @Override
    protected void initDate(Context context) {
        permission = MyRequestPermission.getInstance(getActivity());
        permission.setOnCheckedRequestPermissionListener(this);
    }

    @Override
    protected void getDate(Context context) {

    }

    @Override
    protected void setDate() {

    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        iTitle.setLeftText(null, "GPRS打印机设置", v -> getActivity().finish());
        iTitle.setTitleText("");
    }

    @OnClick(R.id.btn_scanning)
    void onClick(){
        if(permission.isCheckPermission(new String[]{Manifest.permission.CAMERA})){
            startScan();
        }
    }

    private void startScan() {
        Intent intent;
        intent = new Intent(getContext(),CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void connState(int state) {

    }

    @Override
    public void isCheckedOK() {
        startScan();
    }

    @Override
    public void isCheckError() {

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SCAN:
                if (data != null) {
                    String content = data.getStringExtra(DECODED_CONTENT_KEY);
                    Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
//                    resultTv.setText("解码结果： \n" + content);
                    String[] split = content.split("\\?");
                    LogUtil.e("解码结果：" + content, getClass());
                    String contentMAC = (split.length > 1 ? split[1] : split[0]);
                    LogUtil.e("解码结果2：" + contentMAC, getClass());
//                    contentMAC = "feca368c80a74cf6";
//                    userBind(contentMAC);
                    MSTCUtil.userBind(getContext(), contentMAC, data1 -> {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    });
                }
                break;
        }
    }
}
