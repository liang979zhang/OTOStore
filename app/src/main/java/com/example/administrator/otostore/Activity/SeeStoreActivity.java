package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.otostore.Bean.SeeStoreBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeStoreActivity extends BaseActivity {
    @BindView(R.id.ordertype)
    TextView ordertype;
    @BindView(R.id.DeliverFee)
    TextView DeliverFee;
    @BindView(R.id.DeliveryNoChargeAmt)
    TextView DeliveryNoChargeAmt;
    @BindView(R.id.PackingFee)
    TextView PackingFee;
    @BindView(R.id.PackingNoChargeAmt)
    TextView PackingNoChargeAmt;
    @BindView(R.id.Discount)
    TextView Discount;
    @BindView(R.id.ClosingMode)
    TextView ClosingMode;
    @BindView(R.id.ClosingStartDate)
    TextView ClosingStartDate;
    @BindView(R.id.ClosingEndDate)
    TextView ClosingEndDate;
    @BindView(R.id.remark)
    TextView remark;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_see_store;
    }

    @Override
    public void initView() {
        setcenterTitle("查看管理");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        RetrofitHttpUtil.getApiService()
                .GetShopSettings("", SPUtils.getShopId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        SeeStoreBean seeStoreBean = GsonUtil.parseJsonWithGson(s, SeeStoreBean.class);
                        if (seeStoreBean.getOrderType().equals("1")) {
                            ordertype.setText("到店");
                        } else if (seeStoreBean.getOrderType().equals("2")) {
                            ordertype.setText("配送");
                        } else if (seeStoreBean.getOrderType().equals("3")) {
                            ordertype.setText("全部");
                        }

                        DeliverFee.setText(seeStoreBean.getDeliverFee().toString());
                        DeliveryNoChargeAmt.setText(seeStoreBean.getDeliveryNoChargeAmt().toString());
                        PackingFee.setText(seeStoreBean.getPackingFee().toString());
                        PackingNoChargeAmt.setText(seeStoreBean.getPackingNoChargeAmt().toString());
                        Discount.setText(seeStoreBean.getDiscount().toString());
                        if (seeStoreBean.getClosingMode().equals("1")) {
                            ClosingMode.setText("持续营业");
                        } else if (seeStoreBean.getClosingMode().equals("2")) {
                            ClosingMode.setText("阶段营业");
                        }
                        ClosingStartDate.setText(seeStoreBean.getClosingStartDate().toString());
                        ClosingEndDate.setText(seeStoreBean.getClosingEndDate().toString());
                        remark.setText(seeStoreBean.getRemark().toString());
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
