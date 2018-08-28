package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.IndustryFirstBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/9.
 */

public class ClassifyAdapterLeft extends BaseQuickAdapter<IndustryFirstBean, BaseViewHolder> {
    public ClassifyAdapterLeft(int layoutResId, @Nullable List<IndustryFirstBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndustryFirstBean item) {
        helper.setText(R.id.classify_left_text, item.getItemTitle());
        helper.addOnClickListener(R.id.classify_left_text);
    }

}
