package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
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

public class ResetPassActivity extends BaseActivity {

    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.yanzhengma)
    EditText yanzhengma;
    @BindView(R.id.daojiashi)
    TextView daojiashi;
    @BindView(R.id.clickbtn)
    Button clickbtn;
    private TimeCount timeCount;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_reset_pass;
    }

    @Override
    public void initView() {
        setcenterTitle("重置密码");
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
                    showToast("电话号码为空");
                } else if (!PhoneUtils.isMobileNO(edittext(phone))) {
                    showToast("电话号码不正确");
                } else {
                    GenSMSVerifyCode(edittext(phone), 10);
                    timeCount.start();
                }
                break;
            case R.id.clickbtn:
                if (ismepty(phone)) {
                    showToast("电话号码为空");
                } else if (ismepty(yanzhengma)) {
                    showToast("验证码为空");
                } else {
                    VerifyVerCode(1,10,edittext(yanzhengma));

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }

    private String edittext(TextView e) {
        return e.getText().toString().trim();
    }

    private boolean ismepty(TextView a) {
        return a.getText().toString().trim().isEmpty();
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
                            ResetUserPayPwd(1, edittext(yanzhengma));
                        } else if (bean.getResult().equals("0")) {
                            showToast("非法验证码");
                        } else if (bean.getResult().equals("2")) {
                            showToast("验证码过期");
                        }
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

    private void ResetUserPayPwd(int VerifyMode, String VerCode) {
        RetrofitHttpUtil.getApiService()
                .ResetUserPayPwd("", SPUtils.getUserId(context), VerifyMode, VerCode)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        VerificationCodeBean bean = GsonUtil.parseJsonWithGson(s, VerificationCodeBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("重置用户支付密码成功");
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前用户不存在");
                        } else if (bean.getResult().equals("2")) {
                            showToast("重置支付密码失败");
                        } else if (bean.getResult().equals("3")) {
                            showToast("获取模板失败");
                        }else if (bean.getResult().equals("4")) {
                            showToast("生成短信或Email失败");
                        }
                    }
                });
    }
}
