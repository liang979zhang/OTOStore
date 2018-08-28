package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.CommentListAdapter;
import com.example.administrator.otostore.Adapter.MomentItemAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.CommentListBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MomentBean;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsMomentActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    private Context context;

    private MomentItemAdapter itemAdapter;
    private int pinglun = 2;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclercommet;
    private List<MomentBean> beans = new ArrayList<>();
    private EditText a;
    private int add = 1;
    private int RowsPerPage = 5;

    private boolean loadmoredata = true;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_friends_moment;

    }

    @Override
    public void initView() {
        setcenterTitle("好友动态");
        showright(true);
        setrightTitle("发表");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize(true);
        itemAdapter = new MomentItemAdapter();
        itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (itemAdapter.getData().get(position).getUserID().equals(String.valueOf(SPUtils.getUserId(context)))) {
                    Bundle bundle = new Bundle();
                    bundle.putString("momentid", itemAdapter.getData().get(position).getMovementID());
                    bundle.putString("remark", itemAdapter.getData().get(position).getRemark());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(FriendsMomentActivity.this, UpdateFriendsMomentActivity.class);
                    startActivity(intent);

                } else {
                    showToast("好友动态不能修改");
                }
            }
        });
        recycler.setAdapter(itemAdapter);
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        startActivity(AddFriendsMomentActivity.class);
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    protected void receiveStickyEvent(MessageEvent event) {
        super.receiveStickyEvent(event);
        switch (event.getCode()) {
            case MyEventCode.CODE_B:
                if (event.getData().toString().equals("MomentPublish")) {
//                    GetUserPubMovementList(10, 1);
                    easylayout.autoRefresh();
                }
                break;
            default:
        }
    }

    @Override
    public void initData() {
        initListener();
//
//        easylayout.autoRefresh();
//        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
//        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
//            @Override
//            public void onLoadMore() {
//                GetUserPubMovementList(5, add);
//            }
//
//            @Override
//            public void onRefreshing() {
//                GetUserPubMovementList(5, add);
//            }
//        });
    }

    private void initListener() {
        easylayout.autoRefresh();
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                GetUserPubMovementList(RowsPerPage, add);

            }

            @Override
            public void onRefreshing() {
                add = 1;
                GetUserPubMovementList(RowsPerPage, add);

            }
        });

        itemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                relativeLayout = (RelativeLayout) adapter.getViewByPosition(recycler, position, R.id.messageshow);
                recyclercommet = (RecyclerView) adapter.getViewByPosition(recycler, position, R.id.pingjiarecycler);
                switch (view.getId()) {

                    case R.id.momentpinglun:
                        LogUtils.d("1111111111111");
                        relativeLayout.setVisibility(View.VISIBLE);
                        showPinjia(Long.valueOf(beans.get(position).getMovementID()));
                        List<MomentBean> data = itemAdapter.getData();

                        break;
                    case R.id.messagesend:
                        LogUtils.d("333333333333");
                        a = (EditText) adapter.getViewByPosition(recycler, position, R.id.commentedit);
                        if (!a.getText().toString().trim().isEmpty()) {
                            DoCommentMovement(Long.valueOf(beans.get(position).getMovementID()), Long.valueOf("0"), a.getText().toString().trim());
                            if (pinglun == 1) {
                                relativeLayout.setVisibility(View.GONE);
                            }
                        } else {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.dianzan:
                        LogUtils.d("数值是：" + beans.get(position).getLikeCnt());
                        if (beans.get(position).getLikeCnt().equals("1")) {
                            DoCancelMovementAppraise(Long.valueOf(beans.get(position).getMovementID()), position);
                        } else if (beans.get(position).getLikeCnt().equals("0")) {
                            DoMovementAppraise(Long.valueOf(beans.get(position).getMovementID()), position);
                        }
                        break;
                    case R.id.guanzhu:
                        LogUtils.d("数据是点击:"+beans.get(position).getCollectCnt());
                        if (beans.get(position).getCollectCnt().equals("1")) {
                            DoCancelCollectMovement(Long.valueOf(beans.get(position).getMovementID()));
                        } else if (beans.get(position).getCollectCnt().equals("0")) {
                            DoCollectMovement(Long.valueOf(beans.get(position).getMovementID()));
                        }
                        break;
                }
            }
        });


    }

    private void GetUserPubMovementList(int RowsPerPage, int PageNum) {
        RetrofitHttpUtil.getApiService()
                .GetUserPubMovementList("", SPUtils.getUserId(context), 0, RowsPerPage, PageNum).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (!s.equals("[]")) {
                            List<MomentBean> beanList = GsonUtil.parseJsonArrayWithGson(s, MomentBean.class);
                            if (add == 1) {
                                add++;
                                if (beans.size() > 0) {
                                    beans.clear();
                                    beans.addAll(beanList);
                                }else {
                                    beans.addAll(beanList);}
                                itemAdapter.setNewData(beanList);
                                itemAdapter.notifyDataSetChanged();
                                easylayout.refreshComplete();
                            } else {
                                add++;
                                beans.addAll(beanList);
                                easylayout.closeLoadView();
                                int postion = itemAdapter.getData().size();
                                itemAdapter.addData(beanList);
                                itemAdapter.notifyDataSetChanged();
                                recycler.scrollToPosition(postion);
                            }

                        } else {
                            if (add > 1) {
                                easylayout.closeLoadView();
                                showToast("没有更多数据");
                            }

                        }
                    }
                });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void DoCollectMovement(Long MovementID) {
        RetrofitHttpUtil.getApiService()
                .DoCollectMovement("", Long.valueOf(SPUtils.getUserId(context)), MovementID, "")
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("收藏成功");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("3")) {
                            showToast("收藏失败");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("2")) {
                            showToast("已收藏");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("0")) {
                            showToast("无效的动态");
                            easylayout.autoRefresh();
                        }
                    }
                });
    }

    private void DoCancelCollectMovement(Long MovementID) {
        RetrofitHttpUtil.getApiService()
                .DoCancelCollectMovement("", Long.valueOf(SPUtils.getUserId(context)), MovementID)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("取消动态收藏成功");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("0")) {
                            showToast("取消动态收藏失败");
                            easylayout.autoRefresh();
                        }
                    }
                });
    }

    private void DoMovementAppraise(Long MovementID, final int position) {
        RetrofitHttpUtil.getApiService()
                .DoMovementAppraise("", Long.valueOf(SPUtils.getUserId(context)), MovementID, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            showToast("评价成功");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("3")) {
                            showToast("评价失败");
                        } else if (bean.getResult().equals("2")) {
                            showToast("已经评价");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("0")) {
                            showToast("无效的动态");
                        }
                        LogUtils.d(s);
                    }
                });
    }

    private void DoCancelMovementAppraise(Long MovementID, final int position) {
        RetrofitHttpUtil.getApiService()
                .DoCancelMovementAppraise("", Long.valueOf(SPUtils.getUserId(context)), MovementID, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
//                            MomentBean momentBean = beans.get(position);
//                            momentBean.setDoLike("0");
//                            momentBean.setLikeCnt("0");
//                            beans.set(position, momentBean);
//                            itemAdapter.replaceData(beans);

//                            GetUserPubMovementList(10, 1);
                            showToast("取消动态评价成功");
                            easylayout.autoRefresh();
                        } else if (bean.getResult().equals("0")) {
                            showToast("取消动态评价失败");
                            easylayout.autoRefresh();
                        }
                        LogUtils.d(s);
                    }
                });
    }

    private void DoCommentMovement(long MovementID, long RefCommentID, String Remark) {

        RetrofitHttpUtil.getApiService()
                .DoCommentMovement("", Long.valueOf(SPUtils.getUserId(context)), MovementID, RefCommentID, Remark).compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d(s);
                        CategroyBean bean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                        if (bean.getResult().equals("1")) {
                            pinglun = 1;
                            a.getText().clear();
                            showToast("评论成功");
                            easylayout.autoRefresh();
//                            GetUserPubMovementList(10, 1);
                            relativeLayout.setVisibility(View.GONE);

                        } else {
                            pinglun = 2;
                            showToast("评论失败");
                        }
                    }
                });

    }


    private void showPinjia(Long momentid) {
        LogUtils.d("动态是:" + momentid);
        RetrofitHttpUtil.getApiService()
                .GetMovementCommentList("", momentid).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                if (!s.equals("[]")) {
                    List<CommentListBean> beans = GsonUtil.parseJsonArrayWithGson(s, CommentListBean.class);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    CommentListAdapter adapter = new CommentListAdapter(R.layout.comment_item, beans);
                    recyclercommet.setLayoutManager(linearLayoutManager);
                    recyclercommet.setAdapter(adapter);
                }
                LogUtils.d("评论是" + s);
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
