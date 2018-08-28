package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.FriendsAddBean;
import com.example.administrator.otostore.Bean.VerificationCodeBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.example.administrator.otostore.Utils.TimeCount;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZhifuManagerActivity extends BaseActivity {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.yanzhengma)
    EditText yanzhengma;
    @BindView(R.id.daojiashi)
    TextView daojiashi;
    @BindView(R.id.newpass)
    EditText newpass;
    @BindView(R.id.newrepass)
    EditText newrepass;
    @BindView(R.id.clickbtn)
    Button clickbtn;
    @BindView(R.id.oldpass)
    EditText oldpass;
    @BindView(R.id.showld)
    LinearLayout showld;
    private TimeCount timeCount;
    private int getverification = 0;
    private Context context;
    private String yuanshipass = "";
    private int showoldandnew = 1;

    @Override
    public int getContentViewResId() {

        context = this;
        return R.layout.activity_zhifu_manager;
    }

    @Override
    public void initView() {
        setcenterTitle("支付密码");
        GetUserBaseInfo();
        timeCount = new TimeCount(120000, 1000);

        timeCount.setCountListener(new TimeCount.CountListener() {
            @Override
            public void onTick(Long time) {
                daojiashi.setText(String.valueOf(time / 1000).concat(" s") + "");
                daojiashi.setEnabled(false);//设置不可点击
            }

            @Override
            public void onFinish() {
                daojiashi.setText("重新发送");
                daojiashi.setEnabled(true);
            }
        });
        showright(true);
        setrightTitle("重置");
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        startActivity(ResetPassActivity.class);
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

    @OnClick({R.id.daojiashi, R.id.clickbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.daojiashi:
                if (ismepty(phone)) {
                    showToast("手机号密码不能为空");
                } else if (!PhoneUtils.isMobileNO(edittext(phone))) {
                    showToast("手机号不正确");
                } else {
                    timeCount.start();
                    getverification++;
                    GenSMSVerifyCode(edittext(phone), 9);
                }
                break;
            case R.id.clickbtn:
                if (ismepty(phone)) {
                    showToast("手机号密码不能为空");
                } else if (!PhoneUtils.isMobileNO(edittext(phone))) {
                    showToast("手机号不正确");
                } else if (ismepty(yanzhengma)) {
                    showToast("验证码不能为空");
                } else if (ismepty(newrepass)) {
                    showToast("密码不能为空");
                } else if (ismepty(newrepass)) {
                    showToast("重置密码不能为空");
                } else if (!edittext(newpass).equals(edittext(newrepass))) {
                    showToast("两次密码不一致");
                } else if (getverification == 0) {
                    showToast("请输入验证码");
                } else if (showoldandnew == 2) {
                    if (ismepty(oldpass)) {
                        showToast("请输入原始密码");
                    } else {
                        VerifyVerCode(1, 9, edittext(yanzhengma));
                    }
                } else {
                    VerifyVerCode(1, 9, edittext(yanzhengma));
                }
                break;
        }
    }

    private void GetUserBaseInfo() {
        RetrofitHttpUtil.getApiService().GetUserBaseInfo
                ("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        FriendsAddBean bean = GsonUtil.parseJsonWithGson(s, FriendsAddBean.class);
                        if (bean.getPayPwd().equals("")) {
                            showld.setVisibility(View.GONE);
                            yuanshipass = "";
                            showoldandnew = 1;
                        } else {
                            showoldandnew = 2;
                        }
                    }
                });
    }

    private void SetUserPayPwd(String OldPayPwd, String NewPayPwd) {

        RetrofitHttpUtil.getApiService()
                .SetUserPayPwd("", Long.valueOf(SPUtils.getUserId(context)), OldPayPwd, NewPayPwd)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        VerificationCodeBean bean = GsonUtil.parseJsonWithGson(s, VerificationCodeBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("设置密码成功");
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前用户不存在");
                        } else if (bean.getResult().equals("2")) {
                            showToast("为了您的资金安全考虑, 请勿将支付密码设置为与登录密码一致");
                        } else if (bean.getResult().equals("3")) {
                            showToast("初次设置支付密码失败");
                        } else if (bean.getResult().equals("4")) {
                            showToast("旧支付密码错误, 修改支付密码失败");
                        } else if (bean.getResult().equals("5")) {
                            showToast("修改支付密码失败");
                        }
                        LogUtils.d(s + "设置密码");
                    }
                });
    }

    private void GenSMSVerifyCode(String MobileNum, int VerifyType) {
        RetrofitHttpUtil.getApiService()
                .GenSMSVerifyCode("", SPUtils.getUserId(context), MobileNum, VerifyType)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("获取验证码" + s);
                        VerificationCodeBean bean = GsonUtil.parseJsonWithGson(s, VerificationCodeBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("生成短信并发送成功");
                        } else if (bean.getResult().equals("2")) {
                            showToast("生成验证码失败");
                        } else if (bean.getResult().equals("3")) {
                            showToast("获取短信模板失败");
                        } else if (bean.getResult().equals("4")) {
                            showToast("生成短信失败");
                        }
                    }
                });
    }

    private void VerifyVerCode(int VerifyMode, int VerifyType, String VerifyCode) {
        RetrofitHttpUtil.getApiService()
                .VerifyVerCode("", SPUtils.getUserId(context), VerifyMode, VerifyType, VerifyCode).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("验证返回值" + s);
                        VerificationCodeBean bean = GsonUtil.parseJsonWithGson(s, VerificationCodeBean.class);
                        if (bean.getResult().equals("1")) {
                            if (showoldandnew == 1) {
                                SetUserPayPwd("", MD5Utils.encodeMD5(edittext(newpass)));
                            } else {
                                SetUserPayPwd(MD5Utils.encodeMD52(edittext(oldpass)), MD5Utils.encodeMD5(edittext(newpass)));
                            }
                        } else if (bean.getResult().equals("0")) {
                            showToast("非法验证码");
                        } else if (bean.getResult().equals("2")) {
                            showToast("验证码过期");
                        }
                    }
                });
    }


    private String edittext(TextView e) {
        return e.getText().toString().trim();
    }

    private boolean ismepty(TextView a) {
        return a.getText().toString().trim().isEmpty();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }
}
