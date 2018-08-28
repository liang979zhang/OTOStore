package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.ReferralsResultBean;
import com.example.administrator.otostore.Bean.UserRegisterResultBean;
import com.example.administrator.otostore.Bean.VerificationCodeBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.example.administrator.otostore.Utils.TimeCount;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_verification)
    EditText loginVerification;
    @BindView(R.id.register_getverification)
    Button registerGetverification;
    @BindView(R.id.login_read_confim)
    CheckBox loginReadConfim;
    @BindView(R.id.login_next_step)
    Button loginNextStep;
    @BindView(R.id.login_pass_find)
    EditText loginPassFind;
    @BindView(R.id.login_repass_find)
    EditText loginRepassFind;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_man)
    RadioButton loginMan;
    @BindView(R.id.login_woman)
    RadioButton loginWoman;
    @BindView(R.id.login_gender)
    RadioGroup loginGender;
    @BindView(R.id.referrals_none)
    RadioButton referralsNone;
    @BindView(R.id.referrals_visible)
    RadioButton referralsVisible;
    @BindView(R.id.referrals_group)
    RadioGroup referralsGroup;
    @BindView(R.id.referrals_number)
    EditText referralsNumber;
    @BindView(R.id.referrals_show)
    LinearLayout referralsShow;
    private int choice_gander = 0;
    private TimeCount timeCount;
    private int showreferrals = 0;
    private long parUserID = 0;

    private int getverification = 0;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {

        timeCount = new TimeCount(120000, 1000);

        timeCount.setCountListener(new TimeCount.CountListener() {
            @Override
            public void onTick(Long time) {
                registerGetverification.setText(String.valueOf(time / 1000).concat(" s") + "");
                registerGetverification.setEnabled(false);//设置不可点击
            }

            @Override
            public void onFinish() {
                registerGetverification.setText("重新发送");
                registerGetverification.setEnabled(true);
            }
        });
        loginGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.login_woman:
                        choice_gander = 1;
                        LogUtils.d("参数是" + choice_gander);
                        break;
                    case R.id.login_man:

                        choice_gander = 0;
                        LogUtils.d("参数是" + choice_gander);
                        break;
                    default:
                }


            }
        });

        referralsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.referrals_none:
                        referralsShow.setVisibility(View.GONE);
                        showreferrals = 0;
                        break;
                    case R.id.referrals_visible:
                        referralsShow.setVisibility(View.VISIBLE);
                        showreferrals = 1;
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
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

    @OnClick({R.id.register_getverification, R.id.login_read_confim, R.id.login_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_getverification:
//                String pasmd5 = MD5Utils.encodeMD5("1111111111111111");
//                LogUtils.d("AAAAAAAAAA:密码" + pasmd5.length());
                String yzmphone = loginPhone.getText().toString();
                if (PhoneUtils.isMobileNO(yzmphone)) {
                    timeCount.start();
                    getverification++;
                    showverificationcode(yzmphone);
                } else {
                    showToast("请输入有效电话号码");
                }
                break;
            case R.id.login_read_confim:
                break;
            case R.id.login_next_step:
                String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
                if (loginPhone.getText().toString().trim().isEmpty()) {
                    showToast("请输入手机号");
                } else if (getverification == 0) {
                    showToast("请获取验证码");
                } else if (loginVerification.getText().toString().trim().isEmpty()) {
                    showToast("请输入验证码");
                } else if (!PhoneUtils.isMobileNO(loginPhone.getText().toString().trim())) {
                    showToast("请输入有效电话号码");
                } else if (loginPassFind.getText().toString().trim().isEmpty()) {
                    showToast("密码不能为空");
                } else if (!loginPassFind.getText().toString().trim().matches(regex)) {
                    showToast("请输入正确格式密码");
                } else if (!loginPassFind.getText().toString().equals(loginRepassFind.getText().toString())) {
                    showToast("两次密码不一样");
                } else if (nickname.getText().toString().trim().isEmpty()) {
                    showToast("请输入昵称");
                } else if (loginEmail.getText().toString().trim().isEmpty()) {
                    showToast("请输入电子邮箱");
                } else if (!CommonUtils.isEmail(loginEmail.getText().toString().trim())) {
                    showToast("请输入正确邮箱");
                } else if (!loginReadConfim.isChecked()) {
                    showToast("请注册同意许可");
                } else if (showreferrals == 1 && referralsNumber.getText().toString().trim().isEmpty()) {
                    showToast("推荐人手机号不能为空");
                } else if (showreferrals == 1 && !PhoneUtils.isMobileNO(referralsNumber.getText().toString().trim())) {
                    showToast("推荐人手机号格式不正确");
                } else {
                    if (showreferrals == 1 && PhoneUtils.isMobileNO(referralsNumber.getText().toString().trim())) {
                        getreferrals(referralsNumber.getText().toString().trim());
                    }
                    register(choice_gander, nickname.getText().toString().trim(), parUserID, loginEmail.getText().toString().trim(), loginPhone.getText().toString().trim(), loginPassFind.getText().toString());
//                    startActivity(FillInInformationActivity.class);
                }
                break;
        }
    }

    private void showverificationcode(String phone) {
        RetrofitHttpUtil.getApiService()
                .GenSMSVerifyCode("", 0, phone, 1)
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
                            showToast("验证码发送成功");
                        }

                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }

    private void register(int gender, String nickname, long parUserID, String email, String phone, String pass) {
        String pasmd5 = MD5Utils.encodeMD5(pass);
        LogUtils.d("AAAAAAAAAA:密码" + pasmd5.length());
        RetrofitHttpUtil.getApiService()
                .UserRegister("", 1, parUserID, gender, nickname, email, phone, pasmd5)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s + "AAAAAAAAAAAAAA");
//(0,失败; 1,成功; 2,手机号码长度不正确; 3,手机号码不合法; 4,手机号码已注册; 5,邮箱已注册; 6,昵称已存在; 7,推荐人不存在
                UserRegisterResultBean userRegisterResultBean = GsonUtil.parseJsonWithGson(s, UserRegisterResultBean.class);
                if (userRegisterResultBean.getResult().equals("1")) {

//                    showToast("注册成功");
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", Integer.valueOf(userRegisterResultBean.getUserID()));
                    startActivity(StoreNameActivity.class, bundle);
                    finish();
                } else if (userRegisterResultBean.getResult().equals("0")) {
                    showAlertDialog("注册失败", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("2")) {
                    showAlertDialog("手机号码长度不正确", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("3")) {
                    showAlertDialog("手机号码不合法", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("4")) {
                    showAlertDialog("手机号码已注册", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("5")) {
                    showAlertDialog("邮箱已注册", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("6")) {
                    showAlertDialog("昵称已存在", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (userRegisterResultBean.getResult().equals("7")) {
                    showAlertDialog("推荐人不存在", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

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

    private void getreferrals(String phone) {
        RetrofitHttpUtil.getApiService()
                .GetUserBaseInfoA("", phone)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("推荐人:;;;" + s);
                        if (s.equals("")) {
                            parUserID = 0;

                        } else {
                            ReferralsResultBean referralsResultBean = GsonUtil.parseJsonWithGson(s, ReferralsResultBean.class);
                            parUserID = Long.parseLong(referralsResultBean.getUserID());
                        }
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }

}
