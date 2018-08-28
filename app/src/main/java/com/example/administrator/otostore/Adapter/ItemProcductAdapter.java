package com.example.administrator.otostore.Adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.ProductListBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/1.
 */

public class ItemProcductAdapter extends BaseQuickAdapter<ProductListBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ProductListBean item) {
        helper.setText(R.id.item_product_fabu, item.getProductName());
        LogUtils.d("商品状态:"+item.getRecStatus());
        if (item.getRecStatus().equals("1")) {
            helper.setVisible(R.id.fabuok, true);
        } else if (item.getRecStatus().equals("2")) {
            helper.setVisible(R.id.xiajiaok, true);
        }
        helper.addOnClickListener(R.id.fabuok);
        helper.addOnClickListener(R.id.xiajiaok);
    }

    public ItemProcductAdapter(int layoutResId, @Nullable List<ProductListBean> data) {

        super(layoutResId, data);
    }
}
