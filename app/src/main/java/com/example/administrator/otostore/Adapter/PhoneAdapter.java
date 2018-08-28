package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.PhoneBean;
import com.example.administrator.otostore.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/10.
 */

public class PhoneAdapter extends BaseQuickAdapter<PhoneBean, BaseViewHolder> {
    private List<PhoneBean> phoneBeanList;

    @Override
    protected void convert(BaseViewHolder helper, PhoneBean item) {
        helper.setText(R.id.phonename, item.getPhonename());
        helper.setText(R.id.phonenumber, item.getPhonenumber());

        if (helper.getLayoutPosition() == 0 || !phoneBeanList.get(helper.getLayoutPosition() - 1).getShoufirstletter().equals(item.getShoufirstletter())) {
            helper.setVisible(R.id.phone_letter,true);
            helper.setText(R.id.phone_letter, item.getShoufirstletter());
        }
//        else {
//            helper.setVisible(R.id.phone_letter,false);
//        }

    }

    public PhoneAdapter(int layoutResId, @Nullable List<PhoneBean> data) {
        super(layoutResId, data);
        this.phoneBeanList = data;
    }
}
