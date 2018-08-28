package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.otostore.Bean.UserBaseInfo;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangepasswordActivity extends BaseActivity {

    @BindView(R.id.systemaccount)
    TextView systemaccount;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.systemphone)
    TextView systemphone;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.systempass)
    TextView systempass;
    @BindView(R.id.passchange)
    LinearLayout passchange;
    private Context context;
    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_changepassword;
    }

    @Override
    public void initView() {
        setcenterTitle("个人信息");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        shoGetUserBaseInfo();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void shoGetUserBaseInfo() {
        RetrofitHttpUtil.getApiService()
                .GetUserBaseInfo("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        UserBaseInfo userBaseInfo = GsonUtil.parseJsonWithGson(s, UserBaseInfo.class);
                        account.setText(userBaseInfo.getUserCode());
                        phone.setText(userBaseInfo.getMobileNum());
                    }
                });
    }

    @OnClick(R.id.passchange)
    public void onViewClicked() {
        startActivity(UpdatePersonImageActivity.class);
    }
}
