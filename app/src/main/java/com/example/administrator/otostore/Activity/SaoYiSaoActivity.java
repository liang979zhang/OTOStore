package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.FriendsAddGroupBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.QRCodeUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaoYiSaoActivity extends BaseActivity {

    @BindView(R.id.saoyisao)
    ImageView saoyisao;
    @BindView(R.id.saoyisaotext)
    TextView saoyisaotext;
    private Context context;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_sao_yi_sao;
    }

    @Override
    public void initView() {
        setcenterTitle("扫一扫");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        getUsergroup(String.valueOf(SPUtils.getUserId(context)));
        saoyisaotext.setText("helloworld");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void getUsergroup(String userID) {
        RetrofitHttpUtil.getApiService()
                .GetCurrUserOrg("", Integer.valueOf(userID)).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                FriendsAddGroupBean bean = GsonUtil.parseJsonWithGson(s, FriendsAddGroupBean.class);
                LogUtils.d("组织" + s);
                String friendsid = bean.getUserID();
                String friendgroupid = bean.getUserOrgID();
                String remarks = "添加好友";
                String whole = friendsid + "<" + friendgroupid + "<" + remarks;
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(whole, 480);
                saoyisao.setImageBitmap(mBitmap);
            }
        });
    }
}
