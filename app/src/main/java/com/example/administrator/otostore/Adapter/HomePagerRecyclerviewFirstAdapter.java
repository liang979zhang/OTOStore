package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.HomePagerBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */

public class HomePagerRecyclerviewFirstAdapter extends BaseQuickAdapter<HomePagerBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, HomePagerBean item) {
        helper.setText(R.id.homepagerfirst_text, item.getTitle());
        helper.setImageResource(R.id.homepagerfirst_image, item.getImageResource());
//        helper.addOnClickListener(R.id.homepagerfirst_image);
    }

    public HomePagerRecyclerviewFirstAdapter(int layoutResId, @Nullable List<HomePagerBean> data) {
        super(layoutResId, data);
    }
}
