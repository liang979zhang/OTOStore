package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.JIfenBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/8.
 */

public class JifenAdapter extends BaseQuickAdapter<JIfenBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, JIfenBean item) {
        helper.setText(R.id.time, item.getEditTime().substring(0, item.getEditTime().indexOf(" ")));
        if (item.getInOutType() != null) {
            if (item.getInOutType().equals("1")) {
                helper.setImageResource(R.id.type, R.drawable.shou);
            } else if (item.getInOutType().equals("2")) {
                helper.setImageResource(R.id.type, R.drawable.zhi);
            }
        } else {
            helper.setImageResource(R.id.type, R.drawable.zhi);
        }
        if (item.getBonusPoints() != null) {
            helper.setText(R.id.jifennum, item.getBonusPoints());
        } else if (item.getTransferBonus() != null) {
            helper.setText(R.id.jifennum, item.getTransferBonus());
        }
        String biaozhi = "";
        if (item.getBalanceAmt() != null) {
            biaozhi = "¥:" + item.getBalanceAmt();
        }
        helper.setText(R.id.jifenremark, biaozhi + item.getRemark());

    }

    public JifenAdapter() {
        super(R.layout.jifendetailmonth);
    }
}
