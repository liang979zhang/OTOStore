package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.MomentBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

public class StoreMomentShowActivity extends BaseActivity {
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_store_moment_show;
    }

    @Override
    public void initView() {
        setcenterTitle("动态");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        showmoment();
    }

    private void showmoment() {
        RetrofitHttpUtil.getApiService()
                .GetUserPubMovementList("", SPUtils.getUserId(context), SPUtils.getShopId(context), 1, 1).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s + "动态");
                        List<MomentBean> list = GsonUtil.parseJsonArrayWithGson(s, MomentBean.class);
                    }
                });
    }
}
