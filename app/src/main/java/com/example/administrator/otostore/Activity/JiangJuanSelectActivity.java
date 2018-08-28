package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.otostore.Bean.ProductListBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class JiangJuanSelectActivity extends BaseActivity {
    @BindView(R.id.left)
    RecyclerView recyclerview;
    @BindView(R.id.right)
    RecyclerView recyclerviewright;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    private Context context;
    private ItemProductTypeBeanAdapter itemProductTypeBeanAdapter;
    private ProductListBeanAdapter listBeanAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_jiang_juan_select;
    }

    @Override
    public void initView() {
        setcenterTitle("选择商品");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
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
                final List<ProductListBean> listBeans = GsonUtil.parseJsonArrayWithGson(s, ProductListBean.class);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerviewright.setLayoutManager(linearLayoutManager);
                listBeanAdapter = new ProductListBeanAdapter(R.layout.item_product_select, listBeans);
                listBeanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        LogUtils.d(listBeans.get(position).getProductName() + listBeans.get(position).getProductID());
                        Bundle bundle = new Bundle();
                        bundle.putString("Jiangjuanname", listBeans.get(position).getProductName());
                        bundle.putString("Jiangjuannum", listBeans.get(position).getProductID());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(Constants.JiangJuanSelectResult, intent);
                        finish();
                    }
                });
                recyclerviewright.setAdapter(listBeanAdapter);

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
