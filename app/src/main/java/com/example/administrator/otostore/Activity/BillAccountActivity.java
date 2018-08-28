package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Adapter.JifenAdapter;
import com.example.administrator.otostore.Bean.JIfenBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillAccountActivity extends BaseActivity {
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    private int type = 1;
    private String yearselft = "";
    private String monthselft = "";
    private SimpleDateFormat sdf;
    private Calendar ca;
    private Date resultDate;
    private Context context;
    private JifenAdapter jifenAdapter;
    private int add = 1;

    private int RowsPerPage = 5;

    @Override
    public int getContentViewResId() {
        context = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("QianbaoType").equals("1")) {
            type = 1;
        } else if (bundle.getString("QianbaoType").equals("2")) {
            type = 2;
        } else if (bundle.getString("QianbaoType").equals("3")) {
            type = 3;
        } else if (bundle.getString("QianbaoType").equals("4")) {
            type = 4;
        } else if (bundle.getString("QianbaoType").equals("5")) {
            type = 5;
        } else if (bundle.getString("QianbaoType").equals("6")) {
            type = 6;
        }
        return R.layout.activity_bill_account;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize(true);
        jifenAdapter = new JifenAdapter();
        recycler.setAdapter(jifenAdapter);
        ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date());

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());
        LogUtils.d("日期是：" + date);
        yearselft = date.substring(0, date.indexOf("-"));
        monthselft = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        year.setText(yearselft);
        month.setText(monthselft);

        if (type == 1) {
            setcenterTitle("消费月账单");
        } else if (type == 2) {
            setcenterTitle("积分转账月账单");
        } else if (type == 3) {
            setcenterTitle("积分兑换月账单");
        } else if (type == 4) {
            setcenterTitle("消费月账单");
        } else if (type == 5) {
            setcenterTitle("消费月账单");
        } else if (type == 6) {
            setcenterTitle("消费月账单");
        }
        setfrash(date);
//        GetUserSettledDetial(date, 10, 1);
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

    }

    private void setfrash(final String date) {
        final String startdate = date.substring(0, date.lastIndexOf("-")) + "-01";
        easylayout.autoRefresh();
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                if (type == 1) {
                    GetUserBonusInoutList(startdate, RowsPerPage, add);
                } else if (type == 2) {
                    GetBonusTransferList(startdate, 0, RowsPerPage, add);
                } else if (type == 3) {
                    GetUserIntegralExchList(startdate, RowsPerPage, add);
                } else if (type == 4) {
                    GetUserBalanceInoutList(startdate, RowsPerPage, add);
                } else if (type == 5) {
                    GetBalanceTransferList(0, startdate, RowsPerPage, add);
                } else if (type == 6) {
                    GetWithDrawBillList(startdate, RowsPerPage, add);
                }
            }

            @Override
            public void onRefreshing() {
                add = 1;
                if (type == 1) {
                    GetUserBonusInoutList(startdate, RowsPerPage, add);
                } else if (type == 2) {
                    GetBonusTransferList(startdate, 0, RowsPerPage, add);
                } else if (type == 3) {
                    GetUserIntegralExchList(startdate, RowsPerPage, add);
                } else if (type == 4) {
                    GetUserBalanceInoutList(startdate, RowsPerPage, add);
                } else if (type == 5) {
                    GetBalanceTransferList(0, startdate, RowsPerPage, add);

                } else if (type == 6) {
                    GetWithDrawBillList(startdate, RowsPerPage, add);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                ca.add(Calendar.MONTH, -1);
                resultDate = ca.getTime();
                String date = sdf.format(resultDate);
                jifenAdapter.setNewData(null);
                setfrash(date);
                yearselft = date.substring(0, date.indexOf("-"));
                monthselft = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
                year.setText(yearselft);
                month.setText(monthselft);
                LogUtils.d(sdf.format(resultDate));
                break;
            case R.id.right:
                ca.add(Calendar.MONTH, 1);
                resultDate = ca.getTime();
                date = sdf.format(resultDate);
                jifenAdapter.setNewData(null);
                setfrash(date);
                yearselft = date.substring(0, date.indexOf("-"));
                monthselft = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
                year.setText(yearselft);
                month.setText(monthselft);
                LogUtils.d(sdf.format(resultDate));
                break;
        }
    }


    /*第一个，以结算，待结算
    * */
    private void GetUserSettledDetial(String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetUserSettledDetial("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (!s.equals("")) {
                            LogUtils.d(s);
                        }
                    }
                });
    }

    private void GetWithDrawBillList(String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetWithDrawBillList("", Long.valueOf(SPUtils.getWalletId(context)),
                        YearMonth, RowsPerPage, PageNum
                ).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    private void GetBalanceTransferList(int InOutType, String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService().
                GetBalanceTransferList("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, InOutType, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("[]")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    /*积分转账月账单
    * */
    private void GetBonusTransferList(String YearMonth, int InOutType, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetBonusTransferList("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, InOutType, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("[]")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    private void GetUserBalanceInoutList(String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetUserBalanceInoutList("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    /*
    * 积分兑换月账单
    * */
    private void GetUserIntegralExchList(String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetUserIntegralExchList("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    /*获取结算货款明细
    * */
    private void GetSettlePaymentDetailForGoods(int aSettleType, String StartDate, String EndDate, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetSettlePaymentDetailForGoods("", Long.valueOf(SPUtils.getWalletId(context)), aSettleType, StartDate, EndDate, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                    }
                });
    }

    private void GetUserBonusInoutList(String YearMonth, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService().
                GetUserBonusInoutList("", Long.valueOf(SPUtils.getWalletId(context)), YearMonth, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<JIfenBean> beans = GsonUtil.parseJsonArrayWithGson(s, JIfenBean.class);
                            if (add == 1) {
                                add++;
                                jifenAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = jifenAdapter.getData().size();
                                jifenAdapter.addData(beans);
                                jifenAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }
}
