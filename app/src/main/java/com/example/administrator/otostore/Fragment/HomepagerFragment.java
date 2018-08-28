package com.example.administrator.otostore.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Activity.AddFriendActivity;
import com.example.administrator.otostore.Activity.CustomerManagerActivity;
import com.example.administrator.otostore.Activity.DaoDianActivity;
import com.example.administrator.otostore.Activity.LaunchProductActivity;
import com.example.administrator.otostore.Activity.LoginActivity;
import com.example.administrator.otostore.Activity.LotteryActivity;
import com.example.administrator.otostore.Activity.MyCustomerActivity;
import com.example.administrator.otostore.Activity.NoMessageActivity;
import com.example.administrator.otostore.Activity.PeiSongActivity;
import com.example.administrator.otostore.Activity.PeopleNearbyActivity;
import com.example.administrator.otostore.Activity.ProductControlActivity;
import com.example.administrator.otostore.Activity.ProductManagerActivity;
import com.example.administrator.otostore.Activity.SaoYiSaoActivity;
import com.example.administrator.otostore.Activity.SettingActivity;
import com.example.administrator.otostore.Activity.StoreIntroductionActivity;
import com.example.administrator.otostore.Activity.StoreManagementActivity;
import com.example.administrator.otostore.Activity.VarietyActivity;
import com.example.administrator.otostore.Adapter.HomePagerRecyclerviewFirstAdapter;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.HomePagerBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.DividerGridItemDecoration;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.NetUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomepagerFragment extends Fragment {


    @BindView(R.id.homepagerfirst)
    RecyclerView homepagerfirst;
    Unbinder unbinder;
    private int REQUEST_CODE_SCAN = 111;
    private static final Class<?>[] ACTIVITY = {
            CaptureActivity.class, StoreManagementActivity.class, PeopleNearbyActivity.class, AddFriendActivity.class, PeiSongActivity.class,
            DaoDianActivity.class, StoreIntroductionActivity.class, ProductManagerActivity.class, VarietyActivity.class, ProductControlActivity.class,
            MyCustomerActivity.class, CustomerManagerActivity.class,
            LotteryActivity.class, LotteryActivity.class, LotteryActivity.class, LotteryActivity.class,
            SettingActivity.class, NoMessageActivity.class, NoMessageActivity.class, NoMessageActivity.class,
            NoMessageActivity.class, NoMessageActivity.class};
    private static final String[] TITLE = {
            "扫一扫", "店铺管理", "附近的人", "添加好友", "配送订单",
            "到店订单", "店铺介绍", "产品管理", "分类管理", "管理产品",
            "我的用户", "客户管理",
            "折扣卷", "免单卷", "优惠卷", "礼品卷",
            "设置"
//            , "会员折扣", "供应链", "供应链订单", "超级联盟"

    };
    private static final int[] IMG = {R.drawable.saoyisao, R.drawable.dianpuguanli, R.drawable.fujinren, R.drawable.tianjiahaoyou, R.drawable.peisongdingdan, R.drawable.daodiandingdan,
            R.drawable.dianpujieshao, R.drawable.chanpinguanli, R.drawable.fenleiguanli, R.drawable.fabuchanpin, R.drawable.wodeyonghu,
            R.drawable.kehuguanli, R.drawable.zhekouquan, R.drawable.miandanquan, R.drawable.youhuiquan, R.drawable.lipinquan,
            R.drawable.shezhi, R.drawable.huiyuanzhekou, R.drawable.gongyinglian, R.drawable.gongyingliandingdan, R.drawable.chaojilianmeng};
    private ArrayList<HomePagerBean> homePagerRecyclerviewFirstBeanArrayList;

    public HomepagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepager, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initAdapter();
        return view;
    }

    private void initAdapter() {
        HomePagerRecyclerviewFirstAdapter homePagerRecyclerviewFirstAdapter = new HomePagerRecyclerviewFirstAdapter(R.layout.itemhomeapagerrecyclerfirst, homePagerRecyclerviewFirstBeanArrayList);

        homepagerfirst.addItemDecoration(new DividerGridItemDecoration(getContext()));
        if (!CommonUtils.isFastClick()) {
            homePagerRecyclerviewFirstAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (NetUtil.isNetworkAvailable(getContext())) {
                        if ((position < 12 || position > 15)) {
                            if (position != 0) {
                                Intent intent = new Intent(getContext(), ACTIVITY[position]);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        } else {
                            LogUtils.d("数值是·" + position);
                            if (position == 12) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("LotteryType", 3);
                                Intent intent = new Intent(getContext(), ACTIVITY[position]);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (position == 13) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("LotteryType", 4);
                                Intent intent = new Intent(getContext(), ACTIVITY[position]);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (position == 14) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("LotteryType", 1);
                                Intent intent = new Intent(getContext(), ACTIVITY[position]);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (position == 15) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("LotteryType", 2);
                                Intent intent = new Intent(getContext(), ACTIVITY[position]);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "请连接网络", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        homepagerfirst.setAdapter(homePagerRecyclerviewFirstAdapter);
    }

    private void initData() {
        homePagerRecyclerviewFirstBeanArrayList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomePagerBean item = new HomePagerBean();
            item.setTitle(TITLE[i]);
            item.setActivity(ACTIVITY[i]);
            item.setImageResource(IMG[i]);
            homePagerRecyclerviewFirstBeanArrayList.add(item);
        }
    }

    private void initView() {
        homepagerfirst.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
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
                .DoMakeFriend("", SPUtils.getUserId(getActivity()), friendsid, grid, reams).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
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

    private void showToast(String maeg) {
        Toast.makeText(getActivity(), maeg, Toast.LENGTH_SHORT).show();
    }
}
