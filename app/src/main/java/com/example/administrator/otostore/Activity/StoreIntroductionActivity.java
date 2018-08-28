package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.otostore.Bean.FeeListBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.Bean.ShopInfoBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreIntroductionActivity extends BaseActivity {
    @BindView(R.id.shopimage)
    ImageView shopimage;
    @BindView(R.id.xiugai)
    ImageView xiugai;
    @BindView(R.id.dongtai)
    ImageView dongtai;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.kaitong)
    TextView kaitong;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_store_introduction;
    }

    @Override
    public void initView() {
        setcenterTitle("店铺介绍");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        showstoreinfo();
        showstoreimage();
        showfee();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveStickyEvent(MessageEvent event) {
        super.receiveStickyEvent(event);
        switch (event.getCode()) {
            case MyEventCode.CODE_B:
                if (event.getData().toString().equals("UpdateImageInfo")) {
                    showstoreimage();
                } else if (event.getData().toString().equals("nianfeipayok")) {
                    showfee();
                }
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.shopimage, R.id.xiugai, R.id.dongtai, R.id.kaitong,R.id.xiugaia,R.id.nianfeia,R.id.dongtais})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopimage:
                break;
            case R.id.xiugaia:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.dongtais:
                startActivity(FriendsMomentActivity.class);
                break;
            case R.id.nianfeia:
                startActivity(NianFeiActivity.class);
                break;
        }
    }

    private void showstoreinfo() {
        RetrofitHttpUtil.getApiService().GetShopInfoEx("", SPUtils.getShopId(context)).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")){
                        ShopInfoBean bean = GsonUtil.parseJsonWithGson(s, ShopInfoBean.class);
                        name.setText(bean.getShopName());}

                    }
                });
    }

    private void showstoreimage() {
        RetrofitHttpUtil.getApiService().GetShopAttachList("", SPUtils.getShopId(context), 1, 10, 1).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.trim().equals("[]")) {
                            List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                            Glide.with(context).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into(shopimage);
                        }

                    }
                });
    }

    private void showfee() {
        RetrofitHttpUtil.getApiService().GetShopFeeList("", SPUtils.getShopId(context)).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                if (!s.equals("")) {
                    List<FeeListBean> list = GsonUtil.parseJsonArrayWithGson(s, FeeListBean.class);
                    kaitong.setText("截止时间:" + list.get(list.size() - 1).getEndDate().substring(0, list.get(list.size() - 1).getEndDate().indexOf(" ")).replaceAll("/", "-"));
                }
            }
        });
    }
}
