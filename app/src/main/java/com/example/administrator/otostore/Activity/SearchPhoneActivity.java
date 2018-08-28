package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.FriendsAddBean;
import com.example.administrator.otostore.Bean.FriendsAddGroupBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.PhoneUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchPhoneActivity extends BaseActivity {
    @BindView(R.id.searchpone)
    EditText searchpone;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_search_phone;
    }

    @Override
    public void initView() {
        setcenterTitle("搜索");
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
