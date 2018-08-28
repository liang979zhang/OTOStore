package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.Bean.UserInfoBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.textnickname)
    TextView textnickname;
    @BindView(R.id.nickname)
    ImageView nickname;
    @BindView(R.id.erweima)
    ImageView erweima;
    @BindView(R.id.textphone)
    TextView textphone;
    @BindView(R.id.phone)
    ImageView phone;
    @BindView(R.id.textqianming)
    TextView textqianming;
    @BindView(R.id.qianming)
    ImageView qianming;
    @BindView(R.id.textemail)
    TextView textemail;
    @BindView(R.id.email)
    ImageView email;
    @BindView(R.id.exit)
    Button exit;
    @BindView(R.id.imageicon)
    ImageView imageicon;
    private Context context;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_person_info;
    }

    @Override
    public void initView() {
        setcenterTitle("个人信息");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        showinfo();
        showstoreimage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                if (event.getData().toString().equals("UpdateImageInfo")) {
                    showstoreimage();
                } else if (event.getData().toString().equals("UpdatePersonInfo")) {
                    showinfo();
                }
                break;
            default:
        }
    }

    @OnClick({R.id.touxianga,
            R.id.erweimaa,
            R.id.phonea,
            R.id.emaila,
            R.id.image, R.id.nickname, R.id.erweima, R.id.phone, R.id.qianming, R.id.email, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.touxianga:
                startActivity(UpdateImageActivity.class);
                break;
            case R.id.nickname:
                break;
            case R.id.erweimaa:
                Intent intent = new Intent(context, SaoYiSaoActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(context, CaptureActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.phonea:
                Bundle bundle = new Bundle();
                bundle.putInt("updateinfotype", 1);
                startActivity(UpdateInfoActivity.class, bundle);
                break;
            case R.id.qianming:
                break;
            case R.id.emaila:
                Bundle bundlea = new Bundle();
                bundlea.putInt("updateinfotype", 2);
                startActivity(UpdateInfoActivity.class, bundlea);
                break;
            case R.id.exit:
                SPUtils.clearUserId(context);
                SPUtils.clearShopId(context);
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    private void showstoreimage() {
        RetrofitHttpUtil.getApiService()
                .GetShopAttachList("", SPUtils.getShopId(context), 1, 10, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (!s.trim().equals("[]")) {
                            List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                            Glide.with(context).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into(imageicon);
                        }

                    }
                });
    }

    private void showinfo() {
        RetrofitHttpUtil.getApiService().GetUserBaseInfo("", SPUtils.getUserId(context)).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                UserInfoBean bean = GsonUtil.parseJsonWithGson(s, UserInfoBean.class);
                textnickname.setText(bean.getNickName());
                textphone.setText(bean.getMobileNum());
                textemail.setText(bean.getEmail());
                textqianming.setText(bean.getUserName());
                LogUtils.d(s);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                LogUtils.d("扫一扫：" + content);
            }
        }
    }
}
