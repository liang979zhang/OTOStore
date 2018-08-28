package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.ShopRefundBillsBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ShopRefundBillsAdapter extends BaseQuickAdapter<ShopRefundBillsBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ShopRefundBillsBean item) {
        helper.setText(R.id.orders_BuyerName, item.getBuyerName());
        helper.setText(R.id.orders_MobileNum, item.getMobileNum());
        helper.setText(R.id.orders_DeliveryCode, item.getDeliveryCode());
        helper.setText(R.id.orders_RefundBonus, item.getRefundBonus());
        helper.setText(R.id.orders_MaxRefundAmt, item.getMaxRefundAmt());
        helper.setText(R.id.orders_RefundAmt, item.getRefundAmt());
        if (item.getRecStatus().equals("1")) {
            helper.setVisible(R.id.show_RecStatus1, true);
            helper.addOnClickListener(R.id.tongyi);
            helper.addOnClickListener(R.id.jujue);
        } else if (item.getRecStatus().equals("2")) {
            helper.setVisible(R.id.show_RecStatus2,true);
            helper.addOnClickListener(R.id.tuikuan);
        }else if (item.getRecStatus().equals("3")){
            helper.setText(R.id.show_text_RecStatus,"已拒绝");
        }else if (item.getRecStatus().equals("4")){
            helper.setText(R.id.show_text_RecStatus,"小二介入");
        }else if (item.getRecStatus().equals("5")){
            helper.setText(R.id.show_text_RecStatus,"退款成功");
        }else if (item.getRecStatus().equals("6")){
            helper.setText(R.id.show_text_RecStatus,"退款关闭");
        }
    }

    public ShopRefundBillsAdapter() {
        super(R.layout.item_shoprefundbills);
    }
}
