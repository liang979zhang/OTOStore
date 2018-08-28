package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.AddressBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/24.
 */

public class AddressAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {
        helper.setText(R.id.province, item.getProvinceName());
        helper.setText(R.id.city, item.getCityName());
        helper.setText(R.id.dis, item.getDistrictName());
        helper.setText(R.id.dess, item.getAddr());
        helper.setText(R.id.name, item.getConsignee());
        helper.setText(R.id.phone, item.getContactTel());

    }

    public AddressAdapter(int layoutResId, @Nullable List<AddressBean> data) {
        super(layoutResId, data);
    }
}
