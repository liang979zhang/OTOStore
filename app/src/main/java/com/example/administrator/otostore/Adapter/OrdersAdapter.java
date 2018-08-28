package com.example.administrator.otostore.Adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.OrdersShowBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12.
 */

public class OrdersAdapter extends BaseQuickAdapter<OrdersShowBean, BaseViewHolder> {

    @Override
    protected void convert(BaseViewHolder helper, OrdersShowBean item) {
        helper.setText(R.id.orders_nickname, item.getUserNickName());
        helper.setText(R.id.orders_phone, item.getMobileNum());
        helper.setText(R.id.orders_code, item.getOrderCode());
        helper.setText(R.id.orders_time, item.getAppointmentTime());
        helper.setText(R.id.orders_summoney, item.getProductAmt());
        helper.setText(R.id.orders_shouldmoney, item.getPayAmt());
        if (item.getOrderType().equals("2")){
            helper.setVisible(R.id.orders_add,true);
            helper.addOnClickListener(R.id.orders_add);
        }else if (item.getOrderType().equals("3")){
            helper.setVisible(R.id.orders_delay,true);
            helper.addOnClickListener(R.id.orders_delay);
        }
    }

    public OrdersAdapter() {
        super(R.layout.item_orders_show);
    }
}
