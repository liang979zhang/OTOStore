package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.EditShopResultBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateInfoActivity extends BaseActivity {
    @BindView(R.id.infoupdate)
    EditText infoupdate;
    @BindView(R.id.clickupdate)
    Button clickupdate;
    private int selectType;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_update_info;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        selectType = bundle.getInt("updateinfotype");
        if (selectType == 1) {
            setcenterTitle("修改电话");
            infoupdate.setHint("修改电话");
        } else if (selectType == 2) {
            setcenterTitle("修改邮箱");
            infoupdate.setHint("修改邮箱");
        }
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

    @OnClick(R.id.clickupdate)
    public void onViewClicked() {
        if (selectType == 1) {
            if (isempty(infoupdate)) {
                showToast("填写信息不可以为空");
            } else if (!PhoneUtils.isMobileNO(editString(infoupdate))) {
                showToast("正确填写手机号");

            } else {

                updateinfo(1, editString(infoupdate));
            }
        } else if (selectType == 2) {
            if (isempty(infoupdate)) {
                showToast("填写信息不可以为空");
            } else if (!CommonUtils.isEmail(editString(infoupdate))) {
                showToast("正确邮箱号");

            } else {
                updateinfo(2, editString(infoupdate));
            }
        }
    }

    private void updateinfo(int tyoe, String Content) {
        RetrofitHttpUtil.getApiService().EditUserInfo("", SPUtils.getUserId(context), tyoe, Content).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        EditShopResultBean bean = GsonUtil.parseJsonWithGson(s, EditShopResultBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("修改成功");
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B,"UpdatePersonInfo"));
                            finish();
                        } else {
                            showToast("修改失败");
                        }
                    }
                });
    }

    private Boolean isempty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private String editString(EditText editText) {
        return editText.getText().toString().trim();
    }
}
