package shop.fly.com.shop.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;


import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import shop.fly.com.shop.R;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.Login;
import shop.fly.com.shop.bean.UserBean;
import shop.fly.com.shop.constant.Constant;
import shop.fly.com.shop.constant.Url;
import shop.fly.com.shop.custom.FinalToast;
import shop.fly.com.shop.interfaces.NetWorkCallBack;
import shop.fly.com.shop.retrofit.BaseObserver;
import shop.fly.com.shop.retrofit.RetrofitFactory;
import shop.fly.com.shop.ui.activity.parent.ParentActivity;
import shop.fly.com.shop.util.LogUtil;
import shop.fly.com.shop.util.OKHttpUtil;
import shop.fly.com.shop.util.SDFUtil;
import shop.fly.com.shop.util.TaskExecutor;

public class LoginActivity extends ParentActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_account)
    EditText edtName;
    @BindView(R.id.edt_password)
    EditText edtPsw;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        Login login = Constant.getLogin();
        if(login != null && !TextUtils.isEmpty(login.getName()) && !TextUtils.isEmpty(login.getPassword())){
            login(login);
        }
    }
    @OnClick(R.id.btn_login)
    public void onClick() {
        String name = edtName.getText().toString().trim();
        String psw = edtPsw.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            FinalToast.ErrorContext(context, "请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(psw)){
            FinalToast.ErrorContext(context, "请输入密码");
            return;
        }
        Login login = new Login();
        login.setName(name);
        login.setPassword(psw);
        login.setTime(SDFUtil.getTime());
        login.setSourceFrom(3);
        login(login);
    }

    private void login(final Login login) {
        showLoading();
        Constant.user = "B8BDEF99-ECEA-4A69-A23C-521641F66E39";
        TaskExecutor.executeTask(() -> {
            RetrofitFactory.getInstance().login(login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<UserBean>(context) {
                        @Override
                        protected void onHandleSuccess(UserBean userBean) {
                            Constant.logUser = userBean;
                            Constant.user = userBean.getVoucher();
                            Constant.setLogin(login);
                            JPushInterface.setAlias(context, Constant.logUser.getId() + "", (i, s, set) -> {
                                LogUtil.e("i= " + i + "\ns= " + s + "set= " + set + "\nalias= " + Constant.logUser.getId(), LoginActivity.class);
                            });
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    });

        });

    }
}
