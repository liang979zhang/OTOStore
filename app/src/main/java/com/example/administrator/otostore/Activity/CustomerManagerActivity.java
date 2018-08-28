package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.otostore.R;

public class CustomerManagerActivity extends BaseActivity {

    @Override
    public int getContentViewResId() {
        return R.layout.activity_customer_manager;
    }

    @Override
    public void initView() {
        setcenterTitle("客户管理");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

    }
}
