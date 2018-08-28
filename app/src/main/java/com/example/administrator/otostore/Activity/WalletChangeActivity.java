package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adorkable.iosdialog.ActionSheetDialog;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.BankCarSelectBean;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.SelectFriendsUserIdBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.MD5Utils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletChangeActivity extends BaseActivity {
    @BindView(R.id.jinfenduihuanpas)
    EditText jinfenduihuanpas;
    @BindView(R.id.jinfenduihuaninput)
    EditText jinfenduihuaninput;
    @BindView(R.id.jifenduihuanok)
    Button jifenduihuanok;
    @BindView(R.id.jinfenduihuan)
    LinearLayout jinfenduihuan;
    @BindView(R.id.jinfenzhuanzhanguser)
    TextView jinfenzhuanzhanguser;
    @BindView(R.id.jinfenzhuanzhangpas)
    EditText jinfenzhuanzhangpas;
    @BindView(R.id.jinfenzhuanzhanginput)
    EditText jinfenzhuanzhanginput;
    @BindView(R.id.jinfenzhuanzhangremark)
    EditText jinfenzhuanzhangremark;
    @BindView(R.id.jinfenzhuanzhangok)
    Button jinfenzhuanzhangok;
    @BindView(R.id.jinfenzhuanzhang)
    LinearLayout jinfenzhuanzhang;
    @BindView(R.id.yuerzhuanzhanguser)
    TextView yuerzhuanzhanguser;
    @BindView(R.id.yuerzhuanzhangpas)
    EditText yuerzhuanzhangpas;
    @BindView(R.id.yuerzhuanzhanginput)
    EditText yuerzhuanzhanginput;
    @BindView(R.id.yuerzhuanzhangremark)
    EditText yuerzhuanzhangremark;
    @BindView(R.id.yuerzhuanzhangok)
    Button yuerzhuanzhangok;
    @BindView(R.id.yuerzhuanzhang)
    LinearLayout yuerzhuanzhang;
    @BindView(R.id.yuertixianuser)
    TextView yuertixianuser;
    @BindView(R.id.yuertixianpas)
    EditText yuertixianpas;
    @BindView(R.id.yuertixianinput)
    EditText yuertixianinput;
    @BindView(R.id.yuertixianremark)
    EditText yuertixianremark;
    @BindView(R.id.yuertixianok)
    Button yuertixianok;
    @BindView(R.id.yuertixian)
    LinearLayout yuertixian;
    private Context context;
    private String type;
    private Long bankselectid;
    private Long selectfriendsid;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_wallet_change;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("WalletType");
        if (type.equals("1")) {
            setcenterTitle("积分兑换");
            jinfenduihuan.setVisibility(View.VISIBLE);
        } else if (type.equals("2")) {
            setcenterTitle("积分转账");
            jinfenzhuanzhang.setVisibility(View.VISIBLE);
        } else if (type.equals("3")) {
            setcenterTitle("余额转账");
            yuerzhuanzhang.setVisibility(View.VISIBLE);
        } else if (type.equals("4")) {
            setcenterTitle("余额提现");
            yuertixian.setVisibility(View.VISIBLE);
        }

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

    @OnClick({R.id.jifenduihuanok,
            R.id.jinfenduihuan,
            R.id.jinfenzhuanzhangok,
            R.id.yuerzhuanzhangok,
            R.id.yuertixianok,
            R.id.jinfenzhuanzhanguser,
            R.id.yuerzhuanzhanguser,
            R.id.yuertixianuser
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yuerzhuanzhanguser:
                GetUserFriends();
                LogUtils.d("1111111111");
                break;
            case R.id.jinfenzhuanzhanguser:
                LogUtils.d("2222222222");
                GetUserFriends();
                break;
            case R.id.yuertixianuser:
                GetUserBankCardLst();
                LogUtils.d("33333333333333");
                break;
            case R.id.jifenduihuanok:
                if (isemepty(jinfenduihuanpas)) {
                    showToast("请输入密码");
                } else if (isemepty(jinfenduihuaninput)) {
                    showToast("输入积分");
                } else {
                    DoIntegralExch(Integer.valueOf(getEdit(jinfenduihuaninput)), MD5Utils.encodeMD52(getEdit(jinfenduihuanpas)));
                }
                break;
            case R.id.jinfenduihuan:
                break;
            case R.id.jinfenzhuanzhangok:
                if (isemepty(jinfenzhuanzhanguser)) {
                    showToast("收款用户不能为空");
                } else if (isemepty(jinfenzhuanzhanginput)) {
                    showToast("输入数值不能为空");
                } else if (isemepty(jinfenzhuanzhangpas)) {
                    showToast("密码不能为空");
                } else {
                    BonusTransfer(selectfriendsid, Integer.valueOf(getEdit(jinfenzhuanzhanginput)), MD5Utils.encodeMD52(getEdit(jinfenzhuanzhangpas)), getEdit(jinfenzhuanzhangremark));
                }
                break;
            case R.id.yuerzhuanzhangok:
                if (isemepty(yuerzhuanzhanguser)) {
                    showToast("收款用户不能为空");
                } else if (isemepty(yuerzhuanzhanginput)) {
                    showToast("输入数值不能为空");
                } else if (isemepty(yuerzhuanzhangpas)) {
                    showToast("密码不能为空");
                } else {
                    BalanceTransfer(selectfriendsid, BigDecimal.valueOf(Double.valueOf(getEdit(yuerzhuanzhanginput))), MD5Utils.encodeMD52(getEdit(yuerzhuanzhangpas)), getEdit(yuerzhuanzhangremark));
                }
                break;
            case R.id.yuertixianok:
                if (isemepty(yuertixianuser)) {
                    showToast("收款用户不能为空");
                } else if (isemepty(yuertixianinput)) {
                    showToast("输入数值不能为空");
                } else if (isemepty(yuertixianpas)) {
                    showToast("密码不能为空");
                } else {
                    DoWithDraw(bankselectid, BigDecimal.valueOf(Double.valueOf(getEdit(yuertixianinput))), MD5Utils.encodeMD52(getEdit(yuertixianpas)), getEdit(yuertixianremark));
                }
                break;
        }
    }

    private boolean isemepty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    private void DoWithDraw(Long BankCardID, BigDecimal WithDrawAmt, String PayPwd, String Remark) {
        RetrofitHttpUtil.getApiService()
                .DoWithDraw("", Long.valueOf(SPUtils.getUserId(context)), BankCardID, WithDrawAmt, PayPwd, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("提现成功");
                            EventBus.getDefault().post(new MessageEvent(MyEventCode.CODE_B, "DuiHuanSucccess"));
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("用户钱包不存在");
                        } else if (bean.getResult().equals("2")) {
                            showToast("用户钱包尚未激活,请先激活钱包");
                        } else if (bean.getResult().equals("3")) {
                            showToast("用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("4")) {
                            showToast(",用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("5")) {
                            showToast("用户支付密码尚未设置");
                        } else if (bean.getResult().equals("6")) {
                            showToast("用户支付密码错误");
                        } else if (bean.getResult().equals("7")) {
                            showToast("提现金额须为大于0的整数");
                        } else if (bean.getResult().equals("8")) {
                            showToast("钱包余额扣除手续费后不足提现");
                        } else if (bean.getResult().equals("9")) {
                            showToast("当前用户的银行卡不存在");
                        } else if (bean.getResult().equals("10")) {
                            showToast("钱包不存在");
                        } else if (bean.getResult().equals("11")) {
                            showToast(",插入余额明细失败");
                        } else if (bean.getResult().equals("12")) {
                            showToast("变更钱包余额失败");
                        } else if (bean.getResult().equals("13")) {
                            showToast("生成提现单失败");
                        }

                    }
                });
    }

    private void BalanceTransfer(long ToUserID, BigDecimal TransferAmt, String PayPwd, String Remark) {
        RetrofitHttpUtil.getApiService()
                .BalanceTransfer("", ToUserID, Long.valueOf(SPUtils.getUserId(context)), TransferAmt, PayPwd, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("转账成功");
                            EventBus.getDefault().post(new MessageEvent(MyEventCode.CODE_B, "DuiHuanSucccess"));
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("转入用户和转出用户不能相同;");
                        } else if (bean.getResult().equals("2")) {
                            showToast("转入用户的钱包不存在;");
                        } else if (bean.getResult().equals("3")) {
                            showToast("转入用户钱包尚未激活,请先激活钱包");
                        } else if (bean.getResult().equals("4")) {
                            showToast("转入用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("5")) {
                            showToast("转入用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("6")) {
                            showToast("转出用户的钱包不存在");
                        } else if (bean.getResult().equals("7")) {
                            showToast("转出用户钱包尚未激活,请先激活钱包");
                        } else if (bean.getResult().equals("8")) {
                            showToast("转出用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("9")) {
                            showToast("用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("10")) {
                            showToast("用户支付密码尚未设置");
                        } else if (bean.getResult().equals("11")) {
                            showToast("用户支付密码错误");
                        } else if (bean.getResult().equals("12")) {
                            showToast("转出用户钱包余额不足");
                        } else if (bean.getResult().equals("13")) {
                            showToast("钱包不存在");
                        } else if (bean.getResult().equals("14")) {
                            showToast("插入余额明细失败");
                        } else if (bean.getResult().equals("15")) {
                            showToast("变更钱包余额失败");
                        } else if (bean.getResult().equals("16")) {
                            showToast("生成转账单失败");
                        }

                    }
                });
    }

    private String getEdit(TextView textView) {
        return textView.getText().toString().trim();
    }

    private void BonusTransfer(long ToUserID, int TransferBonus, String PayPwd, String Remark) {
        RetrofitHttpUtil.getApiService()
                .BonusTransfer("", ToUserID, Long.valueOf(SPUtils.getUserId(context)), TransferBonus, PayPwd, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("转账成功");
                            EventBus.getDefault().post(new MessageEvent(MyEventCode.CODE_B, "DuiHuanSucccess"));
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("转入用户和转出用户不能相同;");
                        } else if (bean.getResult().equals("2")) {
                            showToast("转入用户的钱包不存在;");
                        } else if (bean.getResult().equals("3")) {
                            showToast("转入用户钱包尚未激活,请先激活钱包");
                        } else if (bean.getResult().equals("4")) {
                            showToast("转入用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("5")) {
                            showToast("转入用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("6")) {
                            showToast("转出用户的钱包不存在");
                        } else if (bean.getResult().equals("7")) {
                            showToast("转出用户钱包尚未激活,请先激活钱包");
                        } else if (bean.getResult().equals("8")) {
                            showToast("转出用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("9")) {
                            showToast("转出用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("10")) {
                            showToast("转出用户支付密码尚未设置");
                        } else if (bean.getResult().equals("11")) {
                            showToast("转出用户支付密码错误");
                        } else if (bean.getResult().equals("12")) {
                            showToast("转出用户钱包余额不足");
                        } else if (bean.getResult().equals("13")) {
                            showToast("钱包不存在");
                        } else if (bean.getResult().equals("14")) {
                            showToast("插入转出/转入积分明细失败");
                        } else if (bean.getResult().equals("15")) {
                            showToast("变更转出/转入钱包积分失败");
                        } else if (bean.getResult().equals("16")) {
                            showToast("生成转账单失败");
                        }
                    }
                });
    }

    private void DoIntegralExch(int ExchAmt, String pass) {
        String remark = "积分兑换";
        RetrofitHttpUtil.getApiService()
                .DoIntegralExch("", Long.valueOf(SPUtils.getUserId(context)), ExchAmt, pass, remark).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("积分兑换成功");
                            EventBus.getDefault().post(new MessageEvent(MyEventCode.CODE_B, "DuiHuanSucccess"));
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("用户钱包不存在;");
                        } else if (bean.getResult().equals("2")) {
                            showToast("用户钱包尚未激活,请先激活钱包;");
                        } else if (bean.getResult().equals("3")) {
                            showToast("用户钱包已冻结,暂不允许使用");
                        } else if (bean.getResult().equals("4")) {
                            showToast("用户钱包已暂停使用,恢复之前不能使用");
                        } else if (bean.getResult().equals("5")) {
                            showToast("用户支付密码尚未设置");
                        } else if (bean.getResult().equals("6")) {
                            showToast("用户支付密码错误");
                        } else if (bean.getResult().equals("7")) {
                            showToast("兑换金额须为大于0的整数");
                        } else if (bean.getResult().equals("8")) {
                            showToast("钱包剩余积分不足兑换");
                        } else if (bean.getResult().equals("9")) {
                            showToast("钱包不存在");
                        } else if (bean.getResult().equals("10")) {
                            showToast("插入积分/余额明细失败");
                        } else if (bean.getResult().equals("11")) {
                            showToast("变更钱包积分/余额失败");
                        } else if (bean.getResult().equals("12")) {
                            showToast("生成兑换单失败");
                        }
                    }
                });
    }

    private void GetUserFriends() {
        RetrofitHttpUtil.getApiService()
                .GetUserFriends("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            final List<SelectFriendsUserIdBean> beans = GsonUtil.parseJsonArrayWithGson(s, SelectFriendsUserIdBean.class);
                            int slectnum = beans.size();
                            ActionSheetDialog sheetDialog = new ActionSheetDialog(context);
                            sheetDialog.builder().setTitle("请选择好友").setCancelable(false).setCanceledOnTouchOutside(false);
                            for (int i = 0; i < slectnum; i++) {
                                sheetDialog.builder().addSheetItem(beans.get(i).getNickName(), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        LogUtils.d("测试的" + which);
                                        selectfriendsid = Long.valueOf(beans.get(which - 1).getFriendUserID());
                                        if (type.equals("2")) {
                                            jinfenzhuanzhanguser.setText(beans.get(which - 1).getNickName());
                                        } else if (type.equals("3")) {
                                            yuerzhuanzhanguser.setText(beans.get(which - 1).getNickName());
                                        }

                                    }
                                });
                            }
                            sheetDialog.builder().show();
                        }
                        else {
                            showToast("请添加好友");
                        }
                    }
                });
    }

    private void GetUserBankCardLst() {
        RetrofitHttpUtil.getApiService()
                .GetUserBankCardLst("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            final List<BankCarSelectBean> beans = GsonUtil.parseJsonArrayWithGson(s, BankCarSelectBean.class);
                            int slectnum = beans.size();
                            ActionSheetDialog sheetDialog = new ActionSheetDialog(context);
                            sheetDialog.builder().setTitle("请选择银行卡").setCancelable(false).setCanceledOnTouchOutside(false);
                            for (int i = 0; i < slectnum; i++) {
                                sheetDialog.builder().addSheetItem(beans.get(i).getBankName(), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        LogUtils.d("测试的" + which);
                                        bankselectid = Long.valueOf(beans.get(which - 1).getBankCardID());
                                        yuertixianuser.setText(beans.get(which - 1).getBankName());
                                    }
                                });
                            }
                            sheetDialog.builder().show();
//                            for (int i = 0; i < beans.size(); i++) {
//                                sheetDialog.builder().addSheetItem(beans.get(i).getBankName(), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                    @Override
//                                    public void onClick(int which) {
//                                        LogUtils.d(which+"测试");
//                                        bankselectid=Long.valueOf(beans.get(which).getBankCardID());
//                                        yuertixianuser.setText(beans.get(which).getBankName());
//                                    }
//                                });
//                                sheetDialog.builder().show();
//                            }
                        }else {
                            showToast("请添加银行卡");
                        }
                    }
                });
    }
}
