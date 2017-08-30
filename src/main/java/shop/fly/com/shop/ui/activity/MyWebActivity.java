package shop.fly.com.shop.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shop.fly.com.shop.R;
import shop.fly.com.shop.ui.activity.parent.ParentActivity;
import shop.fly.com.shop.util.LogUtil;

/**
 * Created by xiaomingMac on 16/7/29.
 */
public class MyWebActivity extends ParentActivity {

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
    @BindView(R.id.web_view)
    WebView webView;
    public static String WEB_URL = "webUrl";
    String url = "";
    public static String WEB_TITLE = "webTitle";
    private String titleName;
    ValueCallback<Uri[]> mUploadMessage;
    public int FILECHOOSER_RESULTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initWebView();
        url = getIntent().getStringExtra(WEB_URL);
        titleName = getIntent().getStringExtra(WEB_TITLE);
        LogUtil.e(url, this.getClass());
        tvTitleRight.setText("X");
        float dimension = getResources().getDimension(R.dimen.text_size_36);
        tvTitleRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);
        llTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setVisibility(View.VISIBLE);
        try {

            setWebChromeClient();
    //        webView.setWebChromeClient(new MyWebChromeClient());
            webView.setDownloadListener(new MyWebViewDownLoadListener());
            if (!url.isEmpty()) {
                    webView.loadUrl(url);
            }
        }catch (Exception e){

        }


        if (!titleName.isEmpty()) {
            tvTitle.setText(titleName);
        } else
            tvTitle.setText(getResources().getString(R.string.site_navigation));


    }
    WebSettings wst;
    void initWebView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        imgTitleLeft.setVisibility(View.VISIBLE);
//        llTitleRight.setVisibility(View.VISIBLE);
//        imgTitleRight.setVisibility(View.VISIBLE);
//        imgTitleRight.setImageResource(R.drawable.icon_share);
        wst= webView.getSettings();
        wst.setJavaScriptEnabled(true);
        wst.setAllowFileAccess(true);
        wst.setBuiltInZoomControls(true);
        wst.setUseWideViewPort(true);
		/*网页自适应屏幕*/
        wst.setUseWideViewPort(true);
        wst.setLoadWithOverviewMode(true);
        wst.setDomStorageEnabled(true);
        webView.requestFocus();
        wst.setGeolocationEnabled(true);
        wst.setLoadWithOverviewMode(true);
        wst.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wst.setPluginState(WebSettings.PluginState.ON);
        wst.setNeedInitialFocus(true);
        wst.setJavaScriptCanOpenWindowsAutomatically(true);
        wst.setSavePassword(true);
        wst.setSaveFormData(true);
        wst.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        wst.setRenderPriority(WebSettings.RenderPriority.HIGH);
        wst.setDisplayZoomControls(false);
        wst.setJavaScriptCanOpenWindowsAutomatically(true);
        wst.setLoadsImagesAutomatically(true);  //支持自动加载图片
        wst.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (!isPressBack){
//                    try {
//                        if (newProgress < 100)
//                            showLoading();
//                         else
//                            hideLoading();
//                    }catch (Exception e){
//
//
//                    }
                }
            }


            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessage = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        FILECHOOSER_RESULTCODE);

                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                wst.setJavaScriptEnabled(true);
//                webView.loadUrl(url);
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else if(url.startsWith("http:") || url.startsWith("https:")) {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            // mUploadMessage = wcci.getmUploadMessage();
            if (null == mUploadMessage)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            Uri[] results = new Uri[]{result};
            mUploadMessage.onReceiveValue(results);
            mUploadMessage = null;
        }
    }

    @OnClick({R.id.img_title_left, R.id.ll_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                getBackAndFinish();
                break;
            case R.id.ll_title_right:
                webView.pauseTimers();
                webView.stopLoading();
                finish();
                break;
        }
    }
    boolean isPressBack =false;


    private void getBackAndFinish(){
        int his= webView.copyBackForwardList().getSize();
        int currentI=  webView.copyBackForwardList().getCurrentIndex();
        LogUtil.e("his" +his +"currentI"+currentI,getClass());
        if (currentI < 1){
            webView.pauseTimers();
            webView.stopLoading();
            finish();
        }else
            webView.goBack();
    }

    @Override
    protected void onPause() {
//        webView.reload ();
        webView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webView.resumeTimers();
        super.onResume();
    }


    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype,long contentLength)          {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
         /*   if(webView.canGoBack()){
                webView.goBack();
            }else{
                finish();
            }*/
            getBackAndFinish();
        }
        return true;
    }


}
