package com.example.administrator.otostore.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.DetailBean;
import com.example.administrator.otostore.R;

/**
 * Created by Administrator on 2018/7/9.
 */

public class DetailThird extends BaseQuickAdapter<DetailBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, DetailBean item) {
        helper.setText(R.id.time, item.getEditTime().substring(0, item.getEditTime().indexOf(" ")));
        if (item.getInOutType().equals("1")) {
            helper.setImageResource(R.id.type, R.drawable.shou);
        } else if (item.getInOutType().equals("2")) {
            helper.setImageResource(R.id.type, R.drawable.zhi);
        }
        helper.setText(R.id.jifennum, "¥:"+item.getSettleAmt());
        helper.setText(R.id.jifenremark,item.getSettleBonus()+" 分");
    }

    public DetailThird() {
        super(R.layout.item_detail_third);
    }
}
