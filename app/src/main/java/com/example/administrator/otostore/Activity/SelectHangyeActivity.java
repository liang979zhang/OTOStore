package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.ClassifyAdapterRight;
import com.example.administrator.otostore.Adapter.MostLeftAdapter;
import com.example.administrator.otostore.Bean.IndustryFirstBean;
import com.example.administrator.otostore.Bean.IndustrySecondBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class SelectHangyeActivity extends BaseActivity {

    @BindView(R.id.type_recyclerview_left)
    RecyclerView typeRecyclerviewLeft;
    @BindView(R.id.type_recyclerview_right)
    RecyclerView typeRecyclerviewRight;
    private Context context;
    private MostLeftAdapter leftAdapter;
    private ClassifyAdapterRight classifyAdapterRight;
    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_select_hangye;
    }

    @Override
    public void initView() {
        setcenterTitle("行业选择");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        leftmenu();
    }

    private void leftmenu() {
        RetrofitHttpUtil.getApiService()
                .GetClassList("")
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);

                        LogUtils.d("数据是" + s.substring(1, s.length() - 1));

                        final List<IndustryFirstBean> industryFirstBean = GsonUtil.parseJsonArrayWithGson(s, IndustryFirstBean.class);

                        LogUtils.d("list" + industryFirstBean);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        typeRecyclerviewLeft.setLayoutManager(linearLayoutManager);
                        leftAdapter = new MostLeftAdapter(context);

                        List<String> prices = new ArrayList<>();
                        for (IndustryFirstBean homePagerBean : industryFirstBean) {
                            prices.add(homePagerBean.getItemTitle());
                            LogUtils.d("参数是:" + homePagerBean.getItemParCode());
                        }
                        leftAdapter.setList(prices);
                        showRight(Integer.valueOf(industryFirstBean.get(0).getItemValue()));
                        leftAdapter.notifyDataSetChanged();
                        leftAdapter.setOnItemClickListener(new MostLeftAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                leftAdapter.setPosition(position);
                                leftAdapter.notifyDataSetChanged();
                                showRight(Integer.valueOf(industryFirstBean.get(position).getItemValue()));
                            }
                        });
                        typeRecyclerviewLeft.setAdapter(leftAdapter);
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }
    private void showRight(int ParCode) {
//        homePagerBeansright = new ArrayList<>();
//        for (int i = 0; i < TITLERIGHT.length; i++) {
//            HomePagerBean item = new HomePagerBean();
//            item.setTitle(TITLERIGHT[i]);
//            homePagerBeansright.add(item);
//        }

        RetrofitHttpUtil.getApiService()
                .GetDevisionList("", ParCode)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                final List<IndustrySecondBean> industrySecondBeanList = GsonUtil.parseJsonArrayWithGson(s, IndustrySecondBean.class);
                LogUtils.d("AAAAAAAAAAAAAA:" + industrySecondBeanList);

                LinearLayoutManager linearLayoutManagerright = new LinearLayoutManager(context);
                linearLayoutManagerright.setOrientation(LinearLayoutManager.VERTICAL);
                typeRecyclerviewRight.setLayoutManager(linearLayoutManagerright);
                classifyAdapterRight = new ClassifyAdapterRight(R.layout.type_right_item, industrySecondBeanList);
                classifyAdapterRight.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                        ShopAddressSelectCode selectCode = new ShopAddressSelectCode();
//                        selectCode.setLeftcode(Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()));
//                        selectCode.setRightcode(Integer.valueOf(industrySecondBeanList.get(position).getItemCode()));
//                        selectCode.setTitle(industrySecondBeanList.get(position).getItemTitle());
//                        startActivity(StoreRegisterActivity.class);
//                        finish();
//                        EventBusUtil.sendStickyEvent(new MessageEvent<>(MyEventCode.CODE_B, selectCode));
//                        LogUtils.d(industrySecondBeanList.get(position).getItemTitle());
//
                        Bundle bundle = new Bundle();
                        bundle.putInt("left", Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()));
                        bundle.putInt("right", Integer.valueOf(industrySecondBeanList.get(position).getItemCode()));
                        bundle.putString("classtitle", industrySecondBeanList.get(position).getItemTitle());
                        Intent intent=new Intent();
                        intent.putExtras(bundle);
                        setResult(Constants.SelectIndustryReSult,intent);
                        finish();

                        LogUtils.d(Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()) + "数据：" + Integer.valueOf(industrySecondBeanList.get(position).getItemCode()));
                    }
                });
                typeRecyclerviewRight.setAdapter(classifyAdapterRight);

            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
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
