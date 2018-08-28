package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.JiangJuanBeanAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.JiangJuanBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class LotteryActivity extends BaseActivity {
    @BindView(R.id.recyclerlottery)
    RecyclerView recyclerlottery;
    @BindView(R.id.lotteryrefresh)
    EasyRefreshLayout lotteryrefresh;
    private int LotteryType;
    private Context context;
    private JiangJuanBeanAdapter jiangJuanBeanAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_lottery;
    }

    @Override
    public void initView() {
        showright(true);
        Bundle bundle = getIntent().getExtras();
        LotteryType = bundle.getInt("LotteryType");
        setrightTitle("创建");
        if (LotteryType == 3) {
            setcenterTitle("折扣卷");
        } else if (LotteryType == 4) {
            setcenterTitle("免单卷");
        } else if (LotteryType == 1) {
            setcenterTitle("优惠卷");
        } else if (LotteryType == 2) {
            setcenterTitle("礼品卷");
        }
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();


        finish();
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        Bundle bundle = new Bundle();
        bundle.putInt("LotteryTypeEdit", 0);
        bundle.putInt("LotteryType", LotteryType);
        startActivity(EditLotteryActivity.class, bundle);
    }

    @Override
    public void initData() {
        lotteryrefresh.autoRefresh();
        lotteryrefresh.setLoadMoreModel(LoadModel.NONE);
        lotteryrefresh.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                showlistlottery();
            }
        });
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
                if (event.getData().toString().equals("Prizevolume")) {
                    lotteryrefresh.autoRefresh();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void showlistlottery() {
        RetrofitHttpUtil.getApiService()
                .GetShopVoucherList("", SPUtils.getShopId(context), LotteryType).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s + "奖券");
                if (!s.trim().equals("")) {
                    final List<JiangJuanBean> list = GsonUtil.parseJsonArrayWithGson(s, JiangJuanBean.class);
                    lotteryrefresh.refreshComplete();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerlottery.setLayoutManager(linearLayoutManager);
                    jiangJuanBeanAdapter = new JiangJuanBeanAdapter(R.layout.jiangjunshowitem, list);
                    jiangJuanBeanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if (jiangJuanBeanAdapter.getData().get(position).getRecStatus().equals("1")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("LotteryTypeEdit", 1);
                                bundle.putInt("LotteryType", LotteryType);
                                bundle.putString("LotteryTypeVoucherCode", list.get(position).getVoucherCode());
                                bundle.putString("LotteryTypeIsAddup", list.get(position).getIsAddup());
                                bundle.putString("LotteryTypeLimitedQty", list.get(position).getLimitedQty());
                                bundle.putString("LotteryTypeStartDate", datechange(list.get(position).getStartDate()));
                                bundle.putString("LotteryTypeEndDate", datechange(list.get(position).getEndDate()));
                                bundle.putString("LotteryTypeVoucherQty", list.get(position).getVoucherQty());
                                bundle.putString("LotteryTypeRemark", list.get(position).getRemark());
                                if (LotteryType == 1) {
                                    bundle.putString("LotteryTypeLimitedAmt", list.get(position).getLimitedAmt());
                                    bundle.putString("LotteryTypeVoucherID", list.get(position).getVoucherID());
                                    bundle.putString("LotteryTypeDiscountAmt", list.get(position).getDiscountAmt());
                                    bundle.putString("LotteryTypeGiftAmt", list.get(position).getGiftAmt());
                                    bundle.putString("LotteryTypeGiftID", list.get(position).getProductID());
                                    bundle.putString("LotteryTypeGiftName", list.get(position).getGiftName());
                                } else if (LotteryType == 2) {
                                    bundle.putString("LotteryTypeLimitedAmt", list.get(position).getLimitedAmt());
                                    bundle.putString("LotteryTypeVoucherID", list.get(position).getVoucherID());
                                    bundle.putString("LotteryTypeGiftID", list.get(position).getProductID());
                                    bundle.putString("LotteryTypeGiftName", list.get(position).getGiftName());
                                    bundle.putString("LotteryTypeGiftAmt", list.get(position).getGiftAmt());
                                } else if (LotteryType == 3) {
                                    bundle.putString("LotteryTypeGiftID", list.get(position).getProductID());
                                    bundle.putString("LotteryTypeDiscountRate", list.get(position).getDiscountRate());
                                    bundle.putString("LotteryTypeVoucherID", list.get(position).getVoucherID());
                                    bundle.putString("LotteryTypeLimitedAmt", list.get(position).getLimitedAmt());
                                } else if (LotteryType == 4) {
                                    bundle.putString("LotteryTypeVoucherID", list.get(position).getVoucherID());
                                    bundle.putString("LotteryTypeFreeAmt", list.get(position).getFreeAmt());

                                }
                                startActivity(EditLotteryActivity.class, bundle);
                                LogUtils.d("编号：" + list.get(position).getVoucherCode());
                                LogUtils.d("累计：" + list.get(position).getIsAddup());
                                LogUtils.d("限领：" + list.get(position).getLimitedQty());
                                LogUtils.d("开始时间：" + datechange(list.get(position).getStartDate()));
                                LogUtils.d("结束时间：" + datechange(list.get(position).getEndDate()));
                                LogUtils.d("数量：" + list.get(position).getVoucherQty());
                                LogUtils.d("Remark：" + list.get(position).getRemark());
                            }
                        }
                    });
                    jiangJuanBeanAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                            switch (view.getId()) {
                                case R.id.fabu:
                                    showAlertDialog("确认发布吗", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            PublicShopVoucher(Long.valueOf(list.get(position).getVoucherID()));
                                        }
                                    });
//                                    LogUtils.d("数值是");
                                    break;
                                case R.id.guoqi:
                                    showAlertDialog("确认过期吗", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SetShopVoucherInvalid(Long.valueOf(list.get(position).getVoucherID()));
                                        }
                                    });
                                    LogUtils.d("test");
                                    break;

                            }
                        }
                    });
                    recyclerlottery.setAdapter(jiangJuanBeanAdapter);

                } else {
                    lotteryrefresh.refreshComplete();
                    showToast("没有更多数据");
                }
            }
        });
    }

    private String datechange(String date) {
        return date.substring(0, date.indexOf(" ")).replaceAll("/", "-");
    }

    private void SetShopVoucherInvalid(Long VoucherID) {
        RetrofitHttpUtil.getApiService().SetShopVoucherInvalid("", VoucherID).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().trim().equals("1")) {
                            lotteryrefresh.autoRefresh();
                            showToast("已设置过期");
                        } else if (bean.getResult().trim().equals("0")) {
                            showToast("已设置过期");
                        }
                    }
                });

    }

    private void PublicShopVoucher(Long VoucherID) {
        RetrofitHttpUtil.getApiService().PublicShopVoucher("", SPUtils.getShopId(context), VoucherID).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().trim().equals("1")) {
                            showToast("发布成功");
                            lotteryrefresh.autoRefresh();
                        } else if (bean.getResult().trim().equals("0")) {
                            showToast("当前店铺的当前代金券不存在");
                        } else if (bean.getResult().trim().equals("2")) {
                            showToast("当前代金券不在待发布状态");
                        } else if (bean.getResult().trim().equals("3")) {
                            showToast("代金券已过期");
                        }
                    }
                });
    }
}
