package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.otostore.R;

public class PeopleNearbyActivity extends BaseActivity {

    @Override
    public int getContentViewResId() {
        return R.layout.activity_people_nearby;
    }

    @Override
    public void initView() {
        setcenterTitle("附近的人暂且没有");
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
