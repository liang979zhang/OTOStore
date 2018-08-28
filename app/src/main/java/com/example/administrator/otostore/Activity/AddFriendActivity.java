package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.FriendsAddBean;
import com.example.administrator.otostore.Bean.FriendsAddGroupBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.example.administrator.otostore.Utils.SPUtils;
import com.example.administrator.otostore.Utils.ShareCallBack;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.searchpone)
    EditText searchpone;
    @BindView(R.id.phone)
    ImageView phone;
    @BindView(R.id.qqtes)
    ImageView qq;
    @BindView(R.id.saoyisao)
    ImageView saoyisao;
    private Context context;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_add_friend;
    }

    @Override
    public void initView() {
        setcenterTitle("添加好友");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        searchpone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            if (PhoneUtils.isMobileNO(searchpone.getText().toString().trim())) {
                                getuserinfo(searchpone.getText().toString().trim());
                            } else {
                                showToast("输入正确手机号");
                            }
                            return true;
                        default:
                            return true;
                    }
                }


                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void onShareCallback(int channel, int status) {
        new ShareCallBack().onShareCallback(channel, status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 分享回调处理
         */
        if (requestCode == ShareConstant.REQUEST_CODE) {
            if (data != null) {
                int channel = data.getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1);
                int status = data.getIntExtra(ShareConstant.EXTRA_SHARE_STATUS, -1);
                onShareCallback(channel, status);
            }
        }

        else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                LogUtils.d("扫一扫："+content);
                LogUtils.d("扫一扫：" + content);
                int friendsid=Integer.valueOf(content.substring(0,content.indexOf("<")));
                LogUtils.d("扫一扫"+friendsid);
                int grid=Integer.valueOf(content.substring(content.indexOf("<")+1,content.lastIndexOf("<")));
                LogUtils.d("扫一扫"+grid);
                String reamrks=content.substring(content.lastIndexOf("<")+1,content.length());
                LogUtils.d("扫一扫"+reamrks);
                addfriends(friendsid,grid,reamrks);
            }
        }
    }
    private void addfriends(int friendsid, int grid, String reams) {
        RetrofitHttpUtil.getApiService()
                .DoMakeFriend("", SPUtils.getUserId(context), friendsid, grid, reams).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                if (bean.getResult().equals("0")) {
                    showToast("当前用户不存在");
                } else if (bean.getResult().equals("1")) {
                    showToast("添加好友成功");
                } else if (bean.getResult().equals("2")) {
                    showToast("好友用户不存在");
                } else if (bean.getResult().equals("3")) {
                    showToast("分组不存在");
                } else if (bean.getResult().equals("4")) {
                    showToast("不能加自己为好友");
                } else if (bean.getResult().equals("5")) {
                    showToast("已申请, 对方未处理");
                } else if (bean.getResult().equals("6")) {
                    showToast("已添加好友;");
                } else if (bean.getResult().equals("7")) {
                    showToast("已添加过好友,状态不正常。重新申请失败;");
                } else if (bean.getResult().equals("8")) {
                    showToast("添加好友失败");
                }
            }
        });
    }
    private void getuserinfo(String phone) {
        RetrofitHttpUtil.getApiService()
                .GetUserBaseInfoA("", phone).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                if (!s.trim().equals(""))

                {
                    FriendsAddBean addBean = GsonUtil.parseJsonWithGson(s, FriendsAddBean.class);
                    LogUtils.d("信息" + s);
                    getUsergroup(addBean.getUserID());
                } else {
                    showToast("用户不存在");
                }
            }
        });
    }

    private void getUsergroup(String userID) {
        RetrofitHttpUtil.getApiService()
                .GetCurrUserOrg("", Integer.valueOf(userID)).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                FriendsAddGroupBean bean = GsonUtil.parseJsonWithGson(s, FriendsAddGroupBean.class);
                LogUtils.d("组织" + s);
                Bundle bundle = new Bundle();
                bundle.putString("friendsid", bean.getUserID());
                bundle.putString("friendgroupid", bean.getUserOrgID());
                bundle.putString("friendnickname", bean.getNickName());
                startActivity(FriendsAddShowActivity.class, bundle);
            }
        });
    }


    @OnClick({R.id.phonea, R.id.qqqaoqinng,R.id.saoyisaoa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phonea:
                startActivity(SearchPhoneActivity.class);
                break;
            case R.id.qqqaoqinng:
                LogUtils.d("11111111111111111111111");
                ShareEntity testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
                testBean.setUrl("https://www.baidu.com"); //分享链接
                testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png");
                ShareUtil.startShare(this, ShareConstant.SHARE_CHANNEL_QQ, testBean, ShareConstant.REQUEST_CODE);
                break;
            case R.id.saoyisaoa:
                Intent intent = new Intent(context, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
        }
    }

}
