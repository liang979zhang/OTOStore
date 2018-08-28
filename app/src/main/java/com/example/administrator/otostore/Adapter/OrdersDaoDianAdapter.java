package com.example.administrator.otostore.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.OrdersShowBean;
import com.example.administrator.otostore.R;

/**
 * Created by Administrator on 2018/7/12.
 */

public class OrdersDaoDianAdapter extends BaseQuickAdapter<OrdersShowBean, BaseViewHolder> {

    @Override
    protected void convert(BaseViewHolder helper, OrdersShowBean item) {
        helper.setText(R.id.orders_nickname, item.getUserNickName());
        helper.setText(R.id.orders_phone, item.getMobileNum());
        helper.setText(R.id.orders_code, item.getOrderCode());
        helper.setText(R.id.orders_time, item.getAppointmentTime());
        helper.setText(R.id.orders_summoney, item.getProductAmt());
        helper.setText(R.id.orders_shouldmoney, item.getPayAmt());
        if (item.getOrderType().equals("2")){
            helper.setVisible(R.id.orders_delay,true);
            helper.addOnClickListener(R.id.orders_delay);
            helper.setVisible(R.id.orders_daodian,true);
            helper.addOnClickListener(R.id.orders_daodian);
        }else if (item.getOrderType().equals("1")){
            helper.setVisible(R.id.orders_d_equils1,true);
            helper.addOnClickListener(R.id.orders_close);
            helper.addOnClickListener(R.id.orders_tiaozheng);
        }else if (item.getOrderType().equals("10")){
            helper.setVisible(R.id.orders_daodian,true);
            helper.addOnClickListener(R.id.orders_daodian);
        }
    }

    public OrdersDaoDianAdapter() {
        super(R.layout.item_ordersdaodian_show);
    }
}
