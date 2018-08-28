package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.LiPinJuanBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import org.feezu.liuli.timeselector.TimeSelector;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditLotteryActivity extends BaseActivity {

    @BindView(R.id.bianhao)
    EditText bianhao;
    @BindView(R.id.man)
    EditText man;
    @BindView(R.id.jian)
    EditText jian;
    @BindView(R.id.lipin)
    TextView lipin;
    @BindView(R.id.lipinvalue)
    EditText lipinvalue;
    @BindView(R.id.leiji)
    CheckBox leiji;
    @BindView(R.id.xianling)
    EditText xianling;
    @BindView(R.id.starttime)
    TextView starttime;
    @BindView(R.id.endtime)
    TextView endtime;
    @BindView(R.id.num)
    EditText num;
    @BindView(R.id.reamrk)
    EditText reamrk;
    @BindView(R.id.yes)
    Button yes;
    @BindView(R.id.showjian)
    LinearLayout showjian;
    @BindView(R.id.showlipin)
    LinearLayout showlipin;
    @BindView(R.id.showjiazhi)
    LinearLayout showjiazhi;
    @BindView(R.id.zhekou)
    EditText zhekou;
    @BindView(R.id.showzhekou)
    LinearLayout showzhekou;
    @BindView(R.id.showman)
    LinearLayout showman;
    @BindView(R.id.testshowjian)
    TextView testshowjian;
    private int LotteryType;
    private Long VoucherID = Long.valueOf("0");
    private Context context;
    private int VoucherType;
    private String VoucherCode;
    private String VoucherName = "";
    private int VoucherQty;
    private int IsAddUp;
    private int LimitedQty;
    private String StartDate = "";
    private String EndDate = "";
    private BigDecimal LimitedAmt;
    private BigDecimal DiscountAmt;
    private long GiftID;
    private Long ProductID = Long.valueOf("0");
    private BigDecimal GiftAmt = BigDecimal.valueOf(0.0);
    private BigDecimal DiscountRate = BigDecimal.valueOf(0.0);
    private BigDecimal FreeAmt = BigDecimal.valueOf(0.0);
    private String Remark;
    private PopupWindow popupWindow;
    private int addorupdate = 0;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_edit_lottery;
    }

    @Override
    public void initView() {
        context = this;
        Bundle bundle = getIntent().getExtras();
        LotteryType = bundle.getInt("LotteryType", 0);
        addorupdate = bundle.getInt("LotteryTypeEdit", 0);
        VoucherType = LotteryType;
        if (LotteryType == 1) {
            setcenterTitle("优惠卷");
        } else if (LotteryType == 2) {
            showjian.setVisibility(View.GONE);
            lipin.setHint("请选择礼品");
            setcenterTitle("礼品卷");
            lipinvalue.setHint("请输入礼品价值");
        } else if (LotteryType == 3) {
            setcenterTitle("折扣卷");
            showzhekou.setVisibility(View.VISIBLE);
            showjian.setVisibility(View.GONE);
            showlipin.setVisibility(View.GONE);
            showjiazhi.setVisibility(View.GONE);


        } else if (LotteryType == 4) {
            setcenterTitle("免单卷");
            showman.setVisibility(View.GONE);
            showlipin.setVisibility(View.GONE);
            showjiazhi.setVisibility(View.GONE);
            testshowjian.setText("免单");
            jian.setHint("请输入免单金额 单位 元");
        }
        if (addorupdate == 1) {

            bianhao.setText(bundle.getString("LotteryTypeVoucherCode"));
            bianhao.setFocusable(false);
            if (Boolean.valueOf(bundle.getString("LotteryTypeIsAddup"))) {
                leiji.setChecked(true);
            } else {
                leiji.setChecked(false);
            }
            LogUtils.d("数值是" + bundle.getString("LotteryTypeIsAddup"));
            xianling.setText(bundle.getString("LotteryTypeLimitedQty"));
            starttime.setText(bundle.getString("LotteryTypeStartDate"));
            endtime.setText(bundle.getString("LotteryTypeEndDate"));
            num.setText(bundle.getString("LotteryTypeVoucherQty"));
            reamrk.setText(bundle.getString("LotteryTypeRemark"));
            if (LotteryType == 1) {
                man.setText(bundle.getString("LotteryTypeLimitedAmt"));
                jian.setText(bundle.getString("LotteryTypeDiscountAmt"));
                lipinvalue.setText(bundle.getString("LotteryTypeGiftAmt"));
                lipin.setText(bundle.getString("LotteryTypeGiftName"));
                ProductID = Long.valueOf(bundle.getString("LotteryTypeGiftID"));
                VoucherID = Long.valueOf(bundle.getString("LotteryTypeVoucherID"));
            } else if (LotteryType == 2) {
                man.setText(bundle.getString("LotteryTypeLimitedAmt"));
                lipinvalue.setText(bundle.getString("LotteryTypeGiftAmt"));
                lipin.setText(bundle.getString("LotteryTypeGiftName"));
                ProductID = Long.valueOf(bundle.getString("LotteryTypeGiftID"));
                VoucherID = Long.valueOf(bundle.getString("LotteryTypeVoucherID"));

            } else if (LotteryType == 3) {
                zhekou.setText(bundle.getString("LotteryTypeDiscountRate"));
                ProductID = Long.valueOf(bundle.getString("LotteryTypeGiftID"));
                VoucherID = Long.valueOf(bundle.getString("LotteryTypeVoucherID"));
                man.setText(bundle.getString("LotteryTypeLimitedAmt"));
            } else if (LotteryType == 4) {
                VoucherID = Long.valueOf(bundle.getString("LotteryTypeVoucherID"));
                jian.setText(bundle.getString("LotteryTypeFreeAmt"));
            }
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

    private void editshopvoucher(long VoucherID, int VoucherType, String VoucherCode, String VoucherName, int VoucherQty, int IsAddUp, int LimitedQty,
                                 String StartDate, String EndDate, BigDecimal LimitedAmt, BigDecimal DiscountAmt, long GiftID, BigDecimal GiftAmt, BigDecimal DiscountRate, BigDecimal FreeAmt, String Remark) {
        RetrofitHttpUtil.getApiService()
                .SaveShopVoucher("", VoucherID, SPUtils.getShopId(context), VoucherType, VoucherCode, VoucherName, VoucherQty, IsAddUp, LimitedQty, StartDate, EndDate, LimitedAmt, DiscountAmt, GiftID, GiftAmt, DiscountRate, FreeAmt, Remark).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LiPinJuanBean liPinJuanBean = GsonUtil.parseJsonWithGson(s, LiPinJuanBean.class);
                        if (liPinJuanBean.getResult().equals("1")) {
                            if (addorupdate == 0) {
                                showToast("创建成功");
                            } else if (addorupdate == 1) {
                                showToast("修改成功");
                            }
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B, "Prizevolume"));
                            finish();
                        } else if (liPinJuanBean.getResult().equals("2")) {
                            showAlertDialog("店铺内存在同代码代金券", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        } else if (liPinJuanBean.getResult().equals("0")) {
                            showAlertDialog("店铺不存在", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                        LogUtils.d(s);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.starttime, R.id.endtime, R.id.yes, R.id.lipin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lipin:
                startActivityForResult(new Intent(context, JiangJuanSelectActivity.class), Constants.JiangJuanSelectRequest);
                break;
            case R.id.starttime:
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String atime = String.valueOf(df.format(new Date()));
                TimeSelector timeSelector = new TimeSelector(context, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        LogUtils.d("" + time);
                        StartDate = time.substring(0, time.indexOf(" "));
                        starttime.setText(StartDate);
                    }
                }, atime, "2035-6-30 10:20");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                break;
            case R.id.endtime:
                SimpleDateFormat dfb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String btime = String.valueOf(dfb.format(new Date()));
                TimeSelector timeSelectora = new TimeSelector(context, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        LogUtils.d("" + time);
                        EndDate = time.substring(0, time.indexOf(" "));
                        endtime.setText(EndDate);
                    }
                }, btime, "2035-6-30 15:20");
                timeSelectora.setMode(TimeSelector.MODE.YMD);
                timeSelectora.show();
                break;
            case R.id.yes:
                if (isempty(bianhao)) {
                    LogUtils.d("数据是:" + "if");
                    showToast("请输入编号");
                } else if (isempty(xianling)) {
                    showToast("请输入限领数量");
                } else if (isempty(num)) {
                    showToast("请输入数量");
                } else if (starttime.getText().toString().trim().isEmpty()) {
                    showToast("请选择开始时间");
                } else if (endtime.getText().toString().trim().isEmpty()) {
                    showToast("请选择结束时间");
                } else if (LotteryType == 1 && (isempty(man)) || LotteryType == 2 && (isempty(man)) || LotteryType == 3 && (isempty(man))) {
                    showToast("请输入满金额");
                } else if (LotteryType == 1 && (isempty(jian))) {
                    showToast("请输入减金额");

                } else if (LotteryType == 2 && lipin.getText().toString().trim().isEmpty()) {
                    showToast("请选择礼品");

                } else if (LotteryType == 3 && isempty(zhekou)) {
                    showToast("请输入折扣");
                } else if (LotteryType == 4 && isempty(jian)) {
                    showToast("请输入免单金额");
                } else {
                    LogUtils.d("数据是:" + "else");
                    VoucherCode = editreturn(bianhao);
                    VoucherQty = Integer.valueOf(editreturn(num));
                    IsAddUp = leiji.isChecked() ? 1 : 0;
                    LimitedQty = Integer.valueOf(editreturn(xianling));
                    if (LotteryType == 2 && !isempty(lipinvalue)) {
                        DiscountAmt = BigDecimal.valueOf(0.0);
                        GiftAmt = BigDecimal.valueOf(Double.valueOf(editreturn(lipinvalue)));
                        LimitedAmt = BigDecimal.valueOf(Double.valueOf(editreturn(man)));
                    } else if (LotteryType == 2 && isempty(lipinvalue)) {
                        DiscountAmt = BigDecimal.valueOf(0.0);
                        GiftAmt = BigDecimal.valueOf(0.0);
                        LimitedAmt = BigDecimal.valueOf(Double.valueOf(editreturn(man)));
                    } else if (LotteryType == 1 && !isempty(lipinvalue)) {
                        DiscountAmt = BigDecimal.valueOf(Double.valueOf(editreturn(jian)));
                        GiftAmt = BigDecimal.valueOf(Double.valueOf(editreturn(lipinvalue)));
                        LimitedAmt = BigDecimal.valueOf(Double.valueOf(editreturn(man)));
                    } else if (LotteryType == 1 && isempty(lipinvalue)) {
                        DiscountAmt = BigDecimal.valueOf(Double.valueOf(editreturn(jian)));
                        GiftAmt = BigDecimal.valueOf(0.0);
                        LimitedAmt = BigDecimal.valueOf(Double.valueOf(editreturn(man)));
                    } else if (LotteryType == 3) {
                        DiscountAmt = BigDecimal.valueOf(0.0);
                        DiscountRate = BigDecimal.valueOf(Double.valueOf(editreturn(zhekou)));
                        GiftAmt = BigDecimal.valueOf(0.0);
                        LimitedAmt = BigDecimal.valueOf(Double.valueOf(editreturn(man)));
                    } else if (LotteryType == 4) {
                        DiscountAmt = BigDecimal.valueOf(0.0);
                        GiftAmt = BigDecimal.valueOf(0.0);
                        LimitedAmt = BigDecimal.valueOf(0.0);
                        FreeAmt = BigDecimal.valueOf(Double.valueOf(editreturn(jian)));
                    }
                    Remark = editreturn(reamrk);
                    GiftID = ProductID;
                    editshopvoucher(VoucherID, VoucherType, VoucherCode, VoucherName, VoucherQty, IsAddUp, LimitedQty, StartDate, EndDate, LimitedAmt, DiscountAmt, GiftID, GiftAmt, DiscountRate, FreeAmt, Remark);
                }
                break;
        }
    }

    private boolean isempty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.JiangJuanSelectRequest && resultCode == Constants.JiangJuanSelectResult) {
            Bundle bundle = data.getExtras();
            ProductID = Long.valueOf(bundle.getString("Jiangjuannum"));
            lipin.setText(bundle.getString("Jiangjuanname"));
            VoucherName = bundle.getString("Jiangjuanname");

        }
    }

    private String editreturn(EditText editText) {
        return editText.getText().toString().trim();
    }

}
