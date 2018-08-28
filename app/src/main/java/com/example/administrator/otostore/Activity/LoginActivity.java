package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.LoginShopBean;
import com.example.administrator.otostore.Bean.UserLoginBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.google.gson.Gson;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_pass)
    EditText loginPass;
    @BindView(R.id.loginpassshoworhide)
    ImageView loginpassshoworhide;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_forget)
    TextView loginForget;
    private boolean isHideFirst = false;
    private Context context;
    private String pass;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
//        userloginverify();
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean isshowtitlebar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @OnClick({R.id.login_btn, R.id.login_register, R.id.login_forget, R.id.loginpassshoworhide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (loginUsername.getText().toString().trim().isEmpty()) {
                    showToast("请输入登录名");

                } else if (loginPass.getText().toString().trim().isEmpty()) {
                    showToast("请输入密码");
                    showAlertDialog("你去确认了吗", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    UserLoginCheck();
                }
                break;
            case R.id.login_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.login_forget:
                startActivity(FindYourPasActivity.class);
                break;
            case R.id.loginpassshoworhide:
                if (isHideFirst == true) {
                    loginPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isHideFirst = false;

                } else {
                    loginPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isHideFirst = true;
                }
                break;
        }
    }

    private void UserLoginCheck() {
//        pass = MD5Utils.encodeMD5(loginPass.getText().toString().trim());
//        LogUtils.d("AAAAAAAA" + pass+"长度是:"+pass.length());
//        pass = MD5Utils.encodeMD52(loginPass.getText().toString().trim());
//        SPUtils.setUserId(context,1);
//        LogUtils.d("AAAAAAAAAAAAAAA:"+SPUtils.getUserId(context));
//        LogUtils.d("AAAAAAAA" + pass);
        String pasmd5 = MD5Utils.encodeMD5(loginPass.getText().toString().trim());
        LogUtils.d("AAAAAAAAAA:密码" + pasmd5.length());
        RetrofitHttpUtil.getApiService()
                .UserLogin("", 2, loginUsername.getText().toString().trim(), pasmd5)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("AAAAAAAAAAA" + s);
                final UserLoginBean userLoginBean = GsonUtil.parseJsonWithGson(s, UserLoginBean.class);
                LogUtils.d("AAAAAA" + userLoginBean.getResult());
                if (userLoginBean.getResult().equals("0")) {
                    showAlertDialog("用户不存在,请去客户端注册", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            startActivity(RegisterActivity.class);
//                            finish();
                        }
                    });
                } else if (userLoginBean.getResult().equals("1")) {
                    SPUtils.setUserId(context, Integer.valueOf(userLoginBean.getUserID()));
                    showAlertDialog("开店成功", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getShopInfo();
                        }
                    });
//                    SPUtils.setUserId(context, Integer.valueOf(userLoginBean.getUserID()));
                } else if (userLoginBean.getResult().equals("2")) {
                    showAlertDialog("密码错误", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userLoginBean.getResult().equals("3")) {
                    showAlertDialog("买家用户未开通店铺，请去开店", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("userid", Integer.valueOf(userLoginBean.getUserID()));
//                            startActivity(StoreNameActivity.class, bundle);
//                            finish();
                        }
                    });
                }
            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });
    }

    private void getShopInfo() {
        RetrofitHttpUtil
                .getApiService()
                .GetShopInfo("", SPUtils.getUserId(context)).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                LoginShopBean shopBean = GsonUtil.parseJsonWithGson(s, LoginShopBean.class);
                SPUtils.setShopId(context, Integer.valueOf(shopBean.getShopID()));
                startActivity(BottomBarActivity.class);
                finish();
            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });
    }
}
