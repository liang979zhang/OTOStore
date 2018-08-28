package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersTypeActivity extends BaseActivity {
    @BindView(R.id.tioazhengqueren)
    Button tioazhengqueren;
    @BindView(R.id.showtiaozhengjiner)
    LinearLayout showtiaozhengjiner;
    @BindView(R.id.tioazhengedit)
    EditText tioazhengedit;
    @BindView(R.id.yanchangdit)
    EditText yanchangdit;
    @BindView(R.id.yanchangok)
    Button yanchangok;
    @BindView(R.id.showyanchang)
    LinearLayout showyanchang;
    @BindView(R.id.daodianshuomingdit)
    EditText daodianshuomingdit;
    @BindView(R.id.daodianyanzengmadit)
    EditText daodianyanzengmadit;
    @BindView(R.id.daodianok)
    Button daodianok;
    @BindView(R.id.showdaodian)
    LinearLayout showdaodian;
    @BindView(R.id.tongyituikuandit)
    EditText tongyituikuandit;
    @BindView(R.id.tongyituikuanok)
    Button tongyituikuanok;
    @BindView(R.id.showtongyituikuan)
    LinearLayout showtongyituikuan;
    @BindView(R.id.tongjujuekuandit)
    EditText tongjujuekuandit;
    @BindView(R.id.jujuekuanok)
    Button jujuekuanok;
    @BindView(R.id.showjujuetuikuan)
    LinearLayout showjujuetuikuan;
    @BindView(R.id.querenkuandit)
    EditText querenkuandit;
    @BindView(R.id.querenkuanok)
    Button querenkuanok;
    @BindView(R.id.showquerentuikuan)
    LinearLayout showquerentuikuan;
    private Context context;
    private int orderstype;
    private Long ordersId;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_orders_type;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        orderstype = Integer.valueOf(bundle.getString("OdersType"));
        if (orderstype == 1) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("调整金额");
            showtiaozhengjiner.setVisibility(View.VISIBLE);
        } else if (orderstype == 2) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("延长订单交易期限");
            showyanchang.setVisibility(View.VISIBLE);
        } else if (orderstype == 3) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("确认到店");
            showyanchang.setVisibility(View.VISIBLE);
        } else if (orderstype == 7) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("同意退款");
            showtongyituikuan.setVisibility(View.VISIBLE);
        } else if (orderstype == 8) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("拒绝退款");
            showjujuetuikuan.setVisibility(View.VISIBLE);
        } else if (orderstype == 9) {
            ordersId = Long.valueOf(bundle.getString("ordersId"));
            setcenterTitle("确认退款");
            showquerentuikuan.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.tioazhengqueren, R.id.showtiaozhengjiner, R.id.yanchangok, R.id.daodianok, R.id.tongyituikuanok, R.id.jujuekuanok, R.id.querenkuanok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tioazhengqueren:
                if (isempty(tioazhengedit)) {
                    showToast("请输入调整金额");
                } else {
                    SellerAdjustAmt(ordersId, BigDecimal.valueOf(Double.valueOf(getedit(tioazhengedit))));
                }
                break;
            case R.id.yanchangok:
                if (isempty(yanchangdit)) {
                    showToast("请输入延长时间");
                } else {
                    DoDelayOrderSuccessTime(ordersId, Integer.valueOf(getedit(yanchangdit)));
                }
                break;
            case R.id.daodianok:
                if (isempty(daodianyanzengmadit)) {
                    showToast("请输入验证码");
                } else {
                    String remark = "";
                    if (isempty(daodianshuomingdit)) {
                        remark = "";
                    } else {
                        remark = getedit(daodianshuomingdit);
                    }
                    ConfirmArrivalConsumption(ordersId, getedit(daodianyanzengmadit), remark);
                }
                break;
            case R.id.tongyituikuanok:
                String remark = "";
                if (isempty(tongyituikuandit)) {
                    remark = "";
                } else {
                    remark = getedit(tongyituikuandit);
                }
                AgreeRefundBill(ordersId, remark);
                break;
            case R.id.jujuekuanok:
                String remarka = "";
                if (isempty(tongjujuekuandit)) {
                    remarka = "";
                } else {
                    remarka = getedit(tongjujuekuandit);
                }
                RefuseRefundBill(ordersId, remarka);
                break;
            case R.id.querenkuanok:
                String remarkb = "";
                if (isempty(querenkuandit)) {
                    remarkb = "";
                } else {
                    remarkb = getedit(querenkuandit);
                }
                ConfirmRefundBill(ordersId,remarkb);
                break;
        }
    }

    private boolean isempty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }
    private void ConfirmRefundBill(Long RefundBillID, String Remark){
        RetrofitHttpUtil
                .getApiService()
                .ConfirmRefundBill("", SPUtils.getUserId(context), RefundBillID, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("确认退款成功");
                            finish();
                        } else if (bean.getResult().equals("2")) {
                            showToast("当前用户不是系统内置用户也不是卖家用户");
                        } else if (bean.getResult().equals("3")) {
                            showToast("不在卖家同意状态");
                        } else if (bean.getResult().equals("4")) {
                            showToast("确认退款失败");
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前退款单不存在");
                        }
                    }
                });
    }
    private void RefuseRefundBill(Long RefundBillID, String Remark) {
        RetrofitHttpUtil.getApiService()
                .RefuseRefundBill("", SPUtils.getUserId(context), RefundBillID, Remark).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("拒绝退款成功");
                            finish();
                        } else {
                            showToast("拒绝退款失败");
                        }
                    }
                });
    }

    private void AgreeRefundBill(Long RefundBillID, String Remark) {
        RetrofitHttpUtil.getApiService()
                .AgreeRefundBill("", SPUtils.getUserId(context), RefundBillID, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("同意退款成功");
                            finish();
                        } else if (bean.getResult().equals("2")) {
                            showToast("不在买家申请状态");
                        } else if (bean.getResult().equals("3")) {
                            showToast("卖家未设置默认退货地址");
                        } else if (bean.getResult().equals("4")) {
                            showToast("同意退款失败");
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前退款单不存在");
                        }
                    }
                });
    }

    private String getedit(TextView textView) {
        return textView.getText().toString().trim();
    }

    private void DoDelayOrderSuccessTime(Long OrderID, int days) {
        RetrofitHttpUtil.getApiService()
                .DoDelayOrderSuccessTime("", SPUtils.getUserId(context), OrderID, days)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("延期成功");
                            finish();
                        } else if (bean.getResult().equals("2")) {
                            showToast("当前用户既不是买家又不是买家");
                        } else if (bean.getResult().equals("3")) {
                            showToast("到店订单不在已付款状态");
                        } else if (bean.getResult().equals("4")) {
                            showToast("配送订单不在已发货状态");
                        } else if (bean.getResult().equals("5")) {
                            showToast("买家已延期,只能延期一次");
                        } else if (bean.getResult().equals("6")) {
                            showToast("延期天数已超过卖家每次最大延期天数");
                        } else if (bean.getResult().equals("7")) {
                            showToast("订单延期失败");
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前订单不存在");
                        }
                    }
                });
    }

    private void ConfirmArrivalConsumption(Long OrderID, String VerifyCode, String Remark) {
        RetrofitHttpUtil.getApiService()
                .ConfirmArrivalConsumption("", SPUtils.getUserId(context), SPUtils.getShopId(context), OrderID, VerifyCode, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("到店消费成功");
                            finish();
                        } else if (bean.getResult().equals("2")) {
                            showToast("非到店订单");
                        } else if (bean.getResult().equals("3")) {
                            showToast("当前用户不是卖家用户也不是系统内置用户");
                        } else if (bean.getResult().equals("4")) {
                            showToast("订单不在已付款、已预约状态");
                        } else if (bean.getResult().equals("5")) {
                            showToast("订单已超期");
                        } else if (bean.getResult().equals("6")) {
                            showToast("到店验证码不正确");
                        } else if (bean.getResult().equals("7")) {
                            showToast("到店消费订单失败");
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前店铺当前订单不存在");
                        }
                    }
                });
    }

    private void SellerAdjustAmt(Long OrderID, BigDecimal SellerAdjustAmt) {
        RetrofitHttpUtil.getApiService()
                .SellerAdjustAmt("", SPUtils.getUserId(context), OrderID, SellerAdjustAmt)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("调整成功");
                            finish();
                        } else if (bean.getResult().equals("2")) {
                            showToast("商家调整订单金额失败");
                        } else if (bean.getResult().equals("3")) {
                            showToast("调整后金额为负数,调整失败");
                        } else if (bean.getResult().equals("4")) {
                            showToast("调整金额在订单金额中占比过大, 调整失败");
                        } else if (bean.getResult().equals("5")) {
                            showToast("将优惠金额重新摊分到订单产品失败");
                        } else if (bean.getResult().equals("6")) {
                            showToast("将剩余金额积分摊分到计金额最大的订单产品上失败");
                        } else if (bean.getResult().equals("0")) {
                            showToast("订单不存在或订单不在待付款状态");
                        }
                    }
                });
    }
}
