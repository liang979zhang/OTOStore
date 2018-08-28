package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.otostore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/13.
 */

public class StoreNameActivity extends BaseActivity {
    @BindView(R.id.store_name)
    EditText storeName;
    @BindView(R.id.select_store)
    TextView selectStore;
    private int userid;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_storename;
    }

    @Override
    public void initView() {
        setcenterTitle("店铺设置");
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

    @OnClick(R.id.select_store)
    public void onViewClicked() {
        if (storeName.getText().toString().trim().isEmpty()) {
            showAlertDialog("店铺填写不能为空", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {

            Bundle bundle = getIntent().getExtras();
            userid = bundle.getInt("userid", 0);
            Bundle bu = new Bundle();
            bu.putInt("userid", userid);
            bu.putString("shopname", storeName.getText().toString());
            startActivity(TypeSelectionActivity.class,bu);
            finish();
        }
    }
}
