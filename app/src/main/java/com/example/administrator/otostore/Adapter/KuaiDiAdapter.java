package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.GetExpressCorpListBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/14.
 */

public class KuaiDiAdapter extends BaseQuickAdapter<GetExpressCorpListBean,BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, GetExpressCorpListBean item) {
        helper.setText(R.id.texta, item.getExpCorpName());

    }


    public KuaiDiAdapter() {
        super(R.layout.item_kuaidiadapter);
    }
}
