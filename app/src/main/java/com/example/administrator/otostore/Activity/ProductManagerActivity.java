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
import com.example.administrator.otostore.Adapter.ItemProcductAdapter;
import com.example.administrator.otostore.Adapter.ItemProductTypeBeanAdapter;
import com.example.administrator.otostore.Adapter.ProductListBeanAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.EditShopResultBean;
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

public class ProductManagerActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.recyclerviewright)
    RecyclerView recyclerviewright;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    private Context context;
    private ItemProductTypeBeanAdapter itemProductTypeBeanAdapter;
    private ItemProcductAdapter listBeanAdapter;
    private int type;

    @Override
    public int getContentViewResId() {

        context = this;
        return R.layout.activity_product_manager;
    }

    @Override
    public void initView() {
        setcenterTitle("产品管理");
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
                if (!s.equals("[]")) {
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
                } else {
                    easylayout.refreshComplete();
                    showToast("没有更多数据");
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
                    listBeanAdapter = new ItemProcductAdapter(R.layout.item_product_fabu, listBeans);
                    listBeanAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()) {

                                case R.id.fabuok:
                                    LogUtils.d("11111111");
                                    type = 1;
                                    setproduonoff(Long.valueOf(listBeans.get(position).getProductID()), 2);
                                    break;
                                case R.id.xiajiaok:
                                    type = 2;
                                    LogUtils.d("22222222222");
                                    setproduonoff(Long.valueOf(listBeans.get(position).getProductID()), 3);
                                    break;
                            }
                        }
                    });
                    recyclerviewright.setAdapter(listBeanAdapter);
                }

            }
        });

    }

    private void setproduonoff(long ProductID, int a) {
        RetrofitHttpUtil.getApiService().SetProductOnOff("", ProductID, a, "")
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        EditShopResultBean bean = GsonUtil.parseJsonWithGson(s, EditShopResultBean.class);
                        if (bean.getResult().equals("1")) {
                            easylayout.autoRefresh();
                            if (type == 1) {
                                showToast("产品成功上架");

                            } else if (type == 2) {
                                showToast("产品成功下架");
                            }
                        } else if (bean.getResult().equals("0")) {
                            showToast("产品不存在");
                        } else if (bean.getResult().equals("2")) {
                            showToast("产品已上架，上架失败");
                        } else if (bean.getResult().equals("3")) {
                            showToast("产品未上架，下架失败");
                        }
                        LogUtils.d(s);
                    }
                });

    }
}
