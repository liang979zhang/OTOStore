package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsAddShowActivity extends BaseActivity {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.namef)
    TextView namef;
    @BindView(R.id.remarks)
    EditText remarks;
    @BindView(R.id.fasong)
    Button fasong;
    private int userid;
    private int gruopid;
    private Context context;
    private int i = 0;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_friends_add_show;
    }

    @Override
    public void initView() {
        setcenterTitle("添加好友");
        LogUtils.d("开始几次" + i++);
        Bundle bundle = getIntent().getExtras();
        userid = Integer.valueOf(bundle.getString("friendsid"));
        gruopid = Integer.valueOf(bundle.getString("friendgroupid"));
        namef.setText(bundle.getString("friendnickname"));
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        showuserimage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fasong)
    public void onViewClicked() {
        addfriends(userid, gruopid, (remarks.getText().toString().trim().isEmpty()) ? "" : remarks.getText().toString().trim());
    }

    private void showuserimage() {
        RetrofitHttpUtil.getApiService()
                .GetUserAttachList("", userid, 2, 10, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (!s.equals("[]")) {
                            List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                            Glide.with(context).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into(image);
                        }

                    }
                });
    }

    private void addfriends(int friendsid, int grid, String reams) {
        RetrofitHttpUtil.getApiService()
                .DoMakeFriend("", SPUtils.getUserId(context), friendsid, grid, reams).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                CategroyBean bean=GsonUtil.parseJsonWithGson(s,CategroyBean.class);
                if (bean.getResult().equals("0")){
                    showToast("当前用户不存在");
                }else if (bean.getResult().equals("1")){
                    showToast("添加好友成功");
                    finish();
                }else if (bean.getResult().equals("2")){
                    showToast("好友用户不存在");
                }else if (bean.getResult().equals("3")){
                    showToast("分组不存在");
                }else if (bean.getResult().equals("4")){
                    showToast("不能加自己为好友");
                }else if (bean.getResult().equals("5")){
                    showToast("已申请, 对方未处理");
                }else if (bean.getResult().equals("6")){
                    showToast("已添加好友;");
                }else if (bean.getResult().equals("7")){
                    showToast("已添加过好友,状态不正常。重新申请失败;");
                }else if (bean.getResult().equals("8")){
                    showToast("添加好友失败");
                }
            }
        });
    }
}
