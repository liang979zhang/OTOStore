package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.UserInfoBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountAndSafeActivity extends BaseActivity {

    @BindView(R.id.account)
    ImageView account;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.xiugaimima)
    ImageView xiugaimima;
    @BindView(R.id.dainpuxinxi)
    ImageView dainpuxinxi;
    @BindView(R.id.closedianpu)
    Button closedianpu;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_account_and_safe;
    }

    @Override
    public void initView() {
        setcenterTitle("账号和安全");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        getUserinfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.account,
            R.id.zccoutna,
            R.id.passchage,
            R.id.dianupuxinx,
            R.id.xiugaimima, R.id.dainpuxinxi, R.id.closedianpu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zccoutna:
//                startActivity(UpdatePersonImageActivity.class);
                startActivity(ChangepasswordActivity.class);
                break;
            case R.id.passchage:
                startActivity(ChangYourPassActivity.class);
                break;
            case R.id.dianupuxinx:
                LogUtils.d("1111111111111111111");
                startActivity(EditRegisterActivity.class);
                break;
            case R.id.closedianpu:
                break;
        }
    }

    private void getUserinfo() {
        RetrofitHttpUtil.getApiService()
                .GetUserBaseInfo("", SPUtils.getUserId(context)).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        UserInfoBean bean = GsonUtil.parseJsonWithGson(s, UserInfoBean.class);
                        phone.setText(bean.getMobileNum());
                    }
                });
    }
}
