package shop.fly.com.shop.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import shop.fly.com.shop.bean.MSTCGetDevicestateBean;
import shop.fly.com.shop.bean.MstChingParameBaseBean;
import shop.fly.com.shop.bean.PrintContentBean;
import shop.fly.com.shop.bean.PrintTextBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.interfaces.MSTCOperationListener;
import shop.fly.com.shop.ui.activity.MainActivity;
import shop.fly.com.shop.ui.fragment.PrintManagerFragment;

/**
 * Created by Administrator on 2017/7/28.
 */

public class MSTCUtil {
    private static Context mContext;
    public static void userBind(Context context, @NonNull final String contentMAC, MSTCOperationListener mstc) {
      /*  if(contentMAC == null){
            contentMAC = "feca368c80a74cf6";
        }*/
        mContext = context;
        Gson gson = new Gson();
        MSTCGetDevicestateBean devicestateBean = new MSTCGetDevicestateBean(contentMAC, Constant.logUser.getId() + "");
        String json =  gson.toJson(devicestateBean);
        LogUtil.e(json, PrintManagerFragment.class);
        MstChingParameBaseBean mpb = new MstChingParameBaseBean();

        String url = Url.MstchingUserBind + "?appid=" + mpb.getAppid() +
                "&nonce=" + mpb.getNonce() +
                "&timestamp=" + mpb.getTimestamp() +
                "&signature=" + mpb.getSignature();
        LogUtil.e(url, PrintManagerFragment.class);
        OKHttpUtil.getInstance().postUrl(url, json, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e(response.code() + "", PrintManagerFragment.class);
                if(response.code() == 200){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int code = object.getInt("Code");
                        if(code == 200){
                            int openUserId = object.getInt("OpenUserId");
                            Constant.setOpenUserId(openUserId);
                            Constant.setUuid(contentMAC);
                            if(mstc != null){
                                TaskExecutor.runTaskOnUiThread(() -> mstc.onSuccess(openUserId + ""));
                            }
                        }else {
                            TaskExecutor.runTaskOnUiThread(() ->
                                    {
                                        if(!(context instanceof MainActivity))
                                            FinalToast.ErrorContext(context, "绑定失败");
                                    });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void getDevicestate(@NonNull String contentMAC, MSTCOperationListener mstc){
      /*  if(contentMAC == null){
            contentMAC = "feca368c80a74cf6";
        }*/
        if(TextUtils.isEmpty(contentMAC)){
            return;
        }
        Gson gson = new Gson();
        MSTCGetDevicestateBean devicestateBean = new MSTCGetDevicestateBean(contentMAC, Constant.logUser.getId() + "");
        String json =  gson.toJson(devicestateBean);
        LogUtil.i(json, PrintManagerFragment.class);
        MstChingParameBaseBean mpb = new MstChingParameBaseBean();
        String url = Url.MstchingGetDeviceState+ "?appid=" + mpb.getAppid() +
                "&nonce=" + mpb.getNonce() +
                "&timestamp=" + mpb.getTimestamp() +
                "&signature=" + mpb.getSignature();
        LogUtil.e(url, PrintManagerFragment.class);
        OKHttpUtil.getInstance().postUrl(url, json, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e(response.code() + "", PrintManagerFragment.class);
                if(response.code() == 200){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int code = object.getInt("Code");
                        if(code == 200){
                            String state = object.getString("State");
                            if(mstc != null)
                                TaskExecutor.runTaskOnUiThread(() -> {
                                    mstc.onSuccess(state);});
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void printContent(String uuid, String printContent, int openUserId, MSTCOperationListener mstc){
        Gson gson = new Gson();
        PrintTextBean pt = null;
        try {
            String data = printContent;
            pt = new PrintTextBean(Base64Util.stringToBase64(data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArrayList<PrintTextBean> printTextBeen = new ArrayList<>();
        printTextBeen.add(pt);
        PrintContentBean pc = new PrintContentBean(uuid, printTextBeen, openUserId);
        String json =  gson.toJson(pc);
        json = json.replace("[", "'[");
        json = json.replace("]", "]'");
        MstChingParameBaseBean mpb = new MstChingParameBaseBean();
        String url = Url.MstchingPrintContent2 + "?appid=" + mpb.getAppid() +
                "&nonce=" + mpb.getNonce() +
                "&timestamp=" + mpb.getTimestamp() +
                "&signature=" + mpb.getSignature();
        OKHttpUtil.getInstance().postUrl(url, json, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e(response.code() + "", PrintManagerFragment.class);
                if(response.code() == 200){
                    try {
                        String string = response.body().string();
                        LogUtil.e("data= " + string, PrintManagerFragment.class);
                        JSONObject object = new JSONObject(string);
                        int code = object.getInt("Code");
                        if(code == 200){
                            String Message = object.getString("Message");
                            if(mstc != null){
                                TaskExecutor.runTaskOnUiThread(() -> mstc.onSuccess(Message));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
