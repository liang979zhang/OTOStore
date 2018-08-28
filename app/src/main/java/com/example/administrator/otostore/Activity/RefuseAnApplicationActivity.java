package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.otostore.Bean.LiPinJuanBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefuseAnApplicationActivity extends BaseActivity {
    @BindView(R.id.imagea)
    CircularImageView imagea;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.huifu)
    EditText huifu;
    @BindView(R.id.refuse)
    Button refuse;
    private Context context;
    private Long userid;
    private Long friendid;

    @Override
    public int getContentViewResId() {

        context = this;
        return R.layout.activity_refuse_an_application;
    }

    @Override
    public void initView() {
        setcenterTitle("好友申请");
        Bundle bundle = getIntent().getExtras();
        userid = Long.valueOf(bundle.getString("getfrienduserid"));
        friendid = Long.valueOf(bundle.getString("getfrienduid"));
        name.setText(bundle.getString("getfriendnickname"));
        remark.setText(bundle.getString("getfriendremark"));
        RetrofitHttpUtil.getApiService()
                .GetUserAttachList("", Long.valueOf(userid), 2, 10, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (!s.equals("[]")) {
                            List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                            Glide.with(context).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into(imagea);

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
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.refuse)
    public void onViewClicked() {
        ResponseToMakingFriend(userid,friendid,2,remark.getText().toString(),1);
    }

    private void ResponseToMakingFriend(Long userid, Long FriendID, int ResAction, String Remark, int Appending) {
        RetrofitHttpUtil.getApiService()
                .ResponseToMakingFriend("", userid, FriendID, ResAction, Remark, Appending)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            LiPinJuanBean bean = GsonUtil.parseJsonWithGson(s, LiPinJuanBean.class);
                            if (bean.getResult().equals("1")) {
                                finish();
                                EventBus.getDefault().post(new MessageEvent(MyEventCode.CODE_B, "RefuseFriendsSucccess"));
                                Toast.makeText(context, "回复好友成功", Toast.LENGTH_SHORT).show();
                            } else if (bean.getResult().equals("0")) {
                                Toast.makeText(context, "好友申请记录不存在", Toast.LENGTH_SHORT).show();
                            } else if (bean.getResult().equals("2")) {
                                Toast.makeText(context, "当前用户不是被申请用户", Toast.LENGTH_SHORT).show();
                            } else if (bean.getResult().equals("3")) {
                                Toast.makeText(context, "状态不在申请状态", Toast.LENGTH_SHORT).show();
                            } else if (bean.getResult().equals("4")) {
                                Toast.makeText(context, "回复申请失败", Toast.LENGTH_SHORT).show();
                            } else if (bean.getResult().equals("5")) {
                                Toast.makeText(context, "追加好友失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
