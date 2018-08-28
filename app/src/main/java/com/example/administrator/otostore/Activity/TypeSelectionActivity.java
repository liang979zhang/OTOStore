package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.ClassifyAdapterRight;
import com.example.administrator.otostore.Adapter.MostLeftAdapter;
import com.example.administrator.otostore.Bean.HomePagerBean;
import com.example.administrator.otostore.Bean.IndustryFirstBean;
import com.example.administrator.otostore.Bean.IndustrySecondBean;
import com.example.administrator.otostore.Bean.ShopRegisterBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2018/6/9.
 */

public class TypeSelectionActivity extends BaseActivity {
    private static final String[] TITLELEFT = {"头像", "昵称", "二维码", "联系电话", "签名", "邮箱"};
    private static final String[] TITLERIGHT = {"头像", "昵称", "二维码", "联系电话", "签名", "邮箱"};
    //    private static final String[] TITLERIGHTTWO = {"头像", "昵称", "二维码", "联系电话", "签名", "邮箱"};
    @BindView(R.id.type_recyclerview_left)
    RecyclerView typeRecyclerviewLeft;
    @BindView(R.id.type_recyclerview_right)
    RecyclerView typeRecyclerviewRight;
    private MostLeftAdapter leftAdapter;
    private ClassifyAdapterRight classifyAdapterRight;
    private ArrayList<HomePagerBean> homePagerBeansleft;
    private ArrayList<HomePagerBean> homePagerBeansright;
    private Context context;
    private int userid;
    private String storename = "";
    private int updateshop = 0;
//    private List<IndustryFirstBean> industryFirstBean;

    //    private List<IndustryFirstBean> industryFirstBean;
    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_type_select;
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initView() {
        setcenterTitle("选择类型");
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        userid = bundle.getInt("userid", 0);
        storename = bundle.getString("shopname");

        leftmenu();
//        if (showleft == 1) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            typeRecyclerviewLeft.setLayoutManager(linearLayoutManager);
//            leftAdapter = new MostLeftAdapter(context);
//
//            List<String> prices = new ArrayList<>();
//            for (int i = 0; i < industryFirstBean.size(); i++) {
//                prices.add(industryFirstBean.get(i).getItemTitle());
//            }
//
//            for (IndustryFirstBean homePagerBean : industryFirstBean) {
//                prices.add(homePagerBean.getItemTitle());
//            }
//            leftAdapter.setList(prices);
//            showRight();
//            leftAdapter.notifyDataSetChanged();
//            leftAdapter.setOnItemClickListener(new MostLeftAdapter.MyItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    leftAdapter.setPosition(position);
//                    leftAdapter.notifyDataSetChanged();
//                    showRight();
//                }
//            });
//            typeRecyclerviewLeft.setAdapter(leftAdapter);
//        }
//        homePagerBeansleft = new ArrayList<>();
//        for (int i = 0; i < TITLELEFT.length; i++) {
//            HomePagerBean item = new HomePagerBean();
//            item.setTitle(TITLELEFT[i]);
//            homePagerBeansleft.add(item);
//        }
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        typeRecyclerviewLeft.setLayoutManager(linearLayoutManager);
//        leftAdapter = new MostLeftAdapter(context);
//
//        List<String> prices = new ArrayList<>();
//        for (HomePagerBean homePagerBean : homePagerBeansleft) {
//            prices.add(homePagerBean.getTitle());
//        }
//        leftAdapter.setList(prices);
//        showRight();
//        leftAdapter.notifyDataSetChanged();
//        leftAdapter.setOnItemClickListener(new MostLeftAdapter.MyItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //设置position，根据position的状态刷新
//                leftAdapter.setPosition(position);
//                leftAdapter.notifyDataSetChanged();
//                showRight();
//            }
//        });
//        typeRecyclerviewLeft.setAdapter(leftAdapter);

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
//                        showToast("11111111111111");
                        storeupdate(Integer.valueOf(industrySecondBeanList.get(position).getItemCode()), Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()));
//                        if (updateshop == 1) {
//                            register(userid, storename, Integer.valueOf(industrySecondBeanList.get(position).getItemCode()), Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()));
//                        }
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

    private void storeupdate(final int left, final int right) {
        RetrofitHttpUtil.getApiService()
                .PromoteUserType("", userid)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("AAAAAAAAA" + s);
                if (s.contains("1")) {
                    updateshop = 1;
                    register(userid, storename, left, right);
                }
            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });

    }

    private void register(long userid, String name, int left, int right) {
        RetrofitHttpUtil.getApiService()
                .ShopRegister("", userid, name, left, right)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("注册:" + s);
                ShopRegisterBean shopRegisterBean = GsonUtil.parseJsonWithGson(s, ShopRegisterBean.class);

                if (shopRegisterBean.getResult().equals("1")) {
                    showAlertDialog("注册成功", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    });
                }

            }
        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
