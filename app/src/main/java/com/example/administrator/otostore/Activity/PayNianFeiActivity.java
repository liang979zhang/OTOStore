package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.adorkable.iosdialog.ActionSheetDialog;
import com.adorkable.iosdialog.AlertDialog;
import com.apkfuns.logutils.LogUtils;
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

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayNianFeiActivity extends BaseActivity {
    @BindView(R.id.first)
    TextView first;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.qujiusan)
    Button qujiusan;
    private int type;
    private BigDecimal fee;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_pay_nian_fei;
    }

    @Override
    public void initView() {
        setcenterTitle("付款");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

//        b.putString("payfeititleText", paytitle);
//        b.putInt("payfeivalu", feivalue);
        Bundle bundle = getIntent().getExtras();
        first.setText(bundle.getString("payfeititleText"));
        type = bundle.getInt("payfeitype");
        fee = BigDecimal.valueOf(Double.valueOf(bundle.getInt("payfeivalu")));
        second.setText(String.valueOf(bundle.getInt("payfeivalu")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.qujiusan)
    public void onViewClicked() {
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("支付宝支付", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                payfee();
                            }
                        })
                .addSheetItem("微信支付", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                payfee();
                            }
                        }).show();
    }

    private void payfee() {
        RetrofitHttpUtil.getApiService()
                .NewShopFee("", SPUtils.getShopId(context), type, fee).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("支付成功");
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B,"nianfeipayok"));
                            startActivity(StoreIntroductionActivity.class);
                            finish();
                        }
                    }
                });
    }
}
