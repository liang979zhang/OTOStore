package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.ItemProductTypeBeanAdapter;
import com.example.administrator.otostore.Adapter.ProductListBeanAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ProductListBean;
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

public class ProductControlActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.recyclerviewright)
    RecyclerView recyclerviewright;
    private Context context;
    private ItemProductTypeBeanAdapter itemProductTypeBeanAdapter;
    private ProductListBeanAdapter listBeanAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_product_control;
    }

    @Override
    public void initView() {
        setcenterTitle("产品管理");
        showright(true);
        setrightTitle("新增");
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
        bundle.putString("producttype", "0");
        startActivity(LaunchProductActivity.class, bundle);
    }

    @Override
    public void initData() {
        easylayout.autoRefresh();
        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                showtype();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void showtype() {
        RetrofitHttpUtil.getApiService()
                .GetShopCategory("", SPUtils.getShopId(context), "10000000").compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("数值是" + s);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                recyclerview.setLayoutManager(gridLayoutManager);

                final List<CategroyBean> lista = GsonUtil.parseJsonArrayWithGson(s, CategroyBean.class);
                easylayout.refreshComplete();
                itemProductTypeBeanAdapter = new ItemProductTypeBeanAdapter(R.layout.item_product_type, lista);
                showproduct(lista.get(0).getCatCode());
                itemProductTypeBeanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        showproduct(lista.get(position).getCatCode());
                    }
                });
                recyclerview.setAdapter(itemProductTypeBeanAdapter);
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
                if (event.getData().toString().equals("producttype")) {
                    easylayout.autoRefresh();
                }
                break;
            default:
        }
    }

    private void showproduct(String CatCode) {
        RetrofitHttpUtil.getApiService()
                .GetProductList("", SPUtils.getShopId(context), CatCode)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                if (!s.equals("")) {
                    final List<ProductListBean> listBeans = GsonUtil.parseJsonArrayWithGson(s, ProductListBean.class);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerviewright.setLayoutManager(linearLayoutManager);
                    listBeanAdapter = new ProductListBeanAdapter(R.layout.item_product_select, listBeans);
                    listBeanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("producttype", "1");
                            bundle.putString("producttypeProductID", listBeans.get(position).getProductID());
                            bundle.putString("producttypeCatID", listBeans.get(position).getCatID());
                            bundle.putString("producttypeProductCode", listBeans.get(position).getProductCode());
                            bundle.putString("producttypeProductName", listBeans.get(position).getProductName());
                            bundle.putString("producttypeValidDays", listBeans.get(position).getValidDays());
                            bundle.putString("producttypeUnit", listBeans.get(position).getUnit());
                            bundle.putString("producttypeSupportVoucher", listBeans.get(position).getSupportVoucher());
                            bundle.putString("producttypeSupportDispatch", listBeans.get(position).getSupportDispatch());
                            bundle.putString("producttypeRemark", listBeans.get(position).getRemark());

                            startActivity(LaunchProductActivity.class, bundle);
                        }
                    });
                    recyclerviewright.setAdapter(listBeanAdapter);
                }

            }
        });

    }
}
