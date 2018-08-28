package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adorkable.iosdialog.AlertDialog;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.UserRegisterResultBean;
import com.example.administrator.otostore.Bean.VerificationCodeBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.example.administrator.otostore.Utils.TimeCount;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class FindYourPasActivity extends Activity {

    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_verification)
    EditText loginVerification;
    @BindView(R.id.register_getverification)
    Button registerGetverification;
    @BindView(R.id.login_pass_find)
    EditText loginPassFind;
    @BindView(R.id.login_repass_find)
    EditText loginRepassFind;
    @BindView(R.id.login_complete_find)
    Button loginCompleteFind;
    private Context context;
    private TimeCount timeCount;
    private int getverification = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_your_pas);
        context = this;
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        timeCount = new TimeCount(120000, 1000);

        timeCount.setCountListener(new TimeCount.CountListener() {
            @Override
            public void onTick(Long time) {
                registerGetverification.setText(String.valueOf(time / 1000).concat(" s"));
                registerGetverification.setEnabled(false);//设置不可点击
            }

            @Override
            public void onFinish() {
                registerGetverification.setText("重新发送");
                registerGetverification.setEnabled(true);
            }
        });
    }

    @OnClick({R.id.register_getverification, R.id.login_complete_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_getverification:

                String yzmphone = loginPhone.getText().toString();
                if (PhoneUtils.isMobileNO(yzmphone)) {
                    timeCount.start();
                    getverification++;
                    showverificationcode(yzmphone);
                } else {
                    Toast.makeText(context, "请输入有效电话号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_complete_find:
                String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
                if (loginPhone.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (getverification == 0) {
                    Toast.makeText(context, "请获取验证码", Toast.LENGTH_SHORT).show();
                } else if (loginVerification.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!PhoneUtils.isMobileNO(loginPhone.getText().toString().trim())) {
                    Toast.makeText(context, "请输入有效手机号", Toast.LENGTH_SHORT).show();
                } else if (loginPassFind.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!loginPassFind.getText().toString().trim().matches(regex)) {
                    Toast.makeText(context, "请输入正确格式密码", Toast.LENGTH_SHORT).show();
                } else if (loginRepassFind.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "验证密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!loginPassFind.getText().toString().equals(loginRepassFind.getText().toString())) {
                    Toast.makeText(context, "两次密码不一样", Toast.LENGTH_SHORT).show();
                } else {
                    resetpass(loginPhone.getText().toString().trim(), loginPassFind.getText().toString().trim());
                }
                break;
        }
    }

    private void showverificationcode(String phone) {
        int userid = SPUtils.getUserId(context);
        if (userid != 0) {
            RetrofitHttpUtil.getApiService()
                    .GenSMSVerifyCode("", userid, phone, 3)
                    .compose(SchedulerTransformer.<String>transformer())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {

                        }
                    })
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String s) {
                            VerificationCodeBean verificationCodeBean = GsonUtil.parseJsonWithGson(s, VerificationCodeBean.class);
                            if (verificationCodeBean.getResult().equals("1")) {

                                Toast.makeText(context, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        protected void onFailed(HttpResponseException responseException) {
                            super.onFailed(responseException);
                        }
                    });
        }
    }

    private void resetpass(String phone, String passnew) {
        RetrofitHttpUtil.getApiService()
                .ResetUserPwd("", phone, MD5Utils.encodeMD52(passnew))
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("密码重置：" + s);
                        UserRegisterResultBean userRegisterResultBean = GsonUtil.parseJsonWithGson(s, UserRegisterResultBean.class);
                        if (userRegisterResultBean.getResult().equals("0")) {
                            new AlertDialog(context).builder().setMsg("号码未注册").setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        } else if (userRegisterResultBean.getResult().equals("1")) {
                            Toast.makeText(context, "密码重置成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.setClass(FindYourPasActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }
}
