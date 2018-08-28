package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.KuaiDiAdapter;
import com.example.administrator.otostore.Bean.GetExpressCorpListBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.administrator.otostore.Utils.Constants.KuaidiSelectResult;

public class SelectKuaidiActivity extends BaseActivity {

    @BindView(R.id.firsta)
    RadioButton firsta;
    @BindView(R.id.firstb)
    RadioButton firstb;
    @BindView(R.id.firstc)
    RadioButton firstc;
    @BindView(R.id.first)
    RadioGroup first;
    @BindView(R.id.seconda)
    RadioButton seconda;
    @BindView(R.id.secondb)
    RadioButton secondb;
    @BindView(R.id.second)
    RadioGroup second;
    @BindView(R.id.thirda)
    RadioButton thirda;
    @BindView(R.id.thirdb)
    RadioButton thirdb;
    @BindView(R.id.third)
    RadioGroup third;
    @BindView(R.id.fourtha)
    RadioButton fourtha;
    @BindView(R.id.fourthb)
    RadioButton fourthb;
    @BindView(R.id.fourth)
    RadioGroup fourth;
    @BindView(R.id.click)
    Button click;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.easylay)
    EasyRefreshLayout easylay;
    private KuaiDiAdapter adapterkuaidi;
    private int firstselect = 1;
    private int secondselect = 1;
    private int thirdselect = 0;
    private int fourthselect = 0;
    private int add = 1;
    private Context context;
    private int RowsPerPage = 20;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_select_kuaidi;
    }

    @Override
    public void initView() {
        setcenterTitle("选择快递公司");

        first.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.firsta:
                        firstselect = 1;
                        break;
                    case R.id.firstb:
                        firstselect = 2;
                        break;
                    case R.id.firstc:
                        firstselect = 3;
                        break;
                }
            }
        });

        second.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.seconda:
                        secondselect = 1;
                        break;
                    case R.id.secondb:
                        secondselect = 0;
                        break;
                }
            }
        });

        third.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.thirda:
                        thirdselect = 1;
                        break;
                    case R.id.thirdb:
                        thirdselect = 0;
                        break;
                }
            }
        });

        fourth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.fourtha:
                        fourthselect = 1;
                        break;
                    case R.id.fourthb:
                        fourthselect = 0;
                        break;
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);

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

    @OnClick(R.id.click)
    public void onViewClicked() {
        adapterkuaidi = new KuaiDiAdapter();
        recyclerview.setAdapter(adapterkuaidi);
        adapterkuaidi.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putString("ExpCorpName", adapterkuaidi.getData().get(position).getExpCorpName());
                bundle.putString("ExpCorpID", adapterkuaidi.getData().get(position).getExpCorpID());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(KuaidiSelectResult, intent);
                finish();
            }
        });
        easylay.autoRefresh();
        easylay.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                GetExpressCorpList(firstselect, secondselect, thirdselect, fourthselect, RowsPerPage, add);
            }

            @Override
            public void onRefreshing() {
                add = 1;
                GetExpressCorpList(firstselect, secondselect, thirdselect, fourthselect, RowsPerPage, add);
            }
        });
    }

    private void GetExpressCorpList(int ExpCorpType, int IsFrequently, int IsOrderOnline, int IsWayBill, int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetExpressCorpList("", ExpCorpType, IsFrequently, IsOrderOnline, IsWayBill, RowsPerPage, PageNum)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (!s.equals("[]")) {
                            List<GetExpressCorpListBean> beans = GsonUtil.parseJsonArrayWithGson(s, GetExpressCorpListBean.class);
                            if (add == 1) {
                                add++;
                                adapterkuaidi.setNewData(beans);
                                easylay.refreshComplete();
                            } else {
                                add++;
                                easylay.closeLoadView();
                                int postion = adapterkuaidi.getData().size();
                                adapterkuaidi.addData(beans);
                                adapterkuaidi.notifyDataSetChanged();
                                recyclerview.scrollToPosition(postion);
                            }
                        } else {
                            easylay.refreshComplete();
                            easylay.closeLoadView();
                            showToast("没有数据");

                        }
                    }
                });

    }

}
