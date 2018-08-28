package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.DeliveryBillBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaHuoDanActivity extends BaseActivity {
    @BindView(R.id.select_kuaidi)
    TextView selectKuaidi;
    @BindView(R.id.yundanhao)
    EditText yundanhao;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.baocun)
    Button baocun;
    private Context context;
    private Long ordersId;
    private String remarktext;
    //    Long DeliveryID, Long OrderID, Long ExpCorpID, String DeliveryCode, String Remark
    private Long DeliveryID = Long.valueOf("0");
    private Long ExpCorpID;
    private String DeliveryCode;
    private int updateoradd = 0;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_fa_huo_dan;
    }

    @Override
    public void initView() {
        setcenterTitle("发货单");
        Bundle bundle = getIntent().getExtras();
        ordersId = Long.valueOf(bundle.getString("ordersId"));
        GetDeliveryBillEx(ordersId);
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

    @OnClick({R.id.select_kuaidi, R.id.baocun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_kuaidi:
                Intent intent = new Intent(context, SelectKuaidiActivity.class);
                startActivityForResult(intent, Constants.KuaiDiSelectRequest);
                break;
            case R.id.baocun:
                if (isempty(selectKuaidi)) {
                    showToast("请选择快递公司");
                } else if (isempty(yundanhao)) {
                    showToast("请输入运单号");
                } else {
                    if (isempty(remark)) {
                        remarktext = "";
                    } else {
                        remarktext = gettext(remark);
                    }
                    DeliveryCode = gettext(yundanhao);
                    SaveDeliveryBill(DeliveryID, ordersId, ExpCorpID, DeliveryCode, remarktext);


                }
                break;
        }
    }

    private String gettext(TextView textView) {
        return textView.getText().toString().trim();
    }

    private boolean isempty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    private void SaveDeliveryBill(Long DeliveryID, Long OrderID, Long ExpCorpID, String DeliveryCode, String Remark) {
        RetrofitHttpUtil.getApiService()
                .SaveDeliveryBill("", SPUtils.getUserId(context), DeliveryID, OrderID, ExpCorpID, DeliveryCode, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (updateoradd == 0) {
                            if (bean.getResult().equals("1")) {
                                showToast("新增运单成功");
                                finish();
                            } else if (bean.getResult().equals("0")) {
                                showToast("订单不存在");
                            } else if (bean.getResult().equals("2")) {
                                showToast("非配送订单，不能发货");
                            } else if (bean.getResult().equals("3")) {
                                showToast("订单不在待发货状态");
                            } else if (bean.getResult().equals("4")) {
                                showToast("新增运单失败");
                            } else if (bean.getResult().equals("5")) {
                                showToast("更新订单状态失败");
                            }

                        } else if (updateoradd == 1) {
                            if (bean.getResult().equals("1")) {
                                showToast("运单存在,修改运单信息成功");
                                finish();
                            } else if (bean.getResult().equals("0")) {
                                showToast("运单存在,单运单与订单不匹配或运单不在可修改状态。更新运单失败");
                            }
                        }
                    }
                });
    }

    private void GetDeliveryBillEx(Long OrderID) {
        RetrofitHttpUtil.getApiService()
                .GetDeliveryBillEx("", OrderID).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("[]")) {
                            updateoradd = 1;
                            DeliveryBillBean billBean = GsonUtil.parseJsonWithGson(s, DeliveryBillBean.class);
                            DeliveryID = Long.valueOf(billBean.getDeliveryID());
                            ExpCorpID = Long.valueOf(billBean.getExpCorpID());
                            DeliveryCode = billBean.getDeliveryCode();
                            remark.setText(billBean.getRemark());
                            yundanhao.setText(DeliveryCode);

                            GetExpressCorp(ExpCorpID);

                        }
                    }
                });
        {
        }
    }

    private void GetExpressCorp(long ExpCorpID) {
        RetrofitHttpUtil.getApiService()
                .GetExpressCorp("", ExpCorpID)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("[]")) {
                            DeliveryBillBean billBean = GsonUtil.parseJsonWithGson(s, DeliveryBillBean.class);
                            selectKuaidi.setText(billBean.getExpCorpName());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.KuaiDiSelectRequest && resultCode == Constants.KuaidiSelectResult) {
            selectKuaidi.setText(data.getStringExtra("ExpCorpName"));
            ExpCorpID = Long.valueOf(data.getStringExtra("ExpCorpID"));
        }
    }
}
