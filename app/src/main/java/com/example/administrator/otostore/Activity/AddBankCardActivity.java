package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adorkable.iosdialog.ActionSheetDialog;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CarBankBean;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.UserInfoBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.cardname)
    EditText cardname;
    @BindView(R.id.cardnum)
    EditText cardnum;
    @BindView(R.id.cardbank)
    TextView cardbank;
    @BindView(R.id.cardbankname)
    TextView cardbankname;
    @BindView(R.id.cardbanktype)
    TextView cardbanktype;
    @BindView(R.id.recardnum)
    EditText recardnum;
    @BindView(R.id.selectcardbanktype)
    TextView selectcardbanktype;
    @BindView(R.id.recardnamenew)
    EditText recardnamenew;
    @BindView(R.id.cardslectmoren)
    RadioGroup cardslectmoren;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.cardbtn)
    Button cardbtn;
    @BindView(R.id.yes)
    RadioButton yes;
    @BindView(R.id.no)
    RadioButton no;
    private Context context;
    private int typeselect;
    private int moren = 1;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_add_bank_card;
    }

    @Override
    public void initView() {
        setcenterTitle("新增银行卡");
        cardslectmoren.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yes:
                        moren = 1;
                        break;
                    case R.id.no:
                        moren = 0;
                        break;

                }
            }
        });
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
        ButterKnife.bind(this);
    }

    @OnClick({R.id.selectcardbanktype, R.id.cardbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectcardbanktype:
                new ActionSheetDialog(context)
                        .builder()
                        .setTitle("选择选择")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("企业账户", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                LogUtils.d("1111111");
                                selectcardbanktype.setText("企业账户");
                                typeselect = 1;

                            }
                        })
                        .addSheetItem("个人账户", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                LogUtils.d("22222222222");
                                selectcardbanktype.setText("个人账户");
                                typeselect = 2;
                            }
                        }).show();

                break;
            case R.id.cardbtn:
                if (edtextempty(phone)) {
                    showToast("序号不能为空");
                } else if (edtextempty(cardname)) {
                    showToast("请填写持卡人姓名");
                } else if (edtextempty(cardnum)) {
                    showToast("请输入卡号");
                } else if (edtextempty(recardnum)) {
                    showToast("请重新输入卡号");
                } else if (edtextempty(recardnamenew)) {
                    showToast("请输入卡别名");
                } else if (!editetext(cardnum).equals(editetext(recardnum))) {
                    showToast("两次输入卡号不一致");
                } else if (edtextempty(selectcardbanktype)) {
                    showToast("请选择卡类型");
                } else {
                    if (typeselect == 2) {
                        GetUserBaseInfo();
                    } else if (typeselect == 1) {
                        GetIssuerCardInfo(editetext(cardnum));
                    }
                }
                break;
        }
    }

    private String editetext(TextView edit) {
        return edit.getText().toString().trim();
    }

    private boolean edtextempty(TextView edit) {
        return edit.getText().toString().trim().isEmpty();
    }

    private void GetUserBaseInfo() {
        RetrofitHttpUtil.getApiService()
                .GetUserBaseInfo("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        UserInfoBean bean = GsonUtil.parseJsonWithGson(s, UserInfoBean.class);
                        if (!bean.getUserName().equals(editetext(cardname))) {
                            showToast("持卡人与当前用户不一致");
                        } else {
                            GetIssuerCardInfo(editetext(cardnum));
                        }
                    }
                });
    }

    private void DoSaveUserBankCard(long BankCardID, long UserID, int AccType, String CardHolder, String BankAccCode, String AliasName, int CardIdx, int DefaultWithdraw, String Remark) {
        RetrofitHttpUtil.getApiService()
                .DoSaveUserBankCard("", BankCardID, UserID, AccType, CardHolder, BankAccCode, AliasName, CardIdx, DefaultWithdraw, Remark)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("成功");
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B,"AddBankCarSuccess"));
                            finish();
                        } else if (bean.getResult().equals("0")) {
                            showToast("当前用户不存在");
                        }else if (bean.getResult().equals("2")) {
                            showToast("当前用户名与持卡人不一致");
                        }else if (bean.getResult().equals("3")) {
                            showToast("卡号错误或不支持的卡");
                        }else if (bean.getResult().equals("4")) {
                            showToast("不支持的卡");
                        }else if (bean.getResult().equals("5")) {
                            showToast("修改卡片失败");
                        }else if (bean.getResult().equals("6")) {
                            showToast("当前卡已存在");
                        }else if (bean.getResult().equals("7")) {
                            showToast("新增卡片失败");
                        }
                    }
                });
    }

    private void GetIssuerCardInfo(String CardNo) {
        RetrofitHttpUtil.getApiService()
                .GetIssuerCardInfo("", CardNo)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);

                        CarBankBean carBankBean = GsonUtil.parseJsonWithGson(s, CarBankBean.class);
                        cardbank.setText(carBankBean.getBankName());
                        cardbankname.setText(carBankBean.getCardName());
                        cardbanktype.setText(carBankBean.getCardType());
                        DoSaveUserBankCard(Long.valueOf("0"), SPUtils.getUserId(context), typeselect, editetext(cardname), editetext(cardnum), editetext(recardnamenew), Integer.valueOf(editetext(phone)), moren, editetext(remark));

                    }
                });
    }
}
