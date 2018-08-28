package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.HomePagerRecyclerviewFirstAdapter;
import com.example.administrator.otostore.Adapter.OrdersAdapter;
import com.example.administrator.otostore.Adapter.ShopRefundBillsAdapter;
import com.example.administrator.otostore.Bean.HomePagerBean;
import com.example.administrator.otostore.Bean.JIfenBean;
import com.example.administrator.otostore.Bean.OrdersShowBean;
import com.example.administrator.otostore.Bean.ShopRefundBillsBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.DividerGridItemDecoration;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeiSongActivity extends BaseActivity {

    @BindView(R.id.recyclerdaipeiosng)
    RecyclerView recyclerdaipeiosng;
    @BindView(R.id.peisonglist)
    RecyclerView peisonglist;
    @BindView(R.id.refreshlayout)
    EasyRefreshLayout easylayout;
    private Context context;
    private int add = 1;
    private int clikbutton = 0;
    private int RowsPerPage = 5;
    private OrdersAdapter ordersAdapter;
    private ShopRefundBillsAdapter shopRefundBillsAdapter;
    private static final String[] TITLE = {
            "待配送",
            "配送中",
            "完成交易",
            "结算中",
            "交易成功",
            "关闭交易",
            "退款中",
            "退款完成"};
    private static final int[] IMG = {R.drawable.daipeisong,
            R.drawable.peisongzhong,
            R.drawable.wanchengdingdan,
            R.drawable.jiesuanzhong, R.drawable.jiaoyiwancheng, R.drawable.jiaoyigunabi, R.drawable.tuikuanzhong, R.drawable.tuikuanwancheng};

    private static final int[] IMGTest = {R.drawable.daifeisongb,
            R.drawable.peisongzzhongb,
            R.drawable.wanchengdingdanb,
            R.drawable.jiesuanzhongb, R.drawable.jiaoyiwanchengb, R.drawable.guanbijiaoyib, R.drawable.tuikuanzhongb, R.drawable.tuikuanwanchengb};
    private ArrayList<HomePagerBean> homePagerRecyclerviewFirstBeanArrayList;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_pei_song;
    }

    @Override
    public void initView() {
        setcenterTitle("配送订单");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerdaipeiosng.setLayoutManager(new GridLayoutManager(context, 4));
        peisonglist.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

        homePagerRecyclerviewFirstBeanArrayList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomePagerBean item = new HomePagerBean();
            item.setTitle(TITLE[i]);
            if (i == 0) {
                item.setImageResource(IMG[i]);
            } else {
                item.setImageResource(IMGTest[i]);
            }
            homePagerRecyclerviewFirstBeanArrayList.add(item);
        }

        setfresh(0);

        final HomePagerRecyclerviewFirstAdapter homePagerRecyclerviewFirstAdapter = new HomePagerRecyclerviewFirstAdapter(R.layout.itemhomeapagerrecyclerfirst, homePagerRecyclerviewFirstBeanArrayList);

        homePagerRecyclerviewFirstAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d(clikbutton + "数据是：" + position);
                homePagerRecyclerviewFirstBeanArrayList.get(clikbutton).setImageResource(IMGTest[clikbutton]);
                homePagerRecyclerviewFirstAdapter.notifyItemChanged(clikbutton);
                homePagerRecyclerviewFirstBeanArrayList.get(position).setImageResource(IMG[position]);
                homePagerRecyclerviewFirstAdapter.notifyItemChanged(position);
                clikbutton = position;
                setfresh(position);
//                if (position == 0) {
//                    showOrders(2, "2", 10, 1);
//                } else if (position == 1) {
//                    showOrders(2, "3", 10, 1);
//                } else if (position == 2) {
//                    showOrders(2, "6", 10, 1);
//
//                } else if (position == 3) {
//                    showOrders(2, "5", 10, 1);
//
//                } else if (position == 4) {
//                    showOrders(2, "4", 10, 1);
//
//                } else if (position == 5) {
//                    showOrders(2, "9", 10, 1);
//
//                } else if (position == 6) {
//                    showOrders(2, "7", 10, 1);
//
//                } else if (position == 7) {
//                    showOrders(2, "8", 10, 1);
//                }
            }
        });
        homePagerRecyclerviewFirstAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerdaipeiosng.addItemDecoration(new DividerGridItemDecoration(context));
        recyclerdaipeiosng.setAdapter(homePagerRecyclerviewFirstAdapter);

    }

    private void setfresh(final int a) {
        if (a == 6) {
            ordersAdapter = new OrdersAdapter();
            ordersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.orders_add:
                            Bundle bundle=new Bundle();
                            bundle.putString("ordersId",ordersAdapter.getData().get(position).getOrderID());
                            startActivity(FaHuoDanActivity.class,bundle);
                            break;
                        case R.id.orders_delay:
                            Bundle bundle2=new Bundle();
                            bundle2.putString("OdersType","2");
                            bundle2.putString("ordersId",ordersAdapter.getData().get(position).getOrderID());
                            startActivity(OrdersTypeActivity.class,bundle2);
                            break;
                    }
                }
            });
            peisonglist.setAdapter(ordersAdapter);
        } else {
            shopRefundBillsAdapter = new ShopRefundBillsAdapter();
            shopRefundBillsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.tongyi:
                            Bundle bundle=new Bundle();
                            bundle.putString("OdersType","7");
                            bundle.putString("ordersId",shopRefundBillsAdapter.getData().get(position).getRefundBillID());
                            startActivity(OrdersTypeActivity.class,bundle);
                            LogUtils.d(11111111);
                            break;
                        case R.id.jujue:
                            Bundle bundle1=new Bundle();
                            bundle1.putString("OdersType","8");
                            bundle1.putString("ordersId",shopRefundBillsAdapter.getData().get(position).getRefundBillID());
                            startActivity(OrdersTypeActivity.class,bundle1);
                            LogUtils.d(11111111);
                            break;
                        case R.id.tuikuan:
                            Bundle bundle2=new Bundle();
                            bundle2.putString("OdersType","9");
                            bundle2.putString("ordersId",shopRefundBillsAdapter.getData().get(position).getRefundBillID());
                            startActivity(OrdersTypeActivity.class,bundle2);
                            LogUtils.d(11111111);
                            break;
                    }
                }
            });
            peisonglist.setAdapter(shopRefundBillsAdapter);
        }
        easylayout.autoRefresh();
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                if (a == 0) {
                    showOrders(2, "2", RowsPerPage, add);
                } else if (a == 1) {
                    showOrders(2, "3", RowsPerPage, add);
                } else if (a == 2) {
                    showOrders(2, "6", RowsPerPage, add);
                } else if (a == 3) {
                    showOrders(2, "5", RowsPerPage, add);
                } else if (a == 4) {
                    showOrders(2, "4", RowsPerPage, add);
                } else if (a == 5) {
                    showOrders(2, "9", RowsPerPage, add);
                } else if (a == 6) {
                    GetShopRefundBills(0, RowsPerPage, add);
                } else if (a == 7) {
                    showOrders(2, "8", RowsPerPage, add);
                }
            }

            @Override
            public void onRefreshing() {
                add = 1;
                if (a == 0) {
                    showOrders(2, "2", RowsPerPage, add);
                } else if (a == 1) {
                    showOrders(2, "3", RowsPerPage, add);
                } else if (a == 2) {
                    showOrders(2, "6", RowsPerPage, add);
                } else if (a == 3) {
                    showOrders(2, "5", RowsPerPage, add);
                } else if (a == 4) {
                    showOrders(2, "4", RowsPerPage, add);
                } else if (a == 5) {
                    showOrders(2, "9", RowsPerPage, add);
                } else if (a == 6) {
                    GetShopRefundBills(0, RowsPerPage, add);
                } else if (a == 7) {
                    showOrders(2, "8", RowsPerPage, add);
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

    private void showOrders(int OrderType, String OrderStatusLst, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetShopOrders("", SPUtils.getShopId(context), OrderType,3, OrderStatusLst, RowsPerPage, PageNum).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {

                        if (!s.equals("")) {
                            LogUtils.d(s + "配送");
                            List<OrdersShowBean> beans = GsonUtil.parseJsonArrayWithGson(s, OrdersShowBean.class);
                            if (add == 1) {
                                add++;
                                ordersAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = ordersAdapter.getData().size();
                                ordersAdapter.addData(beans);
                                ordersAdapter.notifyDataSetChanged();
                                peisonglist.scrollToPosition(postion);
                            }
                        } else {
                            easylayout.refreshComplete();
                            easylayout.closeLoadView();
                            showToast("没有更多数据");

                        }
                    }
                });
    }

    private void GetShopRefundBills(int RefundStatus, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetShopRefundBills("", Long.valueOf(SPUtils.getShopId(context)), RefundStatus, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            LogUtils.d(s + "配送");
                            List<ShopRefundBillsBean> beans = GsonUtil.parseJsonArrayWithGson(s, ShopRefundBillsBean.class);
                            if (add == 1) {
                                add++;
                                shopRefundBillsAdapter.setNewData(beans);
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                easylayout.closeLoadView();
                                int postion = shopRefundBillsAdapter.getData().size();
                                shopRefundBillsAdapter.addData(beans);
                                shopRefundBillsAdapter.notifyDataSetChanged();
                                peisonglist.scrollToPosition(postion);
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
