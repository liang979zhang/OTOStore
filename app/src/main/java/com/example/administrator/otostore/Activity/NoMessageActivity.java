package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.otostore.R;

public class NoMessageActivity extends BaseActivity {

    @Override
    public int getContentViewResId() {
        return R.layout.activity_no_message;
    }

    @Override
    public void initView() {
        setcenterTitle("未知选项");
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
