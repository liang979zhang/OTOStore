package com.example.administrator.otostore.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Activity.RefuseAnApplicationActivity;
import com.example.administrator.otostore.Adapter.GetFriendsInvetorAdapter;
import com.example.administrator.otostore.Bean.GetFriendsInvetoBean;
import com.example.administrator.otostore.Bean.LiPinJuanBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {


    @BindView(R.id.messageget)
    RecyclerView messageget;
    Unbinder unbinder;
    private GetFriendsInvetorAdapter adapter;
    private Context context;

    public OrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MyEventCode.CODE_B:
                if (messageEvent.getData().toString().equals("RefuseFriendsSucccess")) {
                    getfriends();
                }
                break;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initview();
        return view;
    }

    private void initview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageget.setLayoutManager(linearLayoutManager);
        messageget.setHasFixedSize(true);
        adapter = new GetFriendsInvetorAdapter();
        messageget.setAdapter(adapter);
        getfriends();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d("11111111111");
                if (adapter.getData().get(position) != null) {
                    GetFriendsInvetoBean bean = (GetFriendsInvetoBean) adapter.getData().get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("getfrienduserid", bean.getFriendUserID());
                    bundle.putString("getfrienduid", bean.getFriendID());
                    bundle.putString("getfriendnickname", bean.getNickName());
                    bundle.putString("getfriendremark", bean.getRemark());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(context, RefuseAnApplicationActivity.class);
                    startActivity(intent);
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tongyi:
                        if (adapter.getData().get(position) != null) {
                            GetFriendsInvetoBean bean = (GetFriendsInvetoBean) adapter.getData().get(position);
                            ResponseToMakingFriend(Long.valueOf(bean.getFriendUserID()),Long.valueOf(bean.getFriendID()), 1, "添加好友同意", 1, position);
                        }
                }
            }
        });
    }

    private void getfriends() {
        LogUtils.d("当前id" + SPUtils.getUserId(context));
        RetrofitHttpUtil.getApiService()
                .GetMakeFriendApplication("", SPUtils.getUserId(context))
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            List<GetFriendsInvetoBean> beans = GsonUtil.parseJsonArrayWithGson(s, GetFriendsInvetoBean.class);
                            adapter.setNewData(beans);

                        }
                    }
                });
    }

    private void ResponseToMakingFriend(Long frienduserid,Long FriendID, int ResAction, String Remark, int Appending, final int position) {
        RetrofitHttpUtil.getApiService()
                .ResponseToMakingFriend("",frienduserid, FriendID, ResAction, Remark, Appending)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        if (!s.equals("")) {
                            LiPinJuanBean bean = GsonUtil.parseJsonWithGson(s, LiPinJuanBean.class);
                            if (bean.getResult().equals("1")) {
                                Toast.makeText(context, "回复好友成功", Toast.LENGTH_SHORT).show();
                                adapter.remove(position);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
