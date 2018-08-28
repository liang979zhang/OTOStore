package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateFriendsMomentActivity extends BaseActivity {
    @BindView(R.id.momentpublish)
    EditText momentpublish;
    @BindView(R.id.update)
    Button update;
    private Context context;
    private Long momentupdateid;
    private String remarks;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_update_friends_moment;
    }

    @Override
    public void initView() {
        setcenterTitle("修改动态");
        Bundle bundle = getIntent().getExtras();
        momentupdateid = Long.valueOf(bundle.getString("momentid"));
        remarks = bundle.getString("remark");
        momentpublish.setText(remarks);
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

    @OnClick(R.id.update)
    public void onViewClicked() {
        if (momentpublish.getText().toString().isEmpty()) {
            showToast("内容不能为空");
        } else {
            EditMovement(momentupdateid, momentpublish.getText().toString());
        }
    }

    private void EditMovement(Long momentid, String reamrks) {
        RetrofitHttpUtil.getApiService()
                .EditMovement("", Long.valueOf(SPUtils.getUserId(context)), momentid, reamrks)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean categroyBean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (categroyBean.getResult().equals("0")) {
                            showToast("当前用户当前动态不存在");
                        } else if (categroyBean.getResult().equals("1")) {
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B, "MomentPublish"));
                            showToast("编辑成功");
                            finish();
                        } else if (categroyBean.getResult().equals("2")) {
                            showToast("编辑失败");
                        }
                    }
                });
    }
}
