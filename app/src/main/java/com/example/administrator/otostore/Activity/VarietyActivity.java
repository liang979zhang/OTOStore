package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.CategoryBeanAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
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

public class VarietyActivity extends BaseActivity {
    @BindView(R.id.caterecycler)
    RecyclerView caterecycler;
    private Context context;
    private int catecaid;
    private CategoryBeanAdapter categoryBeanAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_variety;
    }

    @Override
    public void initView() {
        setcenterTitle("分类管理");
        showright(true);
        setrightTitle("添加");
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
        bundle.putString("catetype", "0");
        bundle.putString("catecaid", String.valueOf(catecaid + 1));
        startActivity(AddVarietyActivity.class, bundle);
    }

    @Override
    public void initData() {
        showVariety();
    }

    private void showVariety() {
        RetrofitHttpUtil.getApiService()
                .GetShopCategory("", SPUtils.getShopId(context), "10000000")
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                if (!s.equals("[]")) {
                    final List<CategroyBean> list = GsonUtil.parseJsonArrayWithGson(s, CategroyBean.class);
                    if (list.size() > 0) {
                        catecaid = Integer.valueOf(list.get(list.size() - 1).getCatIdx());
                        LogUtils.d("数值是:" + catecaid);
                        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        caterecycler.setLayoutManager(linearLayoutManager);
                        categoryBeanAdapter = new CategoryBeanAdapter(R.layout.item_category, list);
                        caterecycler.setAdapter(categoryBeanAdapter);
                        categoryBeanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Bundle bundle = new Bundle();
                                bundle.putString("catetype", "1");
                                bundle.putString("catidx", list.get(position).getCatID());
                                bundle.putString("catecaid", list.get(position).getCatIdx());
                                bundle.putString("catename", list.get(position).getCatName());
                                bundle.putString("catedes", list.get(position).getRemark());
                                startActivity(AddVarietyActivity.class, bundle);
                            }
                        });
                        categoryBeanAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                                showAlertDialog("你确认删除吗?", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LogUtils.d("BBB" + list.get(position).getCatID() + "AAA" + list.get(position).getCatCode());
                                        deletCategory(list.get(position).getCatCode(), position);
                                    }
                                });
                                return false;
                            }
                        });
                        LogUtils.d("展示是:" + s);
                    } else {
                        catecaid = 0;
//                    LogUtils.d("数值是:" + catecaid);
                    }


                }
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
                if (event.getData().toString().equals("category")) {
                    showVariety();
                }
                break;
        }
    }

    private void deletCategory(String CarCode, final int position) {
        RetrofitHttpUtil.getApiService()
                .DeleteShopCategory("", CarCode)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                CategroyBean categroyBean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                if (categroyBean.getResult().equals("1")) {
                    categoryBeanAdapter.remove(position);
                    showToast("删除成功");

                } else if (categroyBean.getResult().equals("3")) {
                    showToast("已有产品，不可删除");
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
}
