package com.example.administrator.otostore.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Fragment.StoreFragmentOne;
import com.example.administrator.otostore.Fragment.StoreFragmentThree;
import com.example.administrator.otostore.Fragment.StoreFragmentTwo;
import com.example.administrator.otostore.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/10.
 */

public class StoreManagementActivity extends BaseActivity {
    @BindView(R.id.reach_the_store)
    RadioButton reachTheStore;
    @BindView(R.id.distribution)
    RadioButton distribution;
    @BindView(R.id.all)
    RadioButton all;
    @BindView(R.id.myordersmanager)
    RadioGroup myordersmanager;
    @BindView(R.id.store_three_manager)
    FrameLayout storeThreeManager;
    private Context context;
//    private Fragment fragment;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_store_management;
    }

    @Override
    public void initView() {
        setcenterTitle("店铺管理");
        showright(true);
        setrightTitle("查看");
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        startActivity(SeeStoreActivity.class);
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
        setDefaultFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragment = new StoreFragmentOne();
        myordersmanager.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radionbutton = group.getCheckedRadioButtonId();
                switch (radionbutton) {
                    case R.id.reach_the_store:
                        LogUtils.d("AAAAAAAAAAAAAAAAAA");
//                        int backStackCount=getFragmentManager().getBackStackEntryCount();
//                        for (int i = 0; i < backStackCount; i++) {
//                            getFragmentManager().popBackStack();
//                        }
                        setDefaultFragment();
//                        fragment = new StoreFragmentOne();
                        break;
                    case R.id.distribution:
                        LogUtils.d("AAAAAAAAAAAAAAAAAA");
                        FragmentManager fragmentManagerTwo = getFragmentManager();
                        FragmentTransaction fragmentTransactionTwo = fragmentManagerTwo.beginTransaction();
                        StoreFragmentTwo storeFragmentTwo = new StoreFragmentTwo();
                        fragmentTransactionTwo.replace(R.id.store_three_manager, storeFragmentTwo);
                        fragmentTransactionTwo.commit();

//                        int backStackCountt=getFragmentManager().getBackStackEntryCount();
//                        for (int i = 0; i < backStackCountt; i++) {
//                            getFragmentManager().popBackStack();
//                        }
//                        fragment = new StoreFragmentTwo();
                        break;
                    case R.id.all:
                        LogUtils.d("AAAAAAAAAAAAAAAAAA");

//                        int backStackCountta=getFragmentManager().getBackStackEntryCount();
//                        for (int i = 0; i < backStackCountta; i++) {
//                            getFragmentManager().popBackStack();
//                        }
                        FragmentManager fragmentManagerThree = getFragmentManager();
                        FragmentTransaction fragmentTransactionThree = fragmentManagerThree.beginTransaction();
                        StoreFragmentThree storeFragmentThree = new StoreFragmentThree();
                        fragmentTransactionThree.replace(R.id.store_three_manager, storeFragmentThree);
                        fragmentTransactionThree.commit();
//                        FragmentManager fragmentManagerThree=getFragmentManager();
//                        FragmentTransaction fragmentTransactionThree=fragmentManagerThree.beginTransaction();
//                        StoreFragmentThree storeFragmentThree=new StoreFragmentThree();
//                        fragmentTransactionThree.replace(R.id.store_three_manager,storeFragmentThree);
//                        fragmentTransactionThree.commit();
                        break;
                }
            }
        });


//        fragmentTransaction.replace(R.id.store_three_manager, fragment).commit();

//        myordersmanager.check(reachTheStore.getId());
    }

    private void setDefaultFragment() {
        FragmentManager fragmentManagerOne = getFragmentManager();
        FragmentTransaction fragmentTransactionOne = fragmentManagerOne.beginTransaction();
        StoreFragmentOne storeFragmentOne = new StoreFragmentOne();
        fragmentTransactionOne.replace(R.id.store_three_manager, storeFragmentOne);
        fragmentTransactionOne.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
