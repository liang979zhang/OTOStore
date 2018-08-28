package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.otostore.R;
import com.example.administrator.otostore.Utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.anquan)
    ImageView anquan;
    @BindView(R.id.update)
    ImageView update;
    @BindView(R.id.about)
    ImageView about;
    @BindView(R.id.help)
    ImageView help;
    @BindView(R.id.exit)
    Button exit;
    private Context context;

    @Override
    public int getContentViewResId() {
        context=this;
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setcenterTitle("设置");
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

    @OnClick({
            R.id.accounta,
            R.id.aoubta,
            R.id.helas,
            R.id.anquan, R.id.update, R.id.about, R.id.help, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.accounta:
                startActivity(AccountAndSafeActivity.class);
                break;
            case R.id.update:
//                startActivity(UpdatePersonImageActivity.class);
                break;
            case R.id.aoubta:
                Bundle bundle = new Bundle();
                bundle.putString("SettingType", "1");
                startActivity(AboutActivity.class, bundle);
                break;
            case R.id.helas:
                Bundle bundle1 = new Bundle();
                bundle1.putString("SettingType", "2");
                startActivity(AboutActivity.class, bundle1);
                break;
            case R.id.exit:
                SPUtils.clearUserId(context);
                SPUtils.clearShopId(context);
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }
}
