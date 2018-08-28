package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.EditShopResultBean;
import com.example.administrator.otostore.Bean.UserInfoBean;
import com.example.administrator.otostore.Bean.UserLoginBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangYourPassActivity extends BaseActivity {
    @BindView(R.id.passcurrent)
    EditText passcurrent;
    @BindView(R.id.passnew)
    EditText passnew;
    @BindView(R.id.passnewagain)
    EditText passnewagain;
    @BindView(R.id.click)
    Button click;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_chang_your_pass;
    }

    @Override
    public void initView() {
        setcenterTitle("修改密码");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.click)
    public void onViewClicked() {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (isempty(passcurrent)) {
            showToast("当前密码不能为空");
        } else if (isempty(passnew)) {
            showToast("新密码不能为空");
        } else if (isempty(passnewagain)) {
            showToast("确认密码不能为空");
        } else if (!getext(passcurrent).matches(regex)) {
            showToast("当前密码格式不正确");
        } else if (!getext(passnew).matches(regex)) {
            showToast("新密码格式不正确");
        } else if (!getext(passnewagain).matches(regex)) {
            showToast("确认密码格式不正确");
        } else if (!getext(passnew).equals(getext(passnewagain))) {
            showToast("新密码和确认密码不正确");
        } else {
            edityourpassword(getext(passcurrent), getext(passnewagain));
        }
    }

    private boolean isempty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private String getext(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void edityourpassword(String passold, String passnew) {
        RetrofitHttpUtil.getApiService()
                .ChangeUserPwd("", SPUtils.getUserId(context), MD5Utils.encodeMD5(passold), MD5Utils.encodeMD5(passnew))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        UserLoginBean bean = GsonUtil.parseJsonWithGson(s, UserLoginBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("修改成功");
                            finish();
                        } else if (bean.getResult().equals("3")) {
                            showToast("旧密码错误");
                        }
                    }
                });
    }
}
