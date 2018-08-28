package com.example.administrator.otostore.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import com.example.administrator.otostore.R;
import com.example.administrator.otostore.Fragment.DiscoverFragment;
import com.example.administrator.otostore.Fragment.OrdersFragment;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.otostore.Fragment.HomepagerFragment;
import com.example.administrator.otostore.Fragment.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/3.
 */

public class BottomBarActivity extends FragmentActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.alphaIndicator)
    AlphaTabsIndicator alphaIndicator;
    private List<Fragment> fragmentList;
    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private Context context;

    public void initView() {
        this.context = this;
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomepagerFragment());
        fragmentList.add(new OrdersFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MineFragment());

        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        tabFragmentPagerAdapter.setMfragments(fragmentList);
        viewpager.setAdapter(tabFragmentPagerAdapter);
        alphaIndicator.setViewPager(viewpager);
        viewpager.addOnPageChangeListener(tabFragmentPagerAdapter);

        alphaIndicator.getTabView(0).removeShow();
        alphaIndicator.getTabView(1).removeShow();
        alphaIndicator.getTabView(2).removeShow();
        alphaIndicator.getTabView(3).removeShow();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_bottom_bar);
        ButterKnife.bind(this);
        initView();
    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private List<Fragment> mfragments;

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setMfragments(List<Fragment> mfragments) {
            this.mfragments = mfragments;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mfragments.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mfragments.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                alphaIndicator.getCurrentItemView().removeShow();
            } else if (1 == position) {
                alphaIndicator.getCurrentItemView().removeShow();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragmentList.get(0);
        fragment.onActivityResult(requestCode,resultCode,data);
    }
}
