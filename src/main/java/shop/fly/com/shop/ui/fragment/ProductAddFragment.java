package shop.fly.com.shop.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.R;
import shop.fly.com.shop.adapter.SpecItem;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.bean.ProductUpdate;
import shop.fly.com.shop.bean.ProductUpdateList;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.bean.UploadImage;
import shop.fly.com.shop.constant.RequestCode;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.interfaces.ITitle;
import shop.fly.com.shop.menus.SpecMode;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.parent.GroupFragmentActivity;
import shop.fly.com.shop.util.CaptureImageUtil;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.MyRequestPermission;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.PhotoUtil;
import shop.fly.com.shop.util.TaskExecutor;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ProductAddFragment extends GroupItemFragment implements MyRequestPermission.OnCheckedRequestPermissionListener {

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.img_shop)
    ImageView imgShop;
    @BindView(R.id.edt_price)
    EditText edtPrice;
    @BindView(R.id.edt_package_box_num)
    EditText edtPackageBoxNum;
    @BindView(R.id.edt_package_box_price)
    EditText edtPackageBoxPrice;
    @BindView(R.id.edt_stock_num)
    EditText edtStockNum;
    @BindView(R.id.ll_spec_add)
    LinearLayout llSpecAdd;
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.tv_bottom_left)
    TextView tvBottomLeft;
    @BindView(R.id.tv_bottom_right)
    TextView tvBottomRight;
    @BindView(R.id.ll_spec)
    LinearLayout llSpec;
    @BindView(R.id.ll_spec_group)
    LinearLayout llSpecGroup;
    @BindView(R.id.tv_offer)
    TextView tvOffer;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.edt_remark)
    EditText edtRemark;
    @BindView(R.id.ll_stoke)
    LinearLayout llStoke;

    private CategoryBean category;
    private ArrayList<SpecBean> spec;
    private SpecItem specItem = new SpecItem();
    private boolean isUpdate = false;
    private MyRequestPermission permission;
    private Uri imgUri;
    protected int scalSize = 10 * 1024;
    private String upImagUrl;

    private int productId;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_product_add, container, false);
    }

    @Override
    protected void initDate(Context context) {
        tvBottomLeft.setText("删除");
        tvBottomRight.setText("上传");
        Observable.create(new ObservableOnSubscribe<Bundle>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bundle> e) throws Exception {
                e.onNext(getArguments());
            }
        })
                .filter(bundle -> bundle != null)
                .filter(bundle -> {
                    category = (CategoryBean) bundle.getSerializable("category");
                    if(category == null){
                        category = new CategoryBean();
                    }
                    tvCategory.setText(category.getName());
                    isUpdate = bundle.getBoolean("type", false);
                    return isUpdate;
                })
                .subscribe(bundle -> {
                    ProductBean bean = (ProductBean) bundle.getSerializable("data");
                    productId = bean.getId();
                    edtName.setText(bean.getName());
                    edtPrice.setText(bean.getPrice());
                    edtRemark.setText(bean.getRemark());
                    edtPackageBoxNum.setText(bean.getPackageBoxNum());
                    edtPackageBoxPrice.setText(bean.getPackageBoxPrice());
                    upImagUrl = bean.getImage();
                    Glide.with(getContext())
                            .load(upImagUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(imgShop);
                    showLoading();
                    TaskExecutor.executeNetTask(() ->
                        RetrofitFactory.getInstance().getProductProQuery(productId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseObserver<ArrayList<SpecBean>>(context) {
                                    @Override
                                    protected void onHandleSuccess(ArrayList<SpecBean> specBeen) {
                                        hideLoading();
                                        spec = specBeen;
                                        if (spec.size() > 0){
                                            llSpec.setVisibility(View.VISIBLE);
                                            llSpecAdd.setVisibility(View.GONE);
                                        }
                                        specItem.setItem(getContext(), llSpecGroup, spec, false, false);
                                    }
                                }));

                    if(bean.getStock() == -1){
                        tvClose.setVisibility(View.INVISIBLE);
                        tvOffer.setVisibility(View.VISIBLE);
                        llStoke.setVisibility(View.GONE);
                    }else{
                        edtStockNum.setText(bean.getStock() + "");
                        tvClose.setVisibility(View.VISIBLE);
                        tvOffer.setVisibility(View.INVISIBLE);
                        llStoke.setVisibility(View.VISIBLE);
                    }
                });
        permission = MyRequestPermission.getInstance(getActivity());
        getCallpermission(permission);
    }
    private void getCallpermission(MyRequestPermission permission) {
        permission.setOnCheckedRequestPermissionListener(this);
        String[] str = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        permission.isCheckPermission(str);
    }
    @Override
    protected void getDate(final Context context) {
        LogUtil.e("getDate()", context.getClass());
    }

    @Override
    protected void setDate() {
    }

    @Override
    protected void setiTitle(ITitle iTitle) {
        iTitle.setTitleText("");
        iTitle.setLeftText(null, "商品列表", v -> getActivity().finish());
        iTitle.setLeftImage(null, getResources().getDrawable(R.drawable.return_white), v -> getActivity().finish());
        if(isUpdate){
            iTitle.setRightText(null, "保存", v -> {
                AddOrUpProduct(true, "保存并返回");
            });
            tvBottomRight.setText("下架");
            tvBottomRight.setTextColor(getResources().getColor(R.color.black));
        }else{
            tvBottomLeft.setText("保存并返回");
            tvBottomRight.setText("保存并新建");
        }

    }

    int offOrClose = 0;

    @OnClick({R.id.tv_category, R.id.img_shop, R.id.ll_spec_add, R.id.tv_bottom_left, R.id.tv_bottom_right, R.id.ll_spec_group,
            R.id.ll_switch, R.id.ll_edit_category})
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), GroupFragmentActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_category:
                intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, CategoryFragment.class.getName());
                bundle.putSerializable("type", SpecMode.SELECT_CATEGORY);
                intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                getActivity().startActivityForResult(intent, RequestCode.CATEGORY);
                break;
            case R.id.img_shop:
                CaptureImageUtil.showImagePickDialog(getActivity());
                break;
            case R.id.ll_spec_add:
            case R.id.ll_edit_category:
                if (isUpdate){
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, SpecFragment.class.getName());
                    bundle.putInt("productId", productId);
                    bundle.putBoolean("isUpdate", isUpdate);
                    bundle.putBoolean("showPrice", true);
                    intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                    getActivity().startActivityForResult(intent, RequestCode.SPEC);
                }else {
                    intent.putExtra(GroupFragmentActivity.FRAGMENT_NAME, SpecFragment.class.getName());
                    bundle.putSerializable("spec", spec);
                    bundle.putBoolean("showPrice", true);
                    intent.putExtra(GroupFragmentActivity.BUNDLE_NAME, bundle);
                    getActivity().startActivityForResult(intent, RequestCode.SPEC);
                }
                break;
            case R.id.ll_switch:
                if(offOrClose ++ % 2 == 1){
                    tvClose.setVisibility(View.VISIBLE);
                    tvOffer.setVisibility(View.INVISIBLE);
                    llStoke.setVisibility(View.VISIBLE);
                }
                else{
                    tvClose.setVisibility(View.INVISIBLE);
                    tvOffer.setVisibility(View.VISIBLE);
                    llStoke.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_bottom_left:
                if(tvBottomLeft.getText().toString().equals("删除")){
                    operation(2, productId, tvBottomLeft.getText().toString());
                }else{
                    AddOrUpProduct(isUpdate, tvBottomLeft.getText().toString());
                }
                break;
            case R.id.tv_bottom_right:
                if(isUpdate){
                    if(tvBottomRight.getText().toString().equals("上架")){
                        operation(0, productId, tvBottomRight.getText().toString());
                    }else{
                        operation(1, productId, tvBottomRight.getText().toString());
                    }
                }else{
                    AddOrUpProduct(isUpdate, tvBottomRight.getText().toString());
                }
                break;
        }
    }

    private void operation(int type, int productId, String operationName){
        showLoading();
        TaskExecutor.executeNetTask(() -> {
            Observable<BaseBean<String>> baseBeanObservable = null;
            switch (type){
                case 0:
                    baseBeanObservable = RetrofitFactory.getInstance().productDownShelf(productId);
                    break;
                case 1:
                    baseBeanObservable = RetrofitFactory.getInstance().productUpShelf(productId);
                    break;
                case 2:
                    baseBeanObservable = RetrofitFactory.getInstance().productDelete(productId);
                    break;
            }
            baseBeanObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        protected void onHandleSuccess(String s) {
                            hideLoading();
                            if(operationName.equals("上架")){
                                tvBottomRight.setText("下架");
                            }else if(operationName.equals("下架")){
                                tvBottomRight.setText("上架");
                            }else{
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                        }
                    });
        });

    }

    private void AddOrUpProduct(boolean isUpdate, String operationName) {
        int stock = -1;
        if(llStoke.getVisibility() == View.VISIBLE){
            if (TextUtils.isEmpty(edtName.getText())){
                FinalToast.ErrorContext(getContext(), "请输入商品名称");
                return;
            }
            if(TextUtils.isEmpty(edtStockNum.getText())){
                FinalToast.ErrorContext(getContext(), "请输入库存");
                return;
            }
            stock = Integer.parseInt(edtStockNum.getText().toString());
        }
        if(TextUtils.isEmpty(edtPrice.getText())){
            FinalToast.ErrorContext(getContext(), "请填写价格");
            return;
        }
        ProductUpdate update;
        if(!isUpdate){//新建商品
            update = new ProductUpdateList(category.getId(),
                    edtName.getText().toString(),
                    upImagUrl,
                    edtRemark.getText().toString(),
                    "",
                    stock,
                    edtPrice.getText().toString(),
                    edtPackageBoxNum.getText().toString(),
                    edtPackageBoxPrice.getText().toString(),
                    spec);
        }else{
            update = new ProductUpdate(
                    productId,
                    category.getId(),
                    edtName.getText().toString(),
                    upImagUrl,
                    edtRemark.getText().toString(),
                    "",
                    stock,
                    edtPrice.getText().toString(),
                    edtPackageBoxNum.getText().toString(),
                    edtPackageBoxPrice.getText().toString());
        }
        update.setToken();
        showLoading();
        TaskExecutor.executeNetTask(() ->
            RetrofitFactory.getInstance().productAdd(update)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<String>(getContext()) {
                        @Override
                        protected void onHandleSuccess(String s) {
                            hideLoading();
                            FinalToast.ErrorContext(getContext(), isUpdate ? "商品更新成功" : "商品添加成功");
                            if(operationName.equals("保存并新建")){
                                llSpecGroup.removeAllViews();
                                edtName.setText("");
                                edtStockNum.setText("");
                                edtPrice.setText("");
                                tvClose.setVisibility(View.VISIBLE);
                                tvOffer.setVisibility(View.INVISIBLE);
                                llSpecAdd.setVisibility(View.VISIBLE);
                                imgShop.setImageResource(R.drawable.ic_launcher);
                                edtRemark.setText("");
                                tvCategory.setText("");
                                if(spec != null)
                                    spec.clear();
                                imgUri = null;
                            }else{
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                        }
                    }));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {
            switch (requestCode) {
                case RequestCode.CATEGORY:
                    if(data != null){
                        category = (CategoryBean) data.getSerializableExtra("data");
                        tvCategory.setText(category.getName());
                    }
                    break;
                case RequestCode.SPEC:
                    if(data != null){
                        llSpec.setVisibility(View.VISIBLE);
                        spec = (ArrayList<SpecBean>) data.getSerializableExtra("spec");
                        specItem.setItem(getContext(), llSpecGroup, spec, false, false);
                        if(spec.size() == 0){
                            llSpecAdd.setVisibility(View.VISIBLE);
                            llSpec.setVisibility(View.GONE);
                        }else{
                            llSpecAdd.setVisibility(View.GONE);
                            llSpec.setVisibility(View.VISIBLE);
                        }
                        llSpecAdd.setVisibility(View.GONE);
                    }
                    break;

                // 拍照获取图片
                case CaptureImageUtil.GET_IMAGE_BY_CAMERA:
                    // uri传入与否影响图片获取方式,以下二选一
                    // 方式一,自定义Uri(ImageUtil.imageUriFromCamera),用于保存拍照后图片地址
                    if (CaptureImageUtil.imageUriFromCamera != null) {
                        // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//					iv.setImageURI(ImageUtil.imageUriFromCamera);
                        // 对图片进行裁剪
                        CaptureImageUtil.cropImage(getActivity(), CaptureImageUtil.imageUriFromCamera);
                        break;
                    }
                    break;

                // 手机相册获取图片
                case CaptureImageUtil.GET_IMAGE_FROM_PHONE:
                    if (data != null && data.getData() != null) {
                        // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
                        // iv.setImageURI(data.getData());
                        // 对图片进行裁剪
                        CaptureImageUtil.cropImage(getActivity(), data.getData());
                    }
                    break;

                // 裁剪图片后结果
                case CaptureImageUtil.CROP_IMAGE:
                    if (CaptureImageUtil.cropImageUri != null) {
                        // 可以直接显示图片,或者进行其他处理(如压缩等)
//					img_icon.setImageURI(CaptureImageUtil.cropImageUri);

                        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                        ContentResolver resolver = getActivity().getContentResolver();
                        //此处的用于判断接收的Activity是不是你想要的那个
                        try {
                            imgUri = /*data.getData()*/CaptureImageUtil.cropImageUri; //获得图片的uri
                            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, imgUri);
                            //这里开始的第二部分，获取图片的路径：
                            String[] proj = {MediaStore.Images.Media.DATA};
                            //好像是android多媒体数据库的封装接口，具体的看Android文档
                            Cursor cursor = getActivity().managedQuery(imgUri, proj, null, null, null);
                            //按我个人理解 这个是获得用户选择的图片的索引值
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            //将光标移至开头 ，这个很重要，不小心很容易引起越界
                            cursor.moveToFirst();
                            //最后根据索引值获取图片路径
                            String path = cursor.getString(column_index);
                            File file = PhotoUtil.scal(path, scalSize);
                            path = file.getPath();
                            imgShop.setImageURI(imgUri);
                            UploadImage upBean = new UploadImage();
                            upBean.setData(CaptureImageUtil.fileToBase64(file));
                            upBean.setFileName(file.getName());
                            upBean.setType(2);
                            upBean.setTokent();
                            String json = new Gson().toJson(upBean);
                            showLoading();
                            OKHttpUtil.getInstance().postUrl(Url.UpdateImage, json, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            if(response.isSuccessful()){
                                                try {
                                                    String strJson = response.body().string();
                                                    LogUtil.e("onResponse: " + strJson, this.getClass());
                                                    JSONObject jsonObject = new JSONObject(strJson);
                                                    boolean result = jsonObject.getBoolean("Result");
                                                    String message = jsonObject.getString("Message");
                                                    if(result){
                                                        upImagUrl = jsonObject.getString("Data");
                                                        Glide.with(getContext()).load(upImagUrl).into(imgShop);
                                                        hideLoading();
                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                    imgShop.setImageResource(R.drawable.ic_launcher);
                                                    FinalToast.ErrorContext(getContext(), "上传失败");
                                                }
                                            }else{
                                                imgShop.setImageResource(R.drawable.ic_launcher);
                                                FinalToast.ErrorContext(getContext(), "上传失败");
                                            }

                                        }
                                    });

                                }
                            });
                        } catch (IOException e) {
                            Log.e("TAG-->Error", e.toString());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void isCheckedOK() {

    }

    @Override
    public void isCheckError() {

    }
}
