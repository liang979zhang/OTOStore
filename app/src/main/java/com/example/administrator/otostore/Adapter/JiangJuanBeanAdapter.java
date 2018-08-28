package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.JiangJuanBean;
import com.example.administrator.otostore.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/6/28.
 */

public class JiangJuanBeanAdapter extends BaseQuickAdapter<JiangJuanBean, BaseViewHolder> {

    @Override
    protected void convert(BaseViewHolder helper, JiangJuanBean item) {
        LogUtils.d("数值是：" + item.getVoucherType());
        if (item.getVoucherType().trim().equals("1")) {
            helper.setImageResource(R.id.imagetyep, R.drawable.youhuiquan1);
            helper.setText(R.id.textnum, item.getDiscountAmt());
            helper.setText(R.id.title, "优惠金额");
        } else if (item.getVoucherType().trim().equals("2")) {
            helper.setImageResource(R.id.imagetyep, R.drawable.lipinquan1);
            helper.setText(R.id.textnum, item.getGiftAmt());
            helper.setText(R.id.title, "礼品价值");

        } else if (item.getVoucherType().trim().equals("3")) {
            helper.setImageResource(R.id.imagetyep, R.drawable.zhekouquan1);
            helper.setText(R.id.textnum, item.getDiscountRate());
            helper.setText(R.id.title, "折扣");


        } else if (item.getVoucherType().trim().equals("4")) {
            helper.setImageResource(R.id.imagetyep, R.drawable.miandanquan1);
            helper.setText(R.id.textnum, item.getFreeAmt());
            helper.setText(R.id.title, "免单金额");
        }
        helper.setText(R.id.numcode, String.valueOf(helper.getLayoutPosition()+1));
        helper.setText(R.id.textxianling, item.getLimitedQty());
        helper.setText(R.id.jiangjuandate, date(item.getStartDate()) + " ~ " + date(item.getEndDate()));
        helper.setText(R.id.remarktest, item.getRemark());
        helper.addOnClickListener(R.id.fabu);
        helper.addOnClickListener(R.id.guoqi);
    }

    public JiangJuanBeanAdapter(int layoutResId, @Nullable List<JiangJuanBean> data) {
        super(layoutResId, data);
    }

    public static String getstrtime(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日");
        Long long1 = Long.valueOf(str.replaceAll("/", " - "));
        String format = simpleDateFormat.format(new Date(long1 * 1000L));
        return format;
    }

    private String date(String date) {
        return date.substring(0, date.indexOf(" ")).replaceAll("/", "-");
    }
}
