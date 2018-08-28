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
import com.example.administrator.otostore.Adapter.DetailFourth;
import com.example.administrator.otostore.Bean.DetailBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFourthActivity extends BaseActivity {

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
    private Context context;
    private String yearselft = "";
    private String monthselft = "";
    private SimpleDateFormat sdf;
    private Calendar ca;
    private Date resultDate;
    private int add = 1;
    private int RowsPerPage = 5;
    private DetailFourth fourth;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_detail_fourth;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize(true);
        fourth = new DetailFourth();
        recycler.setAdapter(fourth);
        ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date());

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String date = sdf.format(new Date());
        LogUtils.d("日期是：" + date);
        yearselft = date.substring(0, date.indexOf("-"));
        monthselft = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        year.setText(yearselft);
        month.setText(monthselft);
        setfrash(date);
        setcenterTitle("贷款明细");
    }

    private void setfrash(final String date) {
        final String endtime = date.substring(0, date.lastIndexOf("-")) + "-01";
        String startdate = date.substring(0, date.lastIndexOf("-")) + "-01";
        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = null;
        try {
            sDate = sdff.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("String类型转Date类型 " + sDate);//要实现日期+1 需要String转成Date类型

        Format f = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Date结束日期:" + f.format(sDate));

        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        c.add(Calendar.MONTH, 1);           //利用Calendar 实现 Date日期+1天

        sDate = c.getTime();
        System.out.println("Date结束日期+1 " + f.format(sDate));

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        startdate = sdf1.format(sDate);
        System.out.println("Date类型转String类型  " + startdate);
        final String timeseco = startdate;

        easylayout.autoRefresh();
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                GetSettlePaymentDetailForGoods(endtime, timeseco, 0, RowsPerPage, add);
            }

            @Override
            public void onRefreshing() {
                add = 1;
                GetSettlePaymentDetailForGoods(endtime, timeseco, 0, RowsPerPage, add);
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

    @OnClick({R.id.left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                ca.add(Calendar.MONTH, -1);
                resultDate = ca.getTime();
                String date = sdf.format(resultDate);
                fourth.setNewData(null);
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
                fourth.setNewData(null);
                setfrash(date);
                yearselft = date.substring(0, date.indexOf("-"));
                monthselft = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
                year.setText(yearselft);
                month.setText(monthselft);
                LogUtils.d(sdf.format(resultDate));
                break;
        }
    }

    private void GetSettlePaymentDetailForGoods(String startdate, String enddate, int aSettleType, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetSettlePaymentDetailForGoods("", Long.valueOf(SPUtils.getWalletId(context)), aSettleType, startdate, enddate, RowsPerPage, PageNum).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<DetailBean> beans = GsonUtil.parseJsonArrayWithGson(s, DetailBean.class);
                            if (add == 1) {
                                add++;
                                fourth.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = fourth.getData().size();
                                fourth.addData(beans);
                                fourth.notifyDataSetChanged();
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
