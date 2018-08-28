package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.BankBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */

public class BankShowAdapter extends BaseQuickAdapter<BankBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, BankBean item) {
        helper.setText(R.id.bankname, item.getBankName());
        helper.setText(R.id.banholder, item.getCardHolder());
        helper.setText(R.id.banknum, item.getBankAccCode());
    }

    public BankShowAdapter() {
        super(R.layout.item_bankshow);
    }
}
