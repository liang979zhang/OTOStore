package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.administrator.otostore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NianFeiActivity extends BaseActivity {
    @BindView(R.id.feioneyear)
    RadioButton feioneyear;
    @BindView(R.id.feihalfyear)
    RadioButton feihalfyear;
    @BindView(R.id.feithreemonth)
    RadioButton feithreemonth;
    @BindView(R.id.feigroup)
    RadioGroup feigroup;
    @BindView(R.id.jiesuan)
    RelativeLayout jiesuan;
    private int feivalue = 4;
    private String paytitle;
    private int paytype;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_nian_fei;
    }

    @Override
    public void initView() {
        setcenterTitle("支付年费");
        feigroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.feioneyear:
                        paytitle = "一年会员";
                        feivalue = 4;
                        paytype = 4;
                        break;
                    case R.id.feihalfyear:
                        paytitle = "半年会员";
                        feivalue = 3;
                        paytype = 3;
                        break;
                    case R.id.feithreemonth:
                        paytitle = "三月会员";
                        feivalue = 2;
                        paytype = 2;
                        break;
                }
            }
        });
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

    @OnClick(R.id.jiesuan)
    public void onViewClicked() {
        Bundle b = new Bundle();
        b.putInt("payfeitype", paytype);
        b.putString("payfeititleText", paytitle);
        b.putInt("payfeivalu", feivalue);
        startActivity(PayNianFeiActivity.class, b);
    }
}
