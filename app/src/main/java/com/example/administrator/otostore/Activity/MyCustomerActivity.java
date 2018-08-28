package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.otostore.R;
import com.example.administrator.otostore.Utils.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCustomerActivity extends BaseActivity {
    @BindView(R.id.phonenum)
    EditText phonenum;
    @BindView(R.id.clickadd)
    TextView clickadd;
    @BindView(R.id.yonghunum)
    TextView yonghunum;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_customer;
    }

    @Override
    public void initView() {
        setcenterTitle("我的用户");
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

    @OnClick(R.id.clickadd)
    public void onViewClicked() {
        if (phonenum.getText().toString().trim().isEmpty()) {
            showToast("请输入电话号码");
        } else if (!PhoneUtils.isMobileNO(phonenum.getText().toString().trim())) {
            showToast("输入有效电话号码");
        } else {

        }
    }
}
