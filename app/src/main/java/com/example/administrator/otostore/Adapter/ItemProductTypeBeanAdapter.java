package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ItemProductTypeBeanAdapter extends BaseQuickAdapter<CategroyBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, CategroyBean item) {
        helper.setText(R.id.item_product_type, item.getCatName());
        LogUtils.d("数值是:"+item.getCatName());
    }

    public ItemProductTypeBeanAdapter(int layoutResId, @Nullable List<CategroyBean> data) {
        super(layoutResId, data);
    }
}
