package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.MomentBean;
import com.example.administrator.otostore.Bean.MomentListBean;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class MomentItemAdapter extends BaseQuickAdapter<MomentBean, BaseViewHolder> {
    @Override
    protected void convert(final BaseViewHolder helper, MomentBean item) {
        RetrofitHttpUtil.getApiService()
                .GetUserAttachList("", SPUtils.getUserId(mContext), 2, 10, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (!s.equals("[]")){
                        List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                        Glide.with(mContext).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into((ImageView) helper.getView(R.id.momneta));

                    }}
                });
        RetrofitHttpUtil.getApiService()
                .GetMovementAttachList("",Long.valueOf(item.getMovementID())).compose(SchedulerTransformer.<String>transformer()).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                if (!s.equals("[]")){
                    helper.setVisible(R.id.dianzan,true);
                    List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                    Glide.with(mContext).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into((ImageView) helper.getView(R.id.momnetid));
                }
            }
        });
        LogUtils.d("动态是" + item.getNickName());
        helper.setText(R.id.momentnickname, item.getNickName());
        helper.setText(R.id.momenttime, item.getEditTime().toString().substring(0, item.getEditTime().toString().indexOf(" ")).replaceAll("/", " - "));
        helper.setText(R.id.momentremark, item.getRemark());
        helper.setText(R.id.momentlike, item.getDoLike());
        helper.setText(R.id.momentshoucang, item.getCollectCnt());
        helper.addOnClickListener(R.id.momentpinglun);
        helper.addOnClickListener(R.id.messagesend);
        helper.addOnClickListener(R.id.dianzan);
        helper.addOnClickListener(R.id.guanzhu);
    }

    public MomentItemAdapter() {
        super(R.layout.moment_show_item, null);
    }


    private void showpersonimage() {

    }
}
