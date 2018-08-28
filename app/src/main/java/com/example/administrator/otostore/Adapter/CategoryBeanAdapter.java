package com.example.administrator.otostore.Adapter;

import android.icu.util.ULocale;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class CategoryBeanAdapter extends BaseQuickAdapter<CategroyBean,BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, CategroyBean item) {
        helper.setText(R.id.catenum,item.getCatIdx());
        helper.setText(R.id.catetitle,item.getCatName());
        helper.setText(R.id.catedes,item.getRemark());
    }

    public CategoryBeanAdapter(int layoutResId, @Nullable List<CategroyBean> data) {
        super(layoutResId, data);
    }
}
