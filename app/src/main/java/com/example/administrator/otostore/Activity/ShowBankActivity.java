package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.BankShowAdapter;
import com.example.administrator.otostore.Bean.BankBean;
import com.example.administrator.otostore.Bean.EditShopResultBean;
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

public class ShowBankActivity extends BaseActivity {
    @BindView(R.id.recyclerbank)
    RecyclerView recyclerbank;
    private Context context;
    private BankShowAdapter bankShowAdapter;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_show_bank;
    }

    @Override
    public void initView() {
        setcenterTitle("银行卡");
        showright(true);
        setrightTitle("新增");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerbank.setLayoutManager(linearLayoutManager);
        recyclerbank.setHasFixedSize(true);
        bankShowAdapter = new BankShowAdapter();
        recyclerbank.setAdapter(bankShowAdapter);
        bankShowAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                DeleteUserBankCard(Long.valueOf(bankShowAdapter.getItem(position).getBankCardID()), position);
                return false;
            }
        });

    }

    private void DeleteUserBankCard(Long BankCardID, final int deletpos) {
        RetrofitHttpUtil.getApiService()
                .DeleteUserBankCard("", Long.valueOf(SPUtils.getUserId(context)), BankCardID)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        EditShopResultBean bean = GsonUtil.parseJsonWithGson(s, EditShopResultBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("删除成功");
                            bankShowAdapter.remove(deletpos);

                        } else {
                            showToast("删除失败");
                        }
                        LogUtils.d(s);
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
                if (event.getData().toString().equals("AddBankCarSuccess")) {
                    GetUserBankCardLst();
                }
                break;
        }
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        startActivity(AddBankCardActivity.class);
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        GetUserBankCardLst();
    }

    private void GetUserBankCardLst() {
        RetrofitHttpUtil.getApiService()
                .GetUserBankCardLst("", Long.valueOf(SPUtils.getUserId(context)))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (!s.equals("")) {
                            LogUtils.d(s);
                            List<BankBean> beans = GsonUtil.parseJsonArrayWithGson(s, BankBean.class);
                            bankShowAdapter.setNewData(beans);
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
