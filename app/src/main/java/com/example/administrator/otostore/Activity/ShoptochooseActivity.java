package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.otostore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/9.
 */

public class ShoptochooseActivity extends BaseActivity {
    @BindView(R.id.shopselect)
    TextView shopselect;
    @BindView(R.id.shopselectbtn)
    Button shopselectbtn;
    @BindView(R.id.shopname)
    EditText shopname;
    private boolean isselectindustry = false;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_shoptochoose;
    }

    @Override
    public void initView() {
        setcenterTitle("店铺选择");
//        showright(true);
//        setrightTitle("test");

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

    @OnClick({R.id.shopselect, R.id.shopselectbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopselect:
                startActivity(TypeSelectionActivity.class);
                break;
            case R.id.shopselectbtn:
                if (shopname.getText().toString().trim().isEmpty()) {
                    showAlertDialog("请输入店铺名称", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (!isselectindustry) {
                    showAlertDialog("请选择行业类型", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else startActivity(RegisterActivity.class);
                break;
        }
    }
}
