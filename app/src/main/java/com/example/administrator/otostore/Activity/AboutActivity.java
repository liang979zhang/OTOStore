package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.otostore.Bean.AboutBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.firsta)
    TextView firsta;
    @BindView(R.id.firstb)
    TextView firstb;
    @BindView(R.id.seconda)
    TextView seconda;
    @BindView(R.id.secondb)
    TextView secondb;
    @BindView(R.id.thirda)
    TextView thirda;
    @BindView(R.id.thirdb)
    TextView thirdb;
    private int type;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        type = Integer.valueOf(bundle.getString("SettingType"));
        if (type == 1) {
            setcenterTitle("关于我们");
        } else if (type == 2) {
            setcenterTitle("帮助");
        }
        getAbout();
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

    }

    private void getAbout() {
        RetrofitHttpUtil.getApiService()
                .GetAboutInfo("")
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        List<AboutBean> list = GsonUtil.parseJsonArrayWithGson(s, AboutBean.class);
                        if (type == 1) {
                            firsta.setText("公司:");
                            firstb.setText(list.get(0).getItemValue());
                            seconda.setText("网址:");
                            secondb.setText(list.get(1).getItemValue());
                            thirda.setText("地址:");
                            thirdb.setText(list.get(5).getItemValue());
                        } else if (type == 2) {
                            firsta.setText("电话:");
                            firstb.setText(list.get(3).getItemValue());
                            seconda.setText("邮箱:");
                            secondb.setText(list.get(2).getItemValue());
                            thirda.setText("版权:");
                            thirdb.setText(list.get(12).getItemValue());
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
