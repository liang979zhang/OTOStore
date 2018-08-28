package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.ProductListBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ProductListBeanAdapter extends BaseQuickAdapter<ProductListBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ProductListBean item) {
        helper.setText(R.id.item_product_select, item.getProductName());
    }

    public ProductListBeanAdapter(int layoutResId, @Nullable List<ProductListBean> data) {
        super(layoutResId, data);
    }
}
