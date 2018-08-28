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
import com.example.administrator.otostore.Adapter.ProductSpecAdapter;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ProductSpec;
import com.example.administrator.otostore.Bean.UserRegisterResultBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.functions.Action;

public class SpecificationManagementActivity extends BaseActivity {
    @BindView(R.id.productrecycler)
    RecyclerView productrecycler;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    private String productid;
    private Context context;
    private ProductSpecAdapter specAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_specification_management;
    }

    @Override
    public void initView() {

        setcenterTitle("规格管理");
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
        bundle.putString("saveproductrule", productid);
        bundle.putString("specificationType", "0");
        startActivity(EditSpecificationManagementActivity.class, bundle);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        productid = bundle.getString("rulesproduct");
        showRefresh();
    }

    private void showRefresh() {
        easylayout.autoRefresh();
        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                showProductSpe(Long.valueOf(productid));
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
                if (event.getData().toString().equals("ProductSpecifi")) {
                    easylayout.autoRefresh();
                }
                break;
        }
    }

    private void showProductSpe(final Long ProductID) {
        LogUtils.d("产品是" + ProductID);
        RetrofitHttpUtil.getApiService()
                .GetProductSpecList("", ProductID, "1,3")
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("产品是" + s);
                if (!s.equals("")) {
                    easylayout.refreshComplete();
                    final List<ProductSpec> specList = GsonUtil.parseJsonArrayWithGson(s, ProductSpec.class);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    productrecycler.setLayoutManager(linearLayoutManager);
                    specAdapter = new ProductSpecAdapter(R.layout.itempoductspec, specList);
                    specAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("specificationType", "1");
                            bundle.putString("saveproductrule", productid);
                            bundle.putString("specificationProductSpecID", specList.get(position).getProductSpecID());
                            bundle.putString("specificationTypeSpecIdx", specList.get(position).getSpecIdx());
                            bundle.putString("specificationTypeSpecCode", specList.get(position).getSpecCode());
                            bundle.putString("specificationTypeProductSpec", specList.get(position).getProductSpec());
                            bundle.putString("specificationTypePrice", specList.get(position).getPrice());
                            bundle.putString("specificationTypeQuantity", specList.get(position).getQuantity());
                            bundle.putString("specificationTypeRemark", specList.get(position).getRemark());
                            startActivity(EditSpecificationManagementActivity.class, bundle);
                        }
                    });
                    specAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                            showAlertDialog("你确定删除吗", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteProductSpec(Long.valueOf(specList.get(position).getProductSpecID()), position);
                                }
                            });
                            return false;
                        }
                    });
                    productrecycler.setAdapter(specAdapter);
                } else {
                    easylayout.refreshComplete();
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

    private void deleteProductSpec(Long ProductSpecID, final int position) {
        RetrofitHttpUtil.getApiService().DeleteProductSpec("", ProductSpecID)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("s" + s);
                UserRegisterResultBean bean = GsonUtil.parseJsonWithGson(s, UserRegisterResultBean.class);

                if (bean.getResult().equals("1")) {
                    specAdapter.remove(position);
                }
            }
        });
    }
}
