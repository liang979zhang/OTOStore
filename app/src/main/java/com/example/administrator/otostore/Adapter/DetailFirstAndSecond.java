package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.DetailBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */

public class DetailFirstAndSecond extends BaseQuickAdapter<DetailBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, DetailBean item) {
        helper.setText(R.id.time, item.getEditTime().substring(0, item.getEditTime().indexOf(" ")));
        helper.setText(R.id.type, item.getEditTime());
        helper.setText(R.id.jifennum, item.getEditTime());
    }

    public DetailFirstAndSecond() {
        super(R.layout.item_detail_firstandsecond);
    }
}
