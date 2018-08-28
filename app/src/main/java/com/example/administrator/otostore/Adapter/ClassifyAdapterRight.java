package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.IndustrySecondBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/9.
 */

public class ClassifyAdapterRight extends BaseQuickAdapter<IndustrySecondBean, BaseViewHolder> {
    public ClassifyAdapterRight(int layoutResId, @Nullable List<IndustrySecondBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndustrySecondBean item) {
        helper.setText(R.id.type_right_text, item.getItemTitle());
        helper.setText(R.id.type_right_number, String.valueOf(helper.getLayoutPosition()+1));
//        helper.setText(R.id.type_right_number, item.);
    }
}
