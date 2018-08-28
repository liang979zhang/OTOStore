package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.ProductSpec;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 */

public class ProductSpecAdapter extends BaseQuickAdapter<ProductSpec, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ProductSpec item) {
        helper.setText(R.id.rulesnum, String.valueOf(helper.getLayoutPosition() + 1));
        helper.setText(R.id.spedid, item.getSpecIdx());
        helper.setText(R.id.SpecCode, item.getSpecCode());
        helper.setText(R.id.price, item.getPrice());
        helper.setText(R.id.num, item.getQuantity());
        helper.setText(R.id.remark, item.getRemark());
        helper.setText(R.id.productspec, item.getProductSpec());
    }

    public ProductSpecAdapter(int layoutResId, @Nullable List<ProductSpec> data) {
        super(layoutResId, data);
    }
}
