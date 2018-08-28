package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.CommentListBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CommentListAdapter extends BaseQuickAdapter<CommentListBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, CommentListBean item) {
        helper.setText(R.id.nickname, item.getNickName());
        helper.setText(R.id.textcomment, item.getRemark());
    }

    public CommentListAdapter(int layoutResId, @Nullable List<CommentListBean> data) {
        super(layoutResId, data);
    }
}
