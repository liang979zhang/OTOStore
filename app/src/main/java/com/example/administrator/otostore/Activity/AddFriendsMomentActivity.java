package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.GetShopInfo;
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

public class AddFriendsMomentActivity extends BaseActivity {
    @BindView(R.id.momentpublish)
    EditText momentpublish;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_add_friends_moment;
    }

    @Override
    public void initView() {
        setcenterTitle("发表动态");
        showright(true);
        setrightTitle("发布");
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        if (momentpublish.getText().toString().trim().isEmpty()) {
            showToast("请填写动态");
        } else {
            PublishMoment(momentpublish.getText().toString().trim());
        }
    }

    private void PublishMoment(String remark) {
        RetrofitHttpUtil.getApiService()
                .PublishMovement("", Long.valueOf(SPUtils.getUserId(context)), Long.valueOf("0"), remark).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s + "动态");
                        GetShopInfo getShopInfo = GsonUtil.parseJsonWithGson(s, GetShopInfo.class);
                        if (getShopInfo.getResult().equals("1")) {
                            showToast("发布动态成功");
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B,"MomentPublish"));
                            Bundle bundle=new Bundle();
                            bundle.putString("momentid",getShopInfo.getMovementID());
                            startActivity(MomentImageLaunchActivity.class,bundle);
                            finish();
                        } else if (getShopInfo.getResult().equals("2")) {
                            showToast("店铺不存在");
                        } else if (getShopInfo.getResult().equals("3")) {
                            showToast("当前用户与店铺卖家用户不匹配");
                        } else if (getShopInfo.getResult().equals("4")) {
                            showToast("发布动态失败");
                        } else if (getShopInfo.getResult().equals("0")) {
                            showToast("用户不存在");
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
}
