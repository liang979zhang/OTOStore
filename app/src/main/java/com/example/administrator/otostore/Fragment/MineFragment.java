package com.example.administrator.otostore.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Activity.BillAccountActivity;
import com.example.administrator.otostore.Activity.DetailFirstActivity;
import com.example.administrator.otostore.Activity.DetailFourthActivity;
import com.example.administrator.otostore.Activity.DetailSecondActivity;
import com.example.administrator.otostore.Activity.DetailThirdActivity;
import com.example.administrator.otostore.Activity.ShowBankActivity;
import com.example.administrator.otostore.Activity.WalletChangeActivity;
import com.example.administrator.otostore.Activity.ZhifuManagerActivity;
import com.example.administrator.otostore.Bean.GetUserSettleSumBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.UserWallet;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    @BindView(R.id.left_base_bar)
    ImageView leftBaseBar;
    @BindView(R.id.center_base_bar_title)
    TextView centerBaseBarTitle;
    @BindView(R.id.right_base_bar)
    TextView rightBaseBar;
    @BindView(R.id.basetitle_show_or_hide)
    LinearLayout basetitleShowOrHide;
    @BindView(R.id.dongtai)
    ImageView dongtai;
    @BindView(R.id.jinfentext)
    TextView jinfentext;
    @BindView(R.id.jifen)
    LinearLayout jifen;


    @BindView(R.id.yuertext)
    TextView yuertext;
    @BindView(R.id.yu)
    LinearLayout yu;

    @BindView(R.id.zongjietext)
    TextView zongjietext;
    @BindView(R.id.zong)
    LinearLayout zong;
    @BindView(R.id.zongjiedai)
    TextView zongjiedai;
    @BindView(R.id.dai)
    LinearLayout dai;
    @BindView(R.id.zongjiewan)
    TextView zongjiewan;
    @BindView(R.id.wan)
    LinearLayout wan;
    @BindView(R.id.yinghangka)
    Button yinghangka;
    @BindView(R.id.zhifumima)
    Button zhifumima;
    Unbinder unbinder;
    @BindView(R.id.zhuanclick)
    ImageView zhuanclick;
    @BindView(R.id.zhuan)
    TextView zhuan;
    @BindView(R.id.duiclick)
    ImageView duiclick;
    @BindView(R.id.dui)
    TextView dui;
    @BindView(R.id.yuzhuanclick)
    ImageView yuzhuanclick;
    @BindView(R.id.yuzhuan)
    TextView yuzhuan;
    @BindView(R.id.yuticlick)
    ImageView yuticlick;
    @BindView(R.id.yuti)
    TextView yuti;
    @BindView(R.id.zongjiedaikuan)
    TextView zongjiedaikuan;
    @BindView(R.id.daikuan)
    LinearLayout daikuan;
    private String WalletId;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        getJIfen();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MyEventCode.CODE_B:
                if (messageEvent.getData().toString().equals("DuiHuanSucccess")) {
                    getJIfen();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.zhuanclick, R.id.duiclick, R.id.yuzhuanclick, R.id.yuticlick,
            R.id.jifen, R.id.zhuan, R.id.dui, R.id.yu, R.id.yuzhuan, R.id.yuti, R.id.zong,
            R.id.dai, R.id.wan, R.id.yinghangka, R.id.zhifumima,R.id.daikuan})
    public void onViewClicked(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.zhuanclick:
                Bundle bundlezhuanzhang = new Bundle();
                bundlezhuanzhang.putString("WalletType", "2");
                intent = new Intent(getActivity(), WalletChangeActivity.class);
                intent.putExtras(bundlezhuanzhang);
                startActivity(intent);
                break;
            case R.id.duiclick:
                Bundle bundleduihuan = new Bundle();
                bundleduihuan.putString("WalletType", "1");
                intent = new Intent(getActivity(), WalletChangeActivity.class);
                intent.putExtras(bundleduihuan);
                startActivity(intent);
                break;
            case R.id.yuzhuanclick:
                Bundle bundleyuezhuanzhang = new Bundle();
                bundleyuezhuanzhang.putString("WalletType", "3");
                intent = new Intent(getActivity(), WalletChangeActivity.class);
                intent.putExtras(bundleyuezhuanzhang);
                startActivity(intent);
                break;
            case R.id.yuticlick:
                Bundle bundleyuerti = new Bundle();
                bundleyuerti.putString("WalletType", "4");
                intent = new Intent(getActivity(), WalletChangeActivity.class);
                intent.putExtras(bundleyuerti);
                startActivity(intent);
                break;
            case R.id.jifen:
                Bundle bundle = new Bundle();
                bundle.putString("QianbaoType", "1");
                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.zhuan:
                Bundle bundle2 = new Bundle();
                bundle2.putString("QianbaoType", "2");

                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
                break;
            case R.id.dui:
                Bundle bundle3 = new Bundle();
                bundle3.putString("QianbaoType", "3");

                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle3);
                startActivity(intent);
                break;
            case R.id.yu:
                Bundle bundle4 = new Bundle();
                bundle4.putString("QianbaoType", "4");

                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle4);
                startActivity(intent);
                break;
            case R.id.yuzhuan:
                Bundle bundle5 = new Bundle();
                bundle5.putString("QianbaoType", "5");

                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle5);
                startActivity(intent);
                break;
            case R.id.yuti:
                Bundle bundle6 = new Bundle();
                bundle6.putString("QianbaoType", "6");

                intent = new Intent(getActivity(), BillAccountActivity.class);
                intent.putExtras(bundle6);
                startActivity(intent);
                break;
            case R.id.zong:
                intent=new Intent(getActivity(), DetailFirstActivity.class);
                startActivity(intent);
                break;
            case R.id.dai:
                intent=new Intent(getActivity(), DetailSecondActivity.class);
                startActivity(intent);
                break;
            case R.id.wan:
                intent=new Intent(getActivity(), DetailThirdActivity.class);
                startActivity(intent);
                break;
            case R.id.daikuan:
                intent=new Intent(getActivity(), DetailFourthActivity.class);
                startActivity(intent);
                break;
            case R.id.yinghangka:
                intent = new Intent(getActivity(), ShowBankActivity.class);
                startActivity(intent);
                break;
            case R.id.zhifumima:
                intent = new Intent(getActivity(), ZhifuManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getJIfen() {
        RetrofitHttpUtil.getApiService()
                .GetUserWallet("", Long.valueOf(SPUtils.getUserId(getActivity())))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        UserWallet userWallet = GsonUtil.parseJsonWithGson(s, UserWallet.class);
                        WalletId = userWallet.getWalletID();
                        LogUtils.d("钱包是" + WalletId);
                        SPUtils.setWalletId(getActivity(), Integer.valueOf(WalletId));
                        LogUtils.d("获取的数据" + SPUtils.getWalletId(getActivity()));
                        GetUserSettleSum(Long.valueOf(WalletId));
                        jinfentext.setText(userWallet.getBonusPoints());
                        yuertext.setText(userWallet.getBalance());
                    }
                });
    }

    private void GetUserSettleSum(Long aLong) {
        RetrofitHttpUtil
                .getApiService()
                .GetUserSettleSum("", aLong)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("测试的数据" + s);
                        GetUserSettleSumBean bean = GsonUtil.parseJsonWithGson(s, GetUserSettleSumBean.class);
                        zongjietext.setText(bean.getSettleBonusSum());
                        zongjiedai.setText(bean.getSettlingBonusSum());
                        zongjiewan.setText(bean.getSettledBonusSum());
                    }
                });
    }
}
